package util;

import java.util.Collection;

public class IdGenerator {

    private final String prefix;
    private int nextNumber;

    public IdGenerator(String prefix) {
        this.prefix = prefix;
        this.nextNumber = 1;
    }

    public String next() {
        String id = prefix + "-" + nextNumber;
        nextNumber++;
        return id;
    }

    public int getNextNumber() {
        return nextNumber;
    }

    public void ensureNextNumberAtLeast(int number) {
        if (number > nextNumber) {
            nextNumber = number;
        }
    }

    public void resetAfter(Collection<String> existingIds) {
        int max = 0;
        for (String id : existingIds) {
            max = Math.max(max, TextUtils.extractCounter(id));
        }
        nextNumber = max + 1;
    }
}
