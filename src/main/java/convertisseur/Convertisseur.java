package convertisseur;

import com.google.appengine.repackaged.com.google.common.base.genfiles.ByteArray;
import entities.Video;

import java.io.*;

/**
 * Created by Matthieu on 03/11/2017.
 */
public class Convertisseur implements Runnable{
    private boolean status;
    private Video vid;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Video getVid() {
        return vid;
    }

    public void setVid(Video vid) {
        this.vid = vid;
    }

    public Convertisseur(){
        status = false;
    }

    public void run(){
        status = false;
        convert();
    }

    private void convert(){
        ByteArray out = new ByteArray();
        int totalSize = (vid.getLength() * 1048576) / 4;
        for (int i = 0; i < totalSize; i++) {
                out.add((byte)0);
        }
        double generatedLong = (Math.random() * (2.5 - 1.8)) * 1.8;
        try {
            Thread.sleep((int) (generatedLong * 1000));
            status = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
