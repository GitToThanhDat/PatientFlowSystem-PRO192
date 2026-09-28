/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.util.ArrayList;
import java.util.List;

import model.Patient;
import util.TextUtils;

/**
 *
 * @author ACER
 */
public class PatientService {
    private List<Patient> _patientList;
    private int _idCounter;
    
    public PatientService() {
        this._patientList = new ArrayList<>();
        this._idCounter = 1;
    }
    
    public Patient findById(String id) {        
        for(Patient p : _patientList) {
            if(p.getId().equals(id)) {
                return p;
            }
        }
        
        return null;
    }
    
    public String register(String name) {
        if (name == null || name.trim().isEmpty()){
            System.out.println("Loi: ten benh nhan khong duoc de trong.");
            return null;
        }
        
        String id = "PT-" + TextUtils.cleanName(name.trim()) + "-" + _idCounter;
        _idCounter++;
        Patient patient = new Patient(id, name.trim());
        _patientList.add(patient);
        
        System.out.println("Dang ky thanh cong benh nhan: " + id);
        
        return id;
    }
    
    public boolean update(String id, String newName) {
        Patient patient = findById(id);
        
        if (patient == null) {
            System.out.println("Loi: khong tim thay benh nhan co id " + id);
            return false;
        }
        
        if (newName == null || newName.trim().isEmpty()) {
            System.out.println("Loi: ten moi khong duoc de trong.");
            return false;
        }
        
        patient.setName(newName.trim());
        
        System.out.println("Cap nhat thanh cong benh nhan: " + id);
        
        return true;
    }
    
    public boolean remove(String id) {
        Patient patient = findById(id);
        
        if (patient == null) {
            System.out.println("Loi: khong tim thay benh nhan co id " + id);
            return false;
        }
        
        _patientList.remove(patient);
        System.out.println("Da xoa benh nhan: " + id);
        return true;
    }

    public List<Patient> searchByName(String keyword) {
        List<Patient> result = new ArrayList<>();
        
        if (keyword == null) {
            return result;
        }
        
        for (Patient p : _patientList) {
            if (p.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(p);
            }
        }
        
        return result;
    }

    public List<Patient> getAll() {
        return _patientList;
    }
}
