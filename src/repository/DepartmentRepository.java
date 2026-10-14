package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import model.Department;

public class DepartmentRepository {

    private static final String DIR = "data";
    private static final String FILE_PATH = "data/departments.txt";

    public void save(List<Department> list) throws IOException {
        new File(DIR).mkdirs();
        File file = new File(FILE_PATH);
        try (BufferedWriter w = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            for (Department d : list) {
                w.write(String.join("|", d.getId(), d.getName()));
                w.newLine();
            }
        }
    }

    public List<Department> load() throws IOException {
        List<Department> result = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return result;
        }
        try (BufferedReader r = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split("\\|", -1);
                if (p.length < 2) {
                    continue;
                }
                result.add(new Department(p[0], p[1]));
            }
        }
        return result;
    }
}