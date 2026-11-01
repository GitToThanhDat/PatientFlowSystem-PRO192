package service;

import java.util.*;
import java.util.stream.Collectors;
import model.*;
import exception.*;
import java.util.stream.Stream;

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
    
    public Record findActiveByPatient(String patientId)
    {
        for(Record record : this.recordList)
        {
            if(record.getPatientId().equals(patientId)
                    && record.getStatus() != RecordStatus.DONE)
            {
                return record;
            }
        }
        return null;
    }
    
    public void ensureNoActiveVisit(String patientId) throws DuplicateDataException
    {
        Record active = this.findActiveByPatient(patientId);
        if(active == null)
            return;
        Patient patient = this.patientService.findById(patientId);
        Department department = this.departmentService.findById(active.getCurrentDepartmentId());
        throw new DuplicateDataException("Patient '" + (patient == null ? patientId : patient.getName())
                + "' already has an active visit"
                + (department == null ? "" : " at '" + department.getName() + "'")
                + " (" + active.getStatus() + ").");
    }
    
    public boolean hasRecordsForPatient(String patientId)
    {
        for(Record record : this.recordList)
        {
            if(record.getPatientId().equals(patientId))
                return true;
        }
        return false;
    }
    
    public boolean isDepartmentReferenced(String departmentId)
    {
        for(Record record : this.recordList)
        {
            if(departmentId.equals(record.getCurrentDepartmentId()))
                return true;
            for(VisitSegment segment : record.getHistory())
            {
                if(departmentId.equals(segment.getDepartmentId()))
                    return true;
            }
        }
        return false;
    }
    
    public boolean hasExaminedBy(String doctorId)
    {
        for(Record record : this.recordList)
        {
            for(VisitSegment segment : record.getHistory())
            {
                if(doctorId.equals(segment.getDoctorId()))
                    return true;
            }
        }
        return false;
    }
    
    public String register(
            String patientId,
            String departmentId,
            Priority priority)
        throws InvalidInputException, NotFoundException, DuplicateDataException
    {
        if(patientId == null)
            throw new InvalidInputException("patientId must not be null!");
        Patient patient = this.patientService.findById(patientId);
        if(patient == null)
            throw new NotFoundException("Patient not found.");
        
        if(departmentId == null)
            throw new InvalidInputException("departmentId must not be null!");
        Department department = this.departmentService.findById(departmentId);
        if(department == null)
            throw new NotFoundException("Department not found.");
        
        if(priority == null)
            throw new InvalidInputException("priority must not be null");
        
        this.ensureNoActiveVisit(patientId);
        
        String recordId = "vis-" + this.idCounter;
        this.idCounter++;
        
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
            throw new NotFoundException("Visit record not found.");
        
        record.setStatus(newStatus);

        Department dept = this.departmentService.findById(record.getCurrentDepartmentId());
        if(dept != null)
        {
            if(newStatus == RecordStatus.WAITING)
            {
                if(!dept.getWaitingQueue().contains(recordId))
                    dept.addToQueue(recordId);
            }
            else
            {
                dept.getWaitingQueue().remove(recordId);
            }
        }
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
        if(filter == null) return new ArrayList<>(this.recordList);
        
        List<String> recordIdList = new ArrayList<>();
        boolean searchAll = true;
        
        if(filter.getPatientId() != null)
        {
            Patient patient = this.patientService.findById(filter.getPatientId());
            if(patient != null)
            {
                recordIdList.addAll(patient.getRecordIds());
            }
            searchAll = false;
        }
        else if(filter.getPatientNameKeyword() != null)
        {
            List<Patient> patientList = this.patientService.searchByName(filter.getPatientNameKeyword());
            for(Patient patient : patientList)
            {
                recordIdList.addAll(patient.getRecordIds());
            }
            searchAll = false;
        }
        
        Collections.sort(recordIdList);
        List<Record> resultList = new ArrayList<>();
        
        for(Record record : this.recordList)
        {
            if(searchAll || Collections.binarySearch(recordIdList, record.getId()) >= 0)
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
    
    public int getNextNumber()
    {
        return this.idCounter;
    }
    
    public void ensureNextNumberAtLeast(int number)
    {
        if(number > this.idCounter)
            this.idCounter = number;
    }
    
    public void loadData(List<Record> list)
    {
        this.recordList.clear();
        if(list != null) this.recordList.addAll(list);

        int maxId = 0;
        for(Record record : this.recordList)
        {
            maxId = Math.max(maxId, util.TextUtils.extractCounter(record.getId()));
        }
        this.idCounter = maxId + 1;
    }
    
}
