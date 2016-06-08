package com.example.renatoaugusto;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;

import com.example.renatoaugusto.sqlite.AcessoBanco;
import com.example.renatoaugusto.sqlite.R;

public class Compromissos extends AppCompatActivity implements View.OnClickListener {

    AcessoBanco db = new AcessoBanco(this);
    private static long id;

    private Button bt_concluirAlteracao, bt_voltar;
    private EditText edt_nome;
    private EditText edt_data;
    private EditText edt_local;
    private EditText edt_descricao;
    private EditText edt_participantes;
    private Spinner spnMostrarTipo;

    private ArrayAdapter adpTipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compromissos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edt_nome = (EditText) findViewById(R.id.edt_nome);
        edt_data = (EditText) findViewById(R.id.edt_data);
        edt_local = (EditText) findViewById(R.id.edt_local);
        edt_descricao = (EditText) findViewById(R.id.edt_descricao);
        edt_participantes = (EditText) findViewById(R.id.edt_participantes);
        spnMostrarTipo = (Spinner) findViewById(R.id.spnMostrarTipo);
        bt_concluirAlteracao = (Button) findViewById(R.id.bt_concluirAlteracao);
        bt_voltar = (Button) findViewById(R.id.bt_voltar);

        bt_voltar.setOnClickListener(this);
        bt_concluirAlteracao.setOnClickListener(this);

        adpTipo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMostrarTipo.setAdapter(adpTipo);

        adpTipo.add("Saúde");
        adpTipo.add("Família");
        adpTipo.add("Escola");
        adpTipo.add("Trabalho");
        adpTipo.add("Lazer");

        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey("AlterarCompromisso")) {

            String alterar = bundle.getString("AlterarCompromisso");

            db.open();
            Cursor c = db.getCompromissos();

            if (c.moveToFirst()) {
                do {


                    if (alterar.equals(c.getString(1))) {

                        id = c.getLong(0);
                        edt_nome.setText(alterar);
                        edt_data.setText(c.getString(2));
                        edt_local.setText(c.getString(3));
                        edt_descricao.setText(c.getString(4));
                        edt_participantes.setText(String.valueOf(c.getLong(6)));
                        // spnMostrarTipo.setSelection(Integer.parseInt(entidades.getTipo()));

                    }
                } while (c.moveToNext());
            }
            db.close();
        }
    }


    public void Alterar() {

        String Nome = edt_nome.getText().toString();
        String Data = edt_data.getText().toString();
        String Local = edt_local.getText().toString();
        String Descricao = edt_descricao.getText().toString();
        String Participantes = edt_participantes.getText().toString();

        db.open();
        db.alterarCompromisso(id, Nome, Data, Local, Descricao, Participantes);
        db.close();
    }


    @Override
    public void onClick(View v) {
        if (v == bt_voltar) {
            finish();
        }

        if (v == bt_concluirAlteracao) {
            Intent it = new Intent(this, MenuPrincipal.class);
            Alterar();
            Toast.makeText(this, "Compromisso Alterado!" , Toast.LENGTH_SHORT).show();
            startActivity(it);
        }
    }
}
