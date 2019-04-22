package com.example.projet_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class local_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_activity);

        String id = getIntent().getStringExtra("id");
        String cat = getIntent().getStringExtra("cat");
        String adr = getIntent().getStringExtra("adr");

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


    }
}
