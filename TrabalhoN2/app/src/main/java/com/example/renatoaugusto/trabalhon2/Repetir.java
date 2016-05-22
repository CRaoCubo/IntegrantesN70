package com.example.renatoaugusto.trabalhon2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Repetir extends AppCompatActivity implements View.OnClickListener {


    private Spinner spnRepeticao;
    private Spinner spnIntervalo;
    private ArrayAdapter adpRepeticao;
    private ArrayAdapter adpIntervalo;
    private Button btCancelar;
    private Button btConcluido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repetir);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spnRepeticao = (Spinner) findViewById(R.id.spnRepeticao);
        spnIntervalo = (Spinner) findViewById(R.id.spnIntervalo);
        btCancelar = (Button) findViewById(R.id.btCancelar);
        btConcluido = (Button) findViewById(R.id.btConcluido);

        btConcluido.setOnClickListener(this);
        btCancelar.setOnClickListener(this);

        adpRepeticao = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpRepeticao.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnRepeticao.setAdapter(adpRepeticao);

        adpRepeticao.add("Diariamente");
        adpRepeticao.add("Semanalmente");
        adpRepeticao.add("Mensalmente");
        adpRepeticao.add("Anualmente");

        adpIntervalo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpIntervalo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnIntervalo.setAdapter(adpIntervalo);

        adpIntervalo.add("Dias");
        adpIntervalo.add("Semanas");
        adpIntervalo.add("Meses");
        adpIntervalo.add("Anos");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onClick(View v) {


        if (v == btCancelar) {
            Intent it = new Intent(this, Cadastrar.class);
            startActivity(it);
        }


        if (v == btConcluido) {
            //Intent it = new Intent(this, Cadastrar.class);
            //startActivity(it);
        }
    }
}
