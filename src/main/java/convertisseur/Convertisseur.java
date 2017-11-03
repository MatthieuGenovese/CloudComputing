package convertisseur;

import com.google.appengine.repackaged.com.google.common.base.genfiles.ByteArray;
import entities.Video;
import helloworld.QuickstartSample;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Created by Matthieu on 03/11/2017.
 */
public class Convertisseur implements Runnable{
    private boolean status;
    private QuickstartSample storage;
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
        storage = new QuickstartSample();
        status = true;
    }

    public void run(){
        status = false;
        convert();
    }

    private void convert(){
        int totalSize = (vid.getLength() * 1048576) / 4;

        byte[] out = new byte[totalSize];
        for (int i = 0; i < totalSize; i++) {
              out[i] = (byte) i;
        }
        double generatedLong = (Math.random() * (2.5 - 1.8)) * 1.8;
        try {
            Thread.sleep((int) (generatedLong * 1000));
            storage.writeToStorage(vid.getId()+vid.getOwner(),out);
            status = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
