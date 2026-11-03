#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Gỡ comment khỏi mã nguồn Java và lưu vào "sổ ghi chú" (comments.json) để có thể thêm lại sau này.

Cách dùng (chạy ở thư mục gốc của project):
    python comments_tool.py strip   --ledger ghi_chu/comments.json
    python comments_tool.py restore --ledger ghi_chu/comments.json [--only adjustment,note] [--file ConsoleIO] [--dry-run]
    python comments_tool.py report  --ledger ghi_chu/comments.json --out ghi_chu/GHI_CHU_CODE.md

Các loại comment (category):
    license     phần đầu file do NetBeans tạo ("Click nbfs://...")
    author      khối Javadoc có @author
    adjustment  ghi chú "có điều chỉnh với cách viết ban đầu"
    note        mọi ghi chú giải thích / TODO còn lại
"""
import argparse
import json
import os
import sys
from bisect import bisect_right
from datetime import datetime

MARKER = "có điều chỉnh với cách viết ban đầu"
ROUND2 = "(đợt 2)"
CATEGORIES = ["license", "author", "adjustment", "note"]
CATEGORY_TITLE = {
    "license": "Phần đầu file do NetBeans tạo",
    "author": "Javadoc ghi tên người viết (@author)",
    "adjustment": "Ghi chú điều chỉnh code đã viết ban đầu",
    "note": "Ghi chú giải thích / TODO",
}


# ---------------------------------------------------------------- đọc / ghi file
def read_source(path):
    with open(path, "rb") as f:
        data = f.read().decode("utf-8")
    newline = "\r\n" if "\r\n" in data else "\n"
    return data.replace("\r\n", "\n"), newline


def write_source(path, text, newline):
    with open(path, "wb") as f:
        f.write(text.replace("\n", newline).encode("utf-8"))


def java_files(root):
    result = []
    for base, dirs, files in os.walk(os.path.join(root, "src")):
        dirs.sort()
        for name in sorted(files):
            if name.endswith(".java"):
                full = os.path.join(base, name)
                result.append(os.path.relpath(full, root).replace(os.sep, "/"))
    return result


# ---------------------------------------------------------------- tìm comment
def find_comment_spans(text):
    spans = []
    i, n = 0, len(text)
    while i < n:
        c = text[i]
        if c == '"' or c == "'":
            quote = c
            i += 1
            while i < n and text[i] != quote:
                if text[i] == "\\":
                    i += 1
                if i < n and text[i] == "\n":
                    break
                i += 1
            i += 1
        elif text.startswith("//", i):
            j = text.find("\n", i)
            j = n if j == -1 else j
            spans.append((i, j))
            i = j
        elif text.startswith("/*", i):
            j = text.find("*/", i + 2)
            j = n if j == -1 else j + 2
            spans.append((i, j))
            i = j
        else:
            i += 1
    return spans


def classify(text_lines):
    joined = "\n".join(text_lines)
    has_marker = MARKER in joined
    if "Click nbfs://nbhost" in joined:
        category = "license"
    elif "@author" in joined:
        category = "author"
    elif has_marker:
        category = "adjustment"
    else:
        category = "note"
    if has_marker:
        rnd = 2 if ROUND2 in joined else 1
    else:
        rnd = 0
    return category, has_marker, rnd


# ---------------------------------------------------------------- strip
def strip_text(text):
    """Trả về (văn bản đã gỡ comment, danh sách entry)."""
    spans = find_comment_spans(text)
    lines = text.split("\n")
    starts, off = [], 0
    for ln in lines:
        starts.append(off)
        off += len(ln) + 1

    segs = [[] for _ in lines]
    for a, b in spans:
        li = bisect_right(starts, a) - 1
        pos = a
        while True:
            line_end = starts[li] + len(lines[li])
            e = min(b, line_end)
            segs[li].append((pos - starts[li], e - starts[li]))
            if b <= line_end:
                break
            pos = line_end + 1
            li += 1

    kept = []          # (chỉ số dòng gốc, nội dung dòng đã gỡ)
    kinds = {}         # chỉ số dòng gốc -> 'full' | 'trail'
    for li, line in enumerate(lines):
        if not segs[li]:
            kept.append((li, line))
            continue
        out, last = [], 0
        for s, e in sorted(segs[li]):
            out.append(line[last:s])
            last = e
        out.append(line[last:])
        remaining = "".join(out)
        if remaining.strip() == "":
            kinds[li] = "full"
        else:
            kinds[li] = "trail"
            kept.append((li, remaining.rstrip()))

    stripped = [t for _, t in kept]
    stripped_index = {orig: k for k, (orig, _) in enumerate(kept)}

    def occurrence_of(text_trimmed, upto):
        return sum(1 for t in stripped[:upto] if t.strip() == text_trimmed)

    entries = []
    li = 0
    total = len(lines)
    while li < total:
        kind = kinds.get(li)
        if kind == "full":
            start = li
            while li < total and kinds.get(li) == "full":
                li += 1
            end = li - 1
            block = lines[start:end + 1]
            k = sum(1 for orig, _ in kept if orig < start)
            gap = 0
            while k + gap < len(stripped) and stripped[k + gap].strip() == "":
                gap += 1
            if k + gap < len(stripped):
                anchor = stripped[k + gap].strip()
                occ = occurrence_of(anchor, k + gap)
                shown = k + gap + 1
            else:
                anchor, occ, shown = None, 0, None
                gap = len(stripped) - k
            cat, has_marker, rnd = classify(block)
            entries.append({
                "kind": "block", "category": cat, "round": rnd,
                "line": start + 1, "end_line": end + 1,
                "stripped_line": shown,
                "text": block, "anchor": anchor, "occurrence": occ, "gap": gap,
            })
        elif kind == "trail":
            k = stripped_index[li]
            comment_parts = []
            for s, e in sorted(segs[li]):
                comment_parts.append(lines[li][s:e])
            anchor = stripped[k].strip()
            occ = occurrence_of(anchor, k)
            cat, has_marker, rnd = classify(comment_parts)
            entries.append({
                "kind": "trail", "category": cat, "round": rnd,
                "line": li + 1, "end_line": li + 1,
                "stripped_line": k + 1,
                "text": comment_parts, "original": lines[li],
                "anchor": anchor, "occurrence": occ, "gap": 0,
            })
            li += 1
        else:
            li += 1
    return "\n".join(stripped), entries


def cmd_strip(args):
    root = args.root
    if os.path.exists(args.ledger) and not args.force:
        sys.exit("Sổ ghi chú đã tồn tại: " + args.ledger + " (dùng --force để ghi đè).")
    ledger = {
        "format": 1,
        "created": datetime.now().strftime("%Y-%m-%d %H:%M"),
        "note": "Mỗi entry cho biết comment nằm ở đâu (anchor = dòng code ngay sau/cùng dòng, "
                "occurrence = đó là lần xuất hiện thứ mấy của dòng code đó trong file).",
        "files": {},
    }
    total = 0
    pending = {}
    for rel in java_files(root):
        text, newline = read_source(os.path.join(root, rel))
        new_text, entries = strip_text(text)
        if entries:
            ledger["files"][rel] = {"entries": entries}
            pending[rel] = (new_text, newline)
            total += len(entries)
    os.makedirs(os.path.dirname(os.path.abspath(args.ledger)), exist_ok=True)
    with open(args.ledger, "w", encoding="utf-8") as f:
        json.dump(ledger, f, ensure_ascii=False, indent=1)
    for rel, (new_text, newline) in pending.items():
        write_source(os.path.join(root, rel), new_text, newline)
    print("Đã gỡ %d comment trong %d file. Sổ ghi chú: %s" % (total, len(pending), args.ledger))


# ---------------------------------------------------------------- restore
def restore_text(text, entries):
    """Trả về (văn bản mới, danh sách entry không đặt lại được, số entry đã có sẵn)."""
    lines = text.split("\n")
    trails, blocks, failed = [], [], []
    present = 0

    def same_line(ln, anchor):
        t = ln.strip()
        if t == anchor:
            return True
        if t.startswith(anchor):
            rest = t[len(anchor):].lstrip()
            return rest.startswith("//") or rest.startswith("/*")
        return False

    def locate(entry):
        anchor, occ = entry["anchor"], entry["occurrence"]
        seen = -1
        for idx, ln in enumerate(lines):
            if same_line(ln, anchor):
                seen += 1
                if seen == occ:
                    return idx
        return None

    def comment_like(ln):
        t = ln.strip()
        return t == "" or t.startswith(("//", "/*", "*"))

    def block_present(anchor_idx, text_block):
        start = anchor_idx
        while start > 0 and comment_like(lines[start - 1]):
            start -= 1
        region = lines[start:anchor_idx]
        size = len(text_block)
        for j in range(len(region) - size + 1):
            if region[j:j + size] == text_block:
                return True
        return False

    for e in entries:
        if e["kind"] == "trail":
            idx = locate(e)
            if idx is None:
                if e["original"] in lines:
                    present += 1
                else:
                    failed.append(e)
            elif lines[idx] == e["original"]:
                present += 1
            else:
                trails.append((idx, e))
        else:
            if e["anchor"] is None:
                anchor_idx = len(lines)
                pos = len(lines) - e["gap"]
            else:
                idx = locate(e)
                if idx is None:
                    failed.append(e)
                    continue
                anchor_idx = idx
                pos = idx - e["gap"]
            if block_present(anchor_idx, e["text"]):
                present += 1
            else:
                blocks.append((pos, e))

    for idx, e in trails:
        lines[idx] = e["original"]
    for pos, e in sorted(blocks, key=lambda x: x[0], reverse=True):
        lines[pos:pos] = e["text"]
    return "\n".join(lines), failed, present


def cmd_restore(args):
    with open(args.ledger, encoding="utf-8") as f:
        ledger = json.load(f)
    only = set(args.only.split(",")) if args.only else set(CATEGORIES)
    restored = failed_total = already = 0
    for rel, info in ledger["files"].items():
        if args.file and args.file not in rel:
            continue
        path = os.path.join(args.root, rel)
        if not os.path.exists(path):
            print("Bỏ qua (không có file): " + rel)
            continue
        entries = [e for e in info["entries"] if e["category"] in only and not e.get("obsolete")]
        if not entries:
            continue
        text, newline = read_source(path)
        new_text, failed, present = restore_text(text, entries)
        already += present
        restored += len(entries) - len(failed) - present
        failed_total += len(failed)
        for e in failed:
            print("KHÔNG đặt lại được (code đã đổi?): %s dòng gốc %d, gắn với: %s" % (rel, e["line"], e["anchor"]))
        if not args.dry_run:
            write_source(path, new_text, newline)
    print("%s %d comment, đã có sẵn %d, không đặt lại được %d." % (
        "Sẽ thêm" if args.dry_run else "Đã thêm", restored, already, failed_total))


# ---------------------------------------------------------------- report
def cmd_report(args):
    with open(args.ledger, encoding="utf-8") as f:
        ledger = json.load(f)
    files = ledger["files"]
    counts = {}
    for rel, info in files.items():
        c = counts.setdefault(rel, dict((k, 0) for k in CATEGORIES))
        for e in info["entries"]:
            c[e["category"]] += 1
    total = dict((k, sum(c[k] for c in counts.values())) for k in CATEGORIES)

    out = []
    w = out.append
    w("# Ghi chú (comment) đã tách khỏi mã nguồn — Patient Flow System")
    w("")
    w("> Toàn bộ comment trong `src/` đã được gỡ khỏi code để nộp bản code sạch.")
    w("> Nội dung nguyên văn của từng comment được giữ lại ở đây (bản đọc) và trong `comments.json` (bản máy đọc) để có thể thêm lại vào đúng chỗ khi cần.")
    w("> Lập lúc: %s." % ledger["created"])
    w("")
    w("## 1. Các loại ghi chú")
    w("")
    w("| Loại | Ý nghĩa | Số lượng |")
    w("|---|---|---|")
    for k in CATEGORIES:
        w("| `%s` | %s | %d |" % (k, CATEGORY_TITLE[k], total[k]))
    w("| | **Tổng** | **%d** |" % sum(total.values()))
    w("")
    w("## 2. Cách thêm lại vào code")
    w("")
    w("- Cách đơn giản nhất: nhờ trợ lý AI đang hỗ trợ dự án, ví dụ *\"thêm lại các ghi chú điều chỉnh vào code\"* hoặc *\"thêm lại toàn bộ ghi chú\"*.")
    w("- Hoặc tự chạy công cụ đi kèm (cần Python 3), ở thư mục gốc project:")
    w("")
    w("```")
    w("python ghi_chu/comments_tool.py restore --ledger ghi_chu/comments.json")
    w("python ghi_chu/comments_tool.py restore --ledger ghi_chu/comments.json --only adjustment")
    w("python ghi_chu/comments_tool.py restore --ledger ghi_chu/comments.json --only note --file ConsoleIO")
    w("```")
    w("")
    w("- Mỗi comment được gắn với một dòng code (\"gắn với\"). Nếu dòng code đó đã bị sửa sau khi gỡ comment thì công cụ sẽ báo comment nào không đặt lại được thay vì đặt sai chỗ.")
    w("- Phần ghi chú đánh dấu *(đợt 2)* là các điều chỉnh làm trong lần cải tiến thứ hai (bỏ ID, chọn theo tên, gộp luồng lễ tân, lưu cài đặt, ...).")
    w("")
    w("## 3. Thống kê theo file")
    w("")
    w("| File | license | author | adjustment | note |")
    w("|---|---|---|---|---|")
    for rel in sorted(files):
        c = counts[rel]
        w("| `%s` | %d | %d | %d | %d |" % (rel, c["license"], c["author"], c["adjustment"], c["note"]))
    w("")
    w("## 4. Chi tiết từng file")
    w("")
    for rel in sorted(files):
        w("### `%s`" % rel)
        w("")
        for e in files[rel]["entries"]:
            where = "dòng gốc %d" % e["line"] if e["line"] == e["end_line"] else "dòng gốc %d–%d" % (e["line"], e["end_line"])
            tag = e["category"] + (", đợt %d" % e["round"] if e["round"] else "")
            if e["anchor"] is None:
                attach = "cuối file"
            else:
                attach = "`%s`" % e["anchor"].replace("`", "'")
                if len(attach) > 90:
                    attach = attach[:87] + "...`"
            w("- **%s** (%s) — gắn với: %s" % (where, tag, attach))
            w("")
            w("  ```")
            for ln in e["text"]:
                w("  " + ln.rstrip())
            w("  ```")
            w("")
    with open(args.out, "w", encoding="utf-8") as f:
        f.write("\n".join(out) + "\n")
    print("Đã tạo " + args.out)


def main():
    parser = argparse.ArgumentParser(description="Gỡ / thêm lại comment cho mã nguồn Java.")
    sub = parser.add_subparsers(dest="cmd", required=True)
    for name in ("strip", "restore", "report"):
        p = sub.add_parser(name)
        p.add_argument("--root", default=".", help="thư mục gốc project (có thư mục src)")
        p.add_argument("--ledger", default="ghi_chu/comments.json")
        if name == "strip":
            p.add_argument("--force", action="store_true")
        if name == "restore":
            p.add_argument("--only", help="danh sách loại, ví dụ adjustment,note")
            p.add_argument("--file", help="chỉ file có đường dẫn chứa chuỗi này")
            p.add_argument("--dry-run", action="store_true")
        if name == "report":
            p.add_argument("--out", default="ghi_chu/GHI_CHU_CODE.md")
    args = parser.parse_args()
    {"strip": cmd_strip, "restore": cmd_restore, "report": cmd_report}[args.cmd](args)


if __name__ == "__main__":
    main()
