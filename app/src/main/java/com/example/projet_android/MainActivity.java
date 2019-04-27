package com.example.projet_android;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


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
                if(pseudo.equals("") || mdp.equals("")){
                    Toast.makeText(MainActivity.this, "Remplir tout les champs !", Toast.LENGTH_SHORT).show();
                }else {
                    new LoginTask().execute(pseudo, mdp);

                }
            }
        });
        Button btn = (Button) findViewById(R.id.btn_test);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(MainActivity.this, PageVisualisation.class);
                startActivity(ii);
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
                HttpPost httpPost=new HttpPost("http://10.0.2.2:8888/login");
                httpPost.addHeader("Content-Type", "application/json");
                String pseudo = strings[0];
                String mdp = strings[1];
                StringEntity  entity = new StringEntity("{\"pseudo\":\""+pseudo+"\",\"mdp\":\""+mdp+"\"}");
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
                    JSONArray  user = new JSONArray(res);
                    String strThatDay = user.getJSONObject(0).getString("date");
                    String strFirstCo = user.getJSONObject(0).getString("first_co");
                    if(strFirstCo.equals("0")){
                        runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setCancelable(true);
                                builder.setTitle("Alerte");
                                builder.setMessage("C'est votre première connexion ! Changer le mot de passe !");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        EditText pseudoEdit = (EditText) findViewById(R.id.input_pseudo);
                                        String pseudo = pseudoEdit.getText().toString();
                                        Intent i = new Intent(MainActivity.this, Change_mdp.class);
                                        i.putExtra("pseudo",pseudo);
                                        startActivity(i);
                                    }});
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });
                    }
                    else {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                        Date d = null;
                        d = formatter.parse(strThatDay);//catch exception
                        Calendar thatDay = Calendar.getInstance();
                        thatDay.setTime(d);
                        Calendar today = Calendar.getInstance();
                        long diff = today.getTimeInMillis() - thatDay.getTimeInMillis();
                        long days = diff / (24 * 60 * 60 * 1000);
                        if (days > 10 && days <= 15) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setCancelable(true);
                                    builder.setTitle("Alerte");
                                    builder.setMessage("Votre mot de passe va expirer ! Le changer ? ");
                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            EditText pseudoEdit = (EditText) findViewById(R.id.input_pseudo);
                                            String pseudo = pseudoEdit.getText().toString();
                                            Intent i = new Intent(MainActivity.this, Change_mdp.class);
                                            i.putExtra("pseudo", pseudo);
                                            startActivity(i);
                                        }
                                    });
                                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            });

                        } else if (days > 15) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setCancelable(true);
                                    builder.setTitle("Alerte");
                                    builder.setMessage("Votre a expiré ! Changer le maintenant. ");
                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            EditText pseudoEdit = (EditText) findViewById(R.id.input_pseudo);
                                            String pseudo = pseudoEdit.getText().toString();
                                            Intent i = new Intent(MainActivity.this, Change_mdp.class);
                                            i.putExtra("pseudo", pseudo);
                                            startActivity(i);
                                        }
                                    });

                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            });
                        } else {

                            SharedPreferences settings = getSharedPreferences("CurrentUser", 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("currentUser", user.get(0).toString());
                            editor.commit();

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Intent i = new Intent(MainActivity.this, PageVisualisation.class);
                                    startActivity(i);
                                }
                            });
                        }
                    }
                }

              }catch(Exception e)  {
                System.out.println(e);
            }
            return res;
        }
    }

}
