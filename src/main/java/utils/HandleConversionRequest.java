package utils;

import entities.User;
import entities.Video;
import stockage.UserManager;
import stockage.VideoManager;

import java.io.PrintWriter;

/**
 * Created by Matthieu on 25/11/2017.
 */
public class HandleConversionRequest {
    private VideoManager videoManager;
    private UserManager userManager;

    public HandleConversionRequest(){
        videoManager = new VideoManager();
        userManager = new UserManager();
    }

    public boolean handleRequest(Video vid, PrintWriter out){
        if(videoManager.getVideo(vid.getOwner(), vid.getName()) == null) {
            User u = userManager.getUser(vid.getOwner());
            if(checkStatus(u, vid.getLength(), out)) {
                u.setCurrentVideos(videoManager.getAllPendingsVideosFromUsername(u.getUsername()).size()+1);
                videoManager.createVideo(vid);
                userManager.updateUser(u);
                out.println("nb video : " + u.getCurrentVideos());
                return true;
            }
            return false;
        }
        else {
            out.println("Video deja en cours de conversion");
            return false;
        }
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
