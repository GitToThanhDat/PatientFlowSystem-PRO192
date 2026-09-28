/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.util.List;
import model.Record;
import strategy.QueueStrategy;

/**
 *
 * @author ACER
 */
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
}