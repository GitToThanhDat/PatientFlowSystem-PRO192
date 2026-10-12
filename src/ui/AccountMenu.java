/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import service.AuthService;
import service.DepartmentService;

/**
 *
 * @author ACER
 */
public class AccountMenu {
    private final AuthService _authService;
    private final DepartmentService _departmentService;
    
    public AccountMenu(AuthService au, DepartmentService de){
        this._authService=au;
        this._departmentService=de;
    }
    public void run() {
        
    }
}
