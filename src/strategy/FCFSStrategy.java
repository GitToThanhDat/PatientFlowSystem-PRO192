package strategy;

import java.util.List;
import model.Record;

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
