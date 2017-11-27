package servlet;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.gson.*;
import entities.User;
import entities.VideoUser;
import stockage.UserManager;
import utils.HandleConversionRequest;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Matthieu on 03/11/2017.
 */
public class ConvertVideo extends HttpServlet {
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
            User client = userManager.getUser(username);
            if(client != null){
                out.print("utilisateur " + username + "authentifie !");
                VideoUser vid = handler.handleConversionRequest(username, videoname, out);
                if(vid != null){
                    if (client.getAccountLevel().equalsIgnoreCase("bronze")) {
                        Queue bonzeQueue = QueueFactory.getQueue("bronze");
                        bonzeQueue.add(TaskOptions.Builder.withUrl("/bronzequeue")
                                .param("videolength", vid.getLength())
                                .param("username", username)
                                .param("id", videoname));
                        out.println("Video acceptee, la conversion est en cours!");
                    } else {
                        Queue queue = QueueFactory.getQueue("silvergold");
                        queue.add(TaskOptions.Builder.withUrl("/queuedispatch")
                                .param("videolength", vid.getLength())
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
