package com.example.renatoaugusto.trabalhon2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class Compromissos extends AppCompatActivity {


    private EditText edt_nome;
    private EditText edt_data;
    private EditText edt_local;
    private EditText edt_descricao;
    private EditText edt_participantes;
    private EditText edtTipo;

    private Entidades entidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compromissos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edt_nome = (EditText) findViewById(R.id.edt_nome);
        edt_data = (EditText) findViewById(R.id.edt_data);
        edt_local = (EditText) findViewById(R.id.edt_local);
        edt_descricao = (EditText) findViewById(R.id.edt_descricao);
        edt_participantes = (EditText) findViewById(R.id.edt_participantes);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey("DadosDoCompromisso")) {

                entidades = (Entidades) bundle.getSerializable("DadosDoCompromisso");
                edt_nome.setText(entidades.getNome());
                edt_data.setText(entidades.getData());
                edt_local.setText(entidades.getLocal());
                edt_descricao.setText(entidades.getDescricao());
                edt_participantes.setText(entidades.getParticipantes());
            }
        }



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
