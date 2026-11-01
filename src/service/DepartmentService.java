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

    public Department findById(String id) {
        for (Department dept : _departmentList) {
            if (dept.getId().equals(id)) {
                return dept;
            }
        }
        return null;
    }

    public Department findByName(String name) {
        String key = TextUtils.cleanName(name);
        for (Department dept : _departmentList) {
            if (TextUtils.cleanName(dept.getName()).equals(key)) {
                return dept;
            }
        }
        return null;
    }

    public List<Department> searchByName(String keyword) {
        String key = TextUtils.cleanName(keyword);
        List<Department> result = new ArrayList<>();
        for (Department dept : _departmentList) {
            if (TextUtils.cleanName(dept.getName()).contains(key)) {
                result.add(dept);
            }
        }
        return result;
    }

    private String checkName(String name) throws InvalidInputException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Department name must not be empty.");
        }
        if (name.contains("?") || name.trim().equals("0")) {
            throw new InvalidInputException("Department name must not contain '?' and must not be '0'.");
        }
        return name.trim();
    }

    public String addDepartment(String name)
            throws InvalidInputException, DuplicateDataException {
        String cleanName = checkName(name);

        if (findByName(cleanName) != null) {
            throw new DuplicateDataException(
                    "Department '" + cleanName + "' already exists.");
        }

        String newId = "dep-" + _idCounter;
        _departmentList.add(new Department(newId, cleanName));
        _idCounter++;
        return newId;
    }

    public void updateDepartment(String id, String newName)
            throws NotFoundException, InvalidInputException, DuplicateDataException {
        Department dept = findById(id);
        if (dept == null) {
            throw new NotFoundException("Department not found.");
        }
        String cleanName = checkName(newName);

        Department same = findByName(cleanName);
        if (same != null && same != dept) {
            throw new DuplicateDataException(
                    "Department '" + cleanName + "' already exists.");
        }
        dept.setName(cleanName);
    }

    public void remove(String id) throws NotFoundException {
        Department dept = findById(id);
        if (dept == null) {
            throw new NotFoundException("Department not found.");
        }
        _departmentList.remove(dept);
    }

    public List<Department> getAll() {
        return _departmentList;
    }

    public int getNextNumber() {
        return _idCounter;
    }

    public void ensureNextNumberAtLeast(int number) {
        if (number > _idCounter) {
            _idCounter = number;
        }
    }

    public void loadData(List<Department> list) {
        _departmentList.clear();
        _departmentList.addAll(list);

        int max = 0;
        for (Department d : _departmentList) {
            max = Math.max(max, TextUtils.extractCounter(d.getId()));
        }
        _idCounter = max + 1;
    }
}
