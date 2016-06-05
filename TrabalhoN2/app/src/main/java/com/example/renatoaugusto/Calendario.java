package com.example.renatoaugusto;


import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.renatoaugusto.sqlite.AcessoBanco;
import com.example.renatoaugusto.sqlite.R;


public class Calendario extends AppCompatActivity {

    CalendarView calendar;
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
                Cursor c = db.getCadastros();

                if (c.moveToFirst()) {
                    do {
                        mostraRegistro(c, day);

                    } while (c.moveToNext());
                }
                db.close();

            }
        });
    }

    public void mostraRegistro(Cursor c, int day) {

        if (c.getInt(0) == day) {
            Toast.makeText(this, "Nome: " + c.getString(1) + "\n" + "Data: " + c.getString(2) + "\n" + "Local: " + c.getString(3) + "\n" + "Participantes: " + c.getString(4) + "\n", Toast.LENGTH_SHORT).show();
        }
    }

    public void mostraRegistro(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}