/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author ACER
 */
public class Patient {
    
    private final String id;
    private String name;
    private final List<String> recordIds;
    
    public Patient(String id, String name){
        this.recordIds = new ArrayList<>();
        this.id = id;
        this.name = name;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public List<String> getRecordIds(){
        return recordIds;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
