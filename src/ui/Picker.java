package ui;

import exception.InvalidInputException;
import exception.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import model.Department;
import model.Patient;
import model.User;
import service.AuthService;
import service.DepartmentService;
import service.PatientService;
import util.DateUtils;

public class Picker {

    private interface Source<T> {
        String kind();
        List<T> exact(String text);
        List<T> search(String keyword);
        String[] headers();
        String[] row(T item);
    }

    private Picker() {
    }

    public static Department department(DepartmentService service, String prompt)
            throws InvalidInputException, NotFoundException {
        return choose(readText(prompt), departmentSource(service));
    }

    public static Patient patient(PatientService service, String prompt)
            throws InvalidInputException, NotFoundException {
        return choose(readText(prompt), patientSource(service));
    }

    public static Department departmentFromText(DepartmentService service, String text)
            throws InvalidInputException, NotFoundException {
        return choose(text, departmentSource(service));
    }


    public static User account(AuthService service, String prompt)
            throws InvalidInputException, NotFoundException {
        return choose(readText(prompt), accountSource(service));
    }

    private static String readText(String prompt) {
        return ConsoleIO.readLine(prompt + " (add ? to search, 0 to cancel): ");
    }

    private static <T> T choose(String text, Source<T> source)
            throws InvalidInputException, NotFoundException {
        if (text.equals("0")) {
            return null;
        }
        if (text.isEmpty()) {
            throw new InvalidInputException("Please enter a " + source.kind()
                    + " name, or add ? to search.");
        }
        List<T> matches;
        if (text.endsWith("?")) {
            String keyword = text.substring(0, text.length() - 1).trim();
            matches = source.search(keyword);
            if (matches.isEmpty()) {
                throw new NotFoundException("No " + source.kind() + " matches '" + keyword + "'.");
            }
        } else {
            matches = source.exact(text);
            if (matches.isEmpty()) {
                throw new NotFoundException("The " + source.kind() + " '" + text
                        + "' was not found. Add ? at the end to search.");
            }
            if (matches.size() == 1) {
                return matches.get(0);
            }
        }
        return select(matches, source);
    }

    private static <T> T select(List<T> matches, Source<T> source) throws InvalidInputException {
        String[] baseHeaders = source.headers();
        String[] headers = new String[baseHeaders.length + 1];
        headers[0] = "No.";
        System.arraycopy(baseHeaders, 0, headers, 1, baseHeaders.length);

        List<String[]> rows = new ArrayList<>();
        for (int i = 0; i < matches.size(); i++) {
            String[] cells = source.row(matches.get(i));
            String[] row = new String[cells.length + 1];
            row[0] = String.valueOf(i + 1);
            System.arraycopy(cells, 0, row, 1, cells.length);
            rows.add(row);
        }
        ConsoleIO.printTable(headers, rows);

        int choice = ConsoleIO.readChoice("Select number (0 = cancel): ", 0, matches.size());
        if (choice == 0) {
            return null;
        }
        return matches.get(choice - 1);
    }

    private static Source<Department> departmentSource(final DepartmentService service) {
        return new Source<Department>() {
            @Override
            public String kind() {
                return "department";
            }

            @Override
            public List<Department> exact(String text) {
                List<Department> result = new ArrayList<>();
                Department d = service.findByName(text);
                if (d != null) {
                    result.add(d);
                }
                return result;
            }

            @Override
            public List<Department> search(String keyword) {
                return service.searchByName(keyword);
            }

            @Override
            public String[] headers() {
                return new String[]{"Department"};
            }

            @Override
            public String[] row(Department item) {
                return new String[]{item.getName()};
            }
        };
    }

    private static Source<Patient> patientSource(final PatientService service) {
        return new Source<Patient>() {
            @Override
            public String kind() {
                return "patient";
            }

            @Override
            public List<Patient> exact(String text) {
                Patient byCode = service.findByCode(text);
                if (byCode != null) {
                    List<Patient> result = new ArrayList<>();
                    result.add(byCode);
                    return result;
                }
                return service.findByName(text);
            }

            @Override
            public List<Patient> search(String keyword) {
                return service.searchByName(keyword);
            }

            @Override
            public String[] headers() {
                return new String[]{"Code", "Name", "Date of birth", "Visits"};
            }

            @Override
            public String[] row(Patient item) {
                return new String[]{
                    item.getId(), item.getName(),
                    DateUtils.format(item.getDateOfBirth()),
                    String.valueOf(item.getRecordIds().size())
                };
            }
        };
    }

    private static Source<User> accountSource(final AuthService service) {
        return new Source<User>() {
            @Override
            public String kind() {
                return "account";
            }

            @Override
            public List<User> exact(String text) {
                List<User> result = new ArrayList<>();
                User u = service.findByUserName(text);
                if (u != null) {
                    result.add(u);
                }
                return result;
            }

            @Override
            public List<User> search(String keyword) {
                return service.search(keyword);
            }

            @Override
            public String[] headers() {
                return new String[]{"Username", "Full name", "Role", "Active"};
            }

            @Override
            public String[] row(User item) {
                return new String[]{
                    item.getUserName(), item.getFullName(),
                    item.getRole().name(), item.isActivate() ? "yes" : "no"
                };
            }
        };
    }
}
