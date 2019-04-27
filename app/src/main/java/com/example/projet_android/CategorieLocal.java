package com.example.projet_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class CategorieLocal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorie_local);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_local);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_choice, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.visual_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.all_locaux:
                Intent ii = new Intent(CategorieLocal.this, EnsembleLocaux.class);
                startActivity(ii);
                return true;
            case R.id.visual_page:
                Intent i = new Intent(CategorieLocal.this, PageVisualisation.class);
                startActivity(i);
            case R.id.type_menu:
                //voir pour un retour a l'activité déjà lancé
                String text = " Vous êtes déjà sur la page des locaux catégorisé";
                Toast toast = Toast.makeText(getBaseContext(),text,Toast.LENGTH_SHORT);
                toast.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
