package data;
import model.*;
import java.util.*;
import java.io.IOException;

public class RecordData {

    private static final String FILE_NAME = "records.dat";

    public void save(List<Record> list) throws IOException
    {
        SerialStorage.saveList(FILE_NAME, list);
    }
    public List<Record> load() throws IOException
    {
        return SerialStorage.loadList(FILE_NAME, Record.class);
    }

}
