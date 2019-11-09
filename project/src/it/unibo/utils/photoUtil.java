package it.unibo.utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.Base64;

import javax.imageio.ImageIO;

import it.unibo.qactors.QActorContext;
import it.unibo.qactors.akka.QActor;

public class photoUtil {

    public static void takePhotoAndSendToConsole(QActor qa) {
        try {
            File file = new File("res/bag.png");
            BufferedImage bufferedImage = ImageIO.read(file);
            WritableRaster raster = bufferedImage .getRaster();
            DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();
            String photo = jsonUtil.PREFIX + Base64.getEncoder().withoutPadding().encodeToString(data.getData());
            String payload = "bag(picture(P))".replace("P", photo);
            qa.sendMsg("bag","console", QActorContext.dispatch, payload );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
