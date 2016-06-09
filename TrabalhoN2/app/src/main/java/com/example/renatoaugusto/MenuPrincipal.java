package com.example.renatoaugusto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.widget.*;

import com.example.renatoaugusto.sqlite.R;

public class MenuPrincipal extends AppCompatActivity implements View.OnClickListener {

    Button btGravar;
    Button btMostrar;
    Button btAlterar;
    Button btExpurgar;
    Button btCancelar;
    Button btMostrarTodos;
    Button btEncerrar;


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
        btExpurgar = (Button) findViewById(R.id.bt_expurgar);
        btCancelar = (Button) findViewById(R.id.bt_cancelar);
        btMostrarTodos = (Button) findViewById(R.id.bt_mostrarTodos);
        btEncerrar = (Button) findViewById(R.id.bt_encerrar);

        btGravar.setOnClickListener(this);
        btMostrar.setOnClickListener(this);
        btAlterar.setOnClickListener(this);
        btExpurgar.setOnClickListener(this);
        btCancelar.setOnClickListener(this);
        btMostrarTodos.setOnClickListener(this);
        btEncerrar.setOnClickListener(this);

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
        if (v == btGravar) {
            Intent it = new Intent(this, Cadastrar.class);
            startActivity(it);
        }

        if (v == btMostrar) {
            Intent it = new Intent(this, Calendario.class);
            it.putExtra("MostrarCompromisso", 1);
            startActivity(it);
        }

        if (v == btAlterar) {
            Intent it = new Intent(this, TelaListView.class);
            it.putExtra("AlterarCompromisso", 2);
            startActivity(it);
        }

        if (v == btExpurgar) {
            Intent it = new Intent(this, Calendario.class);
            it.putExtra("ExpurgarCompromisso", 3);
            startActivity(it);
        }

        if (v == btCancelar) {
            Intent it = new Intent(this, TelaListView.class);
            it.putExtra("CancelarCompromisso", 4);
            startActivity(it);
        }

        if (v == btMostrarTodos) {
            Intent it = new Intent(this, MostrarTodos.class);
            startActivity(it);
        }

        if (v == btEncerrar) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
        }
    }
}