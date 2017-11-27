package workers;

import utils.Convertisseur;
import entities.User;
import entities.VideoUser;
import utils.MailManager;
import stockage.UserManager;
import stockage.VideoManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        User u = userManager.getUser(username);
        mailManager.setMail(u.getEmail());
        mailManager.setHeader("Demande de conversion");
        mailManager.setUsername(u.getUsername());
        mailManager.setContent("Nous avons bien pris en compte la demande de conversion de la video " + videoName + ", vous serez prévenu lorsque elle sera terminée !\n" +
                "Vous pouvez déjà consulter le statut de vos vidéos à cette adresse : " + "http://sacc-liechtensteger-182811.appspot.com/status?username="+ u.getUsername());
        mailManager.sendEmail();
        Convertisseur convert = new Convertisseur();
        convert.setVid(new VideoUser(username, videoName, videoLength));
        convert.setUser(u);
        convert.run();
    }
}
