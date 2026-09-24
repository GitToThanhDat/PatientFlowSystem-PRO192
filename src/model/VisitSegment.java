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
public class VisitSegment{
    private String departmentId;
    private String doctorId;
    private String examResults;
    private String diagnosis;
    private String testOrder;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public VisitSegment(String departmentId){
        this.departmentId = departmentId;
        this.startTime = LocalDateTime.now();
    }

    public String getDepartmentId()
    {
        return departmentId;
    }
    public String getDoctorId()
    {
        return doctorId;
    }
    public String getExamResults()
    {
        return examResults;
    }
    public String getDiagnosis()
    {
        return diagnosis;
    }
    public String getTestOrder()
    {
        return testOrder;
    }
    public LocalDateTime getStartTime()
    {
        return startTime;
    }
    public LocalDateTime getEndTime()
    {
        return endTime;
    }

    public void setDoctorId(String doctorId)
    {
        this.doctorId = doctorId;
    }
    public void setExamResults(String examResults)
    {
        this.examResults = examResults;
    }
    public void setDiagnosis(String diagnosis)
    {
        this.diagnosis = diagnosis;
    }
    public void setTestOrder(String testOrder)
    {
        this.testOrder = testOrder;
    }
    public void setEndTime(LocalDateTime endTime)
    {
        this.endTime = endTime;
    }  
}
