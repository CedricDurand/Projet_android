package com.example.projet_android;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoriqueAdapter<T> extends ArrayAdapter {
    LayoutInflater infl;
    private int layout;

    public HistoriqueAdapter(Activity activit, int lay, int id, ArrayList<Historique> items){
        super(activit,lay,id,(ArrayList)items);
        this.layout=lay;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();

            infl = LayoutInflater.from(getContext());
            convertView = infl.inflate(this.layout, null);

            holder.date = (TextView) convertView.findViewById(R.id.date_histo);
            holder.log = (TextView) convertView.findViewById(R.id.log_histo);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }
        Historique it =(Historique) getItem(position);
        if (it != null) {
            holder.instance = it;
            holder.date.setText("Date : "+it.getDate());
            holder.log.setText("Log : "+it.getLog());
        }
        return convertView;

    }

    static class ViewHolder{
        public TextView date;
        public TextView log;
        public Historique instance;
    }
}
