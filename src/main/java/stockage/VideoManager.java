package stockage;

import com.google.cloud.datastore.*;
import entities.User;
import entities.Video;

import java.util.ArrayList;

/**
 * Created by Matthieu on 22/11/2017.
 */
public class VideoManager {
    private final Datastore datastore;
    private final KeyFactory keyFactory;

    public VideoManager(){
        datastore = DatastoreOptions.getDefaultInstance().getService();
        keyFactory = datastore.newKeyFactory().setKind("video");
    }

    public Video getVideo(String username, String videoname){
        Video res = null;
        EntityQuery query =
                Query.newEntityQueryBuilder().setKind("video")
                        .build();
        QueryResults<Entity> results = datastore.run(query);
        while (results.hasNext()) {
            Entity entity = results.next();
            if(entity.getString("username").equalsIgnoreCase(username) && entity.getString("videoname").equalsIgnoreCase(videoname)){
                String owner = entity.getString("username");
                String name = entity.getString("videoname");
                String videolength = String.valueOf(entity.getLong("videolength"));
                res = new Video(owner, name, videolength);
            }

        }
        return res;
    }

    public ArrayList<Video> getAllVideosFromUsername(String username){
        ArrayList<Video> res = new ArrayList<>();
        EntityQuery query =
                Query.newEntityQueryBuilder().setKind("video")
                        .build();
        QueryResults<Entity> results = datastore.run(query);
        while (results.hasNext()) {
            Entity entity = results.next();
            if(entity.getString("username").equalsIgnoreCase(username)){
                String owner = entity.getString("username");
                String name = entity.getString("videoname");
                String videolength = String.valueOf(entity.getLong("videolength"));
                res.add(new Video(owner, name, videolength));
            }

        }
        return res;
    }

    public void updateUser(User user){
        EntityQuery query =
                Query.newEntityQueryBuilder()
                        .setKind("user")
                        .setFilter(StructuredQuery.PropertyFilter.eq("username", user.getUsername()))
                        .build();
        QueryResults<Entity> results = datastore.run(query);
        if (!results.hasNext()){
            return;
        }
        Entity.Builder builder = Entity.newBuilder(results.next());
        builder.set("username",user.getUsername());
        builder.set("accountlevel", user.getAccountLevel());
        builder.set("email", user.getEmail());
        builder.set("currentVid", user.getCurrentVideos());
        Entity entity = builder.build();
        datastore.update(entity);
    }

    public void createVideo(Video vid) {
        IncompleteKey key = keyFactory.newKey();
        FullEntity<IncompleteKey> user = Entity.newBuilder(key)
                .set("username", vid.getOwner())
                .set("videoname", vid.getName())
                .set("videolength", vid.getLength())
                .build();
        datastore.add(user);
    }
}
