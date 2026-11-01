package service;

import java.util.List;
import model.Record;
import strategy.QueueStrategy;

public class QueueService {
    private QueueStrategy _strategy;
    
    public QueueService (QueueStrategy strategy){
        this._strategy=strategy;
    }
    
    public void setStrategy (QueueStrategy strategy){
        this._strategy=strategy;
    }
    public Record getNext(List<Record> waitingQueue){
        return this._strategy.getNext(waitingQueue);
    }
    public String getStrategyName(){ return this._strategy.getName();}
}