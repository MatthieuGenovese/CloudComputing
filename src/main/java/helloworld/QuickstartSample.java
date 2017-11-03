package helloworld;

/**
 * Created by Michael on 03/11/2017.
 */
// Imports the Google Cloud client library
import com.google.cloud.datastore.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.gson.*;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class QuickstartSample extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        writeToStorage("okok.txt", "yeeeep!".getBytes());
    }

    public void writeToStorage(String filename, byte[] file){
        // Instantiates a client
        Storage storage = StorageOptions.getDefaultInstance().getService();
        // The name of the bucket
        String bucketName = "sacc-liechtensteger-182811.appspot.com";  // "my-new-bucket";

        Bucket bucket = storage.get(bucketName);

//        InputStream content = new ByteArrayInputStream(jb.toString().getBytes());
        com.google.cloud.storage.Blob blob = bucket.create(filename, file, "text/plain");
    }
}