package servlet;


import org.json.JSONException;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Matthieu on 25/11/2017.
 */


public class MainPage extends HttpServlet {
    private static String message = "toto va a la plage";

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String username = req.getParameter("username");
        String video = req.getParameter("video");
        String taille = req.getParameter("taille");
        uploadToServer();
        message = "Added " + 1 + " tasks to the task queue.";
        req.setAttribute("message", message);
        req.getRequestDispatcher("taskqueues.jsp").forward(req, resp);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.getRequestDispatcher("taskqueues.jsp").forward(req, res);
    }

    public String post() {
        return "toto";
    }


    public void uploadToServer() throws IOException, JSONException {
        try {
            URL url = new URL("http://sacc-liechtensteger-182811.appspot.com/upload");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            String input = "{ \"username\": \"francislebg\",\"video\": \"niketamere\", \"length\": \"40\"";
            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();
            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
