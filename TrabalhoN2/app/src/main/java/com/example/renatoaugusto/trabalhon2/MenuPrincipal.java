package com.example.renatoaugusto.trabalhon2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MenuPrincipal extends AppCompatActivity implements View.OnClickListener {


    private Button bt_novo;
    private Button bt_visualizar;
    private Button bt_alterar;
    private Button bt_cancelar;
    private Button bt_expurgar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bt_novo         = (Button) findViewById(R.id.bt_novo);
        bt_visualizar   = (Button) findViewById(R.id.bt_visualizar);
        bt_alterar      = (Button) findViewById(R.id.bt_alterar);
        bt_cancelar     = (Button) findViewById(R.id.bt_cancelar);
        bt_expurgar     = (Button) findViewById(R.id.bt_expurgar);

        bt_novo.setOnClickListener(this);
        bt_visualizar.setOnClickListener(this);
        bt_alterar.setOnClickListener(this);
        bt_cancelar.setOnClickListener(this);
        bt_expurgar.setOnClickListener(this);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == bt_novo) {
            Intent it = new Intent(this, Cadastrar.class);
            startActivity(it);
        }

        if (v == bt_visualizar) {
            Intent it = new Intent(this, Compromissos.class);
            startActivity(it);
        }

        if (v == bt_alterar) {
            Intent it = new Intent(this, Alterar.class);
            startActivity(it);
        }

        if (v == bt_cancelar) {
            Intent it = new Intent(this, Cancelar.class);
            startActivity(it);
        }

        if (v == bt_expurgar) {
            Intent it = new Intent(this, Expurgar.class);
            startActivity(it);
        }
    }
}
