package data;

import java.io.IOException;
import java.util.List;
import model.Patient;

public class PatientData {

    private static final String FILE_NAME = "patients.dat";

    public void save(List<Patient> list) throws IOException {
        SerialStorage.saveList(FILE_NAME, list);
    }

    public List<Patient> load() throws IOException {
        return SerialStorage.loadList(FILE_NAME, Patient.class);
    }
}
