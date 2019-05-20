package com.example.projet_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Alerte_en_cours extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerte_en_cours);

        String log = getIntent().getStringExtra("log");
        String date = getIntent().getStringExtra("date");

        TextView logT = findViewById(R.id.log_en_cours);
        logT.setText(log);

        TextView dateT = findViewById(R.id.date_en_cours);
        dateT.setText(date);
    }
}
