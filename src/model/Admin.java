package model;

public class Admin extends User {
    public Admin(String id, String userName, String password, String fullName, boolean isActive){
        super(id, Role.ADMIN, userName, password, fullName, isActive);
    }
    
}
