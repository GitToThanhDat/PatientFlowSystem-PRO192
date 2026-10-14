package service;

import model.Record;
import model.RecordStatus;

public class StatisticsService {

    private final PatientService patientService;
    private final DepartmentService departmentService;
    private final RecordService recordService;

    public StatisticsService(PatientService patientService,
                             DepartmentService departmentService,
                             RecordService recordService) {
        this.patientService = patientService;
        this.departmentService = departmentService;
        this.recordService = recordService;
    }

    public int countPatients() {
        return patientService.getAll().size();
    }

    public int countDepartments() {
        return departmentService.getAll().size();
    }

    public int countRecords() {
        return recordService.getAll().size();
    }

    public int countRecordsByStatus(RecordStatus status) {
        int count = 0;
        for (Record r : recordService.getAll()) {
            if (r.getStatus() == status) {
                count++;
            }
        }
        return count;
    }
}