package com.example.projet_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EnsembleLocaux extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ensemble_locaux);
        ListView listLocal = (ListView) findViewById(R.id.ensLocal);
        ArrayList<Local> l = new ArrayList<>();
        l.add(new Local("2 place eugènes bataillon 34000 montpellier","Commerce"));
        l.add(new Local("3 place eugènes bataillon 34000 montpellier","Habitation"));
        l.add(new Local("4 place eugènes bataillon 34000 montpellier","Musée"));
        l.add(new Local("5 place eugènes bataillon 34000 montpellier","Commerce"));
        l.add(new Local("6 place eugènes bataillon 34000 montpellier","Habitation"));
        l.add(new Local("7 place eugènes bataillon 34000 montpellier","Habitation"));
        LocalAdapter<Local> adapter = new LocalAdapter<>(this,R.layout.activity_ensemble_locaux,R.id.rowAdresse,l,this);
        listLocal.setAdapter(adapter);
    }

    @Override
    public void onClick(View vue) {
        LocalAdapter.ViewHolder holder = (LocalAdapter.ViewHolder) vue.getTag();
        //this.getApp().getInventoryItems().add(new Item(holder.instance.getName(),holder.instance.getSellIn(),holder.instance.getQuality()));

        String text = "Selection de "+holder.instance.getAdresse();
        Toast toast = Toast.makeText(getBaseContext(),text,Toast.LENGTH_SHORT);
        toast.show();
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.visual_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.all_locaux:
                String text = " Vous êtes déjà sur la page de l'ensemble des locaux";
                Toast toast = Toast.makeText(getBaseContext(),text,Toast.LENGTH_SHORT);
                toast.show();
                return true;
            case R.id.visual_page:
                Intent i = new Intent(EnsembleLocaux.this, PageVisualisation.class);
                startActivity(i);
            case R.id.type_menu:
                Intent ii = new Intent(EnsembleLocaux.this, CategorieLocal.class);
                startActivity(ii);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
