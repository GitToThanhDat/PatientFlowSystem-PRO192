package ui;

import exception.InvalidInputException;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class ConsoleIO {
    private static final Charset CONSOLE_CHARSET = resolveCharset();
    private static final Scanner SC = new Scanner(new InputStreamReader(System.in, CONSOLE_CHARSET));
    
    private ConsoleIO(){}
    
    private static Charset resolveCharset() {
        String name = System.getProperty("console.encoding");
        if (name != null && !name.trim().isEmpty()) {
            try {
                return Charset.forName(name.trim());
            } catch (RuntimeException e) {
                System.err.println("[Warning] Unknown console.encoding '" + name + "', using UTF-8.");
            }
        }
        return StandardCharsets.UTF_8;
    }
    
    public static void setup() {
        try {
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, CONSOLE_CHARSET.name()));
        } catch (UnsupportedEncodingException e) {
            System.err.println("[Warning] Cannot switch console output to " + CONSOLE_CHARSET.name() + ".");
        }
    }
    
    public static Charset getConsoleCharset() {
        return CONSOLE_CHARSET;
    }
    
    public static void success(String msg) {System.out.println("[Success] "+msg);}
    public static void error(String msg) {System.out.println("[Error] "+msg);}
    public static void info(String msg) {System.out.println("[Info] "+msg);}
    public static void warning(String msg) {System.out.println("[Warning] "+msg);}
    
    public static boolean confirm(String prompt) {
        String answer = readLine(prompt + " (y/N): ");
        return answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes");
    }
    
    public static String readLine(String prompt){
        System.out.print(prompt);
        return SC.nextLine().trim();
    }
    
    public static int readInt(String prompt) throws InvalidInputException{
        String raw = readLine(prompt);
        try{
            return Integer.parseInt(raw);
            
        } catch (NumberFormatException e){
            throw new InvalidInputException(
                "'"+raw+"' is not a valid number. Please enter digits only");
        }
    }
    
    public static int readChoice(String prompt, int min, int max) throws InvalidInputException{
        int choice = readInt(prompt);
        if(choice < min || choice > max){
            throw new InvalidInputException("Choice "+ choice+" is out of range. Please choose from "+min+" to "+max +".");
        }
        return choice;
    }
    
    public static void printTable(String[] headers, List<String[]> rows) {
        int[] width = new int[headers.length];
        for (int i = 0; i < headers.length; i++) width[i] = headers[i].length();
        for (String[] row : rows) {
            for (int i = 0; i < headers.length; i++) {
                String cell = (row[i] == null) ? "" : row[i];
                width[i] = Math.max(width[i], cell.length());
            }
        }
        printRow(headers, width);
        StringBuilder line = new StringBuilder();
        for (int w : width) {
            line.append("+-");
            for (int k = 0; k < w; k++) line.append('-');
            line.append('-');
        }
        System.out.println(line.append('+'));
        for (String[] row : rows) printRow(row, width);
    }

    private static void printRow(String[] cells, int[] width) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < width.length; i++) {
            String cell = (cells[i] == null) ? "" : cells[i];
            sb.append("| ").append(String.format("%-" + width[i] + "s", cell)).append(' ');
        }
        System.out.println(sb.append('|'));
    }
    
    public static void pauseConsole() {
        SC.nextLine();
    }
}
