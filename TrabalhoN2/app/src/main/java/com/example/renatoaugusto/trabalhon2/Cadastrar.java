package com.example.renatoaugusto.trabalhon2;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.renatoaugusto.trabalhon2.BancoDeDados.AcessoBanco;


public class Cadastrar extends AppCompatActivity implements View.OnClickListener {

    private AdicionarAoBanco add;

    private EditText edtNome;
    private EditText edtData;
    private EditText edtLocal;
    private EditText edtDescricao;
    private EditText edtParticipantes;
    private EditText edtTipo;
    private Button btRepetir;
    private Button btTipo;
    private Button btAdicionar;
    private Button btOk;
    private Spinner spnTipo;
    private ArrayAdapter adpTipo;

    private SQLiteDatabase Sql;
    private Entidades entidades;
    private AcessoBanco db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtData = (EditText) findViewById(R.id.edtData);
        edtLocal = (EditText) findViewById(R.id.edtLocal);
        edtDescricao = (EditText) findViewById(R.id.edtDescricao);
        edtParticipantes = (EditText) findViewById(R.id.edtParticipantes);
        edtTipo = (EditText) findViewById(R.id.edtTipo);


        spnTipo =     (Spinner) findViewById(R.id.spnTipo);
        btRepetir =   (Button) findViewById(R.id.btRepetir);
        btTipo =      (Button) findViewById(R.id.btTipo);
        btAdicionar = (Button) findViewById(R.id.btAdicionar);
        btOk =        (Button) findViewById(R.id.btOk);


        edtTipo.setVisibility(View.INVISIBLE);//EditText invisivel
        btAdicionar.setVisibility(View.INVISIBLE);//EditText invisivel


        adpTipo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipo.setAdapter(adpTipo);


        AtualizarBancoDeTipos();
        adpTipo.add("Saúde");
        adpTipo.add("Família");
        adpTipo.add("Escola");
        adpTipo.add("Trabalho");
        adpTipo.add("Lazer");


        btRepetir.setOnClickListener(this);
        btTipo.setOnClickListener(this);
        btAdicionar.setOnClickListener(this);
        btOk.setOnClickListener(this);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey("Compromissos")) {

                entidades = (Entidades) bundle.getSerializable("Compromissos");
                edtNome.setText(entidades.getNome());
                edtData.setText(entidades.getData());
                edtLocal.setText(entidades.getLocal());
                edtDescricao.setText(entidades.getDescricao());
                edtParticipantes.setText(entidades.getParticipantes());
                spnTipo.setSelection(Integer.parseInt(entidades.getTipo()));

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
        if (v == btOk) {
            Intent it = new Intent(this, Compromissos.class);
            Bundle params = new Bundle();

            params.putString("inserir", edtNome.getText().toString());
            params.putString("descricao", edtDescricao.getText().toString());
            params.putString("data", edtData.getText().toString());
            params.putString("local", edtLocal.getText().toString());
            params.putString("participantes", edtParticipantes.getText().toString());
            params.putString("tipo", String.valueOf(spnTipo.getSelectedItemPosition()));
            it.putExtras(params);

            startActivity(it);
        }

        if (v == btTipo) {
            edtTipo.setVisibility(View.VISIBLE);
            btAdicionar.setVisibility(View.VISIBLE);
        }

        if (v == btAdicionar) {
            adpTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnTipo.setAdapter(adpTipo);
            adpTipo.add(edtTipo);

            entidades = new Entidades();
            entidades.setAdicionarTipo(spnTipo.toString());
            add = new AdicionarAoBanco(Sql);
            add.insereNovoTipo(entidades);

            AtualizarBancoDeTipos();

        }

        if (v == btRepetir) {
            Intent it = new Intent(this, Repetir.class);
            startActivity(it);
        }
    }


    public void AtualizarBancoDeTipos() {

        try {
            db = new AcessoBanco(this);
            Sql = db.getWritableDatabase();

            add = new AdicionarAoBanco(Sql);
            adpTipo = add.buscaTipo(this);

            spnTipo.setAdapter(adpTipo);


        } catch (SQLException ex) {
            AlertDialog.Builder d = new AlertDialog.Builder(this);
            d.setMessage("Conexao com Banco NAO criada " + ex.getMessage());
            d.setNeutralButton("ok", null);
            d.show();
        }
    }

}


