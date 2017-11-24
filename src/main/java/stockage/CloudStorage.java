package stockage;

/**
 * Created by Michael on 03/11/2017.
 */
// Imports the Google Cloud client library
import com.google.cloud.storage.*;
import com.google.cloud.storage.Storage;
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

public class CloudStorage extends HttpServlet{

    private static Storage storage = null;

    // [START init]
    static {
        storage = StorageOptions.getDefaultInstance().getService();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        byte[] b = new byte[256];
        String link = uploadFile("testStorage", b, "sacc-liechtensteger-182811.appspot.com");
        PrintWriter out = resp.getWriter();
        out.println(link);
    }

    public String writeToStorage(String filename, byte[] file){
        // Instantiates a client
        Storage storage = StorageOptions.getDefaultInstance().getService();
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

    public String uploadFile(String fileName, byte[] file, final String bucketName) throws IOException {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("-YYYY-MM-dd-HHmmssSSS");
        DateTime dt = DateTime.now(DateTimeZone.UTC);
        String dtString = dt.toString(dtf);
        fileName =fileName + dtString;
        BlobInfo blobInfo =
                storage.create(
                        BlobInfo
                                .newBuilder(bucketName, fileName)
                                .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))))
                                .build(),
                        file);
        return blobInfo.getMediaLink();
    }
}
