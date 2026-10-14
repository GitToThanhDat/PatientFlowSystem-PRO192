package service;

import exception.DuplicateDataException;
import exception.InvalidInputException;
import exception.NotFoundException;
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

    // Hàm tìm kiếm dùng chung nội bộ: không thấy -> null (không ném lỗi)
    public Department findById(String id) {
        for (Department dept : _departmentList) {
            if (dept.getId().equals(id)) {
                return dept;
            }
        }
        return null;
    }

    // Kiểm tra tên hợp lệ, trả về tên đã trim. Dùng chung cho add và update.
    private String checkName(String name) throws InvalidInputException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Department name must not be empty.");
        }
        if (name.contains("|")) {
            throw new InvalidInputException("Department name must not contain '|': " + name);
        }
        return name.trim();
    }

    public String addDepartment(String name)
            throws InvalidInputException, DuplicateDataException {
        String cleanName = checkName(name);

        for (Department dept : _departmentList) {
            if (dept.getName().equalsIgnoreCase(cleanName)) {
                throw new DuplicateDataException(
                        "Department '" + cleanName + "' already exists.");
            }
        }

        String newId = "DEPT-" + TextUtils.cleanName(cleanName) + "-" + _idCounter;
        _departmentList.add(new Department(newId, cleanName));
        _idCounter++;
        return newId;
    }

    public void updateDepartment(String id, String newName)
            throws NotFoundException, InvalidInputException, DuplicateDataException {
        Department dept = findById(id);
        if (dept == null) {
            throw new NotFoundException("Department not found with ID: " + id);
        }
        String cleanName = checkName(newName);

        for (Department d : _departmentList) {
            if (d != dept && d.getName().equalsIgnoreCase(cleanName)) {
                throw new DuplicateDataException(
                        "Department '" + cleanName + "' already exists.");
            }
        }
        dept.setName(cleanName);
    }

    public void remove(String id) throws NotFoundException {
        Department dept = findById(id);
        if (dept == null) {
            throw new NotFoundException("Department not found with ID: " + id);
        }
        _departmentList.remove(dept);
    }

    public List<Department> getAll() {
        return _departmentList;
    }

    // Nạp từ file: list là final nên dùng clear() + addAll()
    public void loadData(List<Department> list) {
        _departmentList.clear();
        _departmentList.addAll(list);

        int max = 0;
        for (Department d : _departmentList) {
            max = Math.max(max, TextUtils.extractCounter(d.getId()));
        }
        _idCounter = max + 1;   // danh sách rỗng -> max = 0 -> counter = 1
    }
}