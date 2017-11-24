package workers;

import convertisseur.Convertisseur;
import entities.User;
import entities.Video;
import helloworld.MailManager;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import stockage.UserManager;
import stockage.VideoManager;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Matthieu on 21/11/2017.
 */
public class BronzeWorker extends HttpServlet  {
    private UserManager userManager = new UserManager();
    private VideoManager videoManager = new VideoManager();
    private MailManager mailManager = new MailManager();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String videoName = req.getParameter("id");
        String videoLength = req.getParameter("videolength");
        if(videoManager.getVideo(username,videoName) == null){
            User u = userManager.getUser(username);
            Video vid = new Video(username, videoName, videoLength);
            videoManager.createVideo(vid);
            u.setCurrentVideos(videoManager.getAllPendingsVideosFromUsername(username).size());
            userManager.updateUser(u);
            mailManager.setMail(u.getEmail());
            mailManager.setHeader("Demande de conversion");
            mailManager.setUsername(u.getUsername());
            mailManager.setContent("Nous avons bien pris en compte la demande de conversion de la video " + vid.getName() +", vous serez prévenu lorsque elle sera terminée !");
            mailManager.sendEmail();
            Convertisseur convert = new Convertisseur();
            convert.setVid(new Video(username,videoName,videoLength));
            convert.setUser(u);
            convert.run();
        }
        else{
            resp.getWriter().println("Video déjà en cours de conversion !");
        }
    }
}
