package workers;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.appengine.api.taskqueue.TaskOptions;
import utils.Convertisseur;
import entities.QueueStatus;
import entities.User;
import entities.VideoUser;
import stockage.QueueStatusManager;
import utils.MailManager;
import stockage.UserManager;

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
    private MailManager mailManager = new MailManager();
    private QueueStatusManager queueStatusManager = new QueueStatusManager();
    private QueueStatus status;
    private int videoNumber;

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String videoName = req.getParameter("id");
        String videoLength = req.getParameter("videolength");
        status = queueStatusManager.getQueueStatus();
        User u = userManager.getUser(username);
        if (u.getAccountLevel().equalsIgnoreCase("silver")) {
            videoNumber = 3;
        } else {
            videoNumber = 5;
        }
        //Queue silverGoldQueue = QueueFactory.getQueue("silver");
        //Queue goldQueue = QueueFactory.getQueue("gold");
        Queue queue = checkQueue(u,status);
        String payload =  username + "/" + videoName + "/" + videoLength;
        queue.add(TaskOptions.Builder.withMethod(TaskOptions.Method.PULL)
                .payload(payload)
                .tag(username));

        List<TaskHandle> tasks =
                queue.leaseTasksByTag(300, TimeUnit.SECONDS, videoNumber, username);
        processTasks(tasks, queue);


    }

    private Queue checkQueue(User u, QueueStatus status){
        Queue resultat;
        if(u.getAccountLevel().equalsIgnoreCase("gold")){
            if(status.getNbGold() <= status.getNbSilver()){
                status.setNbGold(status.getNbGold()+1);
                resultat = QueueFactory.getQueue("gold");
            }
            else{
                status.setNbSilver(status.getNbSilver()+1);
                resultat = QueueFactory.getQueue("silver");
            }
        }
        else{
            if(status.getNbSilver() > 2 * status.getNbGold()){
                status.setNbGold(status.getNbGold()+1);
                resultat = QueueFactory.getQueue("gold");
            }
            else{
                status.setNbSilver(status.getNbSilver()+1);
                resultat = QueueFactory.getQueue("silver");
            }
        }
        queueStatusManager.updateQueueStatus(status);
        return resultat;
    }

    private void processTasks(List<TaskHandle> tasks, Queue q) throws UnsupportedEncodingException {
        for (TaskHandle task : tasks) {
            String payload = new String(task.getPayload());
            String[] array = payload.split("/");
            User u = userManager.getUser(array[0]);
            VideoUser vid = new VideoUser(array[0], array[1], array[2]);
            mailManager.setMail(u.getEmail());
            mailManager.setHeader("Demande de conversion");
            mailManager.setUsername(u.getUsername());
            mailManager.setContent("Nous avons bien pris en compte la demande de conversion de la video " + vid.getName() + ", vous serez prévenu lorsque elle sera terminée !\n" +
            "Vous pouvez déjà consulter le statut de vos vidéos à cette adresse : " + "http://sacc-liechtensteger-182811.appspot.com/status?username="+ u.getUsername());
            mailManager.sendEmail();
            q.deleteTask(task);
            Convertisseur convert = new Convertisseur();
            convert.setVid(vid);
            convert.setUser(u);
            convert.run();
            status = queueStatusManager.getQueueStatus();
            if(q.getQueueName().equalsIgnoreCase("gold")){
                status.setNbGold(status.getNbGold()-1);
            }
            else{
                status.setNbSilver(status.getNbSilver()-1);
            }
            queueStatusManager.updateQueueStatus(status);
        }
    }
}
