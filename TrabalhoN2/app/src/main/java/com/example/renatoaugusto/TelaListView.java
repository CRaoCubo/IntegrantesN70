package com.example.renatoaugusto;

import android.content.*;
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

public class TelaListView extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    final AcessoBanco db = new AcessoBanco(this);

    private static int Controle;
    private static String nome, data, local, descricao, participantes;
    private static long id;


    private TextView txt;
    private Button bt_menuAlterar;
    private ListView lst_alterar;
    private ArrayAdapter<String> adpCompromissos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_list_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lst_alterar = (ListView) findViewById(R.id.lst_alterar);
        bt_menuAlterar = (Button) findViewById(R.id.bt_menuAlterar);
        txt = (TextView) findViewById(R.id.txt);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            if (bundle.containsKey("AlterarCompromisso")) {
                Controle = bundle.getInt("AlterarCompromisso");
                txt.setText("Qual compromisso deseja alterar?");
            } else if (bundle.containsKey("CancelarCompromisso")) {
                Controle = bundle.getInt("CancelarCompromisso");
                txt.setText("Qual compromisso deseja cancelar?");
            }
        }

        InserirDadosNaLista();

        lst_alterar.setOnItemClickListener(this);
        bt_menuAlterar.setOnClickListener(this);


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
        if (v == bt_menuAlterar) {
            Intent it = new Intent(this, MenuPrincipal.class);
            startActivity(it);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (Controle == 2) {

            nome = adpCompromissos.getItem(position);
            boolean verificarCancelado = verificarCancelado(nome);

            if (verificarCancelado == false) {
                Intent it = new Intent(this, Compromissos.class);
                it.putExtra("AlterarCompromisso", nome);
                startActivity(it);
           } else
                Toast.makeText(this, "Este compromisso ja foi cancelado!", Toast.LENGTH_SHORT).show();
        }

        // ============================================================================================

        if (Controle == 4) {

            nome = adpCompromissos.getItem(position);
            boolean verificarCancelado = verificarCancelado(nome);

            if (verificarCancelado == false) {
                cancelarCompromisso();
            } else
                Toast.makeText(this, "Este compromisso ja foi cancelado!", Toast.LENGTH_SHORT).show();
        }
    }

    public void InserirDadosNaLista() {

        try {
            adpCompromissos = buscaCompromissos(this);
            lst_alterar.setAdapter(adpCompromissos);
        } catch (SQLException ex) {
            AlertDialog.Builder d = new AlertDialog.Builder(this);
            d.setMessage("Conexao com Banco NAO criada " + ex.getMessage());
            d.setNeutralButton("ok", null);
            d.show();
        }
    }


    public boolean verificarCancelado(String nome) {

        db.open();
        Cursor c = db.getCompromissos();

        if (c.moveToFirst()) {

            do {
                if (nome.equals(c.getString(1)))
                    if (c.getInt(7) == 1)
                    return true;
            } while (c.moveToNext());
        }

        db.close();
        return false;
    }

    public void cancelarCompromisso() {

        db.open();
        Cursor c = db.getCompromissos();

        if (c.moveToFirst()) {

            do {
                if (nome.equals(c.getString(1))) {
                    id = c.getLong(0);
                    data = c.getString(2);
                    local = c.getString(3);
                    descricao = c.getString(4);
                    participantes = c.getString(5);
                }
            } while (c.moveToNext());
        }

        db.cancelarCompromisso(id, nome, data, local, descricao, participantes);
        db.close();
        InserirDadosNaLista();
    }

    public ArrayAdapter<String> buscaCompromissos(Context c) {

        ArrayAdapter<String> adpDados = new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1);

        db.open();
        Cursor cursor = db.getCompromissos();

        if (cursor.getCount() > 0) {

            cursor.moveToFirst();// Posiciona no primeiro registro

            do {
                adpDados.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return adpDados;
    }
}
