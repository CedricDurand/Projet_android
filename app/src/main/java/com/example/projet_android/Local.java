package com.example.projet_android;

public class Local {
    private String adresse,categorie;
    private int id;

    public Local(String adr, String cat, int id){
        this.adresse=adr;
        this.categorie=cat;
        this.id = id;
    }
    public Local(String adr, String cat){
        this.adresse=adr;
        this.categorie=cat;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
