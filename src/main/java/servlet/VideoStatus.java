package servlet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entities.Video;
import stockage.VideoManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Matthieu on 22/11/2017.
 */
public class VideoStatus extends HttpServlet {
    VideoManager videoManager = new VideoManager();
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
            ArrayList<Video> list = videoManager.getAllVideosFromUsername(username);
            for(Video vid : list){
                out.println(vid);
            }
        }
        catch(Exception e){
            out.println(e.toString());
        }
    }
}

