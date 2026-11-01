package data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public final class SerialStorage {

    private static String directory = "data";

    private SerialStorage() {
    }

    public static String getDirectory() {
        return directory;
    }

    public static void setDirectory(String dir) {
        if (dir == null || dir.trim().isEmpty()) {
            throw new IllegalArgumentException("Data directory must not be empty.");
        }
        directory = dir;
    }

    static <T> void saveList(String fileName, List<T> list) throws IOException {
        File dir = new File(directory);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Cannot create data directory: " + dir.getAbsolutePath());
        }
        File target = new File(dir, fileName);
        File temp = new File(dir, fileName + ".tmp");

        List<T> copy = (list == null) ? new ArrayList<T>() : new ArrayList<T>(list);

        try (ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(temp)))) {
            out.writeObject(copy);
        } catch (IOException e) {
            temp.delete();
            throw new IOException("Cannot write '" + fileName + "': " + e.getMessage(), e);
        }

        try {
            Files.move(temp.toPath(), target.toPath(),
                    StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(temp.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    static <T> List<T> loadList(String fileName, Class<T> type) throws IOException {
        File file = new File(directory, fileName);
        List<T> result = new ArrayList<T>();
        if (!file.exists()) {
            return result;
        }

        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(file)))) {
            Object obj = in.readObject();
            if (!(obj instanceof List)) {
                throw new ClassCastException("root object is not a List");
            }
            for (Object item : (List<?>) obj) {
                if (!type.isInstance(item)) {
                    throw new ClassCastException("unexpected element: " + item);
                }
                result.add(type.cast(item));
            }
            return result;
        } catch (ClassNotFoundException | ClassCastException | ObjectStreamException | EOFException e) {
            File bad = new File(directory, fileName + ".corrupt");
            String moved = "";
            try {
                Files.move(file.toPath(), bad.toPath(), StandardCopyOption.REPLACE_EXISTING);
                moved = " The old file was kept as '" + bad.getName() + "'.";
            } catch (IOException ignore) {
            }
            throw new IOException("Cannot read '" + fileName + "' (" + e + ")."
                    + moved + " Starting with empty data for it.", e);
        }
    }
}
