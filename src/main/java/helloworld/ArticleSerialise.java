package helloworld;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Matthieu on 06/10/2017.
 */
public class ArticleSerialise extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();
        Article a1 = new Article("a1", 10, 125);
        ArrayList<Article> liste = new ArrayList<>();
        Storage.createArticle("a1", 10, 125);
        String s = "";
        for(Article a :liste){
            try {
                Thread.sleep(500);
                s += gson.toJson(a);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        PrintWriter out = resp.getWriter();
        out.println(s);
    }
}
