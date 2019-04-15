package com.example.projet_android;

public class Local {
    private int id;
    private String adresse, categorie;

    Local(int _id, String _adresse, String _categorie){
        id=_id;
        adresse=_adresse;
        categorie=_categorie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
}
