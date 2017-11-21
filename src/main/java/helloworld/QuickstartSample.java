package helloworld;

/**
 * Created by Michael on 03/11/2017.
 */
// Imports the Google Cloud client library
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.*;
import com.google.cloud.storage.Storage;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class QuickstartSample extends HttpServlet{

    private static Storage storage = null;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        byte[] b = new byte[256];
        String link = uploadFile("testStorage", b, "sacc-liechtensteger-182811.appspot.com");
        PrintWriter out = resp.getWriter();
        out.println(link);
    }

    public void writeToStorage(String filename, byte[] file){
        // Instantiates a client
        Storage storage = StorageOptions.getDefaultInstance().getService();
        // The name of the bucket
        String bucketName = "sacc-liechtensteger-182811.appspot.com";  // "my-new-bucket";

        Bucket bucket = storage.get(bucketName);

        bucket.create(filename, file, "text/plain");
    }

    public String uploadFile(String fileName, byte[] file, final String bucketName) throws IOException {
        storage = StorageOptions.getDefaultInstance().getService();
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
