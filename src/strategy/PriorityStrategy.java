package strategy;
import java.util.List;
import model.Record;

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

    @Override
    public String getName() {
        return "Priority";
    }
}
