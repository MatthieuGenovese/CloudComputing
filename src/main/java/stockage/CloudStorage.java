package stockage;

/**
 * Created by Michael on 03/11/2017.
 */
// Imports the Google Cloud client library
import com.google.cloud.storage.*;
import com.google.cloud.storage.Storage;
import java.util.ArrayList;
import java.util.Arrays;

public class CloudStorage{

    private static Storage storage = null;

    // [START init]
    static {
        storage = StorageOptions.getDefaultInstance().getService();
    }

    public String writeToStorage(String filename, byte[] file){
        storage = StorageOptions.getDefaultInstance().getService();
        String bucketName = "sacc-liechtensteger-182811.appspot.com";  // "my-new-bucket";
        BlobInfo blobInfo =
                storage.create(
                        BlobInfo
                                .newBuilder(bucketName, filename)
                                .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))))
                                .build(),
                        file);
        return blobInfo.getMediaLink();
    }

    public void deleteToStorage(String videoname){
        String bucketName = "sacc-liechtensteger-182811.appspot.com";  // "my-new-bucket";
        storage.delete(BlobInfo
                        .newBuilder(bucketName, videoname)
                        .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))))
                        .build().getBlobId());
    }
}
