package com.example.projet_android;

public class Surveillant {
    private int id, id_user, id_local;
    private String tel;

    Surveillant(int _id, int _id_user, int _id_local, String _tel){
        id=_id;
        id_user = _id_user;
        id_local=_id_local;
        tel=_tel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_local() {
        return id_local;
    }

    public void setId_local(int id_local) {
        this.id_local = id_local;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
