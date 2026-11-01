package model;

public class Doctor extends User{
    private static final long serialVersionUID = 1L;
    private String departmentId;
    private String currentRecordId;
    
    public Doctor (String id, String userName, String password, String fullName, boolean isActive, String departmentId){
        super(id, Role.DOCTOR, userName, password, fullName, isActive);
        this.departmentId=departmentId;
        this.currentRecordId=null;
    }
    
    public String getDepartmentId(){
        return this.departmentId;
    }
    public String getCurrentRecordId(){
        return this.currentRecordId;
    }
    public void setDepartmentId(String id){
        this.departmentId=id;
    }
    public void setCurrentRecordId(String id){
        this.currentRecordId=id;
    }
}
