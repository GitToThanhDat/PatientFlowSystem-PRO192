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
public class PriorityStrategy implements QueueStrategy {
    @Override
    public Record getNext(List<Record> records){
        if(records == null || records.isEmpty()){
            return null;
        }
        Record best = records.get(0);
        for (Record r: records){
            if(r.getPriority().ordinal()>best.getPriority().ordinal()){
                best=r;
            }
        }
        return best;
    }
}
