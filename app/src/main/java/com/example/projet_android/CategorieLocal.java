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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

public class CategorieLocal extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Local> l = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorie_local);


        ListView listLocal = (ListView) findViewById(R.id.ensLocalCat);
        LocalAdapter<Local> adapterList = new LocalAdapter<>(this,R.layout.activity_categorie_triee,R.id.rowAdresseCat,l,this);
        listLocal.setAdapter(adapterList);


        Spinner spinner = (Spinner) findViewById(R.id.spinner_local);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_choice, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button find = findViewById(R.id.spinner_btn);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CategorieLocal.CategorieLocalTask(CategorieLocal.this).execute();
            }
        });

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
                Intent main = new Intent(CategorieLocal.this, MainActivity.class);
                SharedPreferences settings = getSharedPreferences("CurrentUser", 0);
                settings.edit().clear().commit();
                startActivity(main);
                return true;
            case R.id.all_locaux:
                Intent ii = new Intent(CategorieLocal.this, EnsembleLocaux.class);
                startActivity(ii);
                return true;
            case R.id.visual_page:
                Intent i = new Intent(CategorieLocal.this, PageVisualisation.class);
                startActivity(i);
                return true;
            case R.id.type_menu:
                //voir pour un retour a l'activité déjà lancé
                String text = " Vous êtes déjà sur la page des locaux catégorisé";
                Toast toast = Toast.makeText(getBaseContext(),text,Toast.LENGTH_SHORT);
                toast.show();
                return true;
            case 1:
                Intent i3 = new Intent(CategorieLocal.this, Inscription.class);
                startActivity(i3);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        LocalAdapter.ViewHolder holder = (LocalAdapter.ViewHolder) v.getTag();

        Intent i = new Intent(getApplicationContext(), local_activity.class);
        i.putExtra("id",Integer.toString(holder.instance.getId()));
        i.putExtra("cat",holder.instance.getCategorie());
        i.putExtra("adr",holder.instance.getAdresse());
        startActivity(i);
    }

    class CategorieLocalTask extends AsyncTask<String,Integer,String> {
        private CategorieLocal activity;

        CategorieLocalTask(CategorieLocal a){
            activity=a;
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = "";
            try{

                Spinner spinner_local = findViewById(R.id.spinner_local);
                String categorie = spinner_local.getSelectedItem().toString();

                SharedPreferences settings = getSharedPreferences("CurrentUser", 0);
                String myString = settings.getString("currentUser", "");
                JSONObject userJson = new JSONObject(myString);
                String id_augmente = userJson.getString("augmente");

                HttpClient httpClient = new DefaultHttpClient();
                String url  = "http://10.0.2.2:8888/local/idAug="+id_augmente+"/type="+categorie;
                HttpGet httpGet = new HttpGet(url.replace(" ","%20"));
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
                    l.clear();
                    for (int i=0; i<locaux.length();i++ ) {
                        cat = locaux.getJSONObject(i).getString("categorie");
                        addr= locaux.getJSONObject(i).getString("adresse");
                        idLocal = Integer.parseInt(locaux.getJSONObject(i).getString("id"));
                        l.add(new Local(addr,cat, idLocal));
                    }

                    runOnUiThread(new Runnable() {
                        public void run() {
                            ListView listLocal = (ListView) findViewById(R.id.ensLocalCat);
                            LocalAdapter<Local> adapter = new LocalAdapter<>(activity,R.layout.activity_categorie_triee,R.id.rowAdresseCat,l,activity);
                            listLocal.setAdapter(adapter);

                            if(l.isEmpty())
                                Toast.makeText(getApplicationContext(), "Aucun local trouvé !", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return res;
        }
    }
}
