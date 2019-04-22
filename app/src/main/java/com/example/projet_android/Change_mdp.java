package com.example.projet_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Change_mdp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_mdp);

        Button button_ok = findViewById(R.id.btn_ok_change);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView mdp_textView = findViewById(R.id.new_mdp);
                String new_mdp = mdp_textView.getText().toString();

                TextView confirm_mdp_textView = findViewById(R.id.mdp_confirm);
                String confirm_mdp = confirm_mdp_textView.getText().toString();

                TextView old_mdp_textView = findViewById(R.id.old_mdp);
                String old_mdp = old_mdp_textView.getText().toString();


                if(new_mdp.length()<5){
                    Toast.makeText(Change_mdp.this, "Votre nouveau mot de passe doit faire plus de 5 charactères.", Toast.LENGTH_LONG).show();
                }
                else if(new_mdp.equals(old_mdp))
                {
                    Toast.makeText(Change_mdp.this, "Donner un mot de passe non identique à l'ancien !", Toast.LENGTH_LONG).show();
                }
                else if (old_mdp.equals("")){
                    Toast.makeText(Change_mdp.this, "Renseigner votre ancien mot de passe.", Toast.LENGTH_LONG).show();
                }
                else{
                    if(new_mdp.equals(confirm_mdp)){
                        String pseudo = getIntent().getStringExtra("pseudo");
                        new Change_mdpTask().execute(pseudo, old_mdp,confirm_mdp);
                    }else{
                        Toast.makeText(Change_mdp.this, "Mot de passe non identique !", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        Button button_annuler = findViewById(R.id.btn_annule_change);
        button_annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Change_mdp.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    class Change_mdpTask extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... strings) {
            String res = "";
            try {
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost("http://10.0.2.2:8888/login");
                httpPost.addHeader("Content-Type", "application/json");
                String pseudo = strings[0];
                String mdp = strings[1];
                String new_mdp = strings[2];
                StringEntity entity = null;
                entity = new StringEntity("{\"pseudo\":\""+pseudo+"\",\"mdp\":\""+mdp+"\"}");

                httpPost.setEntity(entity);
                HttpResponse httpResponse=  httpClient.execute(httpPost);

                res = EntityUtils.toString(httpResponse.getEntity());
                if(res.toString().equals("\"Erreur\"")){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Mauvaise information de connexion !", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Calendar today = Calendar.getInstance();
                    today.add(Calendar.DATE, 0);
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                    String date = format1.format(today.getTime());
                    String url = "http://10.0.2.2:8888/updateUser/pseudo="+pseudo+"/mdp="+new_mdp+"/date="+date;
                    HttpPut httpPut = new HttpPut(url);
                    httpPut.addHeader("Content-Type", "application/json");
                    httpResponse=  httpClient.execute(httpPut);
                    String res_autre = EntityUtils.toString(httpResponse.getEntity());
                    if(res_autre.toString().equals("\"\nModification non reussie !\n\"")){
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(Change_mdp.this, "Problème serveur !", Toast.LENGTH_LONG).show();
                            }
                        });

                    }else{
                        JSONArray user = new JSONArray(res);
                        String id  =  user.getJSONObject(0).getString("id");
                        System.out.println(id);
                        url = "http://10.0.2.2:8888/firstCo/id="+id;
                        httpPut = new HttpPut(url);
                        httpPut.addHeader("Content-Type", "application/json");
                        httpResponse=  httpClient.execute(httpPut);
                        res_autre = EntityUtils.toString(httpResponse.getEntity());
                        if(res_autre.equals("\"\nModification non reussie !\n\"")){
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(Change_mdp.this, "Problème serveur !", Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            JSONObject person= user.getJSONObject(0);
                            person.put("first_co",1);
                            SharedPreferences settings = getSharedPreferences("CurrentUser", 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("currentUser", person.toString());
                            editor.commit();

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Intent i = new Intent(Change_mdp.this, PageVisualisation.class);
                                    startActivity(i);
                                    Toast.makeText(Change_mdp.this, "Changement de mot de passe reussi !", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return res;
        }
    }
}
