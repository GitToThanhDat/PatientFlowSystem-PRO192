/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 *
 * @author algori07
 */
public class Record implements Serializable {
    private final String id;
    private final String patientId;
    
    private RecordStatus status;
    private Priority priority;
    
    private VisitSegment currentSegment = null;
    private String currentDepartmentId;
    
    private final List<VisitSegment> history;
    
    private LocalDateTime visitTime;
    
    
    public Record(
            String id,
            String patientId,
            String departmentId,
            Priority priority)
    {
        this.id = id;
        this.patientId = patientId;
        this.currentDepartmentId = departmentId;
        this.priority = priority;
        
        this.status = RecordStatus.WAITING;
        this.history = new ArrayList<>();
        this.visitTime = LocalDateTime.now();
    }
    
    public String getId()
    {
        return this.id;
    }
    
    public String getPatientId()
    {
        return this.patientId;
    }
    
    public RecordStatus getStatus()
    {
        return this.status;
    }
    
    public void setStatus(RecordStatus status)
    {
        this.status = status;
    }
    
    public Priority getPriority()
    {
        return this.priority;
    }
    public void setPriority(Priority newPriority)
    {
        this.priority = newPriority;
    }
    
    public VisitSegment getCurrentSegment()
    {
        return this.currentSegment;
    }
    
    public void setCurrentSegment(VisitSegment newSegment)
    {
        this.currentSegment = newSegment;
    }
    
    public String getCurrentDepartmentId()
    {
        return this.currentDepartmentId;
    }
    
    public void setCurrentDepartmentId(String newDeptId)
    {
        this.currentDepartmentId = newDeptId;
    }
    
    public List<VisitSegment> getHistory()
    {
        return Collections.unmodifiableList(this.history);
    }
    
    public LocalDateTime getVisitTime()
    {
        return this.visitTime;
    }
}
