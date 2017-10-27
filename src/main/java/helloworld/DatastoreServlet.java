package helloworld;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.*;
import com.google.gson.*;
import org.json.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.ArrayList;

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

    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    KeyFactory keyFactory = datastore.newKeyFactory().setKind("article");
    IncompleteKey key = keyFactory.setKind("article").newKey();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            ServletException {
        // store only the first two octets of a users ip address
        String userIp = req.getRemoteAddr();
        InetAddress address = InetAddress.getByName(userIp);
        if (address instanceof Inet6Address) {
            // nest indexOf calls to find the second occurrence of a character in a string
            // an alternative is to use Apache Commons Lang: StringUtils.ordinalIndexOf()
            userIp = userIp.substring(0, userIp.indexOf(":", userIp.indexOf(":") + 1)) + ":*:*:*:*:*:*";
        } else if (address instanceof Inet4Address) {
            userIp = userIp.substring(0, userIp.indexOf(".", userIp.indexOf(".") + 1)) + ".*.*";
        }



        // Record a visit to the datastore, storing the IP and timestamp.
        FullEntity<IncompleteKey> curVisit = FullEntity.newBuilder(key)
                .set("user_ip", userIp).set("timestamp", Timestamp.now()).build();
        datastore.add(curVisit);

        // Retrieve the last 10 visits from the datastore, ordered by timestamp.
        EntityQuery query = Query.newEntityQueryBuilder().setKind("visit")
                .setOrderBy(StructuredQuery.OrderBy.desc("timestamp")).setLimit(10).build();
        QueryResults<com.google.cloud.datastore.Entity> results = datastore.run(query);

        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        out.print("Last 10 visits:\n");
        while (results.hasNext()) {
            Entity entity = results.next();
            out.format("Time: %s Addr: %s\n", entity.getTimestamp("timestamp"),
                    entity.getString("user_ip"));
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();
        PrintWriter out = resp.getWriter();
        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null) {
//                out.print(line + " ok ");
                jb.append(line);
            }
        } catch (Exception e) { /*report an error*/ }

        try {
//            JSONObject json =  HTTP.toJSONObject(jb.toString());
            JsonParser jparser = new JsonParser();
            JsonElement obj = jparser.parse(jb.toString());
            JsonObject jsontest = obj.getAsJsonObject();
//            out.print(jsontest.toString());
//            gson.toJson(jb.toString());
            Article a = new Article(jsontest.get("nom").getAsString(), Integer.valueOf(jsontest.get("prix").getAsString()), Integer.valueOf(jsontest.get("quantite").getAsString()));
//            datastore
            
            FullEntity<Article> list = FullEntity.newBuilder();
            Entity entity = new Entity();

//            PrintWriter out = resp.getWriter();
            out.println(a);
        } catch (Exception e) {
            e.printStackTrace();
            out.println(e.toString());
        }
    }
}