package com.example.projet_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PageVisualisation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_visualisation);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.visual_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.all_locaux:
                Intent i = new Intent(PageVisualisation.this, EnsembleLocaux.class);
                startActivity(i);
                return true;
            case R.id.type_menu:
                Intent ii = new Intent(PageVisualisation.this, CategorieLocal.class);
                startActivity(ii);
                return true;
            case R.id.visual_page:
                String text = " Vous êtes déjà sur la page de visualisation";
                Toast toast = Toast.makeText(getBaseContext(),text,Toast.LENGTH_SHORT);
                toast.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
