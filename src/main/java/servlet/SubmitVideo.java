package servlet;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.gson.*;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.cloud.datastore.*;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.*;
import convertisseur.Convertisseur;
import convertisseur.UrlFetch;
import entities.User;
import entities.Video;
import org.json.JSONObject;
import stockage.UserManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthieu on 03/11/2017.
 */
public class SubmitVideo extends HttpServlet {
    Convertisseur conv = new Convertisseur();
    UserManager userManager = new UserManager();
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        for(int i = 0; i < 5; i++) {
            Entity entity = new Entity("user");
            entity.setProperty("username", "toto" + String.valueOf(i));
            entity.setProperty("accountlevel", "gold");
            entity.setProperty("email", "mohamedchennouf06@gmail.com");
            entity.setProperty("currentVid", 0);
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            datastore.put(entity);
        }
    }
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
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
            String videoname = jsontest.get("video").getAsString();
            String videoLength = jsontest.get("length").getAsString();
            String email = jsontest.get("email").getAsString();
            boolean found = false;
            User client = userManager.getUser(username);
            if(client != null){
                out.print("utilisateur " + username + "authentifie !");
                found = true;
                if(checkStatus(client, videoLength, out)) {
                    Queue queue = QueueFactory.getQueue("mainqueue");
                    queue.add(TaskOptions.Builder.withUrl("/queuedispatch")
                            .param("videolength", videoLength)
                            .param("username", username)
                            .param("id", videoname));
                    out.println("Video acceptee, la conversion est en cours!");
                }
            }
            if(!found){
                out.print("Utilisateur non enregistre !");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println(e.toString());
        }
    }

    private boolean checkStatus(User user, String videoLength, PrintWriter out){
        if(user.getAccountLevel().equalsIgnoreCase("bronze") && Integer.valueOf(videoLength) <= 60 && user.getCurrentVideos() == 0){
            return true;
        }
        else{
            if(user.getAccountLevel().equalsIgnoreCase("silver")){
                if(user.getCurrentVideos() < 3) {
                    return true;
                }
                else{
                    out.println("Vous convertissez deja 3 videos");
                    return false;
                }
            }
            else if(user.getAccountLevel().equalsIgnoreCase("gold")){
                if(user.getCurrentVideos() < 5) {
                    return true;
                }
                else{
                    out.println("Vous convertissez deja 5 videos");
                    return false;
                }
            }
        }
        if(Integer.valueOf(videoLength) > 60){
            out.println("Video trop longue !");
            return false;
        }
        out.println("Vous convertissez deja 1 video");
        return false;
    }
}
