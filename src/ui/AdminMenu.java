/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import service.AuthService;
import service.DepartmentService;
import service.QueueService;
import service.StatisticsService;

/**
 *
 * @author ACER
 */
public class AdminMenu {
    private final DepartmentService _departmentService;
    private final QueueService _queueService;
    private final StatisticsService _statisticsService;
    private final AuthService _authService;
    
    public AdminMenu(DepartmentService de, QueueService qu, StatisticsService sta, AuthService auth){
        this._authService=auth;
        this._departmentService=de;
        this._queueService=qu;
        this._statisticsService=sta;
    }
    public void run() {
        
    }
    
}
