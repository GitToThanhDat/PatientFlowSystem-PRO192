/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import model.Doctor;
import service.ExaminationService;
import service.PatientService;
import service.RecordService;

/**
 *
 * @author ACER
 */
public class DoctorMenu {
    private final Doctor _doctor;
    private final RecordService _recordService;
    private final PatientService _patientService;
    private final ExaminationService _examinationService;

    public DoctorMenu(Doctor doctor, RecordService recordService,
                      PatientService patientService, ExaminationService examinationService) {
        this._doctor = doctor;
        this._recordService = recordService;
        this._patientService = patientService;
        this._examinationService = examinationService;
    }

    public void run() {
        
    }

}
