package util;

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
    
    public static boolean sameName(String a, String b) {
        return cleanName(a).equals(cleanName(b));
    }

    public static boolean containsKeyword(String text, String keyword) {
        return cleanName(text).contains(cleanName(keyword));
    }

    public static int extractCounter(String id){
        if (id==null) return 0;
        int idx = id.lastIndexOf('-');
        if (idx<0 || idx==id.length()-1) return 0;
        try{
            return Integer.parseInt(id.substring(idx+1));
        } catch (NumberFormatException e){
            return 0;
        }
    }

}
