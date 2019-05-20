package com.example.projet_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Alerte_fini extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerte_fini);

        String log = getIntent().getStringExtra("log");
        String date = getIntent().getStringExtra("date");
        String action = getIntent().getStringExtra("action");

        TextView logT = findViewById(R.id.log_fini);
        logT.setText(log);

        TextView dateT = findViewById(R.id.date_fini);
        dateT.setText(date);

        TextView actionT = findViewById(R.id.action_fini);
        actionT.setText(action);

    }
}
