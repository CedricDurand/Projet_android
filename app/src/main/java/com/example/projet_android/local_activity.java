package com.example.projet_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class local_activity extends AppCompatActivity {

    String id_local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_activity);

        String id = getIntent().getStringExtra("id");
        String cat = getIntent().getStringExtra("cat");
        String adr = getIntent().getStringExtra("adr");
        id_local=id;
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

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_local, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.historique:
                Intent i = new Intent(local_activity.this, liste_historique.class);
                i.putExtra("id",id_local);
                startActivity(i);
                return true;
            case R.id.evenement:
               /* Intent ii = new Intent(local_activity.this, CategorieLocal.class);
                startActivity(ii);*/
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
