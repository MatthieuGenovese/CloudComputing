package stockage;

import com.google.cloud.datastore.*;
import entities.User;
import entities.Video;
import entities.VideoUser;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

/**
 * Created by Matthieu on 22/11/2017.
 */
public class VideoManager {
    private final Datastore datastore;
    private final KeyFactory keyFactory;

    public VideoManager(){
        datastore = DatastoreOptions.getDefaultInstance().getService();
        keyFactory = datastore.newKeyFactory().setKind("videouser");
    }

    public VideoUser getVideoUser(String username, String videoname){
        VideoUser res = null;
        EntityQuery query =
                Query.newEntityQueryBuilder().setKind("videouser")
                        .build();
        QueryResults<Entity> results = datastore.run(query);
        while (results.hasNext()) {
            Entity entity = results.next();
            if(entity.getString("username").equalsIgnoreCase(username) && entity.getString("videoname").equalsIgnoreCase(videoname)){
                String owner = entity.getString("username");
                String name = entity.getString("videoname");
                String videolength = entity.getString("videolength");
                String status = entity.getString("status");
                DateTime submitTime = DateTime.now(DateTimeZone.UTC);
                res = new VideoUser(owner, name, videolength);
                res.setStatus(status);
                res.setSubmitTime(submitTime);
            }
        }
        return res;
    }

    public Video getVideo(String videoname){
        Video res = null;
        EntityQuery query =
                Query.newEntityQueryBuilder().setKind("video")
                        .build();
        QueryResults<Entity> results = datastore.run(query);
        while (results.hasNext()) {
            Entity entity = results.next();
            if(entity.getString("videoname").equalsIgnoreCase(videoname)){
                String name = entity.getString("videoname");
                String videolength = entity.getString("videolength");
                res = new Video(name, videolength);
            }
        }
        return res;
    }

    public void deleteVideo(String videoname){
        EntityQuery query =
                Query.newEntityQueryBuilder().setKind("video")
                        .build();
        QueryResults<Entity> results = datastore.run(query);
        while (results.hasNext()) {
            Entity entity = results.next();
            if(entity.getString("videoname").equalsIgnoreCase(videoname)){
                datastore.delete(entity.getKey());
            }
        }
    }

    public void deleteVideoUser(String username, String videoname){
        EntityQuery query =
                Query.newEntityQueryBuilder().setKind("videouser")
                        .build();
        QueryResults<Entity> results = datastore.run(query);
        while (results.hasNext()) {
            Entity entity = results.next();
            if(entity.getString("username").equalsIgnoreCase(username) && entity.getString("videoname").equalsIgnoreCase(videoname)){
                datastore.delete(entity.getKey());
            }
        }
    }

    public ArrayList<VideoUser> getAllVideosFromUsername(String username){
        ArrayList<VideoUser> res = new ArrayList<>();
        EntityQuery query =
                Query.newEntityQueryBuilder().setKind("videouser")
                        .build();
        QueryResults<Entity> results = datastore.run(query);
        while (results.hasNext()) {
            Entity entity = results.next();
            if(entity.getString("username").equalsIgnoreCase(username)){
                String owner = entity.getString("username");
                String name = entity.getString("videoname");
                String videolength = entity.getString("videolength");
                String status = entity.getString("status");
                String downloadLink = entity.getString("downloadLink");
                String stringSubmitTime = entity.getString("submitTime");
                DateTimeFormatter formatter = DateTimeFormat.forPattern("-YYYY-MM-dd-HHmmssSSS");
                DateTime submitTime = formatter.parseDateTime(stringSubmitTime);
                VideoUser vid = new VideoUser(owner, name, videolength);
                vid.setDownloadLink(downloadLink);
                vid.setStatus(status);
                vid.setSubmitTime(submitTime);
                res.add(vid);
            }
        }
        return res;
    }

    public ArrayList<VideoUser> getAllPendingsVideosFromUsername(String username){
        ArrayList<VideoUser> res = new ArrayList<>();
        EntityQuery query =
                Query.newEntityQueryBuilder().setKind("videouser")
                        .build();
        QueryResults<Entity> results = datastore.run(query);
        while (results.hasNext()) {
            Entity entity = results.next();
            if(entity.getString("username").equalsIgnoreCase(username)
                    && (entity.getString("status").equalsIgnoreCase("processing")
                        || entity.getString("status").equalsIgnoreCase("waiting"))){
                String owner = entity.getString("username");
                String name = entity.getString("videoname");
                String videolength = entity.getString("videolength");
                String downloadLink = entity.getString("downloadLink");
                String status = entity.getString("status");
                String stringSubmitTime = entity.getString("submitTime");
                DateTimeFormatter formatter = DateTimeFormat.forPattern("-YYYY-MM-dd-HHmmssSSS");
                DateTime submitTime = formatter.parseDateTime(stringSubmitTime);
                VideoUser vid = new VideoUser(owner, name, videolength);
                vid.setStatus(status);
                vid.setSubmitTime(submitTime);
                vid.setDownloadLink(downloadLink);
                res.add(vid);
            }
        }
        return res;
    }

    public ArrayList<VideoUser> getAllVideosUserDone(){
        ArrayList<VideoUser> res = new ArrayList<>();
        EntityQuery query =
                Query.newEntityQueryBuilder().setKind("videouser")
                        .build();
        QueryResults<com.google.cloud.datastore.Entity> results = datastore.run(query);
        while (results.hasNext()) {
            Entity entity = results.next();
            if(entity.getString("status").equalsIgnoreCase("done")) {
                String owner = entity.getString("username");
                String name = entity.getString("videoname");
                String videolength = entity.getString("videolength");
                String status = entity.getString("status");
                String downloadLink = entity.getString("downloadLink");
                String stringSubmitTime = entity.getString("submitTime");
                DateTimeFormatter formatter = DateTimeFormat.forPattern("-YYYY-MM-dd-HHmmssSSS");
                DateTime submitTime = formatter.parseDateTime(stringSubmitTime);
                VideoUser vid = new VideoUser(owner, name, videolength);
                vid.setDownloadLink(downloadLink);
                vid.setStatus(status);
                vid.setSubmitTime(submitTime);
                res.add(vid);
            }
        }
        return res;
    }

    public void updateVideoUser(VideoUser vid){
        EntityQuery query =
                Query.newEntityQueryBuilder()
                        .setKind("videouser")
                        .setFilter(StructuredQuery.PropertyFilter.eq("videoname", vid.getName()))
                        .setFilter(StructuredQuery.PropertyFilter.eq("username", vid.getOwner()))
                        .build();
        QueryResults<Entity> results = datastore.run(query);
        if (!results.hasNext()){
            return;
        }
        IncompleteKey key = keyFactory.newKey();
        FullEntity<IncompleteKey> user = Entity.newBuilder(key)
                .set("username", vid.getOwner())
                .set("videoname", vid.getName())
                .set("videolength", vid.getLength())
                .set("status", vid.getStatus())
                .set("downloadLink", vid.getDownloadLink())
                .set("submitTime", vid.getSubmitTime().toString(DateTimeFormat.forPattern("-YYYY-MM-dd-HHmmssSSS")))
                .build();
        deleteVideoUser(vid.getOwner(), vid.getName());
        datastore.add(user);
    }

    public void createVideoUser(VideoUser vid) {
        IncompleteKey key = keyFactory.newKey();
        FullEntity<IncompleteKey> user = Entity.newBuilder(key)
                .set("username", vid.getOwner())
                .set("videoname", vid.getName())
                .set("videolength", vid.getLength())
                .set("status", vid.getStatus())
                .set("downloadLink", vid.getDownloadLink())
                .set("submitTime", vid.getSubmitTime().toString(DateTimeFormat.forPattern("-YYYY-MM-dd-HHmmssSSS")))
                .build();
        datastore.add(user);
    }

    public void createVideo(Video vid) {
        IncompleteKey key = datastore.newKeyFactory().setKind("video").newKey();
        FullEntity<IncompleteKey> user = Entity.newBuilder(key)
                .set("videoname", vid.getName())
                .set("videolength", vid.getLength())
                .build();
        datastore.add(user);
    }
}
