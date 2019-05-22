package com.example.projet_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class EnsembleLocaux extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ensemble_locaux);
        new EnsembleLocaux.EnsembleLocauxTask(this).execute();
    }

    @Override
    public void onClick(View vue) {
        LocalAdapter.ViewHolder holder = (LocalAdapter.ViewHolder) vue.getTag();
        //this.getApp().getInventoryItems().add(new Item(holder.instance.getName(),holder.instance.getSellIn(),holder.instance.getQuality()));

        Intent i = new Intent(getApplicationContext(), local_activity.class);
        i.putExtra("id",Integer.toString(holder.instance.getId()));
        i.putExtra("cat",holder.instance.getCategorie());
        i.putExtra("adr",holder.instance.getAdresse());
        startActivity(i);
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
                Intent main = new Intent(EnsembleLocaux.this, MainActivity.class);
                SharedPreferences settings = getSharedPreferences("CurrentUser", 0);
                settings.edit().clear().commit();
                startActivity(main);
                return true;
            case R.id.all_locaux:
                String text = " Vous êtes déjà sur la page de l'ensemble des locaux";
                Toast toast = Toast.makeText(getBaseContext(),text,Toast.LENGTH_SHORT);
                toast.show();
                return true;
            case R.id.visual_page:
                Intent i = new Intent(EnsembleLocaux.this, PageVisualisation.class);
                startActivity(i);
                return true;
            case R.id.type_menu:
                Intent ii = new Intent(EnsembleLocaux.this, CategorieLocal.class);
                startActivity(ii);
                return true;
            case 1:
                Intent i3 = new Intent(EnsembleLocaux.this, Inscription.class);
                startActivity(i3);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class EnsembleLocauxTask extends AsyncTask<String,Integer,String> {
        private EnsembleLocaux activity;

        EnsembleLocauxTask(EnsembleLocaux a){
            activity=a;
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = "";
            try{
                SharedPreferences settings = getSharedPreferences("CurrentUser", 0);
                String myString = settings.getString("currentUser", "");
                JSONObject userJson = new JSONObject(myString);
                String id_augmente = userJson.getString("augmente");

                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://10.0.2.2:8888/local/idAug="+id_augmente);
                HttpResponse httpResponse=  httpClient.execute(httpGet);
                res = EntityUtils.toString(httpResponse.getEntity());
                if(res.equals("")){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Erreur serveur !", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    JSONArray locaux = new JSONArray(res);
                    String cat, addr;
                    int idLocal;
                    ListView listLocal = (ListView) findViewById(R.id.ensLocal);
                    ArrayList<Local> l = new ArrayList<>();
                    for (int i=0; i<locaux.length();i++ ) {
                        cat = locaux.getJSONObject(i).getString("categorie");
                        addr= locaux.getJSONObject(i).getString("adresse");
                        idLocal = Integer.parseInt(locaux.getJSONObject(i).getString("id"));
                        l.add(new Local(addr,cat, idLocal));
                    }
                    LocalAdapter<Local> adapter = new LocalAdapter<>(activity,R.layout.activity_ensemble_locaux,R.id.rowAdresse,l,activity);
                    listLocal.setAdapter(adapter);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return res;
        }
    }
}
