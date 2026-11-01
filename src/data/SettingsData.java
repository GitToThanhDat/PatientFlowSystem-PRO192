package data;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import model.AppSettings;

public class SettingsData {

    private static final String FILE_NAME = "settings.dat";

    public void save(AppSettings settings) throws IOException {
        SerialStorage.saveList(FILE_NAME, Collections.singletonList(settings));
    }

    public AppSettings load() throws IOException {
        List<AppSettings> list = SerialStorage.loadList(FILE_NAME, AppSettings.class);
        if (list.isEmpty()) {
            return new AppSettings();
        }
        return list.get(0);
    }
}
