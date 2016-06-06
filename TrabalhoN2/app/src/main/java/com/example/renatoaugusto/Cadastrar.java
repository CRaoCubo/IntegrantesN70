package com.example.renatoaugusto;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;

import com.example.renatoaugusto.sqlite.AcessoBanco;
import com.example.renatoaugusto.sqlite.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Cadastrar extends AppCompatActivity implements View.OnClickListener{

     private EditText edtNome, edtData, edtLocal, edtDescricao, edtParticipantes, edtTipo;

    private Button btRepetir, btTipo, btAdicionar, btCancelarInsercao;
    private Spinner spnTipo;
    private ArrayAdapter adpTipo;
    final AcessoBanco db = new AcessoBanco(this);


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

        spnTipo = (Spinner) findViewById(R.id.spnTipo);
        btRepetir = (Button) findViewById(R.id.btRepetir);
        btTipo = (Button) findViewById(R.id.btTipo);
        btAdicionar = (Button) findViewById(R.id.btAdicionar);
        btCancelarInsercao = (Button) findViewById(R.id.btCancelarInsercao);


        edtTipo.setVisibility(View.INVISIBLE);//EditText invisivel
        btAdicionar.setVisibility(View.INVISIBLE);//Botão invisivel


        adpTipo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipo.setAdapter(adpTipo);

        adpTipo.add("Saúde");
        adpTipo.add("Família");
        adpTipo.add("Escola");
        adpTipo.add("Trabalho");
        adpTipo.add("Lazer");

        btRepetir.setOnClickListener(this);
        btTipo.setOnClickListener(this);
        btAdicionar.setOnClickListener(this);
        btCancelarInsercao.setOnClickListener(this);

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

        if (v == btCancelarInsercao) {
            Intent it = new Intent(this, MenuPrincipal.class);
            startActivity(it);
        }

        if (v == btTipo) {
            edtTipo.setVisibility(View.VISIBLE);
            btAdicionar.setVisibility(View.VISIBLE);
        }

        if (v == btRepetir) {

            Inserir();
        }
    }

    public void Inserir(){

        String novoNome = edtNome.getText().toString();
        String novaData= edtData.getText().toString();
        String novoLocal = edtLocal.getText().toString();
        String novaDescricao = edtDescricao.getText().toString();
        String novosParticipantes = edtParticipantes.getText().toString();

        db.open();
        db.insereCompromisso(novoNome, novaData, novoLocal, novaDescricao, novosParticipantes);
        db.close();

        Toast.makeText(this, "Compromisso Cadastrado!" , Toast.LENGTH_SHORT).show();

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

            SimpleDateFormat sd = new SimpleDateFormat(("dd/MM/yyyy"));
            String dataFormatada = sd.format(data);

            edtData.setText(dataFormatada);

        }
    }

}
