package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import model.Patient;

public class PatientRepository {

    private static final String FILE_PATH = "data/patients.txt";

    public void save(List<Patient> list) throws IOException {
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();
        try (BufferedWriter w = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            for (Patient p : list) {
                w.write(String.join("|", p.getId(), p.getName()));
                w.newLine();
            }
        }
    }

    public List<Patient> load() throws IOException {
        List<Patient> result = new ArrayList<>();
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
                result.add(new Patient(p[0], p[1]));
            }
        }
        return result;
    }
}
