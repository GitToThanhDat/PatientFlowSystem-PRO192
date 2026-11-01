package service;
import exception.AuthenticationException;
import exception.DuplicateDataException;
import exception.InvalidInputException;
import exception.NotFoundException;
import java.util.List;
import java.util.ArrayList;
import model.Admin;
import model.Doctor;
import model.Receptionist;
import model.User;
import model.Role;
import util.TextUtils;

public class AuthService {
    private final List<User> _userList;
    private final DepartmentService _departmentService;

    public AuthService(DepartmentService departmentService){
        this._userList= new ArrayList<>() ;
        this._departmentService = departmentService;
    }

    public User findById(String id){
        for (User u : this._userList){
            if(u.getId().equals(id)) return u;
        }
        return null;
    }

    public User findByUserName(String userName){
        if (userName == null) return null;
        for (User u : this._userList){
            if(u.getUserName().equalsIgnoreCase(userName.trim())) return u;
        }
        return null;
    }

    public boolean exists(String userName){
        return findByUserName(userName) != null;
    }

    public List<User> search(String keyword){
        String key = TextUtils.cleanName(keyword);
        List<User> result = new ArrayList<>();
        for (User u : this._userList){
            if (TextUtils.cleanName(u.getUserName()).contains(key)
                    || TextUtils.cleanName(u.getFullName()).contains(key)) {
                result.add(u);
            }
        }
        return result;
    }

    private String checkText(String value, String label) throws InvalidInputException {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidInputException(label + " must not be empty.");
        }
        return value.trim();
    }

    public String register(
            Role role,
            String userName,
            String password,
            String fullName) throws InvalidInputException, DuplicateDataException{

        if (role == null) {
            throw new InvalidInputException("Role must not be null.");
        }
        String cleanUserName = checkText(userName, "Username");
        String cleanPassword = checkText(password, "Password");
        String cleanFullName = checkText(fullName, "Full name");
        if (cleanUserName.contains("?") || cleanUserName.equals("0")) {
            throw new InvalidInputException("Username must not contain '?' and must not be '0'.");
        }

        if (findByUserName(cleanUserName) != null){
            throw new DuplicateDataException("Username '" + cleanUserName + "' already exists.");
        }
        String id = cleanUserName;
        switch (role){
            case DOCTOR:
                Doctor doctor = new Doctor(id, cleanUserName, cleanPassword, cleanFullName, true, null);
                this._userList.add(doctor);
                break;
            case RECEPTIONIST:
                Receptionist recep  = new Receptionist(id, cleanUserName, cleanPassword, cleanFullName, true);
                this._userList.add(recep);
                break;
            case ADMIN:
                Admin ad =new Admin(id, cleanUserName, cleanPassword, cleanFullName, true);
                this._userList.add(ad);
                break;
        }
        return id;
    }

    public User login(String userName, String password) throws AuthenticationException{
        if (userName == null || password == null) {
            throw new AuthenticationException("Username and password must not be empty.");
        }
        User u = findByUserName(userName);
        if (u == null) {
            throw new AuthenticationException("Account '" + userName.trim() + "' does not exist.");
        }
        if (!u.getPassword().equals(password)) {
            throw new AuthenticationException("Incorrect password.");
        }
        if (!u.isActivate()) {
            throw new AuthenticationException("Account '" + u.getUserName()
                    + "' has been deactivated. Please contact an administrator.");
        }
        return u;
    }

    public void updateAccount(String userName, String newFullName) throws NotFoundException, InvalidInputException
    {
        User u = findByUserName(userName);
        if (u == null) {
            throw new NotFoundException("Account '" + userName + "' not found.");
        }
        u.setFullName(checkText(newFullName, "Full name"));
    }

    public void deactivateAccount(String userName) throws NotFoundException {
        User u= findByUserName(userName);
        if (u == null) {
            throw new NotFoundException("Account '" + userName + "' not found.");
        }
        u.setActivate(false);
    }

    public void activateAccount(String userName) throws NotFoundException {
        User u = findByUserName(userName);
        if (u == null) {
            throw new NotFoundException("Account '" + userName + "' not found.");
        }
        u.setActivate(true);
    }

    public void deleteAccount(String userName) throws NotFoundException {
        User u = findByUserName(userName);
        if (u == null) {
            throw new NotFoundException("Account '" + userName + "' not found.");
        }
        this._userList.remove(u);
    }

    public int countDoctorsInDepartment(String departmentId) {
        int count = 0;
        for (User u : this._userList) {
            if (u instanceof Doctor && departmentId != null
                    && departmentId.equals(((Doctor) u).getDepartmentId())) {
                count++;
            }
        }
        return count;
    }

    public void assignDoctorToDepartment(String userName, String departmentId)
            throws NotFoundException, InvalidInputException {
        User u = findByUserName(userName);
        if (u == null) {
            throw new NotFoundException("Account '" + userName + "' not found.");
        }
        if (!(u instanceof Doctor)) {
            throw new InvalidInputException("Account '" + u.getUserName() + "' is a "
                    + u.getRole() + ", not a DOCTOR. Only doctors can be assigned to a department.");
        }
        if (_departmentService.findById(departmentId) == null) {
            throw new NotFoundException("Department not found.");
        }
        ((Doctor) u).setDepartmentId(departmentId);
    }

    public List<User> getAll(){
        return this._userList;
    }

    public void loadData(List<User> list) {
        this._userList.clear();
        if (list != null) {
            this._userList.addAll(list);
        }
    }
}
