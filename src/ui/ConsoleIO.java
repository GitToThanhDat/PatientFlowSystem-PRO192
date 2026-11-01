package ui;

import exception.InvalidInputException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Scanner;

public class ConsoleIO {
    private static final Scanner SC = new Scanner(System.in);
    
    private ConsoleIO(){}
    
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_PARSER =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    
    public static void success(String msg) {System.out.println("[Success] "+msg);}
    public static void error(String msg) {System.out.println("[Error] "+msg);}
    public static void info(String msg) {System.out.println("[Info] "+msg);}
    public static void warning(String msg) {System.out.println("[Warning] "+msg);}
    
    public static String formatDateTime(LocalDateTime time) {
        return time == null ? "-" : time.format(DATE_TIME_FORMAT);
    }
    
    public static String formatDate(LocalDate date) {
        return date == null ? "-" : date.format(DATE_FORMAT);
    }
    
    public static LocalDate parseDate(String text) throws InvalidInputException {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(text.trim(), DATE_PARSER);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Invalid date '" + text.trim() + "'. Please use dd/MM/yyyy, for example 25/12/1990.");
        }
    }
    
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
