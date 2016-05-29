package com.example.renatoaugusto.trabalhon2;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Visualizar extends AppCompatActivity implements View.OnClickListener{

    private static long id;

    private Button bt_concluirAlteracao, bt_voltar;
    private EditText edt_nome;
    private EditText edt_data;
    private EditText edt_local;
    private EditText edt_descricao;
    private EditText edt_participantes;
    private Spinner spnMostrarTipo;

    private ArrayAdapter adpTipo;

    private AdicionarAoBanco add;
    private Entidades entidades;
    private SQLiteDatabase Sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edt_nome             = (EditText) findViewById(R.id.edt_nome);
        edt_data             = (EditText) findViewById(R.id.edt_data);
        edt_local            = (EditText) findViewById(R.id.edt_local);
        edt_descricao        = (EditText) findViewById(R.id.edt_descricao);
        edt_participantes    = (EditText) findViewById(R.id.edt_participantes);
        spnMostrarTipo       = (Spinner) findViewById(R.id.spnMostrarTipo);
        bt_concluirAlteracao = (Button) findViewById(R.id.bt_concluirAlteracao);
        bt_voltar            = (Button) findViewById(R.id.bt_voltar);

        bt_voltar.setOnClickListener(this);
        bt_concluirAlteracao.setOnClickListener(this);

        adpTipo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMostrarTipo.setAdapter(adpTipo);

        adpTipo.add("Saúde");
        adpTipo.add("Família");
        adpTipo.add("Escola");
        adpTipo.add("Trabalho");
        adpTipo.add("Lazer");

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey("VisualizarCompromisso")) {

                entidades = (Entidades) bundle.getSerializable("VisualizarCompromisso");
                edt_nome.setText(entidades.getNome());
                edt_data.setText(entidades.getData());
                edt_local.setText(entidades.getLocal());
                edt_descricao.setText(entidades.getDescricao());
                edt_participantes.setText(entidades.getParticipantes());
                spnMostrarTipo.setSelection(Integer.parseInt(entidades.getTipo()));
                bt_concluirAlteracao.setVisibility(View.INVISIBLE);//Botão invisivel
            }

            if (bundle.containsKey("AlterarCompromisso")) {

                entidades = (Entidades) bundle.getSerializable("AlterarCompromisso");
                id = entidades.getId();
                edt_nome.setText(entidades.getNome());
                edt_data.setText(entidades.getData());
                edt_local.setText(entidades.getLocal());
                edt_descricao.setText(entidades.getDescricao());
                edt_participantes.setText(entidades.getParticipantes());
                spnMostrarTipo.setSelection(Integer.parseInt(entidades.getTipo()));
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

    @Override
    public void onClick(View v) {
        if (v == bt_voltar) {
            finish();
        }

        if (v == bt_concluirAlteracao) {
            Intent it = new Intent(this, Compromissos.class);
            Bundle params = new Bundle();

            params.putLong("id", id);
            params.putString("alterar", edt_nome.getText().toString());
            params.putString("descricao", edt_descricao.getText().toString());
            params.putString("data", edt_data.getText().toString());
            params.putString("local", edt_local.getText().toString());
            params.putString("participantes", edt_participantes.getText().toString());
            params.putString("tipo", String.valueOf(spnMostrarTipo.getSelectedItemPosition()));
            it.putExtras(params);

            startActivity(it);
        }
    }
}
