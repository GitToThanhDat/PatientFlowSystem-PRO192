package ui;

import exception.InvalidInputException;
import exception.NotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Department;
import model.Doctor;
import model.Patient;
import model.Record;
import model.RecordFilter;
import model.RecordStatus;
import service.DepartmentService;
import service.ExaminationService;
import service.PatientService;
import service.RecordService;
import util.DateUtils;

public class DoctorMenu {
    private final static int MAX = 4;
    
    private final Doctor doctor;
    private final RecordService recordService;
    private final PatientService patientService;
    private final DepartmentService departmentService;
    private final ExaminationService examinationService;

    public DoctorMenu(Doctor doctor, RecordService recordService,
                      PatientService patientService, DepartmentService departmentService,
                      ExaminationService examinationService) {
        this.doctor = doctor;
        this.recordService = recordService;
        this.patientService = patientService;
        this.departmentService = departmentService;
        this.examinationService = examinationService;
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            try {
                int choice;
                if(isValidDoctor()) choice = ConsoleIO.readChoice("Choose: ", 0, MAX);
                else choice = ConsoleIO.readChoice("Choose: ", 0, 0);
                if (choice == 0) running = false; else handle(choice);
            } catch (InvalidInputException | NotFoundException e) {
                ConsoleIO.error(e.getMessage());
            }
        }
    }
    
    private boolean isValidDoctor()
    {
        return doctor.getDepartmentId() != null;
    }
    private boolean isValidDepartment()
    {
        if(isValidDoctor())
            return departmentService.findById(doctor.getDepartmentId()) != null;
        else
            return false;
    }
    private boolean isExamining()
    {
        return doctor.getCurrentRecordId() != null;
    }
    
    private void printMenu()
    {
        System.out.println();
        System.out.println("===== DOCTOR MENU =====");
        
        
        System.out.println();
        if(!isValidDoctor())
        {
            ConsoleIO.warning("You are currently not in any department!");
            ConsoleIO.warning("Contact your admin for more information!");
        }
        else if(!isValidDepartment())
        {
            ConsoleIO.warning("The department you are in is not found!");
            ConsoleIO.warning("Contact your admin for more information!");
        }
        
        System.out.println("Username: " + doctor.getUserName());
        System.out.println("Name: " + doctor.getFullName());
        if(isValidDepartment())
        {
            Department department = departmentService.findById(doctor.getDepartmentId());
            System.out.println("Department: " + department.getName());
            int pendingPatients = department.getWaitingQueue().size();
            System.out.println("Pending queue: " + pendingPatients + " patient" + (pendingPatients != 1 ? "s":""));
        }
        if(isExamining())
        {
            Record record = recordService.findById(doctor.getCurrentRecordId());
            System.out.println();
            if(record == null)
            {
                ConsoleIO.warning("The record you were examining was not found!");
            }
            else
            {
                Patient patient = patientService.findById(record.getPatientId());
                System.out.println("Current examining patient: "
                        + (patient == null ? record.getPatientId() : patient.getName()));
                System.out.println("Patient's visit time: " + DateUtils.format(record.getVisitTime()));
            }
        }
        
        System.out.println();
        
        if(isValidDoctor())
        {
            System.out.println("1. View waiting queue of my department");
            System.out.println("2. Call next patient");
            System.out.println("3. Complete current examination");
            System.out.println("4. Search records");
        }
        
        System.out.println("0. Logout");
    }

    private void handle(int choice)
            throws InvalidInputException, NotFoundException
    {
        switch (choice) {
            case 1:
                viewWaitingQueue();
                break;
            case 2:
                callNextPatient();
                break;
            case 3:
                completeCurrentExamination();
                break;
            case 4:
                searchRecord();
                break;
            default:
                break;
        }
    }
    
    private void viewWaitingQueue()
    {
        List<String[]> rows = new ArrayList<>();
        
        for(Record record : recordService.getWaitingByDepartment(doctor.getDepartmentId()))
        {
            Patient patient = patientService.findById(record.getPatientId());
            rows.add(new String[]{
                patient == null ? record.getPatientId() : patient.getName(),
                record.getPriority().toString(),
                DateUtils.format(record.getVisitTime())
            });
        }
        
        if(rows.isEmpty())
        {
            ConsoleIO.info("The waiting queue of your department is empty.");
            return;
        }
        ConsoleIO.printTable(new String[]{"Patient Name", "Priority", "Visit Time"}, rows);
    }
    
    private void callNextPatient()
    {
        try{
            Record called = examinationService.callNext(doctor);
            if(called == null) {
                ConsoleIO.info("There is no patient waiting in your department.");
            } else {
                Patient patient = patientService.findById(called.getPatientId());
                ConsoleIO.success("Now examining: "
                        + (patient == null ? called.getPatientId() : patient.getName())
                        + " (" + called.getPriority() + ")");
            }
        } catch(InvalidInputException | NotFoundException e) {
            ConsoleIO.error(e.getMessage());
        }
    }
    
    private void completeCurrentExamination()
    {
        try
        {
            String recordId = doctor.getCurrentRecordId();
            if(recordId == null) {
                throw new InvalidInputException("You are not examining any patient. Use option 2 to call one first.");
            }
            Record examined = recordService.findById(recordId);
            Patient examinedPatient = examined == null ? null : patientService.findById(examined.getPatientId());
            String patientName = examinedPatient == null ? "the patient" : examinedPatient.getName();
            String diagnosis = ConsoleIO.readLine("Diagnosis: ");
            examinationService.completeExamination(doctor, diagnosis);
            ConsoleIO.success("Examination completed for " + patientName + ".");
        }
        catch(InvalidInputException|NotFoundException e)
        {
            ConsoleIO.error(e.getMessage());
        }
    }
    
    private void searchRecord()
    {
        System.out.println();
        System.out.println("===== SEARCH RECORDS =====");
        System.out.println("1. Search by patient name");
        System.out.println("2. Search by visit date");
        System.out.println("3. Search by status");
        System.out.println("4. Advanced Search");
        System.out.println("0. Back");

        try
        {
            int choice = ConsoleIO.readChoice("Choose: ", 0, 4);
            RecordFilter filter = new RecordFilter();

            switch (choice) {
                case 1:
                    String patientName = ConsoleIO.readLine("Patient name: ");
                    if (patientName.isEmpty()) {
                        throw new InvalidInputException("Patient name must not be empty.");
                    }
                    filter.setPatientNameKeyword(patientName);
                    printRecordResults(recordService.search(filter));
                    break;
                case 2:
                    filter.setVisitDate(readVisitDate());
                    printRecordResults(recordService.search(filter));
                    break;
                case 3:
                    filter.setStatus(readRecordStatus());
                    printRecordResults(recordService.search(filter));
                    break;
                case 4:
                    advancedSearch();
                    break;
                default:
                    break;
            }
        }
        catch (InvalidInputException e)
        {
            ConsoleIO.error(e.getMessage());
        }
    }

    private void advancedSearch()
    {
        try
        {
            RecordFilter filter = new RecordFilter();

            System.out.println("Patient filter:");
            System.out.println("1. Patient code");
            System.out.println("2. Patient name");
            System.out.println("0. No patient filter");
            int patientChoice = ConsoleIO.readChoice("Choose: ", 0, 2);
            if (patientChoice == 1) {
                String patientCode = ConsoleIO.readLine("Patient code (for example pat-7): ");
                if (!patientCode.isEmpty()) {
                    Patient byCode = patientService.findByCode(patientCode);
                    if (byCode == null) {
                        throw new NotFoundException("Patient code '" + patientCode + "' was not found.");
                    }
                    filter.setPatientId(byCode.getId());
                }
            } else if (patientChoice == 2) {
                String patientName = ConsoleIO.readLine("Patient name: ");
                if (!patientName.isEmpty()) {
                    filter.setPatientNameKeyword(patientName);
                }
            }

            String visitDate = ConsoleIO.readLine("Visit date (dd/MM/yyyy, blank to skip): ");
            if (!visitDate.isEmpty()) {
                filter.setVisitDate(parseVisitDate(visitDate));
            }

            System.out.println("Status filter:");
            System.out.println("0. Any status");
            RecordStatus[] statuses = RecordStatus.values();
            for (int i = 0; i < statuses.length; i++) {
                System.out.println((i + 1) + ". " + statuses[i]);
            }
            int statusChoice = ConsoleIO.readChoice("Choose: ", 0, statuses.length);
            if (statusChoice > 0) {
                filter.setStatus(statuses[statusChoice - 1]);
            }

            String departmentName = ConsoleIO.readLine("Department name (blank to skip): ");
            if (!departmentName.isEmpty()) {
                Department byName = departmentService.findByName(departmentName);
                if (byName == null) {
                    throw new NotFoundException("The department '" + departmentName + "' was not found.");
                }
                filter.setDepartmentId(byName.getId());
            }

            printRecordResults(recordService.search(filter));
        }
        catch (InvalidInputException | NotFoundException e)
        {
            ConsoleIO.error(e.getMessage());
        }
    }

    private LocalDateTime readVisitDate() throws InvalidInputException
    {
        String text = ConsoleIO.readLine("Visit date (dd/MM/yyyy): ");
        if (text.isEmpty()) {
            throw new InvalidInputException("Visit date must not be empty.");
        }
        return parseVisitDate(text);
    }

    private LocalDateTime parseVisitDate(String value) throws InvalidInputException
    {
        return DateUtils.parseDate(value).atStartOfDay();
    }

    private RecordStatus readRecordStatus() throws InvalidInputException
    {
        RecordStatus[] statuses = RecordStatus.values();
        for (int i = 0; i < statuses.length; i++) {
            System.out.println((i + 1) + ". " + statuses[i]);
        }
        int choice = ConsoleIO.readChoice("Choose status: ", 1, statuses.length);
        return statuses[choice - 1];
    }

    private void printRecordResults(List<Record> records)
    {
        if (records.isEmpty()) {
            ConsoleIO.info("No records found.");
            return;
        }

        List<String[]> rows = new ArrayList<>();
        for (Record record : records) {
            Patient patient = patientService.findById(record.getPatientId());
            Department department = departmentService.findById(record.getCurrentDepartmentId());
            rows.add(new String[]{
                patient == null ? record.getPatientId() : patient.getName(),
                DateUtils.format(record.getVisitTime()),
                record.getStatus().toString(),
                department == null ? record.getCurrentDepartmentId() : department.getName()
            });
        }

        ConsoleIO.printTable(
                new String[]{"Patient Name", "Visit Time", "Status", "Department"},
                rows);
    }
}
