package helloworld;

import com.google.gson.*;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.cloud.datastore.*;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.*;
import convertisseur.Convertisseur;
import entities.Video;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Matthieu on 03/11/2017.
 */
public class AuthentificationServlet extends HttpServlet {
    Convertisseur conv = new Convertisseur();
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        /*for(int i = 0; i < 5; i++) {
            Entity entity = new Entity("user");
            entity.setProperty("username", "toto" + String.valueOf(i));
            entity.setProperty("accountlevel", "gold");
            entity.setProperty("email", "totodu36" + String.valueOf(i)+ "@tamere.fr");
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            datastore.put(entity);
        }*/
    }
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        StringBuffer jb = new StringBuffer();
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        KeyFactory keyFactory = datastore.newKeyFactory().setKind("user");
        IncompleteKey key = keyFactory.setKind("user").newKey();
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
            boolean found = false;
            EntityQuery query = Query.newEntityQueryBuilder().setKind("user").build();
            QueryResults<com.google.cloud.datastore.Entity> results = datastore.run(query);
            resp.setContentType("text/plain");
            while (results.hasNext()) {
                com.google.cloud.datastore.Entity entity = results.next();
                if(entity.getString("username").equalsIgnoreCase(username)){
                    out.print("utilisateur " + username + "authentifié !");
                    if(conv.isStatus()){
                        conv.setVid(new Video(username,videoname, Integer.valueOf(videoLength), "152"));
                        conv.run();
                    }
                    found = true;
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
}
