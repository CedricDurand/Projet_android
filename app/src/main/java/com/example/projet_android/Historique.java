package com.example.projet_android;

public class Historique {
    private int id, id_local;
    private String log, date;

    Historique(int _id, int _id_local, String _log, String _date){
        id=_id;
        id_local= _id_local;
        log=_log;
        date=_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_local() {
        return id_local;
    }

    public void setId_local(int id_local) {
        this.id_local = id_local;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
