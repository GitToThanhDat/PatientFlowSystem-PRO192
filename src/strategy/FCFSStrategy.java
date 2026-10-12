/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package strategy;

import java.util.List;
import model.Record;

/**
 *
 * @author ACER
 */
public class FCFSStrategy implements QueueStrategy {
    @Override
    public Record getNext(List<Record> records){
        if (records == null || records.isEmpty()){
            return null;
        }
        Record earliest=records.get(0);
        
        for (Record r : records){
            if (r.getVisitTime().isBefore(earliest.getVisitTime())){
                earliest=r;
            }
        }
        return earliest;
    }

    @Override
    public String getName() {
        return "FCFS";
    }
    
}
