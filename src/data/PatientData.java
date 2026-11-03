package data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Patient;
import util.DateUtils;

public class PatientData {

    private static final String FILE_NAME = "patients.txt";
    private static final String HEADER = "id|name|dateOfBirth(yyyy-MM-dd)|recordIds";

    public void save(List<Patient> list) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Patient p : list) {
            lines.add(TextStorage.joinFields(p.getId(), p.getName(),
                    DateUtils.toStorage(p.getDateOfBirth()),
                    TextStorage.joinList(p.getRecordIds())));
        }
        TextStorage.writeLines(FILE_NAME, HEADER, lines);
    }

    public List<Patient> load() throws IOException {
        List<Patient> result = new ArrayList<>();
        for (String[] row : TextStorage.readRows(FILE_NAME)) {
            TextStorage.requireFields(row, 4, FILE_NAME);
            try {
                Patient p = new Patient(row[0], row[1], DateUtils.dateFromStorage(row[2]));
                for (String recordId : TextStorage.splitList(row[3])) {
                    p.addRecord(recordId);
                }
                result.add(p);
            } catch (RuntimeException e) {
                throw TextStorage.quarantine(FILE_NAME, "invalid patient line for '" + row[0] + "': " + e.getMessage(), e);
            }
        }
        return result;
    }
}
