package com.example.projet_android;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class local_activity extends AppCompatActivity {

    String id_local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_activity);

        String id = getIntent().getStringExtra("id");
        String cat = getIntent().getStringExtra("cat");
        String adr = getIntent().getStringExtra("adr");
        id_local=id;

        new local_activity.LocalTask().execute();

        TextView categorie = findViewById(R.id.cat);
        categorie.setText(cat);
        TextView adresse = findViewById(R.id.adresse);
        adresse.setText(adr);

        Switch switchs = findViewById(R.id.switchVorI);
        switchs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView iORv = findViewById(R.id.imageOrVideo);
                Switch switchs = findViewById(R.id.switchVorI);
                Boolean switchState = switchs.isChecked();
                if(switchState == true){
                    iORv.setText("Vidéo");
                }else{
                    iORv.setText("Image");
                }
            }
        });

        Button simuler = findViewById(R.id.simuler);
        simuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new local_activity.LocalSimulationTask().execute();
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

        getMenuInflater().inflate(R.menu.menu_local, menu);

        if(admin.equals("augmente")){
            menu.add(R.menu.menu_local,1,Menu.NONE,"Historique Augmente");
        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.historique:
                Intent i = new Intent(local_activity.this, liste_historique.class);
                i.putExtra("id",id_local);
                startActivity(i);
                return true;
            case R.id.evenement:
                Intent i2 = new Intent(local_activity.this, liste_evenement.class);
                i2.putExtra("id",id_local);
                startActivity(i2);
                return true;
            case 1:
                Intent i3 = new Intent(local_activity.this, liste_historique.class);
                i3.putExtra("id",id_local);
                i3.putExtra("admin","admin");
                startActivity(i3);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Pour les informations du local + savoir qui a regardé le local */
    class LocalTask extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... strings) {
            String res;
            try {
                // date
                Calendar today = Calendar.getInstance();
                today.add(Calendar.DATE, 0);
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                String date = format1.format(today.getTime());
                //nom de l'utilisateur
                SharedPreferences settings = getSharedPreferences("CurrentUser", 0);
                String myString = settings.getString("currentUser", "");
                JSONObject userJson = new JSONObject(myString);
                String pseudo = userJson.getString("pseudo");
                String log = pseudo+" a visionné ce local.";
                // on envoit la requête
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost("http://10.0.2.2:8888/addHistoriqueAugmente");
                httpPost.addHeader("Content-Type", "application/json");
                StringEntity entity = new StringEntity("{\"id_local\":\""+id_local+"\",\"log\":\""+log+"\",\"date\":\""+date+"\"}");
                httpPost.setEntity(entity);
                HttpResponse httpResponse=  httpClient.execute(httpPost);
                res = EntityUtils.toString(httpResponse.getEntity());

                if(!res.toString().equals("\"Ajout reussie\"")){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Problème serveur pour l'historique !", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }

    class LocalSimulationTask extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... strings) {
            String res;
            try {
                SharedPreferences settings = getSharedPreferences("CurrentUser", 0);
                String myString = settings.getString("currentUser", "");
                JSONObject userJson = null;
                userJson = new JSONObject(myString);
                String augmente = userJson.getString("augmente");

                HttpClient httpClient=new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://10.0.2.2:8888/getTelSurveillants/id="+augmente);
                HttpResponse httpResponse=  httpClient.execute(httpGet);
                res = EntityUtils.toString(httpResponse.getEntity());

                JSONArray allResponsable = new JSONArray(res);
                String msg = "RAPACE APPLICATION ALERTE !\nAlerte sur le local à l'adresse "+getIntent().getStringExtra("adr")+" émis par "+userJson.getString("pseudo")+" !!\n Connectez-vous !";

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_CALENDAR)
                        != PackageManager.PERMISSION_GRANTED) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "L'alerte est simulés mais on ne peut pas envoyer de SMS. Android n'a pas la permission.", Toast.LENGTH_LONG).show();
                        }
                    });

                }else{
                    for(int i= 0; i<allResponsable.length();i++){
                        SmsManager.getDefault().sendTextMessage(allResponsable.getJSONObject(0).getString("tel"), null, msg, null, null);
                    }
                }

                Calendar today = Calendar.getInstance();
                today.add(Calendar.DATE, 0);
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

                String date = format1.format(today.getTime());
                String id_local = getIntent().getStringExtra("id");
                String id_user = userJson.getString("id");
                String log = "Alerte émis par "+userJson.getString("pseudo")+".";

                HttpPost httpPost=new HttpPost("http://10.0.2.2:8888/addEvent");
                httpPost.addHeader("Content-Type", "application/json");
                StringEntity  entity = new StringEntity("{\"id_local\":\""+id_local+"\",\"id_user\":\""+id_user+"\",\"log\":\""+log+"\",\"date\":\""+date+"\"}");
                httpPost.setEntity(entity);
                httpResponse=  httpClient.execute(httpPost);
                res = EntityUtils.toString(httpResponse.getEntity());

                if(!res.toString().equals("\"Ajout reussie\"")){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Problème serveur !", Toast.LENGTH_LONG).show();
                        }
                    });
                }


            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }
}
