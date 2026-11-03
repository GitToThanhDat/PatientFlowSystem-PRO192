package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public final class TextStorage {

    static final String SEPARATOR = "|";
    static final String LIST_SEPARATOR = ",";

    private static String directory = "data";

    private TextStorage() {
    }

    public static String getDirectory() {
        return directory;
    }

    public static void setDirectory(String dir) {
        if (dir == null || dir.trim().isEmpty()) {
            throw new IllegalArgumentException("Data directory must not be empty.");
        }
        directory = dir;
    }

    static void writeLines(String fileName, String header, List<String> lines) throws IOException {
        File dir = new File(directory);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Cannot create data directory: " + dir.getAbsolutePath());
        }
        File target = new File(dir, fileName);
        File temp = new File(dir, fileName + ".tmp");

        try (BufferedWriter out = Files.newBufferedWriter(temp.toPath(), StandardCharsets.UTF_8)) {
            out.write("# " + header);
            out.newLine();
            for (String line : lines) {
                out.write(line);
                out.newLine();
            }
        } catch (IOException e) {
            temp.delete();
            throw new IOException("Cannot write '" + fileName + "': " + e.getMessage(), e);
        }

        try {
            Files.move(temp.toPath(), target.toPath(),
                    StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(temp.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    static List<String[]> readRows(String fileName) throws IOException {
        File file = new File(directory, fileName);
        List<String[]> rows = new ArrayList<>();
        if (!file.exists()) {
            return rows;
        }
        int lineNumber = 0;
        try (BufferedReader in = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            String line;
            while ((line = in.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1 && line.startsWith("﻿")) {
                    line = line.substring(1);
                }
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }
                rows.add(splitFields(line));
            }
        } catch (java.nio.charset.MalformedInputException e) {
            throw quarantine(fileName, "line " + (lineNumber + 1) + " is not valid UTF-8 text", e);
        }
        return rows;
    }

    static IOException quarantine(String fileName, String reason, Exception cause) {
        File file = new File(directory, fileName);
        File bad = new File(directory, fileName + ".corrupt");
        String moved = "";
        try {
            Files.move(file.toPath(), bad.toPath(), StandardCopyOption.REPLACE_EXISTING);
            moved = " The old file was kept as '" + bad.getName() + "'.";
        } catch (IOException ignore) {
        }
        return new IOException("Cannot read '" + fileName + "' (" + reason + ")."
                + moved + " Starting with empty data for it.", cause);
    }

    static String joinFields(String... fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                sb.append(SEPARATOR);
            }
            sb.append(escape(fields[i]));
        }
        return sb.toString();
    }

    static String[] splitFields(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\\' && i + 1 < line.length()) {
                char next = line.charAt(++i);
                switch (next) {
                    case 'n': current.append('\n'); break;
                    case 'r': current.append('\r'); break;
                    case 'p': current.append('|'); break;
                    default: current.append(next); break;
                }
            } else if (c == '|') {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString());
        return fields.toArray(new String[0]);
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\")
                .replace("|", "\\p")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    static String emptyToNull(String value) {
        return (value == null || value.isEmpty()) ? null : value;
    }

    static String joinList(List<String> values) {
        StringBuilder sb = new StringBuilder();
        for (String v : values) {
            if (sb.length() > 0) {
                sb.append(LIST_SEPARATOR);
            }
            sb.append(v);
        }
        return sb.toString();
    }

    static List<String> splitList(String value) {
        List<String> result = new ArrayList<>();
        if (value == null || value.trim().isEmpty()) {
            return result;
        }
        for (String part : value.split(LIST_SEPARATOR)) {
            if (!part.trim().isEmpty()) {
                result.add(part.trim());
            }
        }
        return result;
    }

    static void requireFields(String[] row, int count, String fileName) throws IOException {
        if (row.length < count) {
            throw quarantine(fileName, "a line has " + row.length + " fields, expected " + count, null);
        }
    }
}
