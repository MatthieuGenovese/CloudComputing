package utils;

import com.google.api.gax.rpc.DeadlineExceededException;
import entities.User;
import entities.Video;
import entities.VideoUser;
import stockage.CloudStorage;
import stockage.UserManager;
import stockage.VideoManager;

import java.io.PrintWriter;

/**
 * Created by Matthieu on 25/11/2017.
 */
public class HandleConversionRequest {
    private VideoManager videoManager;
    private CloudStorage storage;
    private UserManager userManager;
    private FileGenerator fileGenerator;

    public HandleConversionRequest(){
        videoManager = new VideoManager();
        userManager = new UserManager();
        fileGenerator = new FileGenerator();
        storage = new CloudStorage();
    }

    public VideoUser handleConversionRequest(String username, String videoname, PrintWriter out){
        VideoUser current = videoManager.getVideoUser(videoname, username);
        if(current == null) {
            Video vid = videoManager.getVideo(videoname);
            if(vid != null) {
                if(videoManager.getVideoUser(username, videoname) == null) {
                    User u = userManager.getUser(username);
                    current = new VideoUser(username, videoname, vid.getLength());
                    if (checkStatus(u, vid.getLength(), out)) {
                        u.setCurrentVideos(videoManager.getAllPendingsVideosFromUsername(u.getUsername()).size() + 1);
                        videoManager.createVideoUser(current);
                        userManager.updateUser(u);
                        out.println("nb video : " + u.getCurrentVideos());
                        return current;
                    }
                    return null;
                }
                else{
                    out.println("Video deja en cours de conversion !");
                    return null;
                }
            }
            else{
                out.println("Cette video n existe pas !");
                return null;
            }
        }
        else {
            out.println("Video deja en cours de conversion");
            return null;
        }
    }

    public boolean handleUploadRequest(Video vid, PrintWriter out){
        if(videoManager.getVideo(vid.getName()) == null) {
            if(Integer.valueOf(vid.getLength())>70){
                int length = Integer.valueOf(vid.getLength());
                int nbPart = length / 70;
                int rest = length - (nbPart * 70);
                int i;
                for(i = 0; i < nbPart; i++) {
                    storage.writeToStorage(vid.getName() + "part" + i, fileGenerator.generateFile(70));
                }
                storage.writeToStorage(vid.getName() + "part" + i, fileGenerator.generateFile(rest));
                vid.setNbPart(String.valueOf(i+1));
            }
            else{
                vid.setNbPart("1");
                storage.writeToStorage(vid.getName() + "part" + 0, fileGenerator.generateFile(Integer.valueOf(vid.getLength())));
            }
            videoManager.createVideo(vid);
            out.println("Video acceptee !");
            return true;
        }
        out.println("Cette video existe deja !");
        return false;
    }

    private boolean checkStatus(User user, String videoLength, PrintWriter out){
        if(user.getAccountLevel().equalsIgnoreCase("bronze") && Integer.valueOf(videoLength) <= 60 && user.getCurrentVideos() == 0){
            return true;
        }
        else{
            if(user.getAccountLevel().equalsIgnoreCase("silver")){
                if(user.getCurrentVideos() < 3) {
                    return true;
                }
                else{
                    out.println("Vous convertissez deja 3 videos");
                    return false;
                }
            }
            else if(user.getAccountLevel().equalsIgnoreCase("gold")){
                if(user.getCurrentVideos() < 5) {
                    return true;
                }
                else{
                    out.println("Vous convertissez deja 5 videos");
                    return false;
                }
            }
        }
        if(Integer.valueOf(videoLength) > 60){
            out.println("Video trop longue !");
            return false;
        }
        out.println("Vous convertissez deja 1 video");
        return false;
    }
}
