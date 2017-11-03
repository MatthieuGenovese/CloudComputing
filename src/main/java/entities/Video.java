package entities;

/**
 * Created by Matthieu on 03/11/2017.
 */
public class Video {
    private String owner;
    private int length;
    private String id;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int content) {
        this.length = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Video(String owner, int length, String id){
        this.owner = owner;
        this.length = length;
        this.id = id;


    }
}
