package servlet;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.cloud.datastore.*;
import entities.Video;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import stockage.CloudStorage;
import stockage.UserManager;
import stockage.VideoManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Michael on 24/11/2017.
 */
public class CronStorage extends HttpServlet {

    private final Datastore datastore;
    private final KeyFactory keyFactory;
    VideoManager videoManager = new VideoManager();
    CloudStorage storage = new CloudStorage();
    UserManager userManager = new UserManager();

    public CronStorage(){
        datastore = DatastoreOptions.getDefaultInstance().getService();
        keyFactory = datastore.newKeyFactory().setKind("video");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        PrintWriter out = resp.getWriter();
        ArrayList<Video> list = videoManager.getAllVideosDone();
        for(Video vid : list){
            DateTime vidTime = vid.getSubmitTime();
            String accountLevel = userManager.getUser(vid.getOwner()).getAccountLevel();
            int timeCheck;
            if(accountLevel.equalsIgnoreCase("gold")){
                timeCheck = 10;
            }
            else{
                timeCheck = 5;
            }
            if(vidTime.plusMinutes(timeCheck).isAfterNow()) {
                out.println(vid);
            }
            else{
                videoManager.deleteVideo(vid.getOwner(),vid.getName());
                storage.deleteToStorage(vid);
            }
        }
    }
}
