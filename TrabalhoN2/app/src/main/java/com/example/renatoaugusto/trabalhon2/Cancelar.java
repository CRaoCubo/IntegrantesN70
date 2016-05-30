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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.renatoaugusto.trabalhon2.BancoDeDados.AcessoBanco;

public class Cancelar extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView lst_cancelar;
    private ArrayAdapter<Entidades> adpCompromissos;
    private Button bt_menuCancelar;

    private AcessoBanco db;
    private AdicionarAoBanco add;
    private SQLiteDatabase Sql;
    private Entidades entidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        lst_cancelar = (ListView) findViewById(R.id.lst_cancelar);
        bt_menuCancelar = (Button) findViewById(R.id.bt_menuCancelar);

        AtualizarBanco();

        bt_menuCancelar.setOnClickListener(this);
        lst_cancelar.setOnItemClickListener(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    public void AtualizarBanco() {


        try {
            db = new AcessoBanco(this);
            Sql = db.getWritableDatabase();

            add = new AdicionarAoBanco(Sql);
            adpCompromissos = add.buscaCompromissos(this);

            lst_cancelar.setAdapter(adpCompromissos);


        } catch (SQLException ex) {
            AlertDialog.Builder d = new AlertDialog.Builder(this);
            d.setMessage("Conexao com Banco NAO criada " + ex.getMessage());
            d.setNeutralButton("ok", null);
            d.show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == bt_menuCancelar) {
            Intent it = new Intent(this, MenuPrincipal.class);
            startActivity(it);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Entidades etd = adpCompromissos.getItem(position);
        Intent it = new Intent(this, Compromissos.class);
        it.putExtra("cancelar", etd);

        startActivity(it);

    }
}
