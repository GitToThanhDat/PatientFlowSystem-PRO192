package data;

import java.io.IOException;
import java.util.List;
import model.Department;

public class DepartmentData {

    private static final String FILE_NAME = "departments.dat";

    public void save(List<Department> list) throws IOException {
        SerialStorage.saveList(FILE_NAME, list);
    }

    public List<Department> load() throws IOException {
        return SerialStorage.loadList(FILE_NAME, Department.class);
    }
}
