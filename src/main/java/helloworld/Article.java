package helloworld;
/**
 * Created by Matthieu on 06/10/2017.
 */
public class Article {

    private String nom;
    private int prix;
    private int quantite;
    public Article(){

    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public Article (String nom, int prix, int quantite){
        this.prix = prix;
        this.nom = nom;
        this.quantite = quantite;
    }
}
