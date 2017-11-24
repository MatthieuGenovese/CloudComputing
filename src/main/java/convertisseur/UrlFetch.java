package convertisseur;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

/**
 * Created by Matthieu on 24/11/2017.
 */
public class UrlFetch {

    public void doPostRequest(String address, JSONObject json) throws IOException {
        URL url = new URL(address);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setInstanceFollowRedirects(false);
        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
        writer.write(URLEncoder.encode(json.toString(), "UTF-8"));
        writer.close();
        conn.getResponseCode();
    }
}
