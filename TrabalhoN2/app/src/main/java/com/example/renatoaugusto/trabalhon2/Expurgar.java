package com.example.renatoaugusto.trabalhon2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Expurgar extends AppCompatActivity implements View.OnClickListener {


    private EditText edt_dataExpurgar;
    private Button btExpurgar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expurgar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edt_dataExpurgar = (EditText) findViewById(R.id.edt_dataExpurgar);
        btExpurgar = (Button) findViewById(R.id.btExpurgar);

        exibeDataListener listener = new exibeDataListener();
        edt_dataExpurgar.setOnClickListener(listener);
        btExpurgar.setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

    @Override
    public void onClick(View v) {

        if (v == btExpurgar) {

            String data = edt_dataExpurgar.getText().toString();

            //converter em inteiro, recebendo apenas os digitos que estejam no intervalo
            String dia = data.substring(0, 2);
            String mes = data.substring(3, 5);
            String ano = data.substring(6, 10);

            Intent it = new Intent(this, Compromissos.class);
            Bundle params = new Bundle();

            params.putString("expurgar", dia);
            params.putString("mes", mes);
            params.putString("ano", ano);
            it.putExtras(params);

            startActivity(it);

        }
    }

    private class exibeDataListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
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

            edt_dataExpurgar.setText(dataFormatada);

        }
    }
}
