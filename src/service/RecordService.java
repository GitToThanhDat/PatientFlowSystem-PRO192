/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.util.*;
import java.util.stream.Collectors;
import model.*;

/**
 *
 * @author algori07
 */
public class RecordService {
    private final List<Record> recordList;
    private int idCounter;
    
    public RecordService()
    {
        this.recordList = new ArrayList<>();
        this.idCounter = 1;
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
    {
        if(patientId == null ||
                departmentId == null ||
                priority == null) return null;
        
        StringBuilder newRecordId = new StringBuilder();
        newRecordId.append("REC-");
        newRecordId.append(this.idCounter);
        this.idCounter++;
        
        Record newRecord = new Record(
                newRecordId.toString(),
                patientId,
                departmentId,
                priority);
        
        this.recordList.add(newRecord);
        
        return newRecordId.toString();
    }
    
    public boolean updateStatus(String recordId, RecordStatus newStatus)
    {
        Record record = this.findById(recordId);
        if(record == null) return false;
        
        record.setStatus(newStatus);
        return true;
    }
    
    public List<Record> search(RecordFilter filter)
    {
        List<Record> resultList = this.recordList.stream()
                .filter(record -> record.match(filter))
                .collect(Collectors.toList());
        
        return resultList;
    }
    
    public List<Record> getAll()
    {
        return this.recordList;
    }
    
}
