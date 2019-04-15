package com.example.projet_android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.ParseException;
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

                }
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
}
