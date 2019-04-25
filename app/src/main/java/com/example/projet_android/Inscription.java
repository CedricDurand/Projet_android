package com.example.projet_android;

import android.content.Intent;
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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Inscription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        Button button_ok = findViewById(R.id.btn_ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView mdp_textView = findViewById(R.id.mdp);
                String mdp = mdp_textView.getText().toString();

                TextView confirm_mdp_textView = findViewById(R.id.confirm_mdp);
                String confirm_mdp = confirm_mdp_textView.getText().toString();

                TextView pseudo = findViewById(R.id.pseudo);
                String string_pseudo = pseudo.getText().toString();

                TextView tel_textview = findViewById(R.id.tel);
                String tel = tel_textview.getText().toString();

                if(string_pseudo.equals("") || mdp.equals("") || tel.equals("")) {
                    Toast.makeText(Inscription.this, "Remplir tout les champs!", Toast.LENGTH_LONG).show();
                }
                else{
                    if(mdp.equals(confirm_mdp)){
                        new InscriptionTask().execute(string_pseudo, confirm_mdp, tel);
                    }else{
                        Toast.makeText(Inscription.this, "Mot de passe non identique !", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        Button button_retour = findViewById(R.id.btn_annule);
        button_retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Inscription.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    class InscriptionTask extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... strings) {
            String res = "";
            try{
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost("http://10.0.2.2:8888/addUser");
                httpPost.addHeader("Content-Type", "application/json");
                String pseudo = strings[0];
                String mdp = strings[1];
                String tel = strings[2];

                Calendar today = Calendar.getInstance();
                today.add(Calendar.DATE, 0);
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                String date = format1.format(today.getTime());
                StringEntity  entity = new StringEntity("{\"pseudo\":\""+pseudo+"\",\"mdp\":\""+mdp+"\",\"tel\":\""+tel+"\",\"date\":\""+date+"\",\"admin\":\"normal\",\"first_co\":0,\"alerte_mode\":0}");
                httpPost.setEntity(entity);
                HttpResponse httpResponse=  httpClient.execute(httpPost);
                res = EntityUtils.toString(httpResponse.getEntity());
                if(res.toString().equals("\"Inscription reussie\"")){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Inscription reussi !", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Inscription.this, MainActivity.class);
                            startActivity(i);
                        }
                    });

                }else {
                    System.out.println(res.toString());
                    if(res.toString().equals("\"Membre existe deja !\"")) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Pseudo déjà utilisé !", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Erreur server !", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

            }catch (Exception e){
                System.out.println(e);
            }

            return res;
        }
    }
}
