package workers;

import convertisseur.Convertisseur;
import entities.User;
import entities.Video;
import stockage.UserManager;
import stockage.VideoManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Matthieu on 21/11/2017.
 */
public class VideoWorker extends HttpServlet {
    UserManager userManager = new UserManager();
    VideoManager videoManager = new VideoManager();
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String videoName = req.getParameter("id");
        String videoLength = req.getParameter("videolength");
        User u = userManager.getUser(username);
        u.setCurrentVideos(u.getCurrentVideos()+1);
        Video vid = new Video(username, videoName, videoLength);
        userManager.updateUser(u);
        if(videoManager.getVideo(username,videoName) == null){
            videoManager.createVideo(vid);
        }
        else{
            resp.getWriter().println("Video déjà en cours de conversion !");
        }
        Convertisseur convert = new Convertisseur();
        convert.setVid(new Video(username,videoName,videoLength));
        convert.setUser(u);
        convert.run();
    }
}
