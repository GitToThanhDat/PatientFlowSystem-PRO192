/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.util.*;
import java.util.stream.Collectors;
import model.*;
import exception.*;
import java.util.stream.Stream;

/**
 *
 * @author algori07
 */
public class RecordService {
    private final List<Record> recordList;
    private int idCounter;
    private final PatientService patientService;
    private final DepartmentService departmentService;
    
    public RecordService(
            PatientService patientService,
            DepartmentService departmentService)
    {
        this.recordList = new ArrayList<>();
        this.idCounter = 1;
        this.patientService = patientService;
        this.departmentService = departmentService;
    }
    
    public Record findById(String id)
    {
        for(Record record : this.recordList)
        {
            if(record.getId().equals(id))
            {
                return record;
            }
        }
        return null;
    }
    
    public String register(
            String patientId,
            String departmentId,
            Priority priority)
        throws InvalidInputException, NotFoundException
    {
        if(patientId == null)
            throw new InvalidInputException("patientId must not be null!");
        Patient patient = this.patientService.findById(patientId);
        if(patient == null)
            throw new NotFoundException("Patient not found with id: " + patientId);
        
        if(departmentId == null)
            throw new InvalidInputException("departmentId must not be null!");
        Department department = this.departmentService.findById(departmentId);
        if(department == null)
            throw new NotFoundException("Department not found with id: " + departmentId);
        
        if(priority == null)
            throw new InvalidInputException("priority must not be null");
        
        StringBuilder recordIdBuilder = new StringBuilder();
        recordIdBuilder.append("REC-");
        recordIdBuilder.append(this.idCounter);
        this.idCounter++;
        
        String recordId = recordIdBuilder.toString();
        
        Record record = new Record(
                recordId,
                patientId,
                departmentId,
                priority);
        
        patient.addRecord(recordId);
        department.addToQueue(recordId);
        this.recordList.add(record);
        
        return recordId;
    }
    
    public void updateStatus(String recordId, RecordStatus newStatus)
            throws InvalidInputException, NotFoundException
    {
        if(newStatus == null) 
            throw new InvalidInputException("newStatus could not be null");
            
        Record record = this.findById(recordId);
        if(record == null)
            throw new NotFoundException("Record with id " + recordId + " not found");
        
        record.setStatus(newStatus);
    }
    
    private boolean match(Record record, RecordFilter filter)
    {
        if(filter.getDepartmentId() != null)
        {
            boolean found = false;
            
            if(record.getCurrentDepartmentId().equals(filter.getDepartmentId()))
                found = true;
            
            if(record.getHistory().stream().anyMatch(
                    s -> s.getDepartmentId().equals(filter.getDepartmentId())))
                found = true;
            
            if(!found)
                return false;
        }
        
        if(filter.getVisitDate() != null)
        {
            if(!record.getVisitTime().toLocalDate().equals(
                    filter.getVisitDate().toLocalDate()))
                return false;
        }
        
        if(filter.getStatus() != null)
        {
            if(!record.getStatus().equals(filter.getStatus()))
                return false;
        }
        
        return true;
    }
    
    public List<Record> search(RecordFilter filter)
    {
        if(filter == null) return Collections.emptyList();
        
        List<String> recordIdList = new ArrayList<>();
        
        if(filter.getPatientId() != null)
        {
            Patient patient = this.patientService.findById(filter.getPatientId());
            if(patient != null)
            {
                recordIdList.addAll(patient.getRecordIds());
            }
        }
        else if(filter.getPatientNameKeyword() != null)
        {
            List<Patient> patientList = this.patientService.searchByName(filter.getPatientNameKeyword());
            for(Patient patient : patientList)
            {
                recordIdList.addAll(patient.getRecordIds());
            }
        }
        
        Collections.sort(recordIdList);
        List<Record> resultList = new ArrayList<>();
        
        for(Record record : this.recordList)
        {
            if(Collections.binarySearch(recordIdList, record.getId()) >= 0)
            {
                if(this.match(record, filter))
                    resultList.add(record);
            }
        }
        
        return resultList;
    }
    
    public List<Record> getWaitingByDepartment(String departmentId)
    {
        RecordFilter filter = new RecordFilter();
        filter.setDepartmentId(departmentId);
        filter.setStatus(RecordStatus.WAITING);
        List<Record> resultFiltered = this.search(filter);
        
        List<Record> results = new ArrayList<>();
        
        for(Record record : resultFiltered)
        {
            if(record.getCurrentDepartmentId().equals(departmentId))
                results.add(record);
        }
        
        return results;
    }
    
    public List<Record> getAll()
    {
        return Collections.unmodifiableList(this.recordList);
    }
    
    public void loadData(List<Record> list)
    {
        this.recordList.clear();
        this.recordList.addAll(list);
        
        int maxId = 0;
        for(Record record : this.recordList)
        {
            int currentId = Integer.parseInt(record.getId().replace("REC-", ""));
            maxId = Math.max(maxId, currentId);
        }
        this.idCounter = maxId;
    }
    
}
