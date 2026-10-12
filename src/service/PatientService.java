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
    private final List<Patient> _patientList;
    private int _idCounter;
    
    public PatientService() {
        this._patientList = new ArrayList<>();
        this._idCounter = 1;
    }
    
    public Patient findById(String id) {
        if(id == null) {
            return null;
        }
        
        for(Patient p : _patientList) {
            if(p.getId().equals(id)) {
                return p;
            }
        }
        
        return null;
    }
    
    public String register(String name) {
        if (name == null || name.trim().isEmpty()){
            return null;
        }
        
        String id = "PT-" + TextUtils.cleanName(name.trim()) + "-" + _idCounter;
        _idCounter++;
        Patient patient = new Patient(id, name.trim());
        _patientList.add(patient);
        
        
        return id;
    }
    
    public boolean update(String id, String newName) {
        Patient patient = findById(id);
        
        if (patient == null) {
            return false;
        }
        
        if (newName == null || newName.trim().isEmpty()) {
            return false;
        }
        
        patient.setName(newName.trim());
        
        
        return true;
    }
    
    public boolean remove(String id) {
        Patient patient = findById(id);
        
        if (patient == null) {
            return false;
        }
        
        _patientList.remove(patient);
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
