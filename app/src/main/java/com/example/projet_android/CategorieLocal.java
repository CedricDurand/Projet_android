package com.example.projet_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

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
        String admin="";
        try{
            SharedPreferences settings = getSharedPreferences("CurrentUser", 0);
            String myString = settings.getString("currentUser", "");
            JSONObject userJson = new JSONObject(myString);
            admin = userJson.getString("admin");
        }catch (Exception e){
            e.printStackTrace();
        }

        getMenuInflater().inflate(R.menu.visual_menu, menu);

        if(admin.equals("augmente")){
            menu.add(R.menu.visual_menu,1,Menu.NONE,"Ajouter un utilisateur");
        }

        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deco:
                Intent main = new Intent(CategorieLocal.this, MainActivity.class);
                SharedPreferences settings = getSharedPreferences("CurrentUser", 0);
                settings.edit().clear().commit();
                startActivity(main);
                return true;
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
            case 1:
                Intent i3 = new Intent(CategorieLocal.this, Inscription.class);
                startActivity(i3);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
