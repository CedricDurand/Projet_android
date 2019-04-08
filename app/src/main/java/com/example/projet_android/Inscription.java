package com.example.projet_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

                if(string_pseudo.isEmpty()){
                    Toast.makeText(Inscription.this, "Remplir le champs pseudo !", Toast.LENGTH_LONG).show();
                }else if(string_pseudo.length()<6){
                    Toast.makeText(Inscription.this, "Votre pseudo doit faire plus de 6 charactères.", Toast.LENGTH_LONG).show();
                }else if(mdp.length()<5){
                    Toast.makeText(Inscription.this, "Votre mot de passe doit faire plus de 5 charactères.", Toast.LENGTH_LONG).show();
                }
                else{
                    if(mdp.equals(confirm_mdp)){
                        System.out.println("METTRE DANS BDD ( INSCRIPTION ) FAIRE LE TEST SI LE PSEUDO EXISTE DEJA");
                        Intent i = new Intent(Inscription.this, MainActivity.class);
                        startActivity(i);
                        Toast.makeText(Inscription.this, "Inscription réussi !", Toast.LENGTH_LONG).show();
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
}
