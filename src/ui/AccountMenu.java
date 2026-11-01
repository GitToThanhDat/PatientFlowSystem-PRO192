package ui;

import exception.DuplicateDataException;
import exception.InvalidInputException;
import exception.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import model.Department;
import model.Doctor;
import model.Role;
import model.User;
import service.AuthService;
import service.DepartmentService;
import service.RecordService;

public class AccountMenu {
    private static final int MAX = 7;

    private final AuthService _authService;
    private final DepartmentService _departmentService;
    private final RecordService _recordService;
    private final User _currentUser;

    public AccountMenu(AuthService au, DepartmentService de, RecordService re, User currentUser){
        this._authService=au;
        this._departmentService=de;
        this._recordService=re;
        this._currentUser=currentUser;
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            try {
                int choice = ConsoleIO.readChoice("Choose: ", 0, MAX);
                if (choice == 0) running = false; else handle(choice);
            } catch (InvalidInputException | NotFoundException | DuplicateDataException e) {
                ConsoleIO.error(e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("===== ACCOUNT MENU =====");
        System.out.println("1. Create account");
        System.out.println("2. List accounts");
        System.out.println("3. Update full name");
        System.out.println("4. Deactivate account");
        System.out.println("5. Reactivate account");
        System.out.println("6. Delete account");
        System.out.println("7. Assign doctor to department");
        System.out.println("0. Back");
    }

    private void handle(int choice)
            throws InvalidInputException, NotFoundException, DuplicateDataException {
        switch (choice) {
            case 1: createAccount(); break;
            case 2: listAccounts(); break;
            case 3: updateFullName(); break;
            case 4: deactivateAccount(); break;
            case 5: reactivateAccount(); break;
            case 6: deleteAccount(); break;
            case 7: assignDoctor(); break;
            default: break;
        }
    }

    private void createAccount() throws InvalidInputException, DuplicateDataException {
        System.out.println("Role: 1. ADMIN   2. DOCTOR   3. RECEPTIONIST");
        int r = ConsoleIO.readChoice("Choose role: ", 1, 3);
        Role role;
        switch (r) {
            case 1: role = Role.ADMIN; break;
            case 2: role = Role.DOCTOR; break;
            default: role = Role.RECEPTIONIST; break;
        }
        String userName = ConsoleIO.readLine("Username: ");
        if (userName.equals("0")) {
            throw new InvalidInputException("Username '0' is reserved for exiting the login screen. Please choose another username.");
        }
        String password = ConsoleIO.readLine("Password: ");
        String fullName = ConsoleIO.readLine("Full name: ");
        String id = _authService.register(role, userName, password, fullName);
        ConsoleIO.success(role + " account created for '" + fullName.trim() + "' (username: " + id + ").");
    }

    private void listAccounts() {
        List<User> users = _authService.getAll();
        if (users.isEmpty()) {
            ConsoleIO.info("No accounts found.");
            return;
        }
        List<String[]> rows = new ArrayList<>();
        for (User u : users) {
            String dept = "";
            if (u instanceof Doctor) {
                String deptId = ((Doctor) u).getDepartmentId();
                Department department = (deptId == null) ? null : _departmentService.findById(deptId);
                dept = (deptId == null) ? "(none)" : (department == null ? "(deleted)" : department.getName());
            }
            rows.add(new String[]{
                u.getUserName(), u.getFullName(),
                u.getRole().name(), u.isActivate() ? "yes" : "no", dept
            });
        }
        ConsoleIO.printTable(
                new String[]{"Username", "Full name", "Role", "Active", "Department"}, rows);
    }

    private void updateFullName() throws InvalidInputException, NotFoundException {
        User target = Picker.account(_authService, "Account to update (username)");
        if (target == null) {
            return;
        }
        String newName = ConsoleIO.readLine("New full name for '" + target.getFullName() + "': ");
        _authService.updateAccount(target.getUserName(), newName);
        ConsoleIO.success("Account '" + target.getUserName() + "' is now named '" + target.getFullName() + "'.");
    }

    private void deactivateAccount() throws InvalidInputException, NotFoundException {
        User target = Picker.account(_authService, "Account to deactivate (username)");
        if (target == null) {
            return;
        }
        if (!target.isActivate()) {
            throw new InvalidInputException("Account '" + target.getUserName() + "' is already deactivated.");
        }
        if (target.getRole() == Role.ADMIN && countActiveAdmins() <= 1) {
            throw new InvalidInputException("Cannot deactivate '" + target.getUserName()
                    + "': it is the last active ADMIN account. Create another admin first.");
        }
        _authService.deactivateAccount(target.getUserName());
        ConsoleIO.success("Account '" + target.getUserName() + "' deactivated.");
    }

    private void reactivateAccount() throws InvalidInputException, NotFoundException {
        User target = Picker.account(_authService, "Account to reactivate (username)");
        if (target == null) {
            return;
        }
        if (target.isActivate()) {
            throw new InvalidInputException("Account '" + target.getUserName() + "' is already active.");
        }
        _authService.activateAccount(target.getUserName());
        ConsoleIO.success("Account '" + target.getUserName() + "' reactivated.");
    }

    private void deleteAccount() throws InvalidInputException, NotFoundException {
        User target = Picker.account(_authService, "Account to delete (username)");
        if (target == null) {
            return;
        }
        String name = target.getUserName();
        if (name.equalsIgnoreCase(_currentUser.getUserName())) {
            throw new InvalidInputException("You cannot delete the account you are logged in with.");
        }
        if (target.getRole() == Role.ADMIN && target.isActivate() && countActiveAdmins() <= 1) {
            throw new InvalidInputException("Cannot delete '" + name
                    + "': it is the last active ADMIN account. Create another admin first.");
        }
        if (target instanceof Doctor) {
            if (((Doctor) target).getCurrentRecordId() != null) {
                throw new InvalidInputException("Cannot delete '" + name
                        + "': the doctor is examining a patient right now.");
            }
            if (_recordService.hasExaminedBy(target.getId())) {
                throw new InvalidInputException("Cannot delete '" + name
                        + "': the doctor has examination history. Deactivate the account instead.");
            }
        }
        if (!ConsoleIO.confirm("Delete account '" + name + "' (" + target.getFullName() + ")?")) {
            ConsoleIO.info("Nothing was deleted.");
            return;
        }
        _authService.deleteAccount(name);
        ConsoleIO.success("Account '" + name + "' deleted.");
    }

    private int countActiveAdmins() {
        int count = 0;
        for (User u : _authService.getAll()) {
            if (u.getRole() == Role.ADMIN && u.isActivate()) {
                count++;
            }
        }
        return count;
    }

    private void assignDoctor() throws InvalidInputException, NotFoundException {
        List<Department> departments = _departmentService.getAll();
        if (departments.isEmpty()) {
            ConsoleIO.info("No departments found. Create a department first.");
            return;
        }
        User doctor = Picker.account(_authService, "Doctor account (username)");
        if (doctor == null) {
            return;
        }
        Department department = Picker.department(_departmentService, "Department");
        if (department == null) {
            return;
        }
        _authService.assignDoctorToDepartment(doctor.getUserName(), department.getId());
        ConsoleIO.success("Doctor '" + doctor.getFullName() + "' assigned to department '" + department.getName() + "'.");
    }
}
