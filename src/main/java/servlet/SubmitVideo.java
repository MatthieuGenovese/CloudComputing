package servlet;

import com.google.api.gax.rpc.DeadlineExceededException;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entities.User;
import entities.Video;
import stockage.UserManager;
import utils.HandleConversionRequest;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Matthieu on 27/11/2017.
 */
public class SubmitVideo extends HttpServlet {
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
            String videoname = jsontest.get("video").getAsString();
            String videoLength = jsontest.get("length").getAsString();
            //try {
                handler.handleUploadRequest(new Video(videoname, videoLength), out);
            //}
            //catch (DeadlineExceededException e){

            //}
        } catch (Exception e) {
            e.printStackTrace();
            out.println(e.toString());
        }
    }

}
