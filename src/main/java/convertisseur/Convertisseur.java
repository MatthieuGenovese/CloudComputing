package convertisseur;

import entities.User;
import entities.Video;
import stockage.CloudStorage;
import stockage.UserManager;

/**
 * Created by Matthieu on 03/11/2017.
 */
public class Convertisseur implements Runnable{
    private boolean status;
    private CloudStorage storage;
    private UserManager userManager;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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
        storage = new CloudStorage();
        status = true;
        userManager = new UserManager();
    }

    public void run(){
        status = false;
        convert();
    }

    private void convert(){
        int totalSize = (Integer.valueOf(vid.getLength()) * 1048576) / 4;

        byte[] out = new byte[totalSize];
        for (int i = 0; i < totalSize; i++) {
              out[i] = (byte) i;
        }
        double generatedLong = (Math.random() * (2.5 - 1.8)) * 1.8;
        try {
            Thread.sleep((int) (generatedLong * Integer.valueOf(vid.getLength()) *  1000));
            storage.writeToStorage(vid.getOwner()+vid.getName(),out);
            status = true;
            user.setCurrentVideos(user.getCurrentVideos()-1);
            userManager.updateUser(user);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
