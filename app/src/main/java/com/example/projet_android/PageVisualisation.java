package com.example.projet_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PageVisualisation extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_visualisation);

        String name="";
        SharedPreferences settings = getSharedPreferences("CurrentUser", 0);
        String myString = settings.getString("currentUser", "");
        JSONObject userJson = null;
        try {
            userJson = new JSONObject(myString);
            name = userJson.getString("pseudo");
            String idUtilisateur = userJson.getString("id");
            new PageVisualisation.PageVisuTask(this).execute(idUtilisateur);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView hello = findViewById(R.id.hello);
        hello.setText("Bienvenue dans RAPACE "+name+" !");
    }

    public boolean onCreateOptionsMenu(Menu menu){
        String admin="";
        try{
            SharedPreferences settings = getSharedPreferences("CurrentUser", 0);
            String myString = settings.getString("currentUser", "");
            JSONObject userJson = new JSONObject(myString);
            admin = userJson.getString("admin");
        }catch (Exception e){
            e.printStackTrace();
        }

        getMenuInflater().inflate(R.menu.visual_menu, menu);

        if(admin.equals("augmente")){
            menu.add(R.menu.visual_menu,1,Menu.NONE,"Ajouter un utilisateur");
        }

        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deco:
                Intent main = new Intent(PageVisualisation.this, MainActivity.class);
                SharedPreferences settings = getSharedPreferences("CurrentUser", 0);
                settings.edit().clear().commit();
                startActivity(main);
                return true;
            case R.id.all_locaux:
                Intent i = new Intent(PageVisualisation.this, EnsembleLocaux.class);
                startActivity(i);
                return true;
            case R.id.type_menu:
                Intent ii = new Intent(PageVisualisation.this, CategorieLocal.class);
                startActivity(ii);
                return true;
            case R.id.visual_page:
                String text = " Vous êtes déjà sur la page de profil";
                Toast toast = Toast.makeText(getBaseContext(),text,Toast.LENGTH_SHORT);
                toast.show();
                return true;
            case 1:
                Intent i3 = new Intent(PageVisualisation.this, Inscription.class);
                startActivity(i3);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        EventAdapter.ViewHolder holder = (EventAdapter.ViewHolder) v.getTag();

        String action = holder.instance.getAction();
        if(action.equals("")){ // alerte en cours
            Intent i = new Intent(getApplicationContext(), Alerte_en_cours.class);
            i.putExtra("idLocal",holder.instance.getId_local());
            i.putExtra("id",Integer.toString(holder.instance.getId()));
            i.putExtra("log",holder.instance.getLog());
            i.putExtra("date",holder.instance.getDate());
            startActivity(i);

        }else{ //alerte pas en cours
            Intent i = new Intent(getApplicationContext(), Alerte_fini.class);
            i.putExtra("id",holder.instance.getId_local());
            i.putExtra("log",holder.instance.getLog());
            i.putExtra("date",holder.instance.getDate());
            i.putExtra("action",action);
            startActivity(i);
        }
    }

    class PageVisuTask extends AsyncTask<String,Integer,String> {
        public PageVisualisation activity;

        public PageVisuTask(PageVisualisation a){
            activity =a ;
        }

        @Override
        protected String doInBackground(String... strings) {
            String res="";
            try {
                String id = strings[0];
                HttpClient httpClient = new DefaultHttpClient();
                String url = "http://10.0.2.2:8888/event/idUser="+id;
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
                    JSONArray alerte = new JSONArray(res);
                    String date, log, action;
                    ArrayList<Event> l = new ArrayList<>();
                    for (int i=0; i<alerte.length();i++ ) {
                        date = alerte.getJSONObject(i).getString("date");
                        log = alerte.getJSONObject(i).getString("log");
                        action = alerte.getJSONObject(i).getString("action");
                        String idEvent= alerte.getJSONObject(i).getString("id");
                        String idLocal = alerte.getJSONObject(i).getString("id_local");
                        l.add(new Event(Integer.parseInt(idLocal),Integer.parseInt(idEvent), log, date, action));
                    }

                    EventAdapter<Event> adapter = new EventAdapter<>(activity,R.layout.activity_liste_evenement,R.id.VisualDate_evenement,l,activity);
                    ListView listLocal = findViewById(R.id.VisualEnsEvement);
                    listLocal.setAdapter(adapter);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return res;
        }
    }
}
