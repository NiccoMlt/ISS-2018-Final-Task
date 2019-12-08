package it.unibo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import com.google.gson.Gson;

import it.unibo.qactors.QActorContext;
import it.unibo.qactors.akka.QActor;
import it.unibo.systemstate.systemStateUtil;

public class photoUtil {

    public static boolean bomb = false;
    private static final Gson gson = new Gson();

    public static void takePhotoAndSendToConsole(QActor qa) {
        try {
            File file = new File(bomb ? "res/bomb.png" : "res/bag.png");
            bomb = !bomb;
            String photo = encodeFileToBase64Binary(file);
            String payload = "bag(picture(\"P\"))".replace("P", photo);
            qa.sendMsg("bag", "console", QActorContext.dispatch, payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String encodeFileToBase64Binary(File file) {
        String encodedfile = null;
        FileInputStream fileInputStreamReader = null;
        try {
            fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = Base64.getEncoder().withoutPadding().encodeToString(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStreamReader.close();
            } catch (IOException e) {
            }
        }

        return encodedfile;
    }

    public static void storePhoto(QActor qa, String picture) {
        saveBytes("bombs/bomb.png", Base64.getDecoder().decode(picture.substring(1, picture.length() - 1)));
        saveBytes("bombs/bomb.txt", gson.toJson(systemStateUtil.getSystemState()).getBytes());
    }

    private static void saveBytes(String fileName, byte[] object) {
        File file = new File(fileName);
        FileOutputStream fos = null;
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
            // Open streams
            fos = new FileOutputStream(file);
            // Save object
            fos.write(object);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Close streams
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
