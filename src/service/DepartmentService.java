package service;

import java.util.ArrayList;
import java.util.List;
import model.Department;
import util.TextUtils;

public class DepartmentService {

    private final List<Department> _departmentList;
    private int _idCounter;

    public DepartmentService() {
        _departmentList = new ArrayList<>();
        _idCounter = 1;
    }

    // Hàm tìm kiếm dùng chung nội bộ  
    public Department findById(String id) {
        for (Department dept : _departmentList) {
            if (dept.getId().equalsIgnoreCase(id)) {
                return dept;
            }
        }
        return null;
    }

    // validate -> kiểm tra trùng -> thao tác -> thông báo
    public String addDepartment(String name) {
        // Validate
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Loi: Ten phong ban khong duoc de trong.");
            return null;
        }

        // Kiểm tra trùng tên
        for (Department dept : _departmentList) {
            if (dept.getName().equalsIgnoreCase(name)) {
                System.out.println("Loi: Ten phong ban '" + name + "' da ton tai.");
                return null;
            }
        }

        // Sinh id và tạo đối tượng mới.
        String cleanedName = TextUtils.cleanName(name);
        String newId = "DEPT-" + cleanedName + "-" + _idCounter;
        Department newDept = new Department(newId, name);
        _departmentList.add(newDept);
        _idCounter++;

        // Thông báo và trả về id.
        System.out.println("Da them phong ban '" + name + "' voi id: " + newId);
        return newId;
    }

    public boolean updateDepartment(String id, String newName) {
        Department dept = findById(id);
        if (dept == null) {
            System.out.println("Loi: Khong tim thay phong ban co id: " + id);
            return false;
        }
        dept.setName(newName);
        System.out.println("Da cap nhat phong ban " + id + " thanh ten: " + newName);
        return true;
    }

    public boolean remove(String id) {
        Department dept = findById(id);
        if (dept == null) {
            System.out.println("Loi: Khong tim thay phong ban co id: " + id);
            return false;
        }
        _departmentList.remove(dept);
        System.out.println("Da xoa phong ban co id: " + id);
        return true;
    }

    public List<Department> getAll() {
        return _departmentList;
    }

    // test nhé.
    public static void main(String[] args) {
        DepartmentService service = new DepartmentService();

        // Test addDepartment
        String id1 = service.addDepartment("San Phu Khoa");
        String id2 = service.addDepartment("Noi Tong Quat");

        // Test thêm trùng tên báo lỗi và trả về null
        service.addDepartment("san phu khoa");

        // Test findById
        Department found = service.findById(id1);
        System.out.println("Tim thay: " + (found != null ? found.getName() : "null"));

        // Test updateDepartment
        service.updateDepartment(id2, "Noi Tong Hop");

        // Test updateDepartment với id không tồn tại
        service.updateDepartment("DEPT-khongton-99", "Ten moi");

        // Test getAll
        System.out.println("Danh sach phong ban:");
        for (Department d : service.getAll()) {
            System.out.println("  " + d.getId() + " - " + d.getName());
        }

        // Test remove
        service.remove(id1);
        System.out.println("Sau khi xoa, con lai:");
        for (Department d : service.getAll()) {
            System.out.println("  " + d.getId() + " - " + d.getName());
        }
    }
}