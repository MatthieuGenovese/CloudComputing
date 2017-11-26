package servlet;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Matthieu on 25/11/2017.
 */


public class MainPage extends HttpServlet {
    private static String message = "";
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String username = req.getParameter("username");
        String video = req.getParameter("video");
        String taille = req.getParameter("taille");

        message = "Added " + 1 + " tasks to the task queue.";
        req.setAttribute("message", message);
        req.getRequestDispatcher("taskqueues.jsp").forward(req, resp);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.getRequestDispatcher("taskqueues.jsp").forward(req,res);
    }
}
