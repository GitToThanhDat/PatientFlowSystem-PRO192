package strategy;

import model.Record;
import java.util.List;
public interface QueueStrategy {
    Record getNext(List<Record> records);
    String getName();
}
