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
}
