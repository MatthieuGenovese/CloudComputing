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
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.getRequestDispatcher("mainpage.jsp").forward(req, res);
    }



}
