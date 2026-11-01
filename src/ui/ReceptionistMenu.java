package ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import exception.DuplicateDataException;
import exception.InvalidInputException;
import exception.NotFoundException;
import model.Department;
import model.Patient;
import model.Priority;
import model.Record;
import model.RecordFilter;
import model.RecordStatus;
import service.DepartmentService;
import service.PatientService;
import service.RecordService;

public class ReceptionistMenu {
    private static final int MAX_CHOICE = 7;

    private final PatientService _patientService;
    private final RecordService _recordService;
    private DepartmentService _departmentService;

    public ReceptionistMenu(PatientService pa, RecordService re, DepartmentService de){
        this._departmentService=de;
        this._patientService=pa;
        this._recordService=re;
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            try {
                int choice = ConsoleIO.readChoice("Choose: ", 0, MAX_CHOICE);
                if (choice == 0) running = false; else handle(choice);
            } catch (InvalidInputException | NotFoundException | DuplicateDataException e) {
                ConsoleIO.error(e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("===== RECEPTIONIST MENU =====");
        System.out.println("1. New visit (register patient if needed, then check-in)");
        System.out.println("2. Register new patient only");
        System.out.println("3. Update patient");
        System.out.println("4. Remove patient");
        System.out.println("5. Search patient by name");
        System.out.println("6. View all patients");
        System.out.println("7. Search visit records");
        System.out.println("0. Logout");
    }

    private void handle(int choice)
            throws InvalidInputException, NotFoundException, DuplicateDataException {
        switch (choice) {
            case 1: newVisit(); break;
            case 2: registerPatient(); break;
            case 3: updatePatient(); break;
            case 4: removePatient(); break;
            case 5: printPatients(_patientService.searchByName(ConsoleIO.readLine("Name keyword: "))); break;
            case 6: printPatients(_patientService.getAll()); break;
            case 7: searchRecords(); break;
        }
    }

    private void registerPatient() throws InvalidInputException {
        String name = ConsoleIO.readLine("Patient name: ");
        if (name.isEmpty()) {
            throw new InvalidInputException("Patient name must not be empty.");
        }
        LocalDate dateOfBirth = ConsoleIO.parseDate(
                ConsoleIO.readLine("Date of birth (dd/MM/yyyy, Enter to skip): "));
        String id = _patientService.register(name, dateOfBirth);
        Patient patient = _patientService.findById(id);
        ConsoleIO.success("Patient registered: " + patient.getName() + " (code: " + id + ")");
    }

    private void updatePatient() throws InvalidInputException, NotFoundException {
        Patient patient = Picker.patient(_patientService, "Patient to update");
        if (patient == null) {
            return;
        }
        String newName = ConsoleIO.readLine("New name for '" + patient.getName() + "' (Enter to keep): ");
        String dobText = ConsoleIO.readLine("New date of birth (dd/MM/yyyy, Enter to keep): ");
        LocalDate dateOfBirth = ConsoleIO.parseDate(dobText);
        if (newName.isEmpty() && dateOfBirth == null) {
            ConsoleIO.info("Nothing was changed.");
            return;
        }
        if (!newName.isEmpty()) {
            _patientService.update(patient.getId(), newName);
        }
        if (dateOfBirth != null) {
            _patientService.updateDateOfBirth(patient.getId(), dateOfBirth);
        }
        ConsoleIO.success("Patient updated: " + patient.getName());
    }

    private void removePatient() throws InvalidInputException, NotFoundException {
        Patient patient = Picker.patient(_patientService, "Patient to remove");
        if (patient == null) {
            return;
        }
        if (_recordService.hasRecordsForPatient(patient.getId())) {
            throw new InvalidInputException("Cannot remove '" + patient.getName()
                    + "': the patient already has visit records.");
        }
        if (!ConsoleIO.confirm("Remove patient '" + patient.getName() + "'?")) {
            ConsoleIO.info("Nothing was removed.");
            return;
        }
        String name = patient.getName();
        _patientService.remove(patient.getId());
        ConsoleIO.success("Patient removed: " + name);
    }

    private void printPatients(List<Patient> patients) {
        if (patients.isEmpty()) {
            ConsoleIO.info("No patients found.");
            return;
        }
        List<String[]> rows = new ArrayList<>();
        for (Patient p : patients) {
            rows.add(new String[]{
                p.getId(), p.getName(),
                ConsoleIO.formatDate(p.getDateOfBirth()),
                String.valueOf(p.getRecordIds().size())
            });
        }
        ConsoleIO.printTable(new String[]{"Code", "Name", "Date of birth", "Visits"}, rows);
    }

    private void printDepartments() {
        List<Department> departments = _departmentService.getAll();
        if (departments.isEmpty()) {
            ConsoleIO.info("No departments found.");
            return;
        }
        List<String[]> rows = new ArrayList<>();
        for (Department d : departments) {
            rows.add(new String[]{d.getName()});
        }
        ConsoleIO.printTable(new String[]{"Department"}, rows);
    }

    private void newVisit() throws InvalidInputException, NotFoundException, DuplicateDataException {
        String text = ConsoleIO.readLine("Patient name or code (add ? to search, 0 to cancel): ");
        Patient patient;
        try {
            patient = Picker.patientFromText(_patientService, text);
        } catch (NotFoundException e) {
            if (text.endsWith("?")) {
                throw e;
            }
            if (!ConsoleIO.confirm("Patient '" + text + "' is not registered. Register now?")) {
                return;
            }
            LocalDate dateOfBirth = ConsoleIO.parseDate(
                    ConsoleIO.readLine("Date of birth (dd/MM/yyyy, Enter to skip): "));
            patient = _patientService.findById(_patientService.register(text, dateOfBirth));
            ConsoleIO.success("Patient registered: " + patient.getName() + " (code: " + patient.getId() + ")");
        }
        if (patient == null) {
            return;
        }
        _recordService.ensureNoActiveVisit(patient.getId());

        printDepartments();
        Department department = Picker.department(_departmentService, "Department");
        if (department == null) {
            return;
        }
        System.out.println("Priority: 1. NORMAL   2. URGENT   3. EMERGENCY");
        int p = ConsoleIO.readChoice("Choose priority: ", 1, 3);
        Priority priority = Priority.values()[p - 1];

        _recordService.register(patient.getId(), department.getId(), priority);
        ConsoleIO.success("Visit created: " + patient.getName() + " -> " + department.getName()
                + " (" + priority + ").");
    }

    private void searchRecords() {
        RecordFilter filter = new RecordFilter();
        filter.setPatientNameKeyword(ConsoleIO.readLine("Patient name keyword (Enter to skip): "));

        String statusText = ConsoleIO.readLine("Status (Enter to skip): ");
        if (!statusText.isEmpty()) {
            try {
                filter.setStatus(RecordStatus.valueOf(statusText.toUpperCase()));
            } catch (IllegalArgumentException e) {
                ConsoleIO.error("Invalid status: '" + statusText
                        + "'. Valid values: WAITING, IN_PROGRESS, DONE.");
                return;
            }
        }

        List<Record> records = _recordService.search(filter);
        if (records == null || records.isEmpty()) {
            ConsoleIO.info("No visit records found.");
            return;
        }
        List<String[]> rows = new ArrayList<>();
        for (Record r : records) {
            Patient patient = _patientService.findById(r.getPatientId());
            Department department = _departmentService.findById(r.getCurrentDepartmentId());
            rows.add(new String[]{
                patient == null ? r.getPatientId() : patient.getName(),
                department == null ? r.getCurrentDepartmentId() : department.getName(),
                String.valueOf(r.getPriority()),
                String.valueOf(r.getStatus()),
                ConsoleIO.formatDateTime(r.getVisitTime())
            });
        }
        ConsoleIO.printTable(new String[]{"Patient", "Department", "Priority", "Status", "Visit Time"}, rows);
    }
}
