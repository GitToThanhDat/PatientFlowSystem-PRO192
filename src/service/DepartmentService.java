package service;

import exception.DuplicateDataException;
import exception.InvalidInputException;
import exception.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import model.Department;
import util.IdGenerator;
import util.TextUtils;
import util.Validator;

public class DepartmentService {

    private final List<Department> _departmentList;
    private final IdGenerator _ids;

    public DepartmentService() {
        _departmentList = new ArrayList<>();
        _ids = new IdGenerator("dep");
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
        for (Department dept : _departmentList) {
            if (TextUtils.sameName(dept.getName(), name)) {
                return dept;
            }
        }
        return null;
    }

    public List<Department> searchByName(String keyword) {
        List<Department> result = new ArrayList<>();
        for (Department dept : _departmentList) {
            if (TextUtils.containsKeyword(dept.getName(), keyword)) {
                result.add(dept);
            }
        }
        return result;
    }

    private String checkName(String name) throws InvalidInputException {
        return Validator.requireName(name, "Department name");
    }

    public String addDepartment(String name)
            throws InvalidInputException, DuplicateDataException {
        String cleanName = checkName(name);

        if (findByName(cleanName) != null) {
            throw new DuplicateDataException(
                    "Department '" + cleanName + "' already exists.");
        }

        String newId = _ids.next();
        _departmentList.add(new Department(newId, cleanName));
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
        return _ids.getNextNumber();
    }

    public void ensureNextNumberAtLeast(int number) {
        _ids.ensureNextNumberAtLeast(number);
    }

    public void loadData(List<Department> list) {
        _departmentList.clear();
        _departmentList.addAll(list);

        List<String> ids = new ArrayList<>();
        for (Department d : _departmentList) {
            ids.add(d.getId());
        }
        _ids.resetAfter(ids);
    }
}
