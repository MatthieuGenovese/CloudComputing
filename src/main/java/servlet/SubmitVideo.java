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
import entities.User;
import entities.Video;
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
            entity.setProperty("email", "gm102445@etu.unice.fr");
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
                out.print("utilisateur " + username + "authentifié !");
                found = true;
                if(checkStatus(client, videoLength)) {
                    Queue queue = QueueFactory.getQueue("pull-queue");
                    queue.add(TaskOptions.Builder.withUrl("/videoupload")
                            .param("videolength", videoLength)
                            .param("username", username)
                            .param("id", videoname));
                }
            }
            if(!found){
                out.print("Utilisateur non enregistré !");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println(e.toString());
        }
    }

    private boolean checkStatus(User user, String videoLength){
        if(user.getAccountLevel().equalsIgnoreCase("bronze") && Integer.valueOf(videoLength) <= 60 && user.getCurrentVideos() == 0){
            return true;
        }
        else{
            if(user.getAccountLevel().equalsIgnoreCase("silver")){
                if(user.getCurrentVideos() < 3) {
                    return true;
                }
                else{
                    return false;
                }
            }
            else if(user.getAccountLevel().equalsIgnoreCase("gold")){
                if(user.getCurrentVideos() < 5) {
                    return true;
                }
                else{
                    return false;
                }
            }
        }
        return false;
    }
}
