package servlet;

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
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.getRequestDispatcher("taskqueues.jsp").forward(req,resp);
        /*resp.setContentType( "text/html" );
        PrintWriter out = resp.getWriter();
        out.println( "<HTML>" );
        out.println( "<HEAD>");
        out.println( "<TITLE>Page generee par une servlet</TITLE>" );
        out.println( "</HEAD>" );
        out.println( "<BODY>" );
        out.println( "<H1>Bonjour</H1>" );
        out.println( "</BODY>" );
        out.println( "</HTML>" );
        out.close();*/
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        /*res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println("<html><head></head><body>");
        out.println("Hello world !!!");
        out.println("</body></html>");*/
        req.getRequestDispatcher("taskqueues.jsp").forward(req,res);
    }
}
