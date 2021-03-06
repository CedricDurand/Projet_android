package com.example.projet_android;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EventAdapter<T> extends ArrayAdapter {
    LayoutInflater infl;
    private int layout;
    private View.OnClickListener itemClick;

    public EventAdapter(Activity activit, int lay, int id, ArrayList<Event> items, View.OnClickListener onclicklistener){
        super(activit,lay,id,(ArrayList)items);
        this.itemClick=onclicklistener;
        this.layout=lay;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        EventAdapter.ViewHolder holder = null;

        if (convertView == null) {

            holder = new EventAdapter.ViewHolder();

            infl = LayoutInflater.from(getContext());
            convertView = infl.inflate(this.layout, null);

            holder.date = (TextView) convertView.findViewById(R.id.date_evenement);
            holder.log = (TextView) convertView.findViewById(R.id.log_evenement);
            holder.action = (TextView) convertView.findViewById(R.id.action);

            convertView.setTag(holder);
        }
        else{
            holder = (EventAdapter.ViewHolder)convertView.getTag();
        }
        Event it =(Event) getItem(position);
        if (it != null) {
            holder.instance = it;
            holder.date.setText("Date : "+it.getDate());
            holder.log.setText("Log : "+it.getLog());
            if(it.getAction().equals("")){
                holder.action.setText("Action : en cours !!");
            }else{
                holder.action.setText("Action : "+it.getAction());
            }
        }
        convertView.setOnClickListener(this.itemClick);
        return convertView;

    }

    static class ViewHolder{
        public TextView date;
        public TextView log;
        public TextView action;
        public Event instance;
    }
}
