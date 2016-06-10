package com.example.renatoaugusto;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;

import com.example.renatoaugusto.sqlite.AcessoBanco;
import com.example.renatoaugusto.sqlite.R;

public class ExcluirTipo extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    final AcessoBanco db = new AcessoBanco(this);

    private TextView txt;
    private Button bt_excluir_tipo;
    private ListView lst_excluir_tipo;
    private ArrayAdapter<String> adpCompromissos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excluir_tipo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lst_excluir_tipo = (ListView) findViewById(R.id.lst_excluir_tipo);
        bt_excluir_tipo = (Button) findViewById(R.id.bt_voltar_excluir_tipo);
        txt = (TextView) findViewById(R.id.txt_excluir_tipo);

        InserirDadosNaLista();
        lst_excluir_tipo.setOnItemClickListener(this);
        bt_excluir_tipo.setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void InserirDadosNaLista() {

        try {
            adpCompromissos = buscaTipos(this);
            lst_excluir_tipo.setAdapter(adpCompromissos);
        } catch (SQLException ex) {
            AlertDialog.Builder d = new AlertDialog.Builder(this);
            d.setMessage("Conexao com Banco NAO criada " + ex.getMessage());
            d.setNeutralButton("ok", null);
            d.show();
        }
    }

    public ArrayAdapter<String> buscaTipos(Context c) {

        ArrayAdapter<String> adpDados = new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1);

        db.open();
        Cursor cursor = db.getTipos();

        if (cursor.getCount() > 0) {

            cursor.moveToFirst();// Posiciona no primeiro registro

            do {
                adpDados.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return adpDados;
    }

    @Override
    public void onClick(View v) {

        if (v == bt_excluir_tipo) {
            Intent it = new Intent(this, Cadastrar.class);
            startActivity(it);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String nome_tipo = adpCompromissos.getItem(position);
        long id_tipo = 0;

        db.open();
        Cursor c = db.getTipos();

        if (c.moveToFirst()) {

            do {
                if (nome_tipo.equals(c.getString(1))) {
                   id_tipo = c.getLong(0);
                }
            } while (c.moveToNext());
        }
        db.deletarTipo(id_tipo);
        db.close();
        InserirDadosNaLista();
        Toast.makeText(this, "Compromisso Excluido!", Toast.LENGTH_SHORT).show();
    }
}
