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
public class Department {
    
    private final String id;
    private String name;
    private final List<String> waitingQueue;
    
    public Department(String id, String name){
        
        this.waitingQueue = new ArrayList<>();
        this.id = id;
        this.name = name;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public List<String> getWaitingQueue() {
        return waitingQueue;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
