package utils;

import entities.User;
import entities.Video;
import stockage.UserManager;
import stockage.VideoManager;

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

    public User handleRequest(Video vid){
        User u = userManager.getUser(vid.getOwner());
        videoManager.createVideo(vid);
        u.setCurrentVideos(videoManager.getAllPendingsVideosFromUsername(u.getUsername()).size());
        userManager.updateUser(u);
        return u;
    }
}
