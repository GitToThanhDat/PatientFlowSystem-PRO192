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

