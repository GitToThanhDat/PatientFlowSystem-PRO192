/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.util.ArrayList;
import java.util.List;

import exception.InvalidInputException;
import exception.NotFoundException;
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

    // Kiểm tra tên hợp lệ, trả về tên đã trim. Dùng chung cho register và update.
    private String checkName(String name) throws InvalidInputException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Patient name must not be empty.");
        }
        if (name.contains("|")) {
            throw new InvalidInputException("Patient name must not contain '|': " + name);
        }
        return name.trim();
    }

    public String register(String name) throws InvalidInputException {
        String cleanName = checkName(name);

        String id = "PT-" + TextUtils.cleanName(cleanName) + "-" + _idCounter;
        _idCounter++;
        _patientList.add(new Patient(id, cleanName));

        return id;
    }

    public void update(String id, String newName) throws NotFoundException, InvalidInputException {
        Patient patient = findById(id);
        if (patient == null) {
            throw new NotFoundException("Patient not found with ID: " + id);
        }

        patient.setName(checkName(newName));
    }

    public void remove(String id) throws NotFoundException {
        Patient patient = findById(id);
        if (patient == null) {
            throw new NotFoundException("Patient not found with ID: " + id);
        }

        _patientList.remove(patient);
    }

    public List<Patient> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>(_patientList);
        }

        // So sánh không dấu, không phân biệt hoa/thường: "van" tìm được "Nguyễn Văn A".
        String cleanKeyword = TextUtils.cleanName(keyword);
        List<Patient> result = new ArrayList<>();
        for (Patient p : _patientList) {
            if (TextUtils.cleanName(p.getName()).contains(cleanKeyword)) {
                result.add(p);
            }
        }

        return result;
    }

    public List<Patient> getAll() {
        return _patientList;
    }

    // Nạp dữ liệu từ file, rồi đặt _idCounter = số lớn nhất ở đuôi ID + 1.
    public void loadData(List<Patient> list) {
        _patientList.clear();
        if (list != null) {
            _patientList.addAll(list);
        }

        int max = 0;
        for (Patient p : _patientList) {
            max = Math.max(max, TextUtils.extractCounter(p.getId()));
        }
        _idCounter = max + 1;
    }
}
