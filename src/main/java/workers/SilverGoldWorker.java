package workers;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import convertisseur.Convertisseur;
import entities.User;
import entities.Video;
import helloworld.MailManager;
import stockage.UserManager;
import stockage.VideoManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Matthieu on 23/11/2017.
 */
public class SilverGoldWorker extends HttpServlet{
    private UserManager userManager = new UserManager();
    private MailManager mailManager = new MailManager();
    private VideoManager videoManager = new VideoManager();
    private PrintWriter out;

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        out = resp.getWriter();
        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);
            }
        } catch (Exception e) { /*report an error*/ }

        try {
            JsonParser jparser = new JsonParser();
            JsonElement obj = jparser.parse(jb.toString());
            JsonObject jsontest = obj.getAsJsonObject();
            String username = jsontest.get("username").getAsString();
            String videoname = jsontest.get("videoname").getAsString();
            String videoLength = jsontest.get("length").getAsString();
            Queue q = QueueFactory.getQueue("silver-gold");
            q.add(TaskOptions.Builder.withMethod(TaskOptions.Method.PULL)
                                .tag(username + "/" + videoname + "/" + videoLength));
            List<TaskHandle> tasks = q.leaseTasks(3600, TimeUnit.SECONDS, 1);
            processTasks(tasks, q);
        }catch (Exception e) {
            e.printStackTrace();
            out.println(e.toString());
        }
    }

    private void processTasks(List<TaskHandle> tasks, Queue q) throws UnsupportedEncodingException {
        for (TaskHandle task : tasks) {
            String tag = task.getTag();
            String[] array = tag.split("/");
            if(videoManager.getVideo(array[0],array[1]) == null){
                User u = userManager.getUser(array[0]);
                Video vid = new Video(array[0], array[1], array[2]);
                videoManager.createVideo(vid);
                u.setCurrentVideos(videoManager.getAllPendingsVideosFromUsername(array[0]).size());
                userManager.updateUser(u);
                mailManager.setMail(u.getEmail());
                mailManager.setHeader("Demande de conversion");
                mailManager.setUsername(u.getUsername());
                mailManager.setContent("Nous avons bien pris en compte la demande de conversion de la video " + vid.getName() +", vous serez prévenu lorsque elle sera terminée !");
                mailManager.sendEmail();
                q.deleteTask(task);
                out.println("j ai fini fils de pute !");
                Convertisseur convert = new Convertisseur();
                convert.setVid(new Video(array[0],array[1],array[2]));
                convert.setUser(u);
                convert.run();
            }
        }
    }
}
