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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Michael on 24/11/2017.
 */
public class CronStorage extends HttpServlet {

    VideoManager videoManager = new VideoManager();
    CloudStorage storage = new CloudStorage();
    UserManager userManager = new UserManager();


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        PrintWriter out = resp.getWriter();
        ArrayList<VideoUser> list = videoManager.getAllVideosUserDone();
        for(VideoUser vid : list){
            DateTime vidTime = vid.getSubmitTime();
            String accountLevel = userManager.getUser(vid.getOwner()).getAccountLevel();
            int timeCheck;
            if(accountLevel.equalsIgnoreCase("gold")){
                timeCheck = 10;
            }
            else{
                timeCheck = 5;
            }
            if(vidTime.plusMinutes(timeCheck).isAfterNow()) {
                out.println(vid);
            }
            else{
                videoManager.deleteVideoUser(vid.getOwner(),vid.getName());
                for(int i = 0; i < Integer.valueOf(vid.getNbPart()); i++) {
                    storage.deleteToStorage(vid.getName() + vid.getOwner() +"part"+i+ "convertie");
                }
            }
        }
        ArrayList<User> listUsers = userManager.getAllUsers();
        for(User user : listUsers){
            user.setCurrentVideos(videoManager.getAllPendingsVideosFromUsername(user.getUsername()).size());
            userManager.updateUser(user);
        }
    }
}
