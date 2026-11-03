package data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Department;

public class DepartmentData {

    private static final String FILE_NAME = "departments.txt";
    private static final String HEADER = "id|name|waitingQueue";

    public void save(List<Department> list) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Department d : list) {
            lines.add(TextStorage.joinFields(d.getId(), d.getName(),
                    TextStorage.joinList(d.getWaitingQueue())));
        }
        TextStorage.writeLines(FILE_NAME, HEADER, lines);
    }

    public List<Department> load() throws IOException {
        List<Department> result = new ArrayList<>();
        for (String[] row : TextStorage.readRows(FILE_NAME)) {
            TextStorage.requireFields(row, 3, FILE_NAME);
            Department d = new Department(row[0], row[1]);
            for (String recordId : TextStorage.splitList(row[2])) {
                d.addToQueue(recordId);
            }
            result.add(d);
        }
        return result;
    }
}
