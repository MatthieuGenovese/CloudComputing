package entities;

/**
 * Created by Matthieu on 03/11/2017.
 */
public class Video {
    private String owner;
    private String name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Video(String owner, String name, int length, String id){
        this.owner = owner;
        this.length = length;
        this.id = id;
        this.name = name;



    }
}
