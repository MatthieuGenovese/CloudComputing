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
import org.json.JSONObject;
import stockage.UserManager;
import utils.HandleConversionRequest;

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
    UserManager userManager = new UserManager();
    HandleConversionRequest handler = new HandleConversionRequest();
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
            User client = userManager.getUser(username);
            if(client != null){
                out.print("utilisateur " + username + "authentifie !");
                if(handler.handleRequest(new Video(username, videoname, videoLength), out)){
                    if (client.getAccountLevel().equalsIgnoreCase("bronze")) {
                        Queue bonzeQueue = QueueFactory.getQueue("bronze");
                        bonzeQueue.add(TaskOptions.Builder.withUrl("/bronzequeue")
                                .param("videolength", videoLength)
                                .param("username", username)
                                .param("id", videoname));
                        out.println("Video acceptee, la conversion est en cours!");
                    } else {
                        Queue queue = QueueFactory.getQueue("silvergold");
                        queue.add(TaskOptions.Builder.withUrl("/queuedispatch")
                                .param("videolength", videoLength)
                                .param("username", username)
                                .param("id", videoname));
                        out.println("Video acceptee, la conversion est en cours!");
                    }
                }
            }
            else{
                out.print("Utilisateur non enregistre !");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println(e.toString());
        }
    }

}
