/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ACER
 */
public class Receptionist extends User {
    public Receptionist(String id, String userName, String fullName, String password, boolean isActive){
        super(id, Role.RECEPTIONIST, userName, password, fullName, isActive );
    }
}
