/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author ACER
 */
public class TextUtils {
    private TextUtils() {
    }

    public static String cleanName(String name) {
        if (name == null) {
            return "";
        }
        String normalized = java.text.Normalizer.normalize(
                name.trim(), java.text.Normalizer.Form.NFD);
        String noAccent = normalized.replaceAll(
                "\\p{InCombiningDiacriticalMarks}+", "");

        noAccent = noAccent.replace("\u0111", "d"); 
        noAccent = noAccent.replace("\u0110", "D"); 
        return noAccent.replace(" ", "").toLowerCase();
    }

}
