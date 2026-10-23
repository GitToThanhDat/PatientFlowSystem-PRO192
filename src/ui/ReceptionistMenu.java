/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import java.util.ArrayList;
import java.util.List;

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

/**
 *
 * @author ACER
 */
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
            } catch (InvalidInputException | NotFoundException e) {
                ConsoleIO.error(e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("===== RECEPTIONIST MENU =====");
        System.out.println("1. Register new patient");
        System.out.println("2. Update patient name");
        System.out.println("3. Remove patient");
        System.out.println("4. Search patient by name");
        System.out.println("5. View all patients");
        System.out.println("6. Check-in (create visit record)");
        System.out.println("7. Search visit records");
        System.out.println("0. Logout");
    }

    private void handle(int choice) throws InvalidInputException, NotFoundException {
        switch (choice) {
            case 1: registerPatient(); break;
            case 2: updatePatient(); break;
            case 3: removePatient(); break;
            case 4: printPatients(_patientService.searchByName(ConsoleIO.readLine("Name keyword: "))); break;
            case 5: printPatients(_patientService.getAll()); break;
            case 6: checkIn(); break;
            case 7: searchRecords(); break;
        }
    }

    private void registerPatient() throws InvalidInputException {
        String name = ConsoleIO.readLine("Patient name: ");
        String id = _patientService.register(name);
        ConsoleIO.success("Patient registered: " + _patientService.findById(id).getName() + " (ID: " + id + ")");
    }

    private void updatePatient() throws InvalidInputException, NotFoundException {
        String id = ConsoleIO.readLine("Patient ID: ");
        String newName = ConsoleIO.readLine("New name: ");
        _patientService.update(id, newName);
        ConsoleIO.success("Patient updated: " + id);
    }

    private void removePatient() throws NotFoundException {
        String id = ConsoleIO.readLine("Patient ID: ");
        _patientService.remove(id);
        ConsoleIO.success("Patient removed: " + id);
    }

    private void printPatients(List<Patient> patients) {
        if (patients.isEmpty()) {
            ConsoleIO.info("No patients found.");
            return;
        }
        List<String[]> rows = new ArrayList<>();
        for (Patient p : patients) {
            rows.add(new String[]{p.getId(), p.getName()});
        }
        ConsoleIO.printTable(new String[]{"ID", "Name"}, rows);
    }

    private void printDepartments() {
        List<Department> departments = _departmentService.getAll();
        if (departments.isEmpty()) {
            ConsoleIO.info("No departments found.");
            return;
        }
        List<String[]> rows = new ArrayList<>();
        for (Department d : departments) {
            rows.add(new String[]{d.getId(), d.getName()});
        }
        ConsoleIO.printTable(new String[]{"ID", "Name"}, rows);
    }

    // Việc kiểm tra patient/department tồn tại nằm trong RecordService -> để NotFoundException nổi lên.
    private void checkIn() throws InvalidInputException, NotFoundException {
        String patientId = ConsoleIO.readLine("Patient ID: ");
        printDepartments();
        String departmentId = ConsoleIO.readLine("Department ID: ");
        System.out.println("Priority: 1. NORMAL   2. URGENT   3. EMERGENCY");
        int p = ConsoleIO.readChoice("Choose priority: ", 1, 3);
        Priority priority = Priority.values()[p - 1];

        String recordId = _recordService.register(patientId, departmentId, priority);
        ConsoleIO.success("Check-in successful. Record ID: " + recordId);
    }

    private void searchRecords() {
        RecordFilter filter = new RecordFilter();
        // Keyword rỗng -> searchByName trả toàn bộ bệnh nhân -> lấy toàn bộ record.
        filter.setPatientNameKeyword(ConsoleIO.readLine("Patient name keyword (Enter to skip): "));

        String statusText = ConsoleIO.readLine("Status (Enter to skip): ");
        if (!statusText.isEmpty()) {
            try {
                filter.setStatus(RecordStatus.valueOf(statusText.toUpperCase()));
            } catch (IllegalArgumentException e) {
                ConsoleIO.error("Invalid status: '" + statusText
                        + "'. Valid values: WAITING, CALLED, IN_PROGRESS, DONE, CANCELLED.");
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
                r.getId(),
                patient == null ? r.getPatientId() : patient.getName(),
                department == null ? r.getCurrentDepartmentId() : department.getName(),
                String.valueOf(r.getPriority()),
                String.valueOf(r.getStatus())
            });
        }
        ConsoleIO.printTable(new String[]{"Record ID", "Patient", "Department", "Priority", "Status"}, rows);
    }
}
