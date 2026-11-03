package data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Admin;
import model.Doctor;
import model.Receptionist;
import model.Role;
import model.User;

public class UserData {

    private static final String FILE_NAME = "users.txt";
    private static final String HEADER = "role|id|userName|password|fullName|active|departmentId|currentRecordId";

    public void save(List<User> list) throws IOException {
        List<String> lines = new ArrayList<>();
        for (User u : list) {
            String departmentId = "";
            String currentRecordId = "";
            if (u instanceof Doctor) {
                Doctor d = (Doctor) u;
                departmentId = d.getDepartmentId();
                currentRecordId = d.getCurrentRecordId();
            }
            lines.add(TextStorage.joinFields(u.getRole().name(), u.getId(), u.getUserName(),
                    u.getPassword(), u.getFullName(), String.valueOf(u.isActivate()),
                    departmentId, currentRecordId));
        }
        TextStorage.writeLines(FILE_NAME, HEADER, lines);
    }

    public List<User> load() throws IOException {
        List<User> result = new ArrayList<>();
        for (String[] row : TextStorage.readRows(FILE_NAME)) {
            TextStorage.requireFields(row, 8, FILE_NAME);
            try {
                Role role = Role.valueOf(row[0]);
                boolean active = Boolean.parseBoolean(row[5]);
                switch (role) {
                    case ADMIN:
                        result.add(new Admin(row[1], row[2], row[3], row[4], active));
                        break;
                    case RECEPTIONIST:
                        result.add(new Receptionist(row[1], row[2], row[3], row[4], active));
                        break;
                    default:
                        Doctor d = new Doctor(row[1], row[2], row[3], row[4], active,
                                TextStorage.emptyToNull(row[6]));
                        d.setCurrentRecordId(TextStorage.emptyToNull(row[7]));
                        result.add(d);
                        break;
                }
            } catch (RuntimeException e) {
                throw TextStorage.quarantine(FILE_NAME, "invalid account line for '" + row[1] + "': " + e.getMessage(), e);
            }
        }
        return result;
    }
}
