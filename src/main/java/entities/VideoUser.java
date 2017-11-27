package entities;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Created by Matthieu on 27/11/2017.
 */
public class VideoUser extends Video {
    private String status;
    private DateTime submitTime;
    private String downloadLink;
    private String owner;

    public VideoUser(String owner, String name, String length){
        super(name,length);
        this.owner = owner;
        this.status = "waiting";
        this.downloadLink = "N/A";
        this.submitTime = DateTime.now(DateTimeZone.UTC);

    }


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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    @Override
    public String toString() {
        return "Titre : " + super.getName() + "\nDurée : " + super.getLength() + "\nStatus : " + status + "\nDownload link : " + downloadLink+"\n";
    }
}
