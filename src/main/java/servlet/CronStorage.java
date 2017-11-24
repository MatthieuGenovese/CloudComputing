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

    public CronStorage(){
        datastore = DatastoreOptions.getDefaultInstance().getService();
        keyFactory = datastore.newKeyFactory().setKind("video");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        PrintWriter out = resp.getWriter();
        ArrayList<Video> list = getAllVideos();
        for(Video vid : list){
            DateTime vidTime = vid.getSubmitTime();
            if(vidTime.plusMinutes(5).isAfterNow()) {
                out.println(vid);
            }
            else{
                storage.deleteToStorage(vid);
            }
        }
    }

    public ArrayList<Video> getAllVideos(){
        ArrayList<Video> res = new ArrayList<>();
        EntityQuery query =
                Query.newEntityQueryBuilder().setKind("video")
                        .build();
        QueryResults<com.google.cloud.datastore.Entity> results = datastore.run(query);
        while (results.hasNext()) {
            com.google.cloud.datastore.Entity entity = results.next();
            String owner = entity.getString("username");
            String name = entity.getString("videoname");
            String videolength = entity.getString("videolength");
            String status = entity.getString("status");
            String downloadLink = entity.getString("downloadLink");
            String stringSubmitTime = entity.getString("submitTime");
            DateTimeFormatter formatter = DateTimeFormat.forPattern("-YYYY-MM-dd-HHmmssSSS");
            DateTime submitTime = formatter.parseDateTime(stringSubmitTime);
            Video vid = new Video(owner, name, videolength);
            vid.setDownloadLink(downloadLink);
            vid.setStatus(status);
            vid.setSubmitTime(submitTime);
            res.add(vid);
        }
        return res;
    }
}
