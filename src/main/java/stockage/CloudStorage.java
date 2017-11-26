package stockage;

/**
 * Created by Michael on 03/11/2017.
 */
// Imports the Google Cloud client library
import com.google.cloud.storage.*;
import com.google.cloud.storage.Storage;
import entities.Video;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CloudStorage{

    private static Storage storage = null;

    // [START init]
    static {
        storage = StorageOptions.getDefaultInstance().getService();
    }

    public String writeToStorage(String filename, byte[] file){
        // Instantiates a client
        storage = StorageOptions.getDefaultInstance().getService();
        // The name of the bucket
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

    public void deleteToStorage(Video vid){
        String bucketName = "sacc-liechtensteger-182811.appspot.com";  // "my-new-bucket";
        storage.delete(BlobInfo
                        .newBuilder(bucketName, vid.getOwner()+vid.getName())
                        .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))))
                        .build().getBlobId());
    }
}
