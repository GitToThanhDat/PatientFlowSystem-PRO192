package service;

import exception.InvalidInputException;
import exception.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import model.Doctor;
import model.Record;
import model.RecordStatus;
import model.VisitSegment;
import util.Validator;

public class ExaminationService {

    private final RecordService _recordService;
    private final QueueService _queueService;

    public ExaminationService(RecordService recordService, QueueService queueService) {
        this._recordService = recordService;
        this._queueService = queueService;
    }

    public Record callNext(Doctor doctor) throws InvalidInputException, NotFoundException {
        if (doctor == null) {
            throw new InvalidInputException("Doctor must not be null.");
        }
        if (doctor.getDepartmentId() == null) {
            throw new InvalidInputException("Doctor '" + doctor.getFullName()
                    + "' is not assigned to any department. Ask an admin to assign one.");
        }
        if (doctor.getCurrentRecordId() != null) {
            throw new InvalidInputException("Doctor '" + doctor.getFullName()
                    + "' is still examining a patient. Complete the current examination before calling the next patient.");
        }
        List<Record> waiting = _recordService.getWaitingByDepartment(doctor.getDepartmentId());
        Record next = _queueService.getNext(waiting);
        if (next == null) {
            return null;
        }
        VisitSegment seg = new VisitSegment(doctor.getDepartmentId());
        seg.setDoctorId(doctor.getId());
        next.setCurrentSegment(seg);
        _recordService.updateStatus(next.getId(), RecordStatus.IN_PROGRESS);
        doctor.setCurrentRecordId(next.getId());
        return next;
    }

    public void completeExamination(Doctor doctor, String diagnosis)
            throws InvalidInputException, NotFoundException {
        if (doctor == null) {
            throw new InvalidInputException("Doctor must not be null.");
        }
        if (doctor.getCurrentRecordId() == null) {
            throw new InvalidInputException("Doctor '" + doctor.getFullName()
                    + "' is not examining any patient. Call a patient first.");
        }
        String cleanDiagnosis = Validator.requireText(diagnosis, "Diagnosis");

        String recordId = doctor.getCurrentRecordId();
        Record record = _recordService.findById(recordId);
        if (record == null) {
            doctor.setCurrentRecordId(null);
            throw new NotFoundException("The visit record being examined no longer exists."
                    + " The doctor has been released from it.");
        }

        VisitSegment seg = record.getCurrentSegment();
        if (seg == null) {
            seg = new VisitSegment(doctor.getDepartmentId());
            seg.setDoctorId(doctor.getId());
        }
        seg.setDiagnosis(cleanDiagnosis);
        seg.setEndTime(LocalDateTime.now());
        record.addToHistory(seg);
        _recordService.updateStatus(recordId, RecordStatus.DONE);
        doctor.setCurrentRecordId(null);
    }
}
