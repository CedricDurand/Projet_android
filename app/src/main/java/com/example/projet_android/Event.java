package com.example.projet_android;

public class Event {
    private int id, id_local, id_user;
    private String log, date;

    Event(int _id, int _id_local, int _id_user, String _log, String _date){
        id=_id;
        id_local=_id_local;
        id_user=_id_user;
        log=_log;
        date=_date;
    }

    Event(int _id_local, String _log, String _date){
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

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
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
