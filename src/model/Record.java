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
    
    private List<VisitSegment> history;
    
    private LocalDateTime visitTime;
    
    
    // default-static to disable user create new Record without from Service
    Record(
            String id,
            String patientId)
    {
        this.id = id;
        this.patientId = patientId;
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
    
    VisitSegment getCurrentSegment()
    {
        return this.currentSegment;
    }
    
    void setCurrentSegment(VisitSegment newSegment)
    {
        this.currentSegment = newSegment;
    }
    
    List<VisitSegment> getHistory()
    {
        return Collections.unmodifiableList(this.history);
    }
    
    LocalDateTime getVisitTime()
    {
        return this.visitTime;
    }
}
