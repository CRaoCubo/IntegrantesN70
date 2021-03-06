package com.example.renatoaugusto.trabalhon2;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.renatoaugusto.trabalhon2.BancoDeDados.AcessoBanco;

public class Compromissos extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static int diaExpurgar, mesExpurgar, anoExpurgar;


    private ListView lstCompromissos, lstCompromissosCancelados;
    private ArrayAdapter<Entidades> adpAtivos;
    private ArrayAdapter<EntidadesCanceladas> adpCancelados;
    private Button bt_menu;


    private AcessoBanco db;
    private Entidades entidades;
    private EntidadesCanceladas entidadesCanceladas;
    private AdicionarAoBanco add;
    private SQLiteDatabase Sql;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compromissos);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        lstCompromissos           = (ListView) findViewById(R.id.lstCompromissos);
        lstCompromissosCancelados = (ListView) findViewById(R.id.lstCompromissosCancelados);
        bt_menu                   = (Button) findViewById(R.id.bt_menu);

        AtualizarBanco();
        AtualizarBancoCanceladas();

        lstCompromissos.setOnItemClickListener(this);
        bt_menu.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            if (bundle.containsKey("inserir")) {

                long id = bundle.getLong("id");
                String nome = bundle.getString("inserir");
                String descricao = bundle.getString("descricao");
                String data = bundle.getString("data");
                String local = bundle.getString("local");
                String participantes = bundle.getString("participantes");
                String tipo = bundle.getString("tipo");

                entidades = new Entidades();
                entidades.setNome(nome);
                entidades.setData(data);
                entidades.setLocal(local);
                entidades.setDescricao(descricao);
                entidades.setParticipantes(participantes);
                entidades.setTipo(tipo);
                add = new AdicionarAoBanco(Sql);
                add.insereCompromisso(entidades);

                AtualizarBanco();
                AtualizarBancoCanceladas();

            }

            if (bundle.containsKey("alterar")) {

                long id = bundle.getLong("id");
                String nome = bundle.getString("alterar");
                String descricao = bundle.getString("descricao");
                String data = bundle.getString("data");
                String local = bundle.getString("local");
                String participantes = bundle.getString("participantes");
                String tipo = bundle.getString("tipo");

                entidades = new Entidades();
                entidades.setNome(nome);
                entidades.setData(data);
                entidades.setLocal(local);
                entidades.setDescricao(descricao);
                entidades.setParticipantes(participantes);
                entidades.setTipo(tipo);
                add = new AdicionarAoBanco(Sql);
                add.alteraCompromisso(entidades, id);

                AtualizarBanco();
                AtualizarBancoCanceladas();

            }

            if (bundle.containsKey("cancelar")) {

                entidades = (Entidades) bundle.getSerializable("cancelar");
                long id = entidades.getId();
                String nome = entidades.getNome();

                entidadesCanceladas = new EntidadesCanceladas();
                entidadesCanceladas.setNome(nome);
                add = new AdicionarAoBanco(Sql);
                add.cancelarCompromisso(entidadesCanceladas, id);

                AtualizarBanco();
                AtualizarBancoCanceladas();

            }

            if (bundle.containsKey("expurgar")) {

                String dia = bundle.getString("expurgar");
                String mes = bundle.getString("mes");
                String ano = bundle.getString("ano");

                diaExpurgar = Integer.parseInt(dia);
                mesExpurgar = Integer.parseInt(mes);
                anoExpurgar = Integer.parseInt(ano);

                deletarCompromissosPorData();
                AtualizarBanco();
                AtualizarBancoCanceladas();

            }
        }
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

    public void AtualizarBanco() {


        try {
            db = new AcessoBanco(this);
            Sql = db.getWritableDatabase();

            add = new AdicionarAoBanco(Sql);
            adpAtivos = add.buscaCompromissos(this);

            lstCompromissos.setAdapter(adpAtivos);


        } catch (SQLException ex) {
            AlertDialog.Builder d = new AlertDialog.Builder(this);
            d.setMessage("Conexao com Banco NAO criada " + ex.getMessage());
            d.setNeutralButton("ok", null);
            d.show();
        }
    }


    public void AtualizarBancoCanceladas() {


        try {
            db = new AcessoBanco(this);
            Sql = db.getWritableDatabase();

            add = new AdicionarAoBanco(Sql);
            adpCancelados = add.buscaCompromissosCancelados(this);

            lstCompromissosCancelados.setAdapter(adpCancelados);


        } catch (SQLException ex) {
            AlertDialog.Builder d = new AlertDialog.Builder(this);
            d.setMessage("Conexao com Banco NAO criada " + ex.getMessage());
            d.setNeutralButton("ok", null);
            d.show();
        }
    }


    public void deletarCompromissosPorData() {


        try {
            db = new AcessoBanco(this);
            Sql = db.getWritableDatabase();

            add = new AdicionarAoBanco(Sql);
            adpAtivos = add.expurgar(this, diaExpurgar, mesExpurgar, anoExpurgar);

            lstCompromissos.setAdapter(adpAtivos);


        } catch (SQLException ex) {
            AlertDialog.Builder d = new AlertDialog.Builder(this);
            d.setMessage("Conexao com Banco NAO criada " + ex.getMessage());
            d.setNeutralButton("ok", null);
            d.show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Entidades etd = adpAtivos.getItem(position);
        Intent it = new Intent(this, Visualizar.class);
        it.putExtra("VisualizarCompromisso", etd);

        startActivity(it);

    }

    @Override
    public void onClick(View v) {
        if (v == bt_menu) {
            Intent it = new Intent(this, MenuPrincipal.class);
            startActivity(it);
        }
    }
}
