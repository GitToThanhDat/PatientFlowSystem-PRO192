package ui;

import data.DepartmentData;
import data.PatientData;
import data.RecordData;
import data.SettingsData;
import data.UserData;
import exception.AuthenticationException;
import exception.DuplicateDataException;
import exception.InvalidInputException;
import exception.NotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import model.AppSettings;
import model.Department;
import model.Doctor;
import model.Role;
import model.User;
import service.AuthService;
import service.DepartmentService;
import service.ExaminationService;
import service.PatientService;
import service.QueueService;
import service.RecordService;
import service.StatisticsService;
import strategy.FCFSStrategy;
import strategy.PriorityStrategy;
import strategy.QueueStrategy;

public class MainMenu {

    private final DepartmentService departmentService = new DepartmentService();
    private final PatientService patientService = new PatientService();
    private final AuthService authService = new AuthService(departmentService);
    private final RecordService recordService = new RecordService(patientService, departmentService);
    private final QueueService queueService = new QueueService(new FCFSStrategy());
    private final ExaminationService examinationService = new ExaminationService(recordService, queueService);
    private final StatisticsService statisticsService = new StatisticsService(patientService, departmentService, recordService);

    private final DepartmentData departmentData = new DepartmentData();
    private final PatientData patientData = new PatientData();
    private final UserData userData = new UserData();
    private final RecordData recordData = new RecordData();
    private final SettingsData settingsData = new SettingsData();
    private AppSettings settings = new AppSettings();

    public void start() {
        loadAll();
        applySettings();
        seedIfEmpty();
        try {
            boolean running = true;
            while (running) {
                running = loginOnce();
            }
        } catch (NoSuchElementException e) {
            System.out.println();
            ConsoleIO.info("Input closed. Exiting.");
        } finally {
            saveAll();
        }
        ConsoleIO.info("Goodbye!");
    }

    private boolean loginOnce() {
        System.out.println();
        System.out.println("===== PATIENT FLOW SYSTEM - LOGIN =====");
        String userName = ConsoleIO.readLine("Username (0 to exit): ");
        if (userName.equals("0")) {
            return false;
        }
        if (!authService.exists(userName)) {
            ConsoleIO.error("Account '" + userName + "' does not exist.");
            return true;
        }
        String password = ConsoleIO.readLine("Password: ");
        try {
            User user = authService.login(userName, password);
            System.out.println("Welcome, " + user.getFullName() + ".");
            try {
                openMenu(user);
            } catch (NoSuchElementException e) {
                throw e;
            } catch (RuntimeException e) {
                ConsoleIO.error("Unexpected error: " + e);
            }
            saveAll();
        } catch (AuthenticationException e) {
            ConsoleIO.error(e.getMessage());
        }
        return true;
    }

    private void openMenu(User user) {
        switch (user.getRole()) {
            case ADMIN:
                new AdminMenu(departmentService, queueService, statisticsService, authService,
                        recordService, user, new Runnable() {
                            @Override
                            public void run() {
                                saveSettings();
                            }
                        }).run();
                break;
            case RECEPTIONIST:
                new ReceptionistMenu(patientService, recordService, departmentService).run();
                break;
            case DOCTOR:
                if (user instanceof Doctor) {
                    new DoctorMenu((Doctor) user, recordService, patientService,
                            departmentService, examinationService).run();
                } else {
                    ConsoleIO.error("Account '" + user.getUserName() + "' has role DOCTOR but is not a Doctor object.");
                }
                break;
            default:
                ConsoleIO.error("Unknown role: " + user.getRole());
                break;
        }
    }

    private void loadAll() {
        try {
            settings = settingsData.load();
        } catch (IOException e) {
            ConsoleIO.error("Cannot load settings: " + e.getMessage());
        }
        try {
            departmentService.loadData(departmentData.load());
        } catch (IOException e) {
            ConsoleIO.error("Cannot load departments: " + e.getMessage());
        }
        try {
            patientService.loadData(patientData.load());
        } catch (IOException e) {
            ConsoleIO.error("Cannot load patients: " + e.getMessage());
        }
        try {
            authService.loadData(userData.load());
        } catch (IOException e) {
            ConsoleIO.error("Cannot load users: " + e.getMessage());
        }
        try {
            recordService.loadData(recordData.load());
        } catch (IOException e) {
            ConsoleIO.error("Cannot load records: " + e.getMessage());
        }
    }

    private void applySettings() {
        queueService.setStrategy(createStrategy(settings.getQueueStrategy()));
        departmentService.ensureNextNumberAtLeast(settings.getNextDepartmentNumber());
        patientService.ensureNextNumberAtLeast(settings.getNextPatientNumber());
        recordService.ensureNextNumberAtLeast(settings.getNextRecordNumber());
    }

    private QueueStrategy createStrategy(String name) {
        if (new PriorityStrategy().getName().equalsIgnoreCase(name)) {
            return new PriorityStrategy();
        }
        return new FCFSStrategy();
    }

    private void saveSettings() {
        settings.setQueueStrategy(queueService.getStrategyName());
        settings.setNextDepartmentNumber(departmentService.getNextNumber());
        settings.setNextPatientNumber(patientService.getNextNumber());
        settings.setNextRecordNumber(recordService.getNextNumber());
        try {
            settingsData.save(settings);
        } catch (IOException e) {
            ConsoleIO.error("Cannot save settings: " + e.getMessage());
        }
    }

    private void saveAll() {
        saveSettings();
        try {
            departmentData.save(departmentService.getAll());
        } catch (IOException e) {
            ConsoleIO.error("Cannot save departments: " + e.getMessage());
        }
        try {
            patientData.save(patientService.getAll());
        } catch (IOException e) {
            ConsoleIO.error("Cannot save patients: " + e.getMessage());
        }
        try {
            userData.save(authService.getAll());
        } catch (IOException e) {
            ConsoleIO.error("Cannot save users: " + e.getMessage());
        }
        try {
            recordData.save(recordService.getAll());
        } catch (IOException e) {
            ConsoleIO.error("Cannot save records: " + e.getMessage());
        }
    }

    private void seedIfEmpty() {
        if (!authService.getAll().isEmpty()) {
            return;
        }
        try {
            String generalId = null;
            for (Department d : departmentService.getAll()) {
                if (d.getName().equalsIgnoreCase("General")) {
                    generalId = d.getId();
                }
            }
            if (generalId == null) {
                generalId = departmentService.addDepartment("General");
            }
            authService.register(Role.ADMIN, "admin", "admin123", "System Admin");
            authService.register(Role.RECEPTIONIST, "reception", "reception123", "Front Desk");
            String doctorId = authService.register(Role.DOCTOR, "doctor", "doctor123", "General Doctor");
            authService.assignDoctorToDepartment(doctorId, generalId);

            ConsoleIO.info("First run: default data created (department 'General').");
            ConsoleIO.info("  ADMIN        admin / admin123");
            ConsoleIO.info("  RECEPTIONIST reception / reception123");
            ConsoleIO.info("  DOCTOR       doctor / doctor123");
            saveAll();
        } catch (InvalidInputException | DuplicateDataException | NotFoundException e) {
            ConsoleIO.error("Cannot create default data: " + e.getMessage());
        }
    }
}
