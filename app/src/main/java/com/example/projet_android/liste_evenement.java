package com.example.projet_android;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.util.ArrayList;

public class liste_evenement extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_evenement);
        String id = getIntent().getStringExtra("id");
        new liste_evenement.EvenementTask(this).execute(id);
    }

    @Override
    public void onClick(View vue) {
        EventAdapter.ViewHolder holder = (EventAdapter.ViewHolder) vue.getTag();

        String action = holder.instance.getAction();
        if(action.equals("")){ // alerte en cours
            Intent i = new Intent(getApplicationContext(), Alerte_en_cours.class);
            String idLocal = getIntent().getStringExtra("id");
            i.putExtra("idLocal",idLocal);
            i.putExtra("id",Integer.toString(holder.instance.getId()));
            i.putExtra("log",holder.instance.getLog());
            i.putExtra("date",holder.instance.getDate());
            startActivity(i);

        }else{ //alerte pas en cours
            Intent i = new Intent(getApplicationContext(), Alerte_fini.class);
            i.putExtra("id",Integer.toString(holder.instance.getId()));
            i.putExtra("log",holder.instance.getLog());
            i.putExtra("date",holder.instance.getDate());
            i.putExtra("action",action);
            startActivity(i);
        }


    }

    class EvenementTask extends AsyncTask<String,Integer,String> {
        public liste_evenement activity;

        public EvenementTask(liste_evenement a){
            activity =a ;
        }

        @Override
        protected String doInBackground(String... strings) {
            String res="";
            try {
                String id = strings[0];
                HttpClient httpClient = new DefaultHttpClient();
                String url = "http://10.0.2.2:8888/event/idLocal="+id;
                HttpGet httpGet = new HttpGet(url);
                httpGet.addHeader("Content-Type", "application/json");
                HttpResponse httpResponse=  httpClient.execute(httpGet);
                res = EntityUtils.toString(httpResponse.getEntity());
                if(res.equals("")){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Erreur serveur !", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    JSONArray user = new JSONArray(res);
                    String date, log, action;
                    ArrayList<Event> l = new ArrayList<>();
                    for (int i=0; i<user.length();i++ ) {
                        date = user.getJSONObject(i).getString("date");
                        log = user.getJSONObject(i).getString("log");
                        action = user.getJSONObject(i).getString("action");
                        String idEvent= user.getJSONObject(i).getString("id");
                        l.add(new Event(Integer.parseInt(id),Integer.parseInt(idEvent), log, date, action));
                    }
                    System.out.println(l.toString());
                    EventAdapter<Event> adapter = new EventAdapter<>(activity,R.layout.activity_liste_evenement,R.id.date_evenement,l,activity);
                    ListView listLocal = findViewById(R.id.ensEvement);
                    listLocal.setAdapter(adapter);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return res;
        }
    }
}
