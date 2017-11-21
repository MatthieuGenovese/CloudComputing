package stockage;

/**
 * Created by Matthieu on 21/11/2017.
 */
import com.google.cloud.datastore.*;
import entities.User;
import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private static UserManager instance;
    private final Datastore datastore;
    private final KeyFactory keyFactory;

    public UserManager(){
        datastore = DatastoreOptions.getDefaultInstance().getService();
        keyFactory = datastore.newKeyFactory().setKind("user");
    }

    public User getUser(String username){
        User res = null;
        EntityQuery query =
                Query.newEntityQueryBuilder().setKind("user")
                        .build();
        QueryResults<Entity> results = datastore.run(query);
        while (results.hasNext()) {
            Entity entity = results.next();
            if(entity.getString("username").equalsIgnoreCase(username)){
                String level = entity.getString("accountlevel");
                String email = entity.getString("email");
                List<Value<?>> currentVid = entity.getList("currentVid");
                res = new User(username, level, email);
                res.setCurrentVideos(convertList(currentVid));
            }

        }
        return res;
    }

    private ArrayList<String> convertList(List<Value<?>> currentVid){
        ArrayList<String> res = new ArrayList<>();
        for(Value<?> v : currentVid){
            res.add(v.toString());
        }
        return res;
    }

    public void createUser(String username, String accountLevel, String email) {
        IncompleteKey key = keyFactory.newKey();
        FullEntity<IncompleteKey> user = Entity.newBuilder(key)
                .set("username", username)
                .set("email", email)
                .set("accountlevel", accountLevel)
                .build();
        datastore.add(user);
    }

}
