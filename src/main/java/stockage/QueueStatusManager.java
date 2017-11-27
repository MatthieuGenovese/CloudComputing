package stockage;

import com.google.cloud.datastore.*;
import entities.QueueStatus;
import entities.User;

import java.util.ArrayList;

/**
 * Created by Matthieu on 27/11/2017.
 */
public class QueueStatusManager {
    private final Datastore datastore;
    private final KeyFactory keyFactory;

    public QueueStatusManager(){
        datastore = DatastoreOptions.getDefaultInstance().getService();
        keyFactory = datastore.newKeyFactory().setKind("queuestatus");
        init();
    }

    public QueueStatus getQueueStatus(){
        QueueStatus res = null;
        EntityQuery query =
                Query.newEntityQueryBuilder().setKind("queuestatus")
                        .build();
        QueryResults<Entity> results = datastore.run(query);
        while (results.hasNext()) {
            Entity entity = results.next();
            long idRead = entity.getLong("id");
            long nbSilver = entity.getLong("nbSilver");
            long nbGold = entity.getLong("nbGold");
            res = new QueueStatus(nbGold, nbSilver, idRead);

        }
        return res;
    }

    private void init(){
        if(getQueueStatus() == null){
            QueueStatus status = new QueueStatus(0,0,1);
            createQueueStatus(status);
        }
    }


    public void updateQueueStatus(QueueStatus qs){
        EntityQuery query =
                Query.newEntityQueryBuilder()
                        .setKind("queuestatus")
                        .build();
        QueryResults<Entity> results = datastore.run(query);
        if (!results.hasNext()){
            return;
        }
        Entity.Builder builder = Entity.newBuilder(results.next());
        builder.set("id",qs.getId());
        builder.set("nbSilver", qs.getNbSilver());
        builder.set("nbGold", qs.getNbGold());
        Entity entity = builder.build();
        datastore.update(entity);
    }

    public void createQueueStatus(QueueStatus qs) {
        IncompleteKey key = keyFactory.newKey();
        FullEntity<IncompleteKey> user = Entity.newBuilder(key)
                .set("id",qs.getId())
                .set("nbSilver", qs.getNbSilver())
                .set("nbGold", qs.getNbGold())
                .build();
        datastore.add(user);
    }
}
