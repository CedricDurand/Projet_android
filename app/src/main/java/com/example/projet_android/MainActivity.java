package com.example.projet_android;


import android.content.Intent;
import android.os.AsyncTask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.util.EntityUtils;
import org.json.JSONArray;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_login = findViewById(R.id.btn_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText pseudoEdit = (EditText) findViewById(R.id.input_pseudo);
                EditText mdpEdit = (EditText) findViewById(R.id.input_mdp);

                String pseudo = pseudoEdit.getText().toString();
                String mdp = mdpEdit.getText().toString();
                new LoginTask().execute(pseudo,mdp);

            }
        });

        Button btn_inscription = findViewById(R.id.btn_inscription);
        btn_inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Inscription.class);
                startActivity(i);
            }
        });

        Button btn_visual = findViewById(R.id.btn_visualisation);
        btn_visual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PageVisualisation.class);
                startActivity(i);
            }
        });
    }


    class LoginTask extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... strings) {
            String res="";
            try
            {

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost("http://192.168.0.12:8888/login");
                httpPost.addHeader("Content-Type", "application/json");
                String pseudo = strings[0];
                String mdp = strings[1];
                StringEntity  entity = new StringEntity("{\"pseudo\":\""+pseudo+"\",\"mdp\":\""+mdp+"\"}");
                httpPost.setEntity(entity);
                HttpResponse httpResponse=  httpClient.execute(httpPost);

                res = EntityUtils.toString(httpResponse.getEntity());
                JSONArray  parser = new JSONArray(res);
                System.out.println(parser.toString());

                   /*
                String strThatDay = "2019/03/27";
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                Date d = null;
                try {
                    d = formatter.parse(strThatDay);//catch exception
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar thatDay = Calendar.getInstance();
                thatDay.setTime(d);
                Calendar today = Calendar.getInstance();
                long diff = today.getTimeInMillis() - thatDay.getTimeInMillis();
                long days = diff / (24 * 60 * 60 * 1000);
                System.out.println("diff "+days);

                if(days>10 && days <= 15 ){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Alerte");
                    builder.setMessage("Votre mot de passe va expirer ! Le changer ? ");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {

                         }});
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if(days > 15){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Alerte");
                    builder.setMessage("Votre a expiré ! Changer le maintenant. ");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }});

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }*/


              }catch(Exception e)  {
                System.out.println(e);
            }
            return res;
        }
    }

}
