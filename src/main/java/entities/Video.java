package entities;

/**
 * Created by Matthieu on 03/11/2017.
 */
public class Video {
    private String owner;
    private Object content;
    private String id;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Video(String owner, Object content, String id){
        this.owner = owner;
        this.content = content;
        this.id = id;


    }
}
