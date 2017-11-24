package convertisseur;

import entities.User;
import entities.Video;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import stockage.CloudStorage;
import stockage.UserManager;
import stockage.VideoManager;

/**
 * Created by Matthieu on 03/11/2017.
 */
public class Convertisseur implements Runnable{
    private CloudStorage storage;
    private UserManager userManager;
    private VideoManager videoManager;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private Video vid;

    public Video getVid() {
        return vid;
    }

    public void setVid(Video vid) {
        this.vid = vid;
    }

    public Convertisseur(){
        storage = new CloudStorage();
        userManager = new UserManager();
        videoManager = new VideoManager();
    }

    public void run(){
        convert();
    }

    private void convert(){
        int totalSize = (Integer.valueOf(vid.getLength()) * 1048576) / 4;

        byte[] out = new byte[totalSize];
        for (int i = 0; i < totalSize; i++) {
              out[i] = (byte) 1;
        }
        double generatedLong = (Math.random() * (2.5 - 1.8)) + 1.8;
        try {
            Thread.sleep((int) (generatedLong * Integer.valueOf(vid.getLength()) *  1000));
            vid.setStatus("done");
            vid.setDownloadLink(storage.writeToStorage(vid.getOwner()+vid.getName(),out));
            vid.setSubmitTime(DateTime.now(DateTimeZone.UTC));
            videoManager.updateVideo(vid);
            user.setCurrentVideos(videoManager.getAllPendingsVideosFromUsername(vid.getOwner()).size());
            userManager.updateUser(user);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
