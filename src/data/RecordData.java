/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;
import model.*;
import java.util.*;
import java.io.*;
import java.io.IOException;

/**
 *
 * @author khanh3327
 */
public class RecordData {
    
    private static final String FILE_PATH = "data/records.dat";
    
    public void save(List<Record> list) throws IOException
    {
        try(FileOutputStream fo = new FileOutputStream(FILE_PATH);
            ObjectOutputStream out = new ObjectOutputStream(fo))
        {
            out.writeObject(list);
        }
    }
    public List<Record> load() throws IOException
    {
        List<Record> results;
        
        try(FileInputStream fi = new FileInputStream(FILE_PATH);
            ObjectInputStream in = new ObjectInputStream(fi))
        {
            results = (List<Record>) in.readObject();
        }
        catch(ClassNotFoundException e)
        {
            return new ArrayList<Record>();
        }
        
        return results;
    }
    
}
