package ui;

import exception.DuplicateDataException;
import exception.InvalidInputException;
import exception.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import model.Department;
import model.RecordStatus;
import service.AuthService;
import service.DepartmentService;
import service.QueueService;
import service.StatisticsService;
import strategy.FCFSStrategy;
import strategy.PriorityStrategy;

public class AdminMenu {

    private static final int MAX = 7;

    private final DepartmentService departmentService;
    private final QueueService queueService;
    private final StatisticsService statisticsService;
    private final AuthService authService;

    public AdminMenu(DepartmentService departmentService,
                     QueueService queueService,
                     StatisticsService statisticsService,
                     AuthService authService) {
        this.departmentService = departmentService;
        this.queueService = queueService;
        this.statisticsService = statisticsService;
        this.authService = authService;
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            try {
                int choice = ConsoleIO.readChoice("Choose: ", 0, MAX);
                if (choice == 0) {
                    running = false;
                } else {
                    handle(choice);
                }
            } catch (InvalidInputException | NotFoundException | DuplicateDataException e) {
                ConsoleIO.error(e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("===== ADMIN MENU =====");
        System.out.println("1. Add department");
        System.out.println("2. Update department");
        System.out.println("3. Remove department");
        System.out.println("4. View all departments");
        System.out.println("5. Switch queue strategy");
        System.out.println("6. View statistics");
        System.out.println("7. Manage accounts");
        System.out.println("0. Logout");
    }

    private void handle(int choice)
            throws InvalidInputException, NotFoundException, DuplicateDataException {
        switch (choice) {
            case 1:
                addDepartment();
                break;
            case 2:
                updateDepartment();
                break;
            case 3:
                removeDepartment();
                break;
            case 4:
                viewDepartments();
                break;
            case 5:
                switchStrategy();
                break;
            case 6:
                viewStatistics();
                break;
            case 7:
                new AccountMenu(authService, departmentService).run();
                break;
            default:
                break;
        }
    }

    private void addDepartment() throws InvalidInputException, DuplicateDataException {
        String name = ConsoleIO.readLine("Department name: ");
        String id = departmentService.addDepartment(name);
        ConsoleIO.success("Department added with ID: " + id);
    }

    private void updateDepartment()
            throws NotFoundException, InvalidInputException, DuplicateDataException {
        String id = ConsoleIO.readLine("Department ID: ");
        String newName = ConsoleIO.readLine("New name: ");
        departmentService.updateDepartment(id, newName);
        ConsoleIO.success("Department " + id + " updated.");
    }

    private void removeDepartment() throws NotFoundException {
        String id = ConsoleIO.readLine("Department ID: ");
        departmentService.remove(id);
        ConsoleIO.success("Department " + id + " removed.");
    }

    private void viewDepartments() {
        List<Department> list = departmentService.getAll();
        if (list.isEmpty()) {
            ConsoleIO.info("No departments found.");
            return;
        }
        List<String[]> rows = new ArrayList<>();
        for (Department d : list) {
            rows.add(new String[]{d.getId(), d.getName()});
        }
        ConsoleIO.printTable(new String[]{"ID", "Name"}, rows);
    }

    private void switchStrategy() throws InvalidInputException {
        ConsoleIO.info("Current strategy: " + queueService.getStrategyName());
        System.out.println("1. FCFS");
        System.out.println("2. Priority");
        int choice = ConsoleIO.readChoice("Choose strategy: ", 1, 2);
        if (choice == 1) {
            queueService.setStrategy(new FCFSStrategy());
        } else {
            queueService.setStrategy(new PriorityStrategy());
        }
        ConsoleIO.success("Queue strategy switched to " + queueService.getStrategyName());
    }

    private void viewStatistics() {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"Departments", String.valueOf(statisticsService.countDepartments())});
        rows.add(new String[]{"Patients", String.valueOf(statisticsService.countPatients())});
        rows.add(new String[]{"Records", String.valueOf(statisticsService.countRecords())});
        for (RecordStatus s : RecordStatus.values()) {
            rows.add(new String[]{
                "Records - " + s,
                String.valueOf(statisticsService.countRecordsByStatus(s))
            });
        }
        ConsoleIO.printTable(new String[]{"Metric", "Value"}, rows);
    }
}