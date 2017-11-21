package workers;

import convertisseur.Convertisseur;
import entities.Video;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Matthieu on 21/11/2017.
 */
public class VideoWorker extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String videoName = req.getParameter("id");
        String videoLength = req.getParameter("videolength");
        Convertisseur convert = new Convertisseur();
        convert.setVid(new Video(username,videoName,Integer.valueOf(videoLength)));
        convert.run();
    }
}
