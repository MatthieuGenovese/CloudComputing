package entities;

/**
 * Created by Matthieu on 03/11/2017.
 */
public class Video {
    private String owner;
    private String name;
    private String length;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String content) {
        this.length = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", length='" + length + '\'';
    }

    public Video(String owner, String name, String length){
        this.owner = owner;
        this.length = length;
        this.name = name;

    }
}
