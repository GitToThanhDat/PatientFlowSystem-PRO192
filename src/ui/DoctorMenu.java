/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import model.Doctor;
import service.ExaminationService;
import service.PatientService;
import service.RecordService;
import java.io.*;
import exception.*;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import model.*;
import service.*;

/**
 *
 * @author ACER
 */
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
        
        //// CONTEXT INFORMATION
        
        // warning
        System.out.println();
        if(!isValidDoctor())
        {
            System.out.println("(!) You are currently not in any department!");
            System.out.println("(!) Contact your admin for more infomation!");
        }
        else if(!isValidDepartment())
        {
            System.out.println("(!) The department you are in is not found!");
            System.out.println("(!) Contact your admin for more infomation!");
        }
        
        // information
        System.out.println("Id: " + doctor.getId());
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
            Patient patient = patientService.findById(record.getPatientId());
            System.out.println();
            System.out.println("Current examining patient: " + patient.getName());
            System.out.println("Patient's visit time: " + record.getVisitTime());
        }
        
        System.out.println();
        
        // menu
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
                record.getId(),
                patient.getName(),
                record.getVisitTime().toString()
            });
        }
        
        ConsoleIO.printTable(new String[]{"Record Id", "Patient Name", "Visit Time"}, rows);
    }
    
    private void callNextPatient()
    {
        try{
            examinationService.callNext(doctor);
        } catch(InvalidInputException e) {
            ConsoleIO.error(e.getMessage());
        } catch(NotFoundException e) {
            System.out.println("There is no patient!");
            ConsoleIO.pauseConsole();
        }
    }
    
    private void completeCurrentExamination()
    {
        try
        {
            String diagnosis = ConsoleIO.readLine("Diagnosis: ");
            examinationService.completeExamination(doctor, diagnosis);
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
        System.out.println("1. Search by id");
        System.out.println("2. Search by patient name");
        System.out.println("3. Search by visit date");
        System.out.println("4. Search by status");
        System.out.println("5. Advanced Search");
        System.out.println("0. Back");

        try
        {
            int choice = ConsoleIO.readChoice("Choose: ", 0, 5);
            RecordFilter filter = new RecordFilter();

            switch (choice) {
                case 1:
                    String recordId = ConsoleIO.readLine("Record ID: ");
                    if (recordId.isEmpty()) {
                        throw new InvalidInputException("Record ID must not be empty.");
                    }
                    Record record = recordService.findById(recordId);
                    List<Record> records = new ArrayList<>();
                    if (record != null) {
                        records.add(record);
                    }
                    printRecordResults(records);
                    break;
                case 2:
                    String patientName = ConsoleIO.readLine("Patient name: ");
                    if (patientName.isEmpty()) {
                        throw new InvalidInputException("Patient name must not be empty.");
                    }
                    filter.setPatientNameKeyword(patientName);
                    printRecordResults(recordService.search(filter));
                    break;
                case 3:
                    filter.setVisitDate(readVisitDate());
                    printRecordResults(recordService.search(filter));
                    break;
                case 4:
                    filter.setStatus(readRecordStatus());
                    printRecordResults(recordService.search(filter));
                    break;
                case 5:
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
            System.out.println("1. Patient ID");
            System.out.println("2. Patient name");
            System.out.println("0. No patient filter");
            int patientChoice = ConsoleIO.readChoice("Choose: ", 0, 2);
            if (patientChoice == 1) {
                String patientId = ConsoleIO.readLine("Patient ID: ");
                if (!patientId.isEmpty()) {
                    filter.setPatientId(patientId);
                }
            } else if (patientChoice == 2) {
                String patientName = ConsoleIO.readLine("Patient name: ");
                if (!patientName.isEmpty()) {
                    filter.setPatientNameKeyword(patientName);
                }
            }

            String visitDate = ConsoleIO.readLine("Visit date (YYYY-MM-DD, blank to skip): ");
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

            String departmentId = ConsoleIO.readLine("Department ID (blank to skip): ");
            if (!departmentId.isEmpty()) {
                filter.setDepartmentId(departmentId);
            }

            printRecordResults(recordService.search(filter));
        }
        catch (InvalidInputException e)
        {
            ConsoleIO.error(e.getMessage());
        }
    }

    private LocalDateTime readVisitDate() throws InvalidInputException
    {
        return parseVisitDate(ConsoleIO.readLine("Visit date (YYYY-MM-DD): "));
    }

    private LocalDateTime parseVisitDate(String value) throws InvalidInputException
    {
        try {
            return LocalDate.parse(value).atStartOfDay();
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Invalid date. Please use YYYY-MM-DD.");
        }
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
            rows.add(new String[]{
                record.getId(),
                patient == null ? record.getPatientId() : patient.getName(),
                record.getVisitTime().toString(),
                record.getStatus().toString(),
                record.getCurrentDepartmentId()
            });
        }

        ConsoleIO.printTable(
                new String[]{"Record ID", "Patient Name", "Visit Time", "Status", "Department ID"},
                rows);
    }
}
