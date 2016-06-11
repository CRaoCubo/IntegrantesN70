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

        AtualizarTipos();

        bt_voltar.setOnClickListener(this);
        bt_concluirAlteracao.setOnClickListener(this);

        //Extrai o conteudo de outra tela
        Bundle bundle = getIntent().getExtras();

//Alterar o compromisso =============================================================================================

        if (bundle.containsKey("AlterarCompromisso")) {

            String alterar = bundle.getString("AlterarCompromisso");

            db.open();
            Cursor c = db.getCompromissos();

            if (c.moveToFirst()) {
                do {


                    if (alterar.equals(c.getString(2)) && c.getLong(0) == c.getLong(1)) {

                        id = c.getLong(0);
                        edt_nome.setText(c.getString(2));
                        edt_data.setText(c.getString(3));
                        edt_data.setEnabled(false);
                        edt_local.setText(c.getString(4));
                        edt_descricao.setText(c.getString(5));
                        edt_participantes.setText(c.getString(6));
                        spnMostrarTipo.setSelection(Integer.parseInt(c.getString(7)));

                    }
                } while (c.moveToNext());
            }
            db.close();
        }
    }

//Função de Alterar Compromisso =========================================================================================

    public void Alterar() {

        db.open();
        Cursor c = db.getCompromissos();

        if (c.moveToFirst()) {
            do {

                if (c.getLong(0) == c.getLong(1)) {
                    String Nome = edt_nome.getText().toString();
                    String Local = edt_local.getText().toString();
                    String Data = edt_data.getText().toString();
                    String Descricao = edt_descricao.getText().toString();
                    String Participantes = edt_participantes.getText().toString();
                    long Cancelado = 0;

                    db.alterarCompromisso(id, Nome, Data, Local, Descricao, Participantes, Cancelado);
                }


            }
            while (c.moveToNext());
            db.close();
        }
    }

    public void AtualizarTipos() {

        db.open();
        Cursor c = db.getTiposAux();

        adpTipo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (c.moveToFirst()) {
            do {
                adpTipo.add(c.getString(1));
            } while (c.moveToNext());
            spnMostrarTipo.setAdapter(adpTipo);
        }
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
            Toast.makeText(this, "Compromisso Alterado!", Toast.LENGTH_SHORT).show();
            startActivity(it);
        }
    }
}
