package workers;

import convertisseur.Convertisseur;
import entities.User;
import entities.Video;
import stockage.UserManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Matthieu on 21/11/2017.
 */
public class VideoWorker extends HttpServlet {
    UserManager userManager = new UserManager();
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String videoName = req.getParameter("id");
        String videoLength = req.getParameter("videolength");
        User u = userManager.getUser(username);
        u.setCurrentVideos(u.getCurrentVideos()+1);
        userManager.updateUser(u);
        Convertisseur convert = new Convertisseur();
        convert.setVid(new Video(username,videoName,Integer.valueOf(videoLength)));
        convert.run();
    }
}
