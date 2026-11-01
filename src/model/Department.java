package model;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
public class Department implements Serializable {
    private static final long serialVersionUID = 1L;
    
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
    
    public void addToQueue(String recordId)
    {
        this.waitingQueue.add(recordId);
    }
}
