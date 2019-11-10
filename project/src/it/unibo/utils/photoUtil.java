package it.unibo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

import it.unibo.qactors.QActorContext;
import it.unibo.qactors.akka.QActor;

public class photoUtil {
    
    public static boolean bomb = false;

    public static void takePhotoAndSendToConsole(QActor qa) {
        try {
            File file = new File(bomb ? "res/bomb.png" : "res/bag.png");
            bomb = !bomb;
            String photo = encodeFileToBase64Binary(file);
            String payload = "bag(picture(\"P\"))".replace("P", photo);
            qa.sendMsg("bag","console", QActorContext.dispatch, payload );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static String encodeFileToBase64Binary(File file){
        String encodedfile = null;
        FileInputStream fileInputStreamReader = null;
        try {
            fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = Base64.getEncoder().withoutPadding().encodeToString(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
               fileInputStreamReader.close();
           } catch (IOException e) { }
        }

        return encodedfile;
    }
}
