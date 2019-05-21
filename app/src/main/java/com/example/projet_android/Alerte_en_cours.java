package com.example.projet_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONException;
import org.json.JSONObject;

public class Alerte_en_cours extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerte_en_cours);

        String log = getIntent().getStringExtra("log");
        String date = getIntent().getStringExtra("date");

        String idLocal = getIntent().getStringExtra("idLocal");
        System.out.println(getIntent().getStringExtra("id"));

        TextView logT = findViewById(R.id.log_en_cours);
        logT.setText(log);

        TextView dateT = findViewById(R.id.date_en_cours);
        dateT.setText(date);

        Switch switchs = findViewById(R.id.switchVorIAlerte);

        switchs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView iORv = findViewById(R.id.imageOrVideoAlerte);
                Switch switchs = findViewById(R.id.switchVorIAlerte);
                Boolean switchState = switchs.isChecked();
                if(switchState == true){
                    iORv.setText("Vidéo");
                }else{
                    iORv.setText("Image");
                }
            }
        });

        Button ignore = findViewById(R.id.ignorer);
        ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = getSharedPreferences("CurrentUser", 0);
                String myString = settings.getString("currentUser", "");
                JSONObject userJson = null;
                try {
                    userJson = new JSONObject(myString);
                    String pseudo = userJson.getString("pseudo");

                    String id = getIntent().getStringExtra("id");
                    String idLocal = getIntent().getStringExtra("idLocal");
                    String action ="Action ignorée par "+pseudo;
                    new Alerte_en_cours.AlerteEnCoursTask(idLocal).execute(id,idLocal,action);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Button responsable = findViewById(R.id.appel);
        responsable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = getSharedPreferences("CurrentUser", 0);
                String myString = settings.getString("currentUser", "");
                JSONObject userJson = null;
                try {
                    userJson = new JSONObject(myString);
                    String pseudo = userJson.getString("pseudo");
                    String id = getIntent().getStringExtra("id");
                    String idLocal = getIntent().getStringExtra("idLocal");

                    String action ="Appel au responsable par "+pseudo;
                    new Alerte_en_cours.AlerteEnCoursTask(idLocal).execute(id,idLocal,action);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    class AlerteEnCoursTask extends AsyncTask<String,Integer,String> {
        private String idL;

        AlerteEnCoursTask(String _idL){
            idL=_idL;
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = "";
            try{
                String id = strings[0];
                String idLocal = strings[1];
                String action = strings[2];

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost("http://10.0.2.2:8888/endEvent");
                httpPost.addHeader("Content-Type", "application/json");
                StringEntity entity = new StringEntity("{\"id\":\""+id+"\",\"action\":\""+action+"\"}");
                httpPost.setEntity(entity);
                HttpResponse httpResponse=  httpClient.execute(httpPost);

                res = EntityUtils.toString(httpResponse.getEntity());
                if(res.toString().equals("\"\nModification non reussie !\n\"")) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Erreur serveur modification evenement !", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    if(action.contains("ignorée")){
                        Intent i = new Intent(getApplicationContext(), liste_evenement.class);
                        i.putExtra("id",idLocal);
                        startActivity(i);
                    }else{
                        SharedPreferences settings = getSharedPreferences("CurrentUser", 0);
                        String myString = settings.getString("currentUser", "");
                        JSONObject userJson = null;
                        userJson = new JSONObject(myString);
                        String augmente = userJson.getString("augmente");

                        HttpGet httpGet = new HttpGet("http://10.0.2.2:8888/getSurveillant/id="+augmente);
                        httpResponse=  httpClient.execute(httpGet);
                        res = EntityUtils.toString(httpResponse.getEntity());

                        if(res.toString().equals("\"Erreur\"")){
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Erreur get augmente !", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            JSONArray userAugmente = new JSONArray(res);
                            String tel = userAugmente.getJSONObject(0).getString("tel");
                            Uri uri = Uri.parse("tel:"+tel);
                            Intent i = new Intent(getApplicationContext(), liste_evenement.class);
                            i.putExtra("id",idL);
                            startActivity(i);
                            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                            startActivity(intent);
                        }
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            return res;
        }
    }

}
