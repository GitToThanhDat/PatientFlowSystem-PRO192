package model;

public class Receptionist extends User {
    public Receptionist(String id, String userName, String password, String fullName, boolean isActive){
        super(id, Role.RECEPTIONIST, userName, password, fullName, isActive );
    }
}
