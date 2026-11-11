package data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.AppSettings;

public class SettingsData {

    private static final String FILE_NAME = "settings.txt";
    private static final String HEADER = "key|value";

    public void save(AppSettings settings) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add(TextStorage.joinFields("queueStrategy", settings.getQueueStrategy()));
        lines.add(TextStorage.joinFields("nextDepartmentNumber", String.valueOf(settings.getNextDepartmentNumber())));
        lines.add(TextStorage.joinFields("nextPatientNumber", String.valueOf(settings.getNextPatientNumber())));
        lines.add(TextStorage.joinFields("nextRecordNumber", String.valueOf(settings.getNextRecordNumber())));
        TextStorage.writeLines(FILE_NAME, HEADER, lines);
    }

    public AppSettings load() throws IOException {
        AppSettings settings = new AppSettings();
        for (String[] row : TextStorage.readRows(FILE_NAME)) {
            TextStorage.requireFields(row, 2, FILE_NAME);
            try {
                switch (row[0]) {
                    case "queueStrategy": settings.setQueueStrategy(row[1]); break;
                    case "nextDepartmentNumber": settings.setNextDepartmentNumber(Integer.parseInt(row[1])); break;
                    case "nextPatientNumber": settings.setNextPatientNumber(Integer.parseInt(row[1])); break;
                    case "nextRecordNumber": settings.setNextRecordNumber(Integer.parseInt(row[1])); break;
                    default: break;
                }
            } catch (NumberFormatException e) {
                throw TextStorage.quarantine(FILE_NAME, "invalid number for '" + row[0] + "'", e);
            }
        }
        return settings;
    }
}
