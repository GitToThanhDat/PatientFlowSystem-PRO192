package model;

public class Receptionist extends User {
    private static final long serialVersionUID = 1L;
    public Receptionist(String id, String userName, String password, String fullName, boolean isActive){
        super(id, Role.RECEPTIONIST, userName, password, fullName, isActive );
    }
}
