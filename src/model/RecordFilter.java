/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
/**
 *
 * @author ACER
 */
public class RecordFilter{

    private String patientId;
    private String patientNameKeyword;
    private LocalDateTime visitDate;
    private RecordStatus status;
    private String departmentId;

    public RecordFilter(){}

    public String getPatientId()
    {
        return patientId;
    }
    public void setPatientId(String patientId)
    {
        this.patientId = patientId;
    }

    public String getPatientNameKeyword()
    {
        return patientNameKeyword;
    }
    public void setPatientNameKeyword(String patientNameKeyword)
    {
        this.patientNameKeyword = patientNameKeyword;
    }

    public LocalDateTime getVisitDate()
    {
        return visitDate;
    }
    public void setVisitDate(LocalDateTime visitDate)
    {
        this.visitDate = visitDate;
    }

    public RecordStatus getStatus()
    {
        return status;
    }
    public void setStatus(RecordStatus status)
    {
        this.status = status;
    }

    public String getDepartmentId()
    {
        return departmentId;
    }
    public void setDepartmentId(String departmentId)
    {
        this.departmentId = departmentId;
    }
}
