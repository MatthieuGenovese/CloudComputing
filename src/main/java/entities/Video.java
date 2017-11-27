package entities;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Matthieu on 03/11/2017.
 */
public class Video {
    private String name;
    private String nbPart;
    private String length;

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


    public String getNbPart() {
        return nbPart;
    }

    public void setNbPart(String nbPart) {
        this.nbPart = nbPart;
    }

    public Video(String name, String length){
        this.length = length;
        this.name = name;
        this.nbPart = "N/A";
    }
}
