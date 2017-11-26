package workers;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.appengine.api.taskqueue.TaskOptions;
import convertisseur.Convertisseur;
import entities.User;
import entities.Video;
import utils.MailManager;
import stockage.UserManager;
import stockage.VideoManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Matthieu on 25/11/2017.
 */
public class QueueWorker extends HttpServlet {
    private UserManager userManager = new UserManager();
    private VideoManager videoManager = new VideoManager();
    private MailManager mailManager = new MailManager();
    private int videoNumber;

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String videoName = req.getParameter("id");
        String videoLength = req.getParameter("videolength");
        User u = userManager.getUser(username);
        if (u.getAccountLevel().equalsIgnoreCase("silver")) {
            videoNumber = 3;
        } else {
            videoNumber = 5;
        }
        Queue silverGoldQueue = QueueFactory.getQueue("silver-gold");
        String payload =  username + "/" + videoName + "/" + videoLength;

        silverGoldQueue.add(TaskOptions.Builder.withMethod(TaskOptions.Method.PULL)
                .payload(payload)
                .tag(username));

        List<TaskHandle> tasks =
                silverGoldQueue.leaseTasksByTag(300, TimeUnit.SECONDS, videoNumber, username);
        processTasks(tasks, silverGoldQueue);


    }

    private void processTasks(List<TaskHandle> tasks, Queue q) throws UnsupportedEncodingException {
        for (TaskHandle task : tasks) {
            String payload = new String(task.getPayload());
            String[] array = payload.split("/");
            User u = userManager.getUser(array[0]);
            Video vid = new Video(array[0], array[1], array[2]);
            mailManager.setMail(u.getEmail());
            mailManager.setHeader("Demande de conversion");
            mailManager.setUsername(u.getUsername());
            mailManager.setContent("Nous avons bien pris en compte la demande de conversion de la video " + vid.getName() + ", vous serez prévenu lorsque elle sera terminée !");
            mailManager.sendEmail();
            q.deleteTask(task);
            Convertisseur convert = new Convertisseur();
            convert.setVid(new Video(array[0], array[1], array[2]));
            convert.setUser(u);
            convert.run();
        }
    }
}
