/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import service.DepartmentService;
import service.PatientService;
import service.RecordService;

/**
 *
 * @author ACER
 */
public class ReceptionistMenu {
    private final PatientService _patientService;
    private final RecordService _recordService;
    private DepartmentService _departmentService;
    
    public ReceptionistMenu(PatientService pa, RecordService re, DepartmentService de){
        this._departmentService=de;
        this._patientService=pa;
        this._recordService=re;
    }
    public void run() {
        
    }
}
