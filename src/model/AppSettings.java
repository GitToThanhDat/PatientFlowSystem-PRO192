package model;

import java.io.Serializable;

public class AppSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    private String queueStrategy = "FCFS";
    private int nextDepartmentNumber = 1;
    private int nextPatientNumber = 1;
    private int nextRecordNumber = 1;

    public String getQueueStrategy() {
        return queueStrategy;
    }

    public void setQueueStrategy(String queueStrategy) {
        this.queueStrategy = queueStrategy;
    }

    public int getNextDepartmentNumber() {
        return nextDepartmentNumber;
    }

    public void setNextDepartmentNumber(int nextDepartmentNumber) {
        this.nextDepartmentNumber = nextDepartmentNumber;
    }

    public int getNextPatientNumber() {
        return nextPatientNumber;
    }

    public void setNextPatientNumber(int nextPatientNumber) {
        this.nextPatientNumber = nextPatientNumber;
    }

    public int getNextRecordNumber() {
        return nextRecordNumber;
    }

    public void setNextRecordNumber(int nextRecordNumber) {
        this.nextRecordNumber = nextRecordNumber;
    }
}
