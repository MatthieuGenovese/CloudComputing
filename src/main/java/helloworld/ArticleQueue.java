package helloworld;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Matthieu on 13/10/2017.
 */
public class ArticleQueue extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();
        Article a1 = new Article("a1", 10, 125);
        Article a2 = new Article("a2", 154, 45);
        Article a3 = new Article("a3", 1545, 48);
        ArrayList<Article> l = new ArrayList<>();
        l.add(a1);
        l.add(a2);
        l.add(a3);
        String res = "";
        for(Article a : l){
            res += gson.toJson(a);
        }
        PrintWriter out = resp.getWriter();
        Queue queue = QueueFactory.getDefaultQueue();
        queue.add(TaskOptions.Builder.withUrl("/worker").param("key", req.getQueryString()));
        out.println("blblblblblbllbbllb");
        out.println(req.getQueryString());
    }
}
