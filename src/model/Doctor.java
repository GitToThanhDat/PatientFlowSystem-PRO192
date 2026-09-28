/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ACER
 */
public class Doctor extends User{
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
}
