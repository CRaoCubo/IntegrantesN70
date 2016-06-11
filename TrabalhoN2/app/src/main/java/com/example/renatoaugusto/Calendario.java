package com.example.renatoaugusto;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import com.example.renatoaugusto.sqlite.AcessoBanco;
import com.example.renatoaugusto.sqlite.R;

import java.text.*;
import java.util.*;


public class Calendario extends AppCompatActivity implements View.OnClickListener {

    private static int Controle;
    private static Date dataCadastrada, dataInformada;

    CalendarView calendar;
    TextView txt;
    Button bt_menu_calendario;
    EditText edt;

    final AcessoBanco db = new AcessoBanco(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        inicializarCalendario();

    }

// Inicializa o calendario ========================================================================================================================

    public void inicializarCalendario() {

        calendar = (CalendarView) findViewById(R.id.calendar);
        bt_menu_calendario = (Button) findViewById(R.id.bt_menu_calendario);
        txt = (TextView) findViewById(R.id.txtCalendario);

        bt_menu_calendario.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            if (bundle.containsKey("MostrarCompromisso")) {
                Controle = bundle.getInt("MostrarCompromisso");
                txt.setText("Deseja verificar os compromissos de qual dia?");
            }
            if (bundle.containsKey("ExpurgarCompromisso")) {
                Controle = bundle.getInt("ExpurgarCompromisso");
                txt.setText("Todos os compromissos anteriores a data selecionada serão excluidos!");
            }
        }

        //sets the listener to be notified upon selected date change.
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            //show the selected date as a toast
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                Date data = calendar.getTime();

                SimpleDateFormat sd = new SimpleDateFormat(("dd/MM/yyyy"));
                String dataFormatada = sd.format(data);
                month += 1; //Calendario se inicia como janeiro sendo mês 'ZERO'

                if (Controle == 1) {
                    mostraCompromisso(dataFormatada);
                }

                if (Controle == 3) {
                    confirmarExpurgo(dataFormatada);
                }

            }
        });
    }

// Mostra todos os compromissos da data especificada pelo usuário ===================================================================================================

    public void mostraCompromisso(String formatDataInformada) {

        db.open();
        Cursor c = db.getCompromissos();

        if (c.moveToFirst()) {
            do {

                int retornoData;
                String formatDataCadastrada = c.getString(3);
                SimpleDateFormat sd = new SimpleDateFormat(("dd/MM/yyyy"));

                try {
                    dataCadastrada = sd.parse(formatDataCadastrada);
                    dataInformada = sd.parse(formatDataInformada);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                retornoData = dataCadastrada.compareTo(dataInformada);

                if (retornoData == 0) {
                    Toast.makeText(this, "Nome: " + c.getString(2) + "\n" + "Data: " + c.getString(3) + "\n" + "Local: " + c.getString(4) + "\n" + "Descricao: " + c.getString(5) + "\n" + "Participantes: " + c.getString(6) + "\n", Toast.LENGTH_SHORT).show();
                }
            }
            while (c.moveToNext());
        }
        db.close();
    }

// Excluir todos os compromissos antes da data informada pelo usuário ==============================================================================

    public void expurgaCompromisso(String formatDataInformada) {

        db.open();
        Cursor c = db.getCompromissos();

        if (c.moveToFirst()) {
            do {

                int retornoData;
                String formatDataCadastrada = c.getString(3);
                SimpleDateFormat sd = new SimpleDateFormat(("dd/MM/yyyy"));

                try {
                    dataCadastrada = sd.parse(formatDataCadastrada);
                    dataInformada = sd.parse(formatDataInformada);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                retornoData = dataCadastrada.compareTo(dataInformada);

                if (retornoData == -1 || retornoData == 0) {

                    db.expurgarCompromisso(c.getLong(0));

                }

            } while (c.moveToNext());

            db.close();
            Toast.makeText(this, "Todos os compromissos anteriores foram apagados!", Toast.LENGTH_SHORT).show();
        }

    }


// Alerta para o usuário confirmar se deseja expurgar ===============================================================================

    public void confirmarExpurgo(final String data) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("ALERTA!");
        alertDialogBuilder.setMessage("Todos os compromissos anteriores a essa data serão excluidos. Deseja continuar?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() { //ação ao clicar em "sim"
                    public void onClick(DialogInterface dialog, int id) {

                        expurgaCompromisso(data);

                    }

                }).setNegativeButton("Não", new DialogInterface.OnClickListener() {  //Apenas fecha a janela
            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == bt_menu_calendario) {
            Intent it = new Intent(this, MenuPrincipal.class);
            startActivity(it);
        }
    }
}
