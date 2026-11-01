package data;

import java.io.IOException;
import java.util.List;
import model.User;

public class UserData {

    private static final String FILE_NAME = "users.dat";

    public void save(List<User> list) throws IOException {
        SerialStorage.saveList(FILE_NAME, list);
    }

    public List<User> load() throws IOException {
        return SerialStorage.loadList(FILE_NAME, User.class);
    }
}
