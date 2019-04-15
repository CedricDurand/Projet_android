package com.example.projet_android;

public class User {
    private int id, first_co, alerte_mode;
    private String pseudo, mdp, date, admin;

    User(int _id, String _pseudo, String _mdp, String _date, String _admin, int _first_co,int _alerte_mode){
        id=_id;
        pseudo=_pseudo;
        mdp=_mdp;
        date=_date;
        admin=_admin;
        first_co=_first_co;
        alerte_mode=_alerte_mode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFirst_co() {
        return first_co;
    }

    public void setFirst_co(int first_co) {
        this.first_co = first_co;
    }

    public int getAlerte_mode() {
        return alerte_mode;
    }

    public void setAlerte_mode(int alerte_mode) {
        this.alerte_mode = alerte_mode;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }
}
