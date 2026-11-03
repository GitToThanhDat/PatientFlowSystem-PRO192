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
import util.DateUtils;

public class ReceptionistMenu {
    private static final int MAX_CHOICE = 6;

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
        System.out.println("1. New visit (register patient and create visit record)");
        System.out.println("2. Update patient");
        System.out.println("3. Remove patient (only if never examined)");
        System.out.println("4. Search patient by name");
        System.out.println("5. View all patients");
        System.out.println("6. Search visit records");
        System.out.println("0. Logout");
    }

    private void handle(int choice)
            throws InvalidInputException, NotFoundException, DuplicateDataException {
        switch (choice) {
            case 1: newVisit(); break;
            case 2: updatePatient(); break;
            case 3: removePatient(); break;
            case 4: printPatients(_patientService.searchByName(ConsoleIO.readLine("Name keyword: "))); break;
            case 5: printPatients(_patientService.getAll()); break;
            case 6: searchRecords(); break;
        }
    }

    private void updatePatient() throws InvalidInputException, NotFoundException {
        Patient patient = Picker.patient(_patientService, "Patient to update");
        if (patient == null) {
            return;
        }
        String newName = ConsoleIO.readLine("New name for '" + patient.getName() + "' (Enter to keep): ");
        String dobText = ConsoleIO.readLine("New date of birth (dd/MM/yyyy, Enter to keep): ");
        LocalDate dateOfBirth = DateUtils.parseDate(dobText);
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
        if (_recordService.hasExaminedRecords(patient.getId())) {
            throw new InvalidInputException("Cannot remove '" + patient.getName()
                    + "': the patient already has examined visits (IN_PROGRESS or DONE).");
        }
        if (!ConsoleIO.confirm("Remove patient '" + patient.getName()
                + "' and the waiting visit created for this patient?")) {
            ConsoleIO.info("Nothing was removed.");
            return;
        }
        String name = patient.getName();
        _recordService.removePatientWithWaitingVisits(patient.getId());
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
                DateUtils.format(p.getDateOfBirth()),
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
        int number = 1;
        for (Department d : departments) {
            rows.add(new String[]{String.valueOf(number++), d.getName(),
                String.valueOf(d.getWaitingQueue().size())});
        }
        ConsoleIO.printTable(new String[]{"No.", "Department", "Waiting"}, rows);
    }

    private void newVisit() throws InvalidInputException, NotFoundException, DuplicateDataException {
        if (_departmentService.getAll().isEmpty()) {
            throw new InvalidInputException("No departments yet. Ask an admin to add a department first.");
        }
        String name = readPatientName();
        if (name == null) {
            return;
        }
        LocalDate dateOfBirth = readDateOfBirth();
        if (dateOfBirth == null) {
            return;
        }

        List<Patient> inQueue = new ArrayList<>();
        List<Patient> visitedBefore = new ArrayList<>();
        for (Patient p : _patientService.findByNameAndDateOfBirth(name, dateOfBirth)) {
            if (_recordService.findActiveByPatient(p.getId()) != null) {
                inQueue.add(p);
            } else {
                visitedBefore.add(p);
            }
        }

        Patient returning = null;
        if (!visitedBefore.isEmpty()) {
            ConsoleIO.warning("A patient named '" + name + "' born " + DateUtils.format(dateOfBirth)
                    + " has visited this hospital before:");
            printVisitedBefore(visitedBefore);
            if (ConsoleIO.confirm("Is this the same person who has visited here before?")) {
                returning = visitedBefore.size() == 1
                        ? visitedBefore.get(0) : choosePatient(visitedBefore);
                if (returning == null) {
                    ConsoleIO.info("Registration cancelled.");
                    return;
                }
            }
        }
        if (returning == null && !inQueue.isEmpty()) {
            for (Patient p : inQueue) {
                Record active = _recordService.findActiveByPatient(p.getId());
                Department d = _departmentService.findById(active.getCurrentDepartmentId());
                ConsoleIO.warning("Patient '" + p.getName() + "' born " + DateUtils.format(p.getDateOfBirth())
                        + " (code: " + p.getId() + ") is currently in the queue of '"
                        + (d == null ? active.getCurrentDepartmentId() : d.getName())
                        + "' (" + active.getStatus() + ").");
            }
            if (!ConsoleIO.confirm("Confirm this is a NEW, different patient to register?")) {
                ConsoleIO.info("Registration cancelled.");
                return;
            }
        }

        Department department = chooseDepartment();
        if (department == null) {
            return;
        }
        Priority priority = choosePriority();
        if (priority == null) {
            return;
        }

        if (returning != null) {
            _recordService.register(returning.getId(), department.getId(), priority);
            ConsoleIO.success("Visit created for returning patient " + returning.getName()
                    + " (code: " + returning.getId() + ") -> " + department.getName() + " (" + priority + ").");
        } else {
            String recordId = _recordService.registerNewPatientVisit(name, dateOfBirth, department.getId(), priority);
            Patient patient = _patientService.findById(_recordService.findById(recordId).getPatientId());
            ConsoleIO.success("New patient registered: " + patient.getName() + " (code: " + patient.getId()
                    + ") -> " + department.getName() + " (" + priority + ").");
        }
    }

    private String readPatientName() {
        while (true) {
            String text = ConsoleIO.readLine("Patient name (0 to cancel): ");
            if (text.equals("0")) {
                return null;
            }
            try {
                return _patientService.checkName(text);
            } catch (InvalidInputException e) {
                ConsoleIO.error(e.getMessage());
            }
        }
    }

    private LocalDate readDateOfBirth() {
        while (true) {
            String text = ConsoleIO.readLine("Date of birth (dd/MM/yyyy, required, 0 to cancel): ");
            if (text.equals("0")) {
                return null;
            }
            try {
                LocalDate date = DateUtils.parseDate(text);
                _patientService.checkDateOfBirth(date);
                return date;
            } catch (InvalidInputException e) {
                ConsoleIO.error(e.getMessage());
            }
        }
    }

    private void printVisitedBefore(List<Patient> patients) {
        List<String[]> rows = new ArrayList<>();
        int number = 1;
        for (Patient p : patients) {
            Record last = _recordService.findLastVisit(p.getId());
            Department d = last == null ? null : _departmentService.findById(last.getCurrentDepartmentId());
            rows.add(new String[]{
                String.valueOf(number++), p.getId(), p.getName(),
                DateUtils.format(p.getDateOfBirth()),
                String.valueOf(p.getRecordIds().size()),
                last == null ? "-" : DateUtils.format(last.getVisitTime()),
                last == null ? "-" : (d == null ? last.getCurrentDepartmentId() : d.getName())
            });
        }
        ConsoleIO.printTable(new String[]{"No.", "Code", "Name", "Date of birth", "Visits",
            "Last visit", "Last department"}, rows);
    }

    private Patient choosePatient(List<Patient> patients) {
        while (true) {
            try {
                int choice = ConsoleIO.readChoice("Select number (0 = cancel): ", 0, patients.size());
                return choice == 0 ? null : patients.get(choice - 1);
            } catch (InvalidInputException e) {
                ConsoleIO.error(e.getMessage());
            }
        }
    }

    private Department chooseDepartment() {
        List<Department> departments = _departmentService.getAll();
        printDepartments();
        while (true) {
            String text = ConsoleIO.readLine("Department No. or name (add ? to search, 0 to cancel): ");
            try {
                if (text.matches("\\d+")) {
                    int number = Integer.parseInt(text);
                    if (number == 0) {
                        ConsoleIO.info("Registration cancelled.");
                        return null;
                    }
                    if (number > departments.size()) {
                        throw new InvalidInputException("No. " + number + " is out of range. Please choose from 1 to "
                                + departments.size() + ".");
                    }
                    return departments.get(number - 1);
                }
                Department department = Picker.departmentFromText(_departmentService, text);
                if (department != null) {
                    return department;
                }
                printDepartments();
            } catch (InvalidInputException | NotFoundException e) {
                ConsoleIO.error(e.getMessage());
            }
        }
    }

    private Priority choosePriority() {
        System.out.println("Priority: 1. NORMAL   2. URGENT   3. EMERGENCY");
        while (true) {
            try {
                int p = ConsoleIO.readChoice("Choose priority (0 to cancel): ", 0, 3);
                if (p == 0) {
                    ConsoleIO.info("Registration cancelled.");
                    return null;
                }
                return Priority.values()[p - 1];
            } catch (InvalidInputException e) {
                ConsoleIO.error(e.getMessage());
            }
        }
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
                r.getPatientId(),
                patient == null ? r.getPatientId() : patient.getName(),
                department == null ? r.getCurrentDepartmentId() : department.getName(),
                String.valueOf(r.getPriority()),
                String.valueOf(r.getStatus()),
                DateUtils.format(r.getVisitTime())
            });
        }
        ConsoleIO.printTable(new String[]{"Code", "Patient", "Department", "Priority", "Status", "Visit Time"}, rows);
    }
}
