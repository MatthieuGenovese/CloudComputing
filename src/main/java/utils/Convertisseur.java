package utils;

import entities.User;
import entities.Video;
import entities.VideoUser;
import utils.MailManager;
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
    private FileGenerator fileGenerator;
    private MailManager mailManager;
    private User user;
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private VideoUser vid;

    public VideoUser getVid() {
        return vid;
    }

    public void setVid(VideoUser vid) {
        this.vid = vid;
    }

    public Convertisseur(){
        storage = new CloudStorage();
        userManager = new UserManager();
        videoManager = new VideoManager();
        mailManager = new MailManager();
        fileGenerator = new FileGenerator();
    }

    public void run(){
        convert();
    }

    private void convert(){
        double generatedLong = (Math.random() * (2.5 - 1.1)) + 1.1;
        try {
            vid.setStatus("processing");
            videoManager.updateVideoUser(vid);
            Thread.sleep((int) (generatedLong * Integer.valueOf(vid.getLength()) *  1000));
            vid.setStatus("done");
            String downloadLink = "";
            if(Integer.valueOf(vid.getLength())>70){
                int length = Integer.valueOf(vid.getLength());
                int nbPart = length / 70;
                int rest = length - (nbPart * 70);
                int i;
                for(i = 0; i < nbPart; i++) {
                    downloadLink = downloadLink + storage.writeToStorage(vid.getName()+vid.getOwner() + "part" + i, fileGenerator.generateFile(70))+"   \n";
                }
                downloadLink = downloadLink + storage.writeToStorage(vid.getName()+vid.getOwner() + "part" + i, fileGenerator.generateFile(rest))+"   \n";
                vid.setNbPart(String.valueOf(i+1));
            }
            else{
                vid.setNbPart("1");
                downloadLink = storage.writeToStorage(vid.getName()+vid.getOwner() + "part0", fileGenerator.generateFile(Integer.valueOf(vid.getLength())))+"\n";
            }
            vid.setDownloadLink(downloadLink);
            vid.setSubmitTime(DateTime.now(DateTimeZone.UTC));
            videoManager.updateVideoUser(vid);
            user.setCurrentVideos(videoManager.getAllPendingsVideosFromUsername(vid.getOwner()).size());
            userManager.updateUser(user);
            mailManager.setContent("La video " + vid.getName() + "est bien convertie ! \n Vous pouvez consulter et téléchargez vos vidéos à partir de ce lien : "
                    + "http://sacc-liechtensteger-182811.appspot.com/status?username="+ user.getUsername());
            mailManager.setHeader("Conversion terminée !");
            mailManager.setMail(user.getEmail());
            mailManager.setUsername(user.getUsername());
            mailManager.sendEmail();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
