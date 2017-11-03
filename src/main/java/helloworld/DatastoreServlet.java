package helloworld;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.cloud.datastore.*;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Matthieu on 27/10/2017.
 */
@SuppressWarnings("serial")
@WebServlet(name = "datastore", value = "")
public class DatastoreServlet extends HttpServlet {


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        KeyFactory keyFactory = datastore.newKeyFactory().setKind("article");
        IncompleteKey key = keyFactory.setKind("article").newKey();

        // Retrieve the last 10 visits from the datastore, ordered by timestamp.
        EntityQuery query = Query.newEntityQueryBuilder().setKind("article").build();
        QueryResults<com.google.cloud.datastore.Entity> results = datastore.run(query);

        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        while (results.hasNext()) {
            com.google.cloud.datastore.Entity entity = results.next();
            out.print("Article : " + entity.getString("nom") + " prix :" + entity.getLong("prix") + " quantite : " + entity.getLong("quantite") + "\n");
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
            Article a = new Article(jsontest.get("nom").getAsString(), Integer.valueOf(jsontest.get("prix").getAsString()), Integer.valueOf(jsontest.get("quantite").getAsString()));
            Entity entity = new Entity("article");
            entity.setProperty("nom", a.getNom());
            entity.setProperty("prix", a.getPrix());
            entity.setProperty("quantite", a.getQuantite());
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            datastore.put(entity);

//            PrintWriter out = resp.getWriter();
            out.println(a);
        } catch (Exception e) {
            e.printStackTrace();
            out.println(e.toString());
        }
    }
}