package service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import exception.InvalidInputException;
import exception.NotFoundException;
import model.Patient;
import util.IdGenerator;
import util.TextUtils;
import util.Validator;

public class PatientService {
    private final List<Patient> _patientList;
    private final IdGenerator _ids;

    public PatientService() {
        this._patientList = new ArrayList<>();
        this._ids = new IdGenerator("pat");
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
        List<Patient> result = new ArrayList<>();
        for (Patient p : _patientList) {
            if (TextUtils.sameName(p.getName(), name)) {
                result.add(p);
            }
        }

        return result;
    }

    public List<Patient> findByNameAndDateOfBirth(String name, LocalDate dateOfBirth) {
        List<Patient> result = new ArrayList<>();
        for (Patient p : _patientList) {
            if (TextUtils.sameName(p.getName(), name)
                    && dateOfBirth != null && dateOfBirth.equals(p.getDateOfBirth())) {
                result.add(p);
            }
        }
        return result;
    }

    public String checkName(String name) throws InvalidInputException {
        return TextUtils.normalizeName(Validator.requireName(name, "Patient name"));
    }

    public void checkDateOfBirth(LocalDate dateOfBirth) throws InvalidInputException {
        Validator.requirePastDate(dateOfBirth, "Date of birth");
    }

    public String register(String name, LocalDate dateOfBirth) throws InvalidInputException {
        String cleanName = checkName(name);
        checkDateOfBirth(dateOfBirth);

        String id = _ids.next();
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

        List<Patient> result = new ArrayList<>();
        for (Patient p : _patientList) {
            if (TextUtils.containsKeyword(p.getName(), keyword)) {
                result.add(p);
            }
        }

        return result;
    }

    public List<Patient> getAll() {
        return _patientList;
    }

    public int getNextNumber() {
        return _ids.getNextNumber();
    }

    public void ensureNextNumberAtLeast(int number) {
        _ids.ensureNextNumberAtLeast(number);
    }

    public void loadData(List<Patient> list) {
        _patientList.clear();
        if (list != null) {
            _patientList.addAll(list);
        }

        List<String> ids = new ArrayList<>();
        for (Patient p : _patientList) {
            ids.add(p.getId());
        }
        _ids.resetAfter(ids);
    }
}
