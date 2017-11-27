package entities;

/**
 * Created by Matthieu on 27/11/2017.
 */
public class QueueStatus {
    private long id;
    private long nbGold;
    private long nbSilver;

    public QueueStatus(long nbGold, long nbSilver, long id){
        this.nbGold = nbGold;
        this.nbSilver = nbSilver;
        this.id = id;
    }

    public long getNbGold() {
        return nbGold;
    }

    public void setNbGold(long nbGold) {
        this.nbGold = nbGold;
    }

    public long getNbSilver() {
        return nbSilver;
    }

    public void setNbSilver(long nbSilver) {
        this.nbSilver = nbSilver;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
