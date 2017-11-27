package servlet;

import entities.User;
import entities.Video;
import entities.VideoUser;
import org.joda.time.DateTime;
import stockage.CloudStorage;
import stockage.UserManager;
import stockage.VideoManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Matthieu on 27/11/2017.
 */
public class CronSubmitedVideo extends HttpServlet {

    VideoManager videoManager = new VideoManager();
    CloudStorage storage = new CloudStorage();


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        ArrayList<Video> list = videoManager.getAllVideos();
        for(Video vid : list){
            ArrayList<VideoUser> listVideosUser = videoManager.getAllVideosUserFromVideoname(vid.getName());
            if(listVideosUser.isEmpty()) {
                storage.deleteToStorage(vid.getName());
                videoManager.deleteVideo(vid.getName());
            }
        }
    }
}
