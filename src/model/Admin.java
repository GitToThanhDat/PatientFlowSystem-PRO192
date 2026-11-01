package model;

public class Admin extends User {
    private static final long serialVersionUID = 1L;
    public Admin(String id, String userName, String password, String fullName, boolean isActive){
        super(id, Role.ADMIN, userName, password, fullName, isActive);
    }
    
}
