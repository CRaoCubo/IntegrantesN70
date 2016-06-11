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
            boolean verificarCancelado = verificarCancelado(nome);//Verifica se o compromisso não esta cancelado

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

    //Atualiza a ListView ================================================================================================
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


    //Verifica se o compromisso ja esta cancelado e se retornar verdadeiro, não deixa o usuario mexer neste evento =======
    public boolean verificarCancelado(String nome) {

        db.open();
        Cursor c = db.getCompromissos();

        if (c.moveToFirst()) {

            do {
                if (nome.equals(c.getString(2)))
                    if (c.getInt(8) == 1)
                        return true;
            } while (c.moveToNext());
        }

        db.close();
        return false;
    }

    //Cancela um compromisso ==============================================================================================
    public void cancelarCompromisso() {

        db.open();
        Cursor c = db.getCompromissos();

        if (c.moveToFirst()) {

            do {
                if (nome.equals(c.getString(2))) {
                    id = c.getLong(0);
                    nome = c.getString(2);
                    long cancelado = 1;
                    db.cancelarCompromisso(id, nome, cancelado);//Cancela o compromisso
                }
            } while (c.moveToNext());
        }

        db.close();
        InserirDadosNaLista();//Atualiza a ListView
    }


    //Busca os compromissos no banco e retorna eles para a função InserirDadosNaLista(); ==============================================
    public ArrayAdapter<String> buscaCompromissos(Context c) {

        ArrayAdapter<String> adpDados = new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1);

        db.open();
        Cursor cursor = db.getCompromissos();

        if (cursor.moveToFirst()){// Posiciona no primeiro registro

            do {
                if (cursor.getLong(0) == cursor.getLong(1))
                    adpDados.add(cursor.getString(2));
            } while (cursor.moveToNext());//Move para o próximo registro
        }
        db.close();
        return adpDados;
    }
}
