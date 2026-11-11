package data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Priority;
import model.Record;
import model.RecordStatus;
import model.VisitSegment;
import util.DateUtils;

public class RecordData {

    private static final String FILE_NAME = "records.txt";
    private static final String HEADER = "RECORD|id|patientId|status|priority|currentDepartmentId|visitTime"
            + "  /  SEGMENT|recordId|CURRENT or HISTORY|departmentId|doctorId|examResults|diagnosis|testOrder|startTime|endTime";

    public void save(List<Record> list) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Record r : list) {
            lines.add(TextStorage.joinFields("RECORD", r.getId(), r.getPatientId(),
                    r.getStatus().name(), r.getPriority().name(),
                    r.getCurrentDepartmentId(), DateUtils.toStorage(r.getVisitTime())));
            for (VisitSegment s : r.getHistory()) {
                lines.add(segmentLine(r.getId(), "HISTORY", s));
            }
            if (r.getCurrentSegment() != null) {
                lines.add(segmentLine(r.getId(), "CURRENT", r.getCurrentSegment()));
            }
        }
        TextStorage.writeLines(FILE_NAME, HEADER, lines);
    }

    public List<Record> load() throws IOException {
        Map<String, Record> records = new LinkedHashMap<>();
        for (String[] row : TextStorage.readRows(FILE_NAME)) {
            try {
                if (row[0].equals("RECORD")) {
                    TextStorage.requireFields(row, 7, FILE_NAME);
                    Record r = new Record(row[1], row[2], row[5], Priority.valueOf(row[4]));
                    r.setStatus(RecordStatus.valueOf(row[3]));
                    r.setVisitTime(DateUtils.timeFromStorage(row[6]));
                    records.put(r.getId(), r);
                } else if (row[0].equals("SEGMENT")) {
                    TextStorage.requireFields(row, 10, FILE_NAME);
                    Record r = records.get(row[1]);
                    if (r == null) {
                        throw new IllegalArgumentException("segment of unknown record '" + row[1] + "'");
                    }
                    VisitSegment s = new VisitSegment(row[3]);
                    s.setDoctorId(TextStorage.emptyToNull(row[4]));
                    s.setExamResults(TextStorage.emptyToNull(row[5]));
                    s.setDiagnosis(TextStorage.emptyToNull(row[6]));
                    s.setTestOrder(TextStorage.emptyToNull(row[7]));
                    s.setStartTime(DateUtils.timeFromStorage(row[8]));
                    s.setEndTime(DateUtils.timeFromStorage(row[9]));
                    if (row[2].equals("CURRENT")) {
                        r.setCurrentSegment(s);
                    } else {
                        r.addToHistory(s);
                    }
                } else {
                    throw new IllegalArgumentException("unknown line type '" + row[0] + "'");
                }
            } catch (RuntimeException e) {
                throw TextStorage.quarantine(FILE_NAME, e.getMessage(), e);
            }
        }
        return new ArrayList<>(records.values());
    }

    private String segmentLine(String recordId, String kind, VisitSegment s) {
        return TextStorage.joinFields("SEGMENT", recordId, kind, s.getDepartmentId(), s.getDoctorId(),
                s.getExamResults(), s.getDiagnosis(), s.getTestOrder(),
                DateUtils.toStorage(s.getStartTime()), DateUtils.toStorage(s.getEndTime()));
    }
}
