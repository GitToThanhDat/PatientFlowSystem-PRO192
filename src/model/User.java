/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ACER
 */
public class User {
    private String id;
    private Role role;
    private String userName;
    private String password;
    private String fullName;
    private boolean isActivate;
    
    public User(String id, Role role, String userName, String password, String fullName, boolean isActive){
        this.id=id;
        this.fullName=fullName;
        this.isActivate=isActive;
        this.password=password;
        this.role=role;
        this.userName=userName;
    }
    //Get
    public String getId(){
        return this.id;
    }
    public String getFullName(){
        return this.fullName;
    }
    public Role getRole(){
        return this.role;
    }
    public String getUserName(){
        return this.userName;
    }
    public String getPassword(){
        return this.password;
    }
    public boolean isActivate(){
        return this.isActivate;
    }
    //Set
    public void setUserName(String userName){
        this.userName=userName;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public void setFullName(String fullName){
        this.fullName=fullName;
    }
    public void setActivate(boolean isActivate){
        this.isActivate=isActivate;
    }
    public void setRole(Role role){
        this.role=role;
    }
    
    
}
