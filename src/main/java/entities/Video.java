package entities;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Matthieu on 03/11/2017.
 */
public class Video {
    private String owner;
    private String name;
    private String length;
    private String status;
    private DateTime submitTime;
    private String downloadLink;
    //DateTimeFormatter dtf = DateTimeFormat.forPattern("-YYYY-MM-dd-HHmmssSSS");

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DateTime getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(DateTime submitTime) {
        this.submitTime = submitTime;
    }
    //DateTime dt = DateTime.now(DateTimeZone.UTC);

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
        return "Titre : " + name + "; Durée : " + length + "; Status : " + status + "; Download link : " + downloadLink;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public Video(String owner, String name, String length){
        this.owner = owner;
        this.length = length;
        this.name = name;
        this.status = "pending";
        this.downloadLink = "N/A";
        this.submitTime = DateTime.now(DateTimeZone.UTC);
    }
}
