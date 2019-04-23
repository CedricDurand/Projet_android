package com.example.projet_android;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class liste_historique extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_historique);
        String id = getIntent().getStringExtra("id");
        new liste_historique.HistoriqueTask(this).execute(id);
    }


    class HistoriqueTask extends AsyncTask<String,Integer,String> {
        public liste_historique activity;

        public HistoriqueTask(liste_historique a){
            activity =a ;
        }

        @Override
        protected String doInBackground(String... strings) {
            String res="";
            try {
                String id = strings[0];
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://10.0.2.2:8888/historique/idLocal="+id);
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
                    String date, log;
                    ArrayList<Historique> l = new ArrayList<>();
                    for (int i=0; i<user.length();i++ ) {
                        date = user.getJSONObject(i).getString("date");
                        log = user.getJSONObject(i).getString("log");
                        l.add(new Historique(Integer.parseInt(id), log, date));
                    }
                    System.out.println(l.toString());
                    HistoriqueAdapter<Historique> adapter = new HistoriqueAdapter<>(activity,R.layout.activity_liste_historique,R.id.date_histo,l);
                    ListView listLocal = findViewById(R.id.ensHistorique);
                    listLocal.setAdapter(adapter);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return res;
        }
    }
}
