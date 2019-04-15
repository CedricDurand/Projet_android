package com.example.projet_android;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LocalAdapter<T> extends ArrayAdapter {

    LayoutInflater infl;
    private View.OnClickListener itemClick;
    private int layout;

    public LocalAdapter(Activity activit, int lay, int id, ArrayList<Local> items, View.OnClickListener onclicklistener){
        super(activit,lay,id,(ArrayList)items);
        this.itemClick=onclicklistener;
        this.layout=lay;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();

            infl = LayoutInflater.from(getContext());
            convertView = infl.inflate(this.layout, null);

            holder.adresse = (TextView) convertView.findViewById(R.id.rowAdresse);
            holder.categorie = (TextView) convertView.findViewById(R.id.rowCategorie);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }
        Local it =(Local) getItem(position);
        if (it != null) {
            holder.instance = it;
            holder.adresse.setText("Adresse : "+it.getAdresse());
            holder.categorie.setText("Categorie : "+it.getCategorie());
        }
        convertView.setOnClickListener(this.itemClick);
        return convertView;

    }
    static class ViewHolder{
        public TextView adresse;
        public TextView categorie;
        public Local instance;
    }
}
