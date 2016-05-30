package com.example.renatoaugusto.trabalhon2;

import android.app.DatePickerDialog;
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
import android.widget.*;

import com.example.renatoaugusto.trabalhon2.BancoDeDados.AcessoBanco;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


public class Cadastrar extends AppCompatActivity implements View.OnClickListener {

    private EditText edtNome, edtData, edtLocal, edtDescricao, edtParticipantes, edtTipo;

    private Button btRepetir, btTipo, btAdicionar, btOk;
    private Spinner spnTipo;
    private ArrayAdapter adpTipo;

    private AdicionarAoBanco add;
    private AcessoBanco db;
    private SQLiteDatabase Sql;
    private Entidades entidades;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtNome          = (EditText) findViewById(R.id.edtNome);
        edtData          = (EditText) findViewById(R.id.edtData);
        edtLocal         = (EditText) findViewById(R.id.edtLocal);
        edtDescricao     = (EditText) findViewById(R.id.edtDescricao);
        edtParticipantes = (EditText) findViewById(R.id.edtParticipantes);
        edtTipo          = (EditText) findViewById(R.id.edtTipo);

        spnTipo          = (Spinner) findViewById(R.id.spnTipo);
        btRepetir        = (Button) findViewById(R.id.btRepetir);
        btTipo           = (Button) findViewById(R.id.btTipo);
        btAdicionar      = (Button) findViewById(R.id.btAdicionar);
        btOk             = (Button) findViewById(R.id.btOk);


        edtTipo.setVisibility(View.INVISIBLE);//EditText invisivel
        btAdicionar.setVisibility(View.INVISIBLE);//Botão invisivel


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

        exibeDataListener listener = new exibeDataListener();
        edtData.setOnClickListener(listener);
        edtData.setOnFocusChangeListener(listener);

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


        if (v == btRepetir) {

            Intent it = new Intent(this, Repetir.class);
            Bundle params = new Bundle();

            params.putString("nome", edtNome.getText().toString());
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

    //Exibir uma caixa onde usuário pode selecionar a data ---------------------------------------------------------

    private void exibeData() {


        Calendar calendar = Calendar.getInstance();

        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        int ano = calendar.get(Calendar.YEAR);

        DatePickerDialog dlg = new DatePickerDialog(this, new SelecionaDataListener(), ano, mes, dia);
        dlg.show();
    }

    private class exibeDataListener implements View.OnClickListener, View.OnFocusChangeListener {

        @Override
        public void onClick(View v) {
            exibeData();
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus)
                exibeData();
        }
    }

    private class SelecionaDataListener implements DatePickerDialog.OnDateSetListener {


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            Date data = calendar.getTime();

            DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
            String dataFormatada = format.format(data);

            edtData.setText(dataFormatada);

        }
    }
}


