package com.example.projet_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

                if(new_mdp.length()<5){
                    Toast.makeText(Change_mdp.this, "Votre nouveau mot de passe doit faire plus de 5 charactères.", Toast.LENGTH_LONG).show();
                }
                else{
                    System.out.println("FAIRE TEST AVEC ANCIEN MDP (Change_mdp)");
                    if(new_mdp.equals(confirm_mdp)){
                        System.out.println("METTRE A JOUR LA DANS BDD (Change_mdp)");
                        Intent i = new Intent(Change_mdp.this, MainActivity.class);
                        startActivity(i);
                        Toast.makeText(Change_mdp.this, "Modification réussi !", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(Change_mdp.this, "Mot de passe non identique !", Toast.LENGTH_LONG).show();
                    }
                }

                Intent i = new Intent(Change_mdp.this, MainActivity.class);
                startActivity(i);
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
}
