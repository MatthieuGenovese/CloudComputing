package servlet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entities.Video;
import entities.VideoUser;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import stockage.CloudStorage;
import stockage.VideoManager;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by Matthieu on 22/11/2017.
 */
public class VideoStatus extends HttpServlet {
    VideoManager videoManager = new VideoManager();
    CloudStorage storage = new CloudStorage();
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
        } catch (Exception e) { out.println(e.toString());}

        try {
            JsonParser jparser = new JsonParser();
            JsonElement obj = jparser.parse(jb.toString());
            JsonObject jsontest = obj.getAsJsonObject();
            String username = jsontest.get("username").getAsString();
            ArrayList<VideoUser> list = videoManager.getAllVideosFromUsername(username);
            for(VideoUser vid : list){
                out.println(vid);
            }
        }
        catch(Exception e){
            out.println(e.toString());
        }
    }
}

