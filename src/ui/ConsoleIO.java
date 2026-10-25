/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import exception.InvalidInputException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author ACER
 */
public class ConsoleIO {
    private static final Scanner SC = new Scanner(System.in); //static có nghĩa là chỉ có 1, tạo nhiều biến class thì cũng chỉ có một cái này, ngoài ra thì void mà có static thì không cần khởi tạo một biến class để sử dụng mà có thể truy cập trực tiếp.
    
    private ConsoleIO(){} //Tại sao cái này chỉ có private nhỉ?
    
    public static void success(String msg) {System.out.println("[OK] "+msg);}
    public static void error(String msg) {System.out.println("[ERROR] "+msg);}
    public static void info(String msg) {System.out.println("[INFO] "+msg);}
    
    public static String readLine(String prompt){
        System.out.print(prompt); //TODO: check lại chỗ này có cần thiết không? 
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
            sb.append("| ").append(String.format("%-" + width[i] + "s", cell)).append(' ');// Cách để căn lề trái.
        }
        System.out.println(sb.append('|'));
    }
    
    public static void pauseConsole() {
        new Scanner(System.in).nextLine();
    }
}
