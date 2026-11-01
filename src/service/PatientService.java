package service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import exception.InvalidInputException;
import exception.NotFoundException;
import model.Patient;
import util.TextUtils;

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

    public Patient findByCode(String code) {
        if(code == null) {
            return null;
        }

        for(Patient p : _patientList) {
            if(p.getId().equalsIgnoreCase(code.trim())) {
                return p;
            }
        }

        return null;
    }

    public List<Patient> findByName(String name) {
        String key = TextUtils.cleanName(name);
        List<Patient> result = new ArrayList<>();
        for (Patient p : _patientList) {
            if (TextUtils.cleanName(p.getName()).equals(key)) {
                result.add(p);
            }
        }

        return result;
    }

    private String checkName(String name) throws InvalidInputException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Patient name must not be empty.");
        }
        if (name.contains("?") || name.trim().equals("0")) {
            throw new InvalidInputException("Patient name must not contain '?' and must not be '0'.");
        }
        return name.trim();
    }

    private void checkDateOfBirth(LocalDate dateOfBirth) throws InvalidInputException {
        if (dateOfBirth == null) {
            return;
        }
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new InvalidInputException("Date of birth must not be in the future.");
        }
        if (dateOfBirth.getYear() < 1900) {
            throw new InvalidInputException("Date of birth must not be before 1900.");
        }
    }

    public String register(String name, LocalDate dateOfBirth) throws InvalidInputException {
        String cleanName = checkName(name);
        checkDateOfBirth(dateOfBirth);

        String id = "pat-" + _idCounter;
        _idCounter++;
        _patientList.add(new Patient(id, cleanName, dateOfBirth));

        return id;
    }

    public void update(String id, String newName) throws NotFoundException, InvalidInputException {
        Patient patient = findById(id);
        if (patient == null) {
            throw new NotFoundException("Patient not found.");
        }

        patient.setName(checkName(newName));
    }

    public void updateDateOfBirth(String id, LocalDate dateOfBirth) throws NotFoundException, InvalidInputException {
        Patient patient = findById(id);
        if (patient == null) {
            throw new NotFoundException("Patient not found.");
        }
        checkDateOfBirth(dateOfBirth);

        patient.setDateOfBirth(dateOfBirth);
    }

    public void remove(String id) throws NotFoundException {
        Patient patient = findById(id);
        if (patient == null) {
            throw new NotFoundException("Patient not found.");
        }

        _patientList.remove(patient);
    }

    public List<Patient> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>(_patientList);
        }

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

    public int getNextNumber() {
        return _idCounter;
    }

    public void ensureNextNumberAtLeast(int number) {
        if (number > _idCounter) {
            _idCounter = number;
        }
    }

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
