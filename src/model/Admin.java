/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ACER
 */
public class Admin extends User {
    public Admin(String id, String userName, String password, String fullName, boolean isActive){
        super(id, Role.ADMIN, userName, password, fullName, isActive);
    }
}
