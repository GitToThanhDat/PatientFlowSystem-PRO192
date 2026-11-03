# Ghi chú (comment) đã tách khỏi mã nguồn — Patient Flow System

> Toàn bộ comment trong `src/` đã được gỡ khỏi code để nộp bản code sạch.
> Nội dung nguyên văn của từng comment được giữ lại ở đây (bản đọc) và trong `comments.json` (bản máy đọc) để có thể thêm lại vào đúng chỗ khi cần.
> Lập lúc: 2026-11-01 16:28.

## 1. Các loại ghi chú

| Loại | Ý nghĩa | Số lượng |
|---|---|---|
| `license` | Phần đầu file do NetBeans tạo | 32 |
| `author` | Javadoc ghi tên người viết (@author) | 32 |
| `adjustment` | Ghi chú điều chỉnh code đã viết ban đầu | 81 |
| `note` | Ghi chú giải thích / TODO | 22 |
| | **Tổng** | **167** |

## 2. Cách thêm lại vào code

- Cách đơn giản nhất: nhờ trợ lý AI đang hỗ trợ dự án, ví dụ *"thêm lại các ghi chú điều chỉnh vào code"* hoặc *"thêm lại toàn bộ ghi chú"*.
- Hoặc tự chạy công cụ đi kèm (cần Python 3), ở thư mục gốc project:

```
python ghi_chu/comments_tool.py restore --ledger ghi_chu/comments.json
python ghi_chu/comments_tool.py restore --ledger ghi_chu/comments.json --only adjustment
python ghi_chu/comments_tool.py restore --ledger ghi_chu/comments.json --only note --file ConsoleIO
```

- Mỗi comment được gắn với một dòng code ("gắn với"). Nếu dòng code đó đã bị sửa sau khi gỡ comment thì công cụ sẽ báo comment nào không đặt lại được thay vì đặt sai chỗ.
- Phần ghi chú đánh dấu *(đợt 2)* là các điều chỉnh làm trong lần cải tiến thứ hai (bỏ ID, chọn theo tên, gộp luồng lễ tân, lưu cài đặt, ...).
- Phần *(đợt 3)* và *(đợt 4)* xem các mục ngay bên dưới. Entry `obsolete` không được đặt lại vào code. Bảng thống kê và chi tiết từng file ở mục 3–4 là **bản gốc lúc gỡ comment** (trước đợt 3), giữ nguyên để đối chiếu.

## Đợt 3 (02/11/2026) – lưu trữ txt, sửa lỗi tiếng Việt, làm lại New visit

Code vẫn được giữ sạch (không có comment). Các ghi chú của đợt 3 chỉ nằm trong `comments.json` và mục này; lệnh `restore` sẽ đặt chúng vào code nếu cần.

**Tóm tắt thay đổi**

- Lỗi `Bác Sĩ Nhi` → `B c S? Nhi`: do bảng mã khi **nhập từ bàn phím**, không phải do serialization. `ConsoleIO` nay đọc/ghi console bằng UTF-8 cố định (`-Dconsole.encoding=...` để đổi); `Main` gọi `ConsoleIO.setup()` đầu tiên; thêm `run.bat` (`chcp 65001`).
- Lưu trữ: bỏ `SerialStorage` và `implements Serializable`; thêm `TextStorage` (file `.txt` UTF-8, phân tách `|`, ghi file tạm rồi move, file hỏng → `*.corrupt`). 5 file: `departments.txt`, `patients.txt`, `records.txt`, `users.txt`, `settings.txt`.
- New visit: bỏ "Register new patient only"; đăng ký luôn kèm record và khoa; ngày sinh bắt buộc; kiểm tra trùng theo tên chuẩn hóa + ngày sinh với 2 trường hợp (đang trong hàng đợi / đã từng khám); chọn khoa bằng số No.; nhập sai thì hỏi lại đúng bước đó.
- Kéo theo: Remove patient chỉ cho xóa bệnh nhân chưa từng khám (xóa luôn lượt đang chờ); Search visit records có thêm cột Code; menu lễ tân còn 6 mục.
- `RecordService`, `DoctorMenu`: đổi `import model.*` thành import tường minh vì `java.lang.Record` (JDK 16+) trùng tên `model.Record`.

**Số lượng ghi chú sau đợt 3:** 167 entry cũ + 27 entry mới = 194; trong đó 20 entry cũ đánh dấu `obsolete` (không còn đúng, bị bỏ qua khi restore), 7 entry cũ được gắn lại vào dòng code mới. Restore toàn bộ hiện đặt lại được **174** comment, 0 lỗi.

**Ghi chú mới của đợt 3**

- `src/data/DepartmentData.java` — gắn với: `private static final String FILE_NAME = "departments.txt";`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 3): lưu departments.txt: id|name|waitingQueue (các record id cách nhau bởi dấu phẩy).
  ```

- `src/data/PatientData.java` — gắn với: `private static final String FILE_NAME = "patients.txt";`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 3): lưu patients.txt: id|name|dateOfBirth (yyyy-MM-dd)|recordIds.
  ```

- `src/data/RecordData.java` — gắn với: `public class RecordData {`

  ```
  /**
   *
   * @author khanh3327
   */
  ```

- `src/data/RecordData.java` — gắn với: `private static final String FILE_NAME = "records.txt";`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 3): lưu records.txt. Dòng RECORD = 1 lượt khám; dòng SEGMENT = 1 lần khám của lượt đó (HISTORY = đã xong, CURRENT = đang khám).
          // Đọc RECORD trước rồi gắn SEGMENT theo recordId; thời gian ghi tới giây cho dễ đọc.
  ```

- `src/model/Department.java` — gắn với: `public class Department {`

  ```
  //có điều chỉnh với cách viết ban đầu (đợt 3): bỏ implements Serializable và serialVersionUID vì không còn lưu bằng serialization.
  ```

- `src/model/Patient.java` — gắn với: `public class Patient {`

  ```
  //có điều chỉnh với cách viết ban đầu (đợt 3): bỏ implements Serializable và serialVersionUID vì không còn lưu bằng serialization.
  ```

- `src/model/Record.java` — gắn với: `public class Record {`

  ```
  //có điều chỉnh với cách viết ban đầu (đợt 3): bỏ implements Serializable và serialVersionUID vì không còn lưu bằng serialization.
  ```

- `src/model/User.java` — gắn với: `public abstract class User {`

  ```
  //abstract class, phải chọn luôn một vị trí chức vụ.
  ```

- `src/model/User.java` — gắn với: `public abstract class User {`

  ```
  //có điều chỉnh với cách viết ban đầu (đợt 3): bỏ implements Serializable và serialVersionUID vì không còn lưu bằng serialization.
  ```

- `src/model/VisitSegment.java` — gắn với: `public class VisitSegment {`

  ```
  //có điều chỉnh với cách viết ban đầu (đợt 3): bỏ implements Serializable và serialVersionUID vì không còn lưu bằng serialization.
  ```

- `src/service/PatientService.java` — gắn với: `public List<Patient> findByNameAndDateOfBirth(String name, LocalDate dateOfBirth) {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 3): dùng để kiểm tra trùng khi New visit: trùng khi tên đã chuẩn hóa (cleanName: bỏ dấu, bỏ khoảng trắng, chữ thường) VÀ ngày sinh bằng nhau.
  ```

- `src/service/PatientService.java` — gắn với: `public void checkDateOfBirth(LocalDate dateOfBirth) throws InvalidInputException {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 3): ngày sinh bắt buộc (trước đây null được bỏ qua); checkName/checkDateOfBirth đổi sang public để UI hỏi lại ngay khi nhập sai.
  ```

- `src/service/RecordService.java` — gắn với: `import exception.DuplicateDataException;`

  ```
  //có điều chỉnh với cách viết ban đầu (đợt 3): bỏ import model.* / java.util.* vì từ JDK 16 có java.lang.Record trùng tên với model.Record
  // (lỗi 'reference to Record is ambiguous' nếu không biên dịch với --release 8). Bỏ hasRecordsForPatient vì không còn dùng.
  ```

- `src/service/RecordService.java` — gắn với: `public String registerNewPatientVisit(`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 3): bệnh nhân mới luôn được tạo cùng với lượt khám. Kiểm tra khoa và mức ưu tiên TRƯỚC khi tạo bệnh nhân,
          // nên không bao giờ có bệnh nhân không có record.
  ```

- `src/service/RecordService.java` — gắn với: `public void removePatientWithWaitingVisits(String patientId)`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 3): vì bệnh nhân nào cũng có record, Remove patient chỉ được phép khi mọi lượt khám của bệnh nhân còn WAITING;
          // các lượt đó bị xóa khỏi danh sách và khỏi hàng đợi của khoa trước khi xóa bệnh nhân.
  ```

- `src/ui/ConsoleIO.java` — gắn với: `private static final Charset CONSOLE_CHARSET = resolveCharset();`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 3): đọc bàn phím và in màn hình bằng bảng mã cố định (mặc định UTF-8, đổi được bằng -Dconsole.encoding=...).
      // Trước đây new Scanner(System.in) và System.out dùng bảng mã mặc định của JVM. Bảng mã này đổi theo phiên bản JDK (JDK 18+ mặc định UTF-8, JDK 19+ System.out theo stdout.encoding)
      // và thường khác bảng mã của console Windows/NetBeans -> gõ 'Bác Sĩ Nhi' bị thành 'B c S? Nhi'. Lỗi xảy ra lúc nhập, không phải do serialization.
  ```

- `src/ui/ConsoleIO.java` — gắn với: `public static void setup() {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 3): gọi đầu tiên trong main để System.out in ra bằng cùng bảng mã với Scanner.
  ```

- `src/ui/DoctorMenu.java` — gắn với: `import exception.InvalidInputException;`

  ```
  //có điều chỉnh với cách viết ban đầu (đợt 3): bỏ import model.* / service.* / java.util.* / java.io.* (java.io không dùng) vì java.lang.Record (JDK 16+) trùng tên với model.Record.
  ```

- `src/ui/Main.java` — gắn với: `ConsoleIO.setup();`

  ```
          //có điều chỉnh với cách viết ban đầu (đợt 3): chuyển System.out sang UTF-8 trước khi in bất cứ thứ gì.
  ```

- `src/ui/ReceptionistMenu.java` — gắn với: `private void newVisit() throws InvalidInputException, NotFoundException, DuplicateDataExce`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 3): bỏ 'Register new patient only'; mỗi lần đăng ký đều tạo record kèm khoa. Ngày sinh bắt buộc.
          // Trùng tên chuẩn hóa + ngày sinh: người trùng đang trong hàng đợi -> hỏi có phải bệnh nhân mới (N = hủy);
          // người trùng đã từng khám -> hỏi có phải cùng người (y = record mới gắn mã cũ, N = bệnh nhân mới mã mới). Nhập sai ở bước nào thì hỏi lại bước đó.
  ```

- `src/ui/ReceptionistMenu.java` — gắn với: `private Department chooseDepartment() {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 3): chọn khoa bằng số No. trong bảng (vẫn nhận tên hoặc tên kèm ?); sai thì báo lỗi và hỏi lại, không quay về menu.
  ```

- `src/ui/ReceptionistMenu.java` — gắn với: `private void removePatient() throws InvalidInputException, NotFoundException {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 3): chỉ xóa được bệnh nhân chưa từng được khám, xóa luôn lượt khám đang chờ.
  ```

- `src/data/TextStorage.java` — gắn với: `public final class TextStorage {`

  ```
  //có điều chỉnh với cách viết ban đầu (đợt 3): thay cho SerialStorage. Mỗi danh sách lưu thành 1 file .txt UTF-8 (ghi rõ UTF-8 để không phụ thuộc JDK),
  // dòng đầu '# ...' là tên cột, mỗi đối tượng 1 dòng, các trường cách nhau bởi '|'. Ký tự '\', '|', xuống dòng được ghi thành '\\', '\p', '\n'.
  // Giữ cách ghi file tạm rồi move (không mất dữ liệu khi lỗi giữa chừng) và đổi tên file hỏng thành *.corrupt như SerialStorage cũ.
  ```

- `src/data/UserData.java` — gắn với: `private static final String FILE_NAME = "users.txt";`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 3): lưu users.txt: role|id|userName|password|fullName|active|departmentId|currentRecordId (2 cột cuối chỉ dùng cho DOCTOR).
  ```

- `src/data/SettingsData.java` — gắn với: `private static final String FILE_NAME = "settings.txt";`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 3): lưu settings.txt: key|value.
  ```

- `src/model/AppSettings.java` — gắn với: `public class AppSettings {`

  ```
  //có điều chỉnh với cách viết ban đầu (đợt 3): bỏ implements Serializable và serialVersionUID vì không còn lưu bằng serialization.
  ```

- `src/ui/Picker.java` — gắn với: `public static Department departmentFromText(DepartmentService service, String text)`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 3): thay patientFromText (không còn dùng) bằng departmentFromText và patientFromList cho luồng New visit mới.
  ```

**Ghi chú cũ không còn đúng (obsolete)**

| File | Gắn với (cũ) | Lý do |
|---|---|---|
| `src/data/DepartmentData.java` | `public class DepartmentData {` | Đợt 3 bỏ serialization/SerialStorage, quay lại file txt (TextStorage). |
| `src/data/DepartmentData.java` | `private static final String FILE_NAME = "departments.dat";` | Đợt 3 đổi lại sang departments.txt. |
| `src/data/PatientData.java` | `public class PatientData {` | Đợt 3 bỏ serialization/SerialStorage, quay lại file txt (TextStorage). |
| `src/data/PatientData.java` | `private static final String FILE_NAME = "patients.dat";` | Đợt 3 đổi lại sang patients.txt. |
| `src/data/RecordData.java` | `public class RecordData {` | Đợt 3 bỏ serialization/SerialStorage, quay lại file txt (TextStorage). |
| `src/data/RecordData.java` | `private static final String FILE_NAME = "records.dat";` | Đợt 3 đổi lại sang records.txt. |
| `src/model/Admin.java` | `private static final long serialVersionUID = 1L;` | Đợt 3 bỏ serialization nên đã xóa serialVersionUID. |
| `src/model/Department.java` | `public class Department implements Serializable {` | Đợt 3 bỏ implements Serializable. |
| `src/model/Department.java` | `private static final long serialVersionUID = 1L;` | Đợt 3 bỏ serialization nên đã xóa serialVersionUID. |
| `src/model/Doctor.java` | `private static final long serialVersionUID = 1L;` | Đợt 3 bỏ serialization nên đã xóa serialVersionUID. |
| `src/model/Patient.java` | `public class Patient implements Serializable {` | Đợt 3 bỏ implements Serializable. |
| `src/model/Patient.java` | `private static final long serialVersionUID = 1L;` | Đợt 3 bỏ serialization nên đã xóa serialVersionUID. |
| `src/model/Receptionist.java` | `private static final long serialVersionUID = 1L;` | Đợt 3 bỏ serialization nên đã xóa serialVersionUID. |
| `src/model/Record.java` | `private static final long serialVersionUID = 1L;` | Đợt 3 bỏ serialization nên đã xóa serialVersionUID. |
| `src/model/User.java` | `public abstract class User implements Serializable {` | Đợt 3 bỏ implements Serializable; phần ghi chú abstract class được tách thành entry mới. |
| `src/model/User.java` | `private static final long serialVersionUID = 1L;` | Đợt 3 bỏ serialization nên đã xóa serialVersionUID. |
| `src/model/VisitSegment.java` | `private static final long serialVersionUID = 1L;` | Đợt 3 bỏ serialization nên đã xóa serialVersionUID. |
| `src/ui/MainMenu.java` | `private void loadAll() {` | Đợt 3: settings lưu ở settings.txt. |
| `src/ui/MainMenu.java` | `private void saveAll() {` | Đợt 3: settings lưu ở settings.txt. |
| `src/ui/ReceptionistMenu.java` | `private void registerPatient() throws InvalidInputException ` | Đợt 3 bỏ chức năng 'Register new patient only' (registerPatient). |

**Ghi chú cũ được gắn lại**

- `src/model/Department.java`: author → nay gắn với `public class Department {`
- `src/model/Patient.java`: author → nay gắn với `public class Patient {`
- `src/model/Record.java`: author → nay gắn với `public class Record {`
- `src/model/User.java`: author → nay gắn với `public abstract class User {`
- `src/model/VisitSegment.java`: author → nay gắn với `public class VisitSegment {`
- `src/service/PatientService.java`: note → nay gắn với `public String checkName(String name) throws InvalidInputException {`
- `src/ui/ConsoleIO.java`: note → nay gắn với `private static final Scanner SC = new Scanner(new InputStreamReader(System.in, CONSOLE_CHA`


## Đợt 4 (02/11/2026) – tách util, thêm lần khám gần nhất

**Tóm tắt thay đổi**

- `util/IdGenerator` (mới): cấp mã `dep-N / pat-N / vis-N`, thay cho biến đếm viết lặp ở `DepartmentService`, `PatientService`, `RecordService`.
- `util/Validator` (mới): kiểm tra không rỗng, cấm `?` và `0`, ngày sinh (không ở tương lai, không trước 1900); dùng ở `AuthService`, `DepartmentService`, `PatientService`. Thông báo lỗi giữ nguyên chữ.
- `util/DateUtils` (mới): định dạng/parse ngày chuyển từ `ConsoleIO` sang; thêm chuyển đổi ISO khi lưu file cho `PatientData`, `RecordData`.
- `util/TextUtils`: thêm `sameName`, `containsKeyword`.
- New visit: bảng người trùng "đã từng khám" có thêm `No.`, `Last visit`, `Last department` (`RecordService.findLastVisit`); nhiều người trùng thì chọn bằng số. Bỏ `Picker.patientFromList`.

**Số lượng ghi chú sau đợt 4:** 210 entry, trong đó 27 entry `obsolete`. Restore toàn bộ đặt lại được **183** comment, 0 lỗi.

**Ghi chú mới của đợt 4**

- `src/service/AuthService.java` — gắn với: `String cleanUserName = Validator.requireName(userName, "Username");`

  ```
          //có điều chỉnh với cách viết ban đầu (đợt 4): kiểm tra rỗng / '?' / "0" của username dùng chung util.Validator.
  ```

- `src/service/DepartmentService.java` — gắn với: `private final IdGenerator _ids;`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 4): dùng util.IdGenerator thay cho biến đếm idCounter tự quản lý.
  ```

- `src/service/PatientService.java` — gắn với: `private final IdGenerator _ids;`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 4): dùng util.IdGenerator thay cho biến đếm idCounter tự quản lý.
  ```

- `src/service/RecordService.java` — gắn với: `private final IdGenerator ids;`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 4): dùng util.IdGenerator thay cho biến đếm idCounter tự quản lý.
  ```

- `src/service/RecordService.java` — gắn với: `public Record findLastVisit(String patientId)`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 4): lấy lượt khám gần nhất của bệnh nhân để hiện ở bảng 'visited before'.
          // Dùng !isBefore (bằng hoặc muộn hơn): thời gian lưu file tới giây nên hai lượt cùng giây sẽ bằng nhau; khi đó lượt tạo sau (đứng sau trong danh sách) được chọn.
  ```

- `src/ui/ConsoleIO.java` — gắn với: `public class ConsoleIO {`

  ```
  //có điều chỉnh với cách viết ban đầu (đợt 4): bỏ formatDate / formatDateTime / parseDate, chuyển sang util.DateUtils; ConsoleIO chỉ còn nhập xuất.
  ```

- `src/ui/DoctorMenu.java` — gắn với: `return DateUtils.parseDate(value).atStartOfDay();`

  ```
          //có điều chỉnh với cách viết ban đầu (đợt 2, sửa ở đợt 4): ngày nhập theo dd/MM/yyyy, dùng chung DateUtils.parseDate
  ```

- `src/ui/ReceptionistMenu.java` — gắn với: `private void printVisitedBefore(List<Patient> patients) {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 4): bảng người trùng 'đã từng khám' có thêm No., Last visit, Last department;
          // nhiều người trùng thì chọn bằng số ở choosePatient (thay cho Picker.patientFromList).
  ```

- `src/util/TextUtils.java` — gắn với: `public static boolean sameName(String a, String b) {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 4): sameName / containsKeyword thay cho cleanName(a).equals/contains(cleanName(b)) lặp ở 3 service.
  ```

- `src/ui/Picker.java` — gắn với: `public static Department departmentFromText(DepartmentService service, String text)`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 3–4): thay patientFromText (không còn dùng) bằng departmentFromText cho luồng New visit.
  ```

- `src/util/Validator.java` — gắn với: `public final class Validator {`

  ```
  //có điều chỉnh với cách viết ban đầu (đợt 4): gom các kiểm tra trước đây lặp ở AuthService, DepartmentService, PatientService: không rỗng,
  // không chứa '?' và không là "0", ngày không ở tương lai và không trước 1900. Thông báo lỗi giữ nguyên chữ như cũ (nhãn truyền vào).
  ```

- `src/util/IdGenerator.java` — gắn với: `public class IdGenerator {`

  ```
  //có điều chỉnh với cách viết ban đầu (đợt 4): gom phần cấp mã dep-N / pat-N / vis-N trước đây viết lặp ở DepartmentService, PatientService, RecordService
  // (bộ đếm, getNextNumber, ensureNextNumberAtLeast, tính lại = số lớn nhất + 1 sau khi nạp file). Mỗi service giữ một IdGenerator với tiền tố riêng.
  ```

- `src/util/DateUtils.java` — gắn với: `public final class DateUtils {`

  ```
  //có điều chỉnh với cách viết ban đầu (đợt 4): chuyển định dạng / parse ngày từ ConsoleIO sang util vì đây không phải nhập xuất màn hình;
  // thêm toStorage / dateFromStorage / timeFromStorage (ISO, tới giây) dùng chung cho PatientData và RecordData.
  ```

**Ghi chú cũ chuyển theo code sang util**

- từ `src/service/DepartmentService.java` sang `src/util/Validator.java`, gắn với `if (text.contains("?") || text.equals("0")) {`
- từ `src/service/DepartmentService.java` sang `src/util/IdGenerator.java`, gắn với `nextNumber = max + 1;`
- từ `src/service/RecordService.java` sang `src/util/IdGenerator.java`, gắn với `int max = 0;`

**Ghi chú cũ được gắn lại vào dòng code mới**

- `src/service/DepartmentService.java`: nay gắn với `String newId = _ids.next();`
- `src/service/PatientService.java`: nay gắn với `if (TextUtils.containsKeyword(p.getName(), keyword)) {`
- `src/ui/DoctorMenu.java`: nay gắn với `System.out.println("Patient's visit time: " + DateUtils.format(record.getVisitTime()));`
- `src/ui/DoctorMenu.java`: nay gắn với `DateUtils.format(record.getVisitTime())`

**Ghi chú cũ không còn đúng (obsolete) ở đợt 4**

| File | Gắn với (cũ) | Lý do |
|---|---|---|
| `src/service/AuthService.java` | `return value.trim();` | Đợt 4: kiểm tra chuyển vào util.Validator. |
| `src/service/DepartmentService.java` | `if (name.contains("?") \|\| name.trim().equals("0")) {` | Đợt 4: kiểm tra tên chuyển vào util.Validator.requireName. |
| `src/service/DepartmentService.java` | `_idCounter = max + 1;` | Đợt 4: tính bộ đếm chuyển vào util.IdGenerator.resetAfter. |
| `src/service/PatientService.java` | `if (name.contains("?") \|\| name.trim().equals("0")) {` | Đợt 4: kiểm tra tên chuyển vào util.Validator.requireName (ghi chú giống DepartmentService, đã chuyển một bản sang Validator). |
| `src/service/RecordService.java` | `int maxId = 0;` | Đợt 4: tính bộ đếm chuyển vào util.IdGenerator.resetAfter. |
| `src/ui/DoctorMenu.java` | `return ConsoleIO.parseDate(value).atStartOfDay();` | Đợt 4: parseDate chuyển sang util.DateUtils. |
| `src/ui/Picker.java` | `public static Department departmentFromText(DepartmentServic` | Đợt 4 bỏ patientFromList; bảng chọn người trùng chuyển vào ReceptionistMenu. |


## 3. Thống kê theo file

| File | license | author | adjustment | note |
|---|---|---|---|---|
| `src/data/DepartmentData.java` | 0 | 0 | 2 | 1 |
| `src/data/PatientData.java` | 0 | 0 | 2 | 0 |
| `src/data/RecordData.java` | 1 | 1 | 1 | 0 |
| `src/exception/AuthenticationException.java` | 1 | 1 | 0 | 1 |
| `src/exception/DuplicateDataException.java` | 1 | 1 | 0 | 0 |
| `src/exception/InvalidInputException.java` | 1 | 1 | 0 | 0 |
| `src/exception/NotFoundException.java` | 1 | 1 | 0 | 0 |
| `src/model/Admin.java` | 1 | 1 | 1 | 0 |
| `src/model/Department.java` | 1 | 1 | 2 | 0 |
| `src/model/Doctor.java` | 1 | 1 | 1 | 0 |
| `src/model/Patient.java` | 1 | 1 | 3 | 0 |
| `src/model/Priority.java` | 1 | 1 | 0 | 0 |
| `src/model/Receptionist.java` | 1 | 1 | 1 | 0 |
| `src/model/Record.java` | 1 | 1 | 1 | 0 |
| `src/model/RecordFilter.java` | 1 | 1 | 0 | 1 |
| `src/model/RecordStatus.java` | 1 | 1 | 1 | 0 |
| `src/model/Role.java` | 1 | 1 | 0 | 0 |
| `src/model/User.java` | 1 | 1 | 2 | 2 |
| `src/model/VisitSegment.java` | 1 | 1 | 1 | 0 |
| `src/service/AuthService.java` | 1 | 1 | 10 | 1 |
| `src/service/DepartmentService.java` | 0 | 0 | 3 | 4 |
| `src/service/ExaminationService.java` | 1 | 1 | 0 | 0 |
| `src/service/PatientService.java` | 1 | 1 | 2 | 3 |
| `src/service/QueueService.java` | 1 | 1 | 0 | 0 |
| `src/service/RecordService.java` | 1 | 1 | 6 | 0 |
| `src/strategy/FCFSStrategy.java` | 1 | 1 | 0 | 0 |
| `src/strategy/PriorityStrategy.java` | 1 | 1 | 0 | 0 |
| `src/strategy/QueueStrategy.java` | 1 | 1 | 0 | 0 |
| `src/ui/AccountMenu.java` | 1 | 1 | 7 | 0 |
| `src/ui/AdminMenu.java` | 0 | 0 | 7 | 0 |
| `src/ui/ConsoleIO.java` | 1 | 1 | 2 | 4 |
| `src/ui/DoctorMenu.java` | 1 | 1 | 15 | 4 |
| `src/ui/Main.java` | 1 | 1 | 1 | 0 |
| `src/ui/MainMenu.java` | 1 | 1 | 3 | 0 |
| `src/ui/ReceptionistMenu.java` | 1 | 1 | 7 | 1 |
| `src/util/TextUtils.java` | 1 | 1 | 0 | 0 |

## 4. Chi tiết từng file

### `src/data/DepartmentData.java`

- **dòng gốc 7–8** (adjustment, đợt 1) — gắn với: `public class DepartmentData {`

  ```
  //có điều chỉnh với cách viết ban đầu: DepartmentData trước đây ghi file departments.txt (id|name) bằng BufferedWriter/BufferedReader.
  // Nay chuyển sang serialization (departments.dat) thông qua SerialStorage. Chữ ký save/load giữ nguyên.
  ```

- **dòng gốc 9** (note) — gắn với: `public class DepartmentData {`

  ```
  //Repo liên quan đến nhập xuất... File I/O
  ```

- **dòng gốc 11** (adjustment, đợt 1) — gắn với: `private static final String FILE_NAME = "departments.dat";`

  ```
  //có điều chỉnh với cách viết ban đầu: trước là data/departments.txt
  ```

### `src/data/PatientData.java`

- **dòng gốc 7–8** (adjustment, đợt 1) — gắn với: `public class PatientData {`

  ```
  //có điều chỉnh với cách viết ban đầu: PatientData trước đây ghi file patients.txt (id|name) bằng BufferedWriter/BufferedReader.
  // Nay chuyển sang serialization (patients.dat) thông qua SerialStorage. Chữ ký save/load giữ nguyên.
  ```

- **dòng gốc 11** (adjustment, đợt 1) — gắn với: `private static final String FILE_NAME = "patients.dat";`

  ```
  //có điều chỉnh với cách viết ban đầu: trước là data/patients.txt
  ```

### `src/data/RecordData.java`

- **dòng gốc 1–4** (license) — gắn với: `package data;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 10–19** (author, đợt 1) — gắn với: `public class RecordData {`

  ```
  /**
   *
   * @author khanh3327
   *
   * //có điều chỉnh với cách viết ban đầu: bản đầu của Khánh đã dùng serialization nhưng
   *   (1) load() ném FileNotFoundException khi chưa có file records.dat (lần chạy đầu tiên),
   *   (2) không tạo thư mục data/ nên save() lỗi khi chưa có thư mục,
   *   (3) bỏ qua lỗi file hỏng bằng cách trả list rỗng (lần save sau sẽ đè mất dữ liệu cũ).
   *   Nay dùng chung SerialStorage để xử lý cả 3 điểm trên; chữ ký save/load giữ nguyên.
   */
  ```

- **dòng gốc 22** (adjustment, đợt 1) — gắn với: `private static final String FILE_NAME = "records.dat";`

  ```
  //có điều chỉnh với cách viết ban đầu: trước là FILE_PATH = "data/records.dat"
  ```

### `src/exception/AuthenticationException.java`

- **dòng gốc 1–4** (license) — gắn với: `package exception;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 7–10** (author) — gắn với: `public class AuthenticationException extends Exception {`

  ```
  /**
   *
   * @author ACER
   */
  ```

- **dòng gốc 13** (note) — gắn với: `super(message);`

  ```
  // thằng super này sẽ lấy thông tin message này để sau này thông qua class Exception để sử dụng các hàm khác nó sẽ hiển thị thông tin lỗi đúng như ý mình muốn.
  ```

### `src/exception/DuplicateDataException.java`

- **dòng gốc 1–4** (license) — gắn với: `package exception;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 7–10** (author) — gắn với: `public class DuplicateDataException extends Exception {`

  ```
  /**
   *
   * @author ACER
   */
  ```

### `src/exception/InvalidInputException.java`

- **dòng gốc 1–4** (license) — gắn với: `package exception;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 7–10** (author) — gắn với: `public class InvalidInputException extends Exception {`

  ```
  /**
   *
   * @author ACER
   */
  ```

### `src/exception/NotFoundException.java`

- **dòng gốc 1–4** (license) — gắn với: `package exception;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 7–10** (author) — gắn với: `public class NotFoundException extends Exception {`

  ```
  /**
   *
   * @author ACER
   */
  ```

### `src/model/Admin.java`

- **dòng gốc 1–4** (license) — gắn với: `package model;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 7–10** (author) — gắn với: `public class Admin extends User {`

  ```
  /**
   *
   * @author ACER
   */
  ```

- **dòng gốc 12** (adjustment, đợt 1) — gắn với: `private static final long serialVersionUID = 1L;`

  ```
  //có điều chỉnh với cách viết ban đầu: thêm để lưu bằng serialization
  ```

### `src/model/Department.java`

- **dòng gốc 1–4** (license) — gắn với: `package model;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 9–12** (author) — gắn với: `public class Department implements Serializable {`

  ```
  /**
   *
   * @author ACER
   */
  ```

- **dòng gốc 13** (adjustment, đợt 1) — gắn với: `public class Department implements Serializable {`

  ```
  //có điều chỉnh với cách viết ban đầu: thêm implements Serializable để lưu bằng serialization
  ```

- **dòng gốc 14** (adjustment, đợt 1) — gắn với: `private static final long serialVersionUID = 1L;`

  ```
  //có điều chỉnh với cách viết ban đầu: thêm để lưu bằng serialization
  ```

### `src/model/Doctor.java`

- **dòng gốc 1–4** (license) — gắn với: `package model;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 7–10** (author) — gắn với: `public class Doctor extends User{`

  ```
  /**
   *
   * @author ACER
   */
  ```

- **dòng gốc 12** (adjustment, đợt 1) — gắn với: `private static final long serialVersionUID = 1L;`

  ```
  //có điều chỉnh với cách viết ban đầu: thêm để lưu bằng serialization
  ```

### `src/model/Patient.java`

- **dòng gốc 1–4** (license) — gắn với: `package model;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 10–13** (author) — gắn với: `public class Patient implements Serializable {`

  ```
  /**
   *
   * @author ACER
   */
  ```

- **dòng gốc 14** (adjustment, đợt 1) — gắn với: `public class Patient implements Serializable {`

  ```
  //có điều chỉnh với cách viết ban đầu: thêm implements Serializable để lưu bằng serialization
  ```

- **dòng gốc 15** (adjustment, đợt 1) — gắn với: `private static final long serialVersionUID = 1L;`

  ```
  //có điều chỉnh với cách viết ban đầu: thêm để lưu bằng serialization
  ```

- **dòng gốc 22** (adjustment, đợt 2) — gắn với: `public Patient(String id, String name, LocalDate dateOfBirth){`

  ```
  //có điều chỉnh với cách viết ban đầu (đợt 2): thêm tham số dateOfBirth để phân biệt bệnh nhân trùng tên
  ```

### `src/model/Priority.java`

- **dòng gốc 1–4** (license) — gắn với: `package model;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
   */
  ```

- **dòng gốc 7–10** (author) — gắn với: `public enum Priority {`

  ```
  /**
   *
   * @author ACER
   */
  ```

### `src/model/Receptionist.java`

- **dòng gốc 1–4** (license) — gắn với: `package model;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 7–10** (author) — gắn với: `public class Receptionist extends User {`

  ```
  /**
   *
   * @author ACER
   */
  ```

- **dòng gốc 12** (adjustment, đợt 1) — gắn với: `private static final long serialVersionUID = 1L;`

  ```
  //có điều chỉnh với cách viết ban đầu: thêm để lưu bằng serialization
  ```

### `src/model/Record.java`

- **dòng gốc 1–4** (license) — gắn với: `package model;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 11–14** (author) — gắn với: `public class Record implements Serializable {`

  ```
  /**
   *
   * @author algori07
   */
  ```

- **dòng gốc 16** (adjustment, đợt 1) — gắn với: `private static final long serialVersionUID = 1L;`

  ```
  //có điều chỉnh với cách viết ban đầu: thêm để lưu bằng serialization
  ```

### `src/model/RecordFilter.java`

- **dòng gốc 1–4** (license) — gắn với: `package model;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 9–12** (author) — gắn với: `public class RecordFilter{`

  ```
  /**
   *
   * @author ACER
   */
  ```

- **dòng gốc 16** (note) — gắn với: `private String patientNameKeyword = null;`

  ```
      // only search by keyword when patientId is null
  ```

### `src/model/RecordStatus.java`

- **dòng gốc 1–4** (license) — gắn với: `package model;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
   */
  ```

- **dòng gốc 7–10** (author) — gắn với: `public enum RecordStatus {`

  ```
  /**
   *
   * @author ACER
   */
  ```

- **dòng gốc 14** (adjustment, đợt 2) — gắn với: `DONE`

  ```
  //có điều chỉnh với cách viết ban đầu (đợt 2): bỏ hai trạng thái CALLED và CANCELLED vì không có thao tác nào đặt ra chúng
  ```

### `src/model/Role.java`

- **dòng gốc 1–4** (license) — gắn với: `package model;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
   */
  ```

- **dòng gốc 7–10** (author) — gắn với: `public enum Role {`

  ```
  /**
   *
   * @author ACER
   */
  ```

### `src/model/User.java`

- **dòng gốc 1–4** (license) — gắn với: `package model;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 9–12** (author) — gắn với: `public abstract class User implements Serializable {`

  ```
  /**
   *
   * @author ACER
   */
  ```

- **dòng gốc 13** (adjustment, đợt 1) — gắn với: `public abstract class User implements Serializable {`

  ```
  //abstract class, phải chọn luôn một vị trí chức vụ. //có điều chỉnh với cách viết ban đầu: thêm implements Serializable
  ```

- **dòng gốc 14** (adjustment, đợt 1) — gắn với: `private static final long serialVersionUID = 1L;`

  ```
  //có điều chỉnh với cách viết ban đầu: thêm để lưu bằng serialization
  ```

- **dòng gốc 30** (note) — gắn với: `public String getId(){`

  ```
      //Get
  ```

- **dòng gốc 49** (note) — gắn với: `public void setUserName(String userName){`

  ```
      //Set
  ```

### `src/model/VisitSegment.java`

- **dòng gốc 1–4** (license) — gắn với: `package model;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 10–13** (author) — gắn với: `public class VisitSegment implements Serializable {`

  ```
  /**
   *
   * @author ACER
   */
  ```

- **dòng gốc 15** (adjustment, đợt 1) — gắn với: `private static final long serialVersionUID = 1L;`

  ```
  //có điều chỉnh với cách viết ban đầu: thêm để lưu bằng serialization
  ```

### `src/service/AuthService.java`

- **dòng gốc 1–4** (license) — gắn với: `package service;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 19–24** (author, đợt 1) — gắn với: `public class AuthService {`

  ```
  /**
   *
   * @author ACER
   *
   * //có điều chỉnh với cách viết ban đầu: refactor theo hợp đồng A5 (xem từng chỗ ghi chú bên dưới).
   */
  ```

- **dòng gốc 26** (adjustment, đợt 1) — gắn với: `private final List<User> _userList;`

  ```
  //có điều chỉnh với cách viết ban đầu: thêm final, loadData dùng clear()+addAll()
  ```

- **dòng gốc 29** (adjustment, đợt 1) — gắn với: `public AuthService(DepartmentService departmentService){`

  ```
  //có điều chỉnh với cách viết ban đầu: trước là AuthService() không tham số
  ```

- **dòng gốc 69** (adjustment, đợt 2) — gắn với: `return value.trim();`

  ```
          //có điều chỉnh với cách viết ban đầu (đợt 2): bỏ kiểm tra ký tự '|' (chỉ cần cho file txt cũ)
  ```

- **dòng gốc 79** (adjustment, đợt 1) — gắn với: `if (role == null) {`

  ```
          //có điều chỉnh với cách viết ban đầu: trước đây trả null khi thiếu dữ liệu / trùng userName; nay ném exception theo A1
  ```

- **dòng gốc 93** (adjustment, đợt 2) — gắn với: `String id = cleanUserName;`

  ```
          //có điều chỉnh với cách viết ban đầu (đợt 2): ID của tài khoản chính là username (không đổi sau khi tạo) thay cho USR-tên-số
  ```

- **dòng gốc 95** (adjustment, đợt 1) — gắn với: `switch (role){`

  ```
          // isActive = true: //có điều chỉnh với cách viết ban đầu: trước là false nên tài khoản mới tạo bị coi như đã khóa
  ```

- **dòng gốc 113–115** (adjustment, đợt 2) — gắn với: `public User login(String userName, String password) throws AuthenticationException{`

  ```
      //có điều chỉnh với cách viết ban đầu: trước trả null khi sai thông tin và tự gọi u.setActivate(true) (login vô tình mở khóa tài khoản).
      // Nay: không tự kích hoạt; tài khoản bị khóa và sai thông tin ném AuthenticationException với message khác nhau.
      //có điều chỉnh với cách viết ban đầu (đợt 2): tách riêng thông báo "tài khoản không tồn tại" và "sai mật khẩu"
  ```

- **dòng gốc 134** (adjustment, đợt 1) — gắn với: `public void updateAccount(String userName, String newFullName) throws NotFoundExceptio...`

  ```
      //có điều chỉnh với cách viết ban đầu: trước trả boolean; nay void và ném NotFoundException / InvalidInputException
  ```

- **dòng gốc 136** (note) — gắn với: `{`

  ```
  //Chỉ được update tên thôi sao?
  ```

- **dòng gốc 144** (adjustment, đợt 1) — gắn với: `public void deactivateAccount(String userName) throws NotFoundException {`

  ```
      //có điều chỉnh với cách viết ban đầu: đổi tên deActivateAccount -> deactivateAccount, trả void, ném NotFoundException
  ```

- **dòng gốc 200** (adjustment, đợt 2) — gắn với: `public void loadData(List<User> list) {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): bỏ bộ đếm _idCounter vì ID tài khoản là username
  ```

### `src/service/DepartmentService.java`

- **dòng gốc 21** (note) — gắn với: `public Department findById(String id) {`

  ```
      // Hàm tìm kiếm dùng chung nội bộ: không thấy -> null (không ném lỗi)
  ```

- **dòng gốc 52** (note) — gắn với: `private String checkName(String name) throws InvalidInputException {`

  ```
      // Kiểm tra tên hợp lệ, trả về tên đã trim. Dùng chung cho add và update.
  ```

- **dòng gốc 57** (adjustment, đợt 2) — gắn với: `if (name.contains("?") || name.trim().equals("0")) {`

  ```
          //có điều chỉnh với cách viết ban đầu (đợt 2): bỏ kiểm tra ký tự '|' (chỉ cần cho file txt cũ); thêm cấm '?' và "0" vì dùng để tìm kiếm / hủy khi chọn theo tên
  ```

- **dòng gốc 68** (adjustment, đợt 2) — gắn với: `if (findByName(cleanName) != null) {`

  ```
          //có điều chỉnh với cách viết ban đầu (đợt 2): trước so equalsIgnoreCase; nay so theo tên đã chuẩn hóa (bỏ dấu, khoảng trắng, hoa/thường)
  ```

- **dòng gốc 74** (adjustment, đợt 2) — gắn với: `String newId = "dep-" + _idCounter;`

  ```
          //có điều chỉnh với cách viết ban đầu (đợt 2): ID ẩn dạng dep-N (chữ thường, không nhúng tên khoa)
  ```

- **dòng gốc 119** (note) — gắn với: `public void loadData(List<Department> list) {`

  ```
      // Nạp từ file: list là final nên dùng clear() + addAll()
  ```

- **dòng gốc 128** (note) — gắn với: `_idCounter = max + 1;`

  ```
  // danh sách rỗng -> max = 0 -> counter = 1
  ```

### `src/service/ExaminationService.java`

- **dòng gốc 1–4** (license) — gắn với: `package service;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 16–19** (author) — gắn với: `public class ExaminationService {`

  ```
  /**
   *
   * @author ACER
   */
  ```

### `src/service/PatientService.java`

- **dòng gốc 1–4** (license) — gắn với: `package service;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 16–19** (author) — gắn với: `public class PatientService {`

  ```
  /**
   *
   * @author ACER
   */
  ```

- **dòng gốc 69** (note) — gắn với: `private String checkName(String name) throws InvalidInputException {`

  ```
      // Kiểm tra tên hợp lệ, trả về tên đã trim. Dùng chung cho register và update.
  ```

- **dòng gốc 74** (adjustment, đợt 2) — gắn với: `if (name.contains("?") || name.trim().equals("0")) {`

  ```
          //có điều chỉnh với cách viết ban đầu (đợt 2): bỏ kiểm tra ký tự '|' (chỉ cần cho file txt cũ); thêm cấm '?' và "0" vì dùng để tìm kiếm / hủy khi chọn theo tên
  ```

- **dòng gốc 93** (adjustment, đợt 2) — gắn với: `public String register(String name, LocalDate dateOfBirth) throws InvalidInputExceptio...`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): thêm tham số dateOfBirth (có thể null); ID ẩn dạng pat-N không nhúng tên bệnh nhân
  ```

- **dòng gốc 138** (note) — gắn với: `String cleanKeyword = TextUtils.cleanName(keyword);`

  ```
          // So sánh không dấu, không phân biệt hoa/thường: "van" tìm được "Nguyễn Văn A".
  ```

- **dòng gốc 164** (note) — gắn với: `public void loadData(List<Patient> list) {`

  ```
      // Nạp dữ liệu từ file, rồi đặt _idCounter = số lớn nhất ở đuôi ID + 1.
  ```

### `src/service/QueueService.java`

- **dòng gốc 1–4** (license) — gắn với: `package service;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 11–14** (author) — gắn với: `public class QueueService {`

  ```
  /**
   *
   * @author ACER
   */
  ```

### `src/service/RecordService.java`

- **dòng gốc 1–4** (license) — gắn với: `package service;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 13–16** (author) — gắn với: `public class RecordService {`

  ```
  /**
   *
   * @author algori07
   */
  ```

- **dòng gốc 109** (adjustment, đợt 2) — gắn với: `public String register(`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): thêm quy tắc mỗi bệnh nhân chỉ có một lượt khám đang mở; ID lượt khám ẩn dạng vis-N; thông báo lỗi dùng tên thay cho ID
  ```

- **dòng gốc 149** (adjustment, đợt 2) — gắn với: `public void updateStatus(String recordId, RecordStatus newStatus)`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): thông báo lỗi không còn nhắc ID lượt khám
  ```

- **dòng gốc 162–164** (adjustment, đợt 1) — gắn với: `Department dept = this.departmentService.findById(record.getCurrentDepartmentId());`

  ```
          // có điều chỉnh với cách viết ban đầu: đồng bộ Department.waitingQueue với trạng thái.
          // Bản đầu chỉ thêm recordId vào hàng chờ khi check-in mà không bao giờ xóa, nên DoctorMenu
          // hiển thị "Pending queue" sai (đếm cả bệnh nhân đã khám xong).
  ```

- **dòng gốc 215** (adjustment, đợt 1) — gắn với: `if(filter == null) return new ArrayList<>(this.recordList);`

  ```
  //có điều chỉnh với cách viết ban đầu: hợp đồng A5 quy định filter null -> trả toàn bộ (trước đây trả list rỗng)
  ```

- **dòng gốc 291** (adjustment, đợt 1) — gắn với: `if(list != null) this.recordList.addAll(list);`

  ```
  //có điều chỉnh với cách viết ban đầu: chấp nhận list null
  ```

- **dòng gốc 293–295** (adjustment, đợt 1) — gắn với: `int maxId = 0;`

  ```
          // //có điều chỉnh với cách viết ban đầu: bản đầu đặt idCounter = maxId (không +1) nên Record mới sau khi nạp file
          // bị TRÙNG ID với Record cuối cùng; Integer.parseInt cũng ném NumberFormatException nếu ID lạ.
          // Nay dùng TextUtils.extractCounter (an toàn) và cộng thêm 1 theo quy ước A4.
  ```

### `src/strategy/FCFSStrategy.java`

- **dòng gốc 1–4** (license) — gắn với: `package strategy;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 10–13** (author) — gắn với: `public class FCFSStrategy implements QueueStrategy {`

  ```
  /**
   *
   * @author ACER
   */
  ```

### `src/strategy/PriorityStrategy.java`

- **dòng gốc 1–4** (license) — gắn với: `package strategy;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 9–12** (author) — gắn với: `public class PriorityStrategy implements QueueStrategy {`

  ```
  /**
   *
   * @author ACER
   */
  ```

### `src/strategy/QueueStrategy.java`

- **dòng gốc 1–4** (license) — gắn với: `package strategy;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
   */
  ```

- **dòng gốc 9–12** (author) — gắn với: `public interface QueueStrategy {`

  ```
  /**
   *
   * @author ACER
   */
  ```

### `src/ui/AccountMenu.java`

- **dòng gốc 1–4** (license) — gắn với: `package ui;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 20–23** (author) — gắn với: `public class AccountMenu {`

  ```
  /**
   *
   * @author ACER
   */
  ```

- **dòng gốc 32** (adjustment, đợt 2) — gắn với: `public AccountMenu(AuthService au, DepartmentService de, RecordService re, User curren...`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): thêm recordService và currentUser để kiểm tra khi xóa tài khoản
  ```

- **dòng gốc 53** (adjustment, đợt 2) — gắn với: `private void printMenu() {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): thêm Reactivate account và Delete account
  ```

- **dòng gốc 81** (adjustment, đợt 2) — gắn với: `private void createAccount() throws InvalidInputException, DuplicateDataException {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): thông báo dùng họ tên và username thay cho ID
  ```

- **dòng gốc 101** (adjustment, đợt 2) — gắn với: `private void listAccounts() {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): bỏ cột ID, hiện tên khoa thay cho mã khoa
  ```

- **dòng gốc 125** (adjustment, đợt 2) — gắn với: `private void updateFullName() throws InvalidInputException, NotFoundException {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): chọn tài khoản theo username (gõ ? để tìm) thay cho nhập ID
  ```

- **dòng gốc 136** (adjustment, đợt 2) — gắn với: `private void deactivateAccount() throws InvalidInputException, NotFoundException {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): chọn tài khoản theo username; chặn khóa lại tài khoản đã khóa
  ```

- **dòng gốc 206** (adjustment, đợt 2) — gắn với: `private void assignDoctor() throws InvalidInputException, NotFoundException {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): chọn bác sĩ và khoa theo tên thay cho nhập ID
  ```

### `src/ui/AdminMenu.java`

- **dòng gốc 31** (adjustment, đợt 2) — gắn với: `public AdminMenu(DepartmentService departmentService,`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): thêm recordService, currentUser (cho kiểm tra khi xóa) và onStrategyChanged (lưu ngay chiến lược hàng đợi)
  ```

- **dòng gốc 107** (adjustment, đợt 2) — gắn với: `private void addDepartment() throws InvalidInputException, DuplicateDataException {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): thông báo dùng tên khoa thay cho ID
  ```

- **dòng gốc 114** (adjustment, đợt 2) — gắn với: `private void updateDepartment()`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): chọn khoa theo tên (gõ ? để tìm) thay cho nhập ID; thông báo dùng tên
  ```

- **dòng gốc 127** (adjustment, đợt 2) — gắn với: `private void removeDepartment() throws NotFoundException, InvalidInputException {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): chọn khoa theo tên; không cho xóa khoa còn bác sĩ hoặc đã có lượt khám; hỏi xác nhận
  ```

- **dòng gốc 150** (adjustment, đợt 2) — gắn với: `private void viewDepartments() {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): bỏ cột ID, thêm số bác sĩ và số người đang chờ
  ```

- **dòng gốc 170** (adjustment, đợt 2) — gắn với: `private void switchStrategy() throws InvalidInputException {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): gọi onStrategyChanged để lưu ngay chiến lược hàng đợi
  ```

- **dòng gốc 185** (adjustment, đợt 2) — gắn với: `private void viewStatistics() {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): đổi nhãn: số liệu là số lượt khám hiện đang ở từng trạng thái
  ```

### `src/ui/ConsoleIO.java`

- **dòng gốc 1–4** (license) — gắn với: `package ui;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 16–19** (author) — gắn với: `public class ConsoleIO {`

  ```
  /**
   *
   * @author ACER
   */
  ```

- **dòng gốc 21** (note) — gắn với: `private static final Scanner SC = new Scanner(System.in);`

  ```
  //static có nghĩa là chỉ có 1, tạo nhiều biến class thì cũng chỉ có một cái này, ngoài ra thì void mà có static thì không cần khởi tạo một biến class để sử dụng mà có thể truy cập trực tiếp.
  ```

- **dòng gốc 23** (note) — gắn với: `private ConsoleIO(){}`

  ```
  //Tại sao cái này chỉ có private nhỉ?
  ```

- **dòng gốc 30** (adjustment, đợt 2) — gắn với: `public static void success(String msg) {System.out.println("[Success] "+msg);}`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): đổi thẻ [OK] [ERROR] [INFO] thành [Success] [Error] [Info], thêm [Warning]
  ```

- **dòng gốc 61** (note) — gắn với: `System.out.print(prompt);`

  ```
  //TODO: check lại chỗ này có cần thiết không?
  ```

- **dòng gốc 108** (note) — gắn với: `sb.append("| ").append(String.format("%-" + width[i] + "s", cell)).append(' ');`

  ```
  // Cách để căn lề trái.
  ```

- **dòng gốc 114** (adjustment, đợt 1) — gắn với: `SC.nextLine();`

  ```
  //có điều chỉnh với cách viết ban đầu: trước đây tạo thêm 1 Scanner mới trên System.in, hai Scanner cùng đọc một luồng nhập sẽ làm mất / lệch dữ liệu nhập.
  ```

### `src/ui/DoctorMenu.java`

- **dòng gốc 1–4** (license) — gắn với: `package ui;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 18–21** (author) — gắn với: `public class DoctorMenu {`

  ```
  /**
   *
   * @author ACER
   */
  ```

- **dòng gốc 77** (note) — gắn với: `System.out.println();`

  ```
          //// CONTEXT INFORMATION
  ```

- **dòng gốc 79** (note) — gắn với: `System.out.println();`

  ```
          // warning
  ```

- **dòng gốc 81** (adjustment, đợt 2) — gắn với: `if(!isValidDoctor())`

  ```
          //có điều chỉnh với cách viết ban đầu (đợt 2): cảnh báo đi qua ConsoleIO.warning thay cho (!) tự in; sửa lỗi chính tả "infomation"
  ```

- **dòng gốc 93** (note) — gắn với: `System.out.println("Username: " + doctor.getUserName());`

  ```
          // information
  ```

- **dòng gốc 94** (adjustment, đợt 2) — gắn với: `System.out.println("Username: " + doctor.getUserName());`

  ```
  //có điều chỉnh với cách viết ban đầu (đợt 2): hiện username thay cho Id
  ```

- **dòng gốc 107** (adjustment, đợt 1) — gắn với: `if(record == null)`

  ```
  //có điều chỉnh với cách viết ban đầu: thêm kiểm tra null, trước đây record/patient null gây NullPointerException làm thoát chương trình
  ```

- **dòng gốc 116** (adjustment, đợt 2) — gắn với: `System.out.println("Patient's visit time: " + ConsoleIO.formatDateTime(record.getVisit...`

  ```
  //có điều chỉnh với cách viết ban đầu (đợt 2): định dạng HH:mm dd/MM/yyyy
  ```

- **dòng gốc 122** (note) — gắn với: `if(isValidDoctor())`

  ```
          // menu
  ```

- **dòng gốc 163** (adjustment, đợt 1) — gắn với: `patient == null ? record.getPatientId() : patient.getName(),`

  ```
  //có điều chỉnh với cách viết ban đầu: patient null (bệnh nhân đã bị xóa) -> hiện ID thay vì NullPointerException
  ```

- **dòng gốc 164** (adjustment, đợt 1) — gắn với: `record.getPriority().toString(),`

  ```
  //có điều chỉnh với cách viết ban đầu: thêm cột Priority theo phân công (cần để thấy thứ tự khi dùng Priority strategy)
  ```

- **dòng gốc 165** (adjustment, đợt 2) — gắn với: `ConsoleIO.formatDateTime(record.getVisitTime())`

  ```
  //có điều chỉnh với cách viết ban đầu (đợt 2): bỏ cột Record Id; giờ khám định dạng HH:mm dd/MM/yyyy
  ```

- **dòng gốc 169** (adjustment, đợt 1) — gắn với: `if(rows.isEmpty())`

  ```
  //có điều chỉnh với cách viết ban đầu: hàng chờ rỗng -> báo thay vì in bảng trống
  ```

- **dòng gốc 177–178** (adjustment, đợt 1) — gắn với: `private void callNextPatient()`

  ```
      //có điều chỉnh với cách viết ban đầu: ExaminationService.callNext trả null khi hàng chờ rỗng (hợp đồng A5), không ném NotFoundException;
      // và cần in kết quả khi gọi thành công. Bỏ pauseConsole() vì không còn cần dừng màn hình.
  ```

- **dòng gốc 189** (adjustment, đợt 2) — gắn với: `+ " (" + called.getPriority() + ")");`

  ```
  //có điều chỉnh với cách viết ban đầu (đợt 2): bỏ ID lượt khám khỏi thông báo
  ```

- **dòng gốc 200** (adjustment, đợt 1) — gắn với: `String recordId = doctor.getCurrentRecordId();`

  ```
  //có điều chỉnh với cách viết ban đầu: lưu lại ID để in thông báo thành công
  ```

- **dòng gốc 206** (adjustment, đợt 2) — gắn với: `String patientName = examinedPatient == null ? "the patient" : examinedPatient.getName...`

  ```
  //có điều chỉnh với cách viết ban đầu (đợt 2): thông báo dùng tên bệnh nhân thay cho ID
  ```

- **dòng gốc 221** (adjustment, đợt 2) — gắn với: `System.out.println("1. Search by patient name");`

  ```
          //có điều chỉnh với cách viết ban đầu (đợt 2): bỏ mục "Search by id" vì ID lượt khám không còn hiện ra; các mục còn lại được đánh số lại
  ```

- **dòng gốc 334** (adjustment, đợt 2) — gắn với: `return ConsoleIO.parseDate(value).atStartOfDay();`

  ```
  //có điều chỉnh với cách viết ban đầu (đợt 2): ngày nhập theo dd/MM/yyyy, dùng chung ConsoleIO.parseDate
  ```

- **dòng gốc 362** (adjustment, đợt 2) — gắn với: `department == null ? record.getCurrentDepartmentId() : department.getName()`

  ```
  //có điều chỉnh với cách viết ban đầu (đợt 2): bỏ cột Record ID; hiện tên khoa thay cho mã khoa; giờ khám định dạng dễ đọc
  ```

### `src/ui/Main.java`

- **dòng gốc 1–4** (license) — gắn với: `package ui;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 7–10** (author) — gắn với: `public class Main {`

  ```
  /**
   *
   * @author ACER
   */
  ```

- **dòng gốc 13** (adjustment, đợt 1) — gắn với: `new MainMenu().start();`

  ```
  //có điều chỉnh với cách viết ban đầu: trước đây chỉ là System.out.println("Ok") (bản khung)
  ```

### `src/ui/MainMenu.java`

- **dòng gốc 1–4** (license) — gắn với: `package ui;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 34–37** (author) — gắn với: `public class MainMenu {`

  ```
  /**
   *
   * @author ACER
   */
  ```

- **dòng gốc 74** (adjustment, đợt 2) — gắn với: `private boolean loginOnce() {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): hỏi username trước và báo ngay nếu tài khoản không tồn tại; lời chào bỏ thẻ [OK] và vai trò
  ```

- **dòng gốc 132** (adjustment, đợt 2) — gắn với: `private void loadAll() {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): nạp thêm settings.dat (chiến lược hàng đợi và bộ đếm ID)
  ```

- **dòng gốc 187** (adjustment, đợt 2) — gắn với: `private void saveAll() {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): lưu thêm settings.dat
  ```

### `src/ui/ReceptionistMenu.java`

- **dòng gốc 1–4** (license) — gắn với: `package ui;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 24–27** (author) — gắn với: `public class ReceptionistMenu {`

  ```
  /**
   *
   * @author ACER
   */
  ```

- **dòng gốc 54** (adjustment, đợt 2) — gắn với: `private void printMenu() {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): gộp đăng ký bệnh nhân và check-in thành mục "New visit"; mục đăng ký riêng vẫn giữ để đăng ký trước
  ```

- **dòng gốc 81** (adjustment, đợt 2) — gắn với: `private void registerPatient() throws InvalidInputException {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): hỏi thêm ngày sinh (không bắt buộc); thông báo dùng tên và mã bệnh nhân
  ```

- **dòng gốc 94** (adjustment, đợt 2) — gắn với: `private void updatePatient() throws InvalidInputException, NotFoundException {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): chọn bệnh nhân theo tên hoặc mã (gõ ? để tìm), cho sửa cả ngày sinh
  ```

- **dòng gốc 116** (adjustment, đợt 2) — gắn với: `private void removePatient() throws InvalidInputException, NotFoundException {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): chọn theo tên, hỏi xác nhận, không cho xóa bệnh nhân đã có lượt khám
  ```

- **dòng gốc 135** (adjustment, đợt 2) — gắn với: `private void printPatients(List<Patient> patients) {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): bỏ cột ID, thay bằng mã bệnh nhân, thêm ngày sinh và số lượt khám
  ```

- **dòng gốc 165–166** (adjustment, đợt 2) — gắn với: `private void newVisit() throws InvalidInputException, NotFoundException, DuplicateData...`

  ```
      // Việc kiểm tra patient/department tồn tại nằm trong RecordService -> để NotFoundException nổi lên.
      //có điều chỉnh với cách viết ban đầu (đợt 2): thay cho checkIn() cũ; chọn bệnh nhân và khoa theo tên, tự đăng ký bệnh nhân nếu chưa có
  ```

- **dòng gốc 203** (adjustment, đợt 2) — gắn với: `private void searchRecords() {`

  ```
      //có điều chỉnh với cách viết ban đầu (đợt 2): bỏ cột Record ID, hiện thêm giờ khám theo định dạng dễ đọc; danh sách trạng thái chỉ còn 3 giá trị
  ```

- **dòng gốc 206** (note) — gắn với: `filter.setPatientNameKeyword(ConsoleIO.readLine("Patient name keyword (Enter to skip):...`

  ```
          // Keyword rỗng -> searchByName trả toàn bộ bệnh nhân -> lấy toàn bộ record.
  ```

### `src/util/TextUtils.java`

- **dòng gốc 1–4** (license) — gắn với: `package util;`

  ```
  /*
   * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
   * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   */
  ```

- **dòng gốc 7–10** (author) — gắn với: `public class TextUtils {`

  ```
  /**
   *
   * @author ACER
   */
  ```

