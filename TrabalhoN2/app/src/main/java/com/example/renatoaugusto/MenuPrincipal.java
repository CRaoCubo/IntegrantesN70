package com.example.renatoaugusto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.widget.*;

import com.example.renatoaugusto.sqlite.R;

public class MenuPrincipal extends AppCompatActivity implements View.OnClickListener{

    Button btGravar;
    Button btMostrar;
    Button btAlterar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        btGravar = (Button) findViewById(R.id.bt_gravar);
        btMostrar = (Button) findViewById(R.id.bt_mostrar);
        btAlterar = (Button) findViewById(R.id.bt_alterar);


        btGravar.setOnClickListener(this);
        btMostrar.setOnClickListener(this);
        btAlterar.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == btGravar){
            Intent it = new Intent(this, Cadastrar.class);
            startActivity(it);
        }

        if (v == btMostrar){
            Intent it = new Intent(this, Calendario.class);
            startActivity(it);
        }

        if (v == btAlterar){
            Intent it = new Intent(this, TelaListView.class);
            startActivity(it);
        }
    }
}