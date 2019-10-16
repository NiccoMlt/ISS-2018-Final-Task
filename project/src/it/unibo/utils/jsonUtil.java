package it.unibo.utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import com.google.gson.Gson;

public class jsonUtil {

    private static final String PREFIX = "string";
    
    public static <T> String encode(T obj) {
        try {
            final Gson gson = new Gson();
            String objJson = gson.toJson(obj);
            String objBase64 = Base64.getEncoder().withoutPadding().encodeToString(objJson.getBytes("UTF-8"));
            return objBase64;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }
    
    public static <T> T decode(String encoded, Class<T> classOfT) {
        try {
            final Gson gson = new Gson();
            String json = new String(
                    Base64.getDecoder().decode(encoded),
                    "UTF-8");
            return gson.fromJson(json, classOfT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static <T> String encodeForProlog(T obj) {
        return PREFIX + encode(obj);
    }
    
    public static <T> T decodeFromProlog(String encoded, Class<T> classOfT) {
        String prejson = encoded.substring(PREFIX.length());
        return decode(prejson, classOfT);
    }
}
