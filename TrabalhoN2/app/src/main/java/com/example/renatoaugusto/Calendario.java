package com.example.renatoaugusto;


import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.renatoaugusto.sqlite.AcessoBanco;
import com.example.renatoaugusto.sqlite.R;


public class Calendario extends AppCompatActivity {

    private static int Controle;

    CalendarView calendar;
    TextView txt;

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

        //initializes the calendarview
        initializeCalendar();

    }

    public void initializeCalendar() {

        calendar = (CalendarView) findViewById(R.id.calendar);
        txt = (TextView) findViewById(R.id.txtCalendario);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            if (bundle.containsKey("MostrarCompromisso")) {
                Controle = bundle.getInt("MostrarCompromisso");
                txt.setText("Calendario:");

            }
            if (bundle.containsKey("ExpurgarCompromisso")) {
                Controle = bundle.getInt("ExpurgarCompromisso");
                txt.setText("Informe data para expurgar:");


            }

        }


        // sets whether to show the week number.
        calendar.setShowWeekNumber(false);

        // sets the first day of week according to Calendar.
        // here we set Monday as the first day of the Calendar
        calendar.setFirstDayOfWeek(2);


        //sets the listener to be notified upon selected date change.
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            //show the selected date as a toast
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {

                db.open();
                Cursor c = db.getCompromissos();

                if (c.moveToFirst()) {
                    do {

                        if (Controle == 1) {
                            mostraCompromisso(c, day, month, year);
                        }

                        if (Controle == 3) {
                            expurgaCompromisso(c, day, month, year);
                        }


                    } while (c.moveToNext());
                }
                db.close();
            }
        });
    }

    public void mostraCompromisso(Cursor c, int day, int month, int year) {

        String data = c.getString(2);

        String dia = data.substring(0, 2);
        String mes = data.substring(3, 5);
        String ano = data.substring(6, 10);

        month += 1; //Calendario se inicia como janeiro sendo mês 'ZERO'

        if (Integer.valueOf(dia) == day && Integer.valueOf(mes) == month && Integer.valueOf(ano) == year) {
            Toast.makeText(this, "Nome: " + c.getString(1) + "\n" + "Data: " + c.getString(2) + "\n" + "Local: " + c.getString(3) + "\n" + "Participantes: " + c.getString(4) + "\n", Toast.LENGTH_SHORT).show();
        }
    }

    public void expurgaCompromisso(Cursor c, int day, int month, int year) {

            month += 1; //Calendario se inicia como janeiro sendo mês 'ZERO'
            confirmarExpurgo(c, day, month, year);
    }


// Alerta para o usuário confirmar se deseja expurgar ===============================================================================

    public void confirmarExpurgo(final Cursor c, final int day, final int month, final int year) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("ALERTA!");
        alertDialogBuilder.setMessage("Todos os compromissos anteriores a essa data serão excluidos. Deseja continuar?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() { //ação ao clicar em "sim"
                    public void onClick(DialogInterface dialog, int id) {

                        db.open();
                        Cursor c = db.getCompromissos();

                        if (c.moveToFirst()) {
                            do {

                                String data = c.getString(2);

                                String dia = data.substring(0, 2);
                                String mes = data.substring(3, 5);
                                String ano = data.substring(6, 10);


                                if ((Integer.valueOf(dia) < day && Integer.valueOf(mes) <= month && Integer.valueOf(ano) <= year) || (Integer.valueOf(dia) > day && Integer.valueOf(mes) < month && Integer.valueOf(ano) <= year) || (Integer.valueOf(dia) < day && Integer.valueOf(mes) > month && Integer.valueOf(ano) < year)) {
                                    db.expurgarCompromisso(c.getInt(0));
                                }

                            } while (c.moveToNext());
                        }
                        db.close();
                    }
                }).setNegativeButton("Não", new DialogInterface.OnClickListener() {  //Apenas fecha a janela
            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
