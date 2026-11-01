package model;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
public class Patient implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final String id;
    private String name;
    private LocalDate dateOfBirth;
    private final List<String> recordIds;
    
    public Patient(String id, String name, LocalDate dateOfBirth){
        this.recordIds = new ArrayList<>();
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public List<String> getRecordIds(){
        return recordIds;
    }
    
    public void addRecord(String recordId)
    {
        this.recordIds.add(recordId);
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
