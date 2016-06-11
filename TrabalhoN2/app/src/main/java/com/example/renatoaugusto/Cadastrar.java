package com.example.renatoaugusto;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import com.example.renatoaugusto.sqlite.AcessoBanco;
import com.example.renatoaugusto.sqlite.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Cadastrar extends AppCompatActivity implements View.OnClickListener {

    private EditText edtNome, edtData, edtLocal, edtDescricao, edtParticipantes, edtTipo;

    private Button btRepetir, btTipo, btAdicionar, btCancelarInsercao, btDeletarTipo;
    private Spinner spnTipo;
    private ArrayAdapter adpTipo;
    final AcessoBanco db = new AcessoBanco(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        edtNome = (EditText) findViewById(R.id.edtNome);
        edtData = (EditText) findViewById(R.id.edtData);
        edtLocal = (EditText) findViewById(R.id.edtLocal);
        edtDescricao = (EditText) findViewById(R.id.edtDescricao);
        edtParticipantes = (EditText) findViewById(R.id.edtParticipantes);
        edtTipo = (EditText) findViewById(R.id.edtTipo);

        spnTipo = (Spinner) findViewById(R.id.spnTipo);
        btRepetir = (Button) findViewById(R.id.btRepetir);
        btTipo = (Button) findViewById(R.id.btTipo);
        btAdicionar = (Button) findViewById(R.id.btAdicionar);
        btCancelarInsercao = (Button) findViewById(R.id.btCancelarInsercao);
        btDeletarTipo = (Button) findViewById(R.id.bt_deletar_tipo);

        edtTipo.setVisibility(View.INVISIBLE);//EditText invisivel
        btAdicionar.setVisibility(View.INVISIBLE);//Botão invisivel

        btRepetir.setOnClickListener(this);
        btTipo.setOnClickListener(this);
        btAdicionar.setOnClickListener(this);
        btCancelarInsercao.setOnClickListener(this);
        btDeletarTipo.setOnClickListener(this);

        TiposFixos();
        AtualizarTipos();

        exibeDataListener listener = new exibeDataListener();
        edtData.setOnClickListener(listener);
        edtData.setOnFocusChangeListener(listener);

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

        if (v == btCancelarInsercao) {
            Intent it = new Intent(this, MenuPrincipal.class);
            startActivity(it);
        }

        if (v == btTipo) {
            edtTipo.setVisibility(View.VISIBLE);
            btAdicionar.setVisibility(View.VISIBLE);
        }

        if (v == btRepetir) {

            boolean camposVazios = camposVazios();

            if (camposVazios == true)
                mensagem();
                else
            Repetir();
        }

        if (v == btAdicionar) {
            if (TextUtils.isEmpty(edtTipo.getText().toString())) {
                novoTipoVazio();
            } else {
                AdicionarSpinner(edtTipo.getText().toString());
                edtTipo.setText(null);
            }
        }

        if (v == btDeletarTipo) {
            Intent it = new Intent(this, ExcluirTipo.class);
            startActivity(it);
        }
    }


// Passar dados de cadastro para tela de repetição ==============================================================================

    public void Repetir(){

        Intent it = new Intent(this, Repetir.class);
        Bundle params = new Bundle();

        params.putString("nome", edtNome.getText().toString());
        params.putString("descricao", edtDescricao.getText().toString());
        params.putString("data", edtData.getText().toString());
        params.putString("local", edtLocal.getText().toString());
        params.putString("participantes", edtParticipantes.getText().toString());
        params.putString("tipo", String.valueOf(spnTipo.getSelectedItemPosition()));
        it.putExtras(params);

        startActivity(it);
    }

// Inserir no banco os tipos fixos de Spinner ==============================================================================

    public void TiposFixos() {

        String vazio = null;
        db.open();
        Cursor c = db.getTipos();

        if (c.moveToFirst()) {
            do {
                vazio = c.getString(1);
            } while (c.moveToNext());
        }


        if (TextUtils.isEmpty(vazio)) {

            //(saúde, família, escola, trabalho e lazer).
            String saude = "Saúde";
            db.insereNovoTipo(saude);
            db.insereNovoTipoAux(saude);
            String familia = "Família";
            db.insereNovoTipo(familia);
            db.insereNovoTipoAux(familia);
            String escola = "Escola";
            db.insereNovoTipo(escola);
            db.insereNovoTipoAux(escola);
            String trabalho = "Trabalho";
            db.insereNovoTipo(trabalho);
            db.insereNovoTipoAux(trabalho);
            String lazer = "Lazer";
            db.insereNovoTipo(lazer);
            db.insereNovoTipoAux(lazer);
        }
        db.close();
    }

// Adicionar novo Spinner ====================================================================================

    public void AdicionarSpinner(String novoTipo) {

        db.open();
        db.insereNovoTipo(novoTipo);
        db.insereNovoTipoAux(novoTipo);
        db.close();
        Toast.makeText(this, "Novo tipo inserido!", Toast.LENGTH_SHORT).show();

        AtualizarTipos();
    }

// Atualiza o banco de Tipos ================================================================================

    public void AtualizarTipos() {

        db.open();
        Cursor c = db.getTipos();

        adpTipo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (c.moveToFirst()) {
            do {
                adpTipo.add(c.getString(1));
            } while (c.moveToNext());
            spnTipo.setAdapter(adpTipo);
        }
        db.close();
    }

// Verificar campos vazios ===================================================================================

    public boolean camposVazios() {
        if (TextUtils.isEmpty(edtNome.getText().toString()) && TextUtils.isEmpty(edtData.getText().toString()))
            return true;
        else
            return false;
    }


// Mensagem caso os campos estejam vazios ======================================================================


    private void mensagem() {

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("ALERTA!");
        builder.setMessage("Campos nome e data são obrigatórios.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        builder.show();

    }

// Mensagem caso o Spinner esteja vazio =========================================================================

    public void novoTipoVazio() {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("ALERTA!");
        builder.setMessage("Informe o nome do tipo.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        builder.show();
    }


//Exibir uma caixa onde usuário pode selecionar a data ---------------------------------------------------------

    private void exibeData() {


        Calendar calendar = Calendar.getInstance();

        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        int ano = calendar.get(Calendar.YEAR);

        DatePickerDialog dlg = new DatePickerDialog(this, new SelecionaDataListener(), ano, mes, dia);
        dlg.show();
    }

    private class exibeDataListener implements View.OnClickListener, View.OnFocusChangeListener {

        @Override
        public void onClick(View v) {

            exibeData();
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus)
                exibeData();
        }
    }

    private class SelecionaDataListener implements DatePickerDialog.OnDateSetListener {


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            Date data = calendar.getTime();

            SimpleDateFormat sd = new SimpleDateFormat(("dd/MM/yyyy"));
            String dataFormatada = sd.format(data);

            edtData.setText(dataFormatada);

        }
    }
}
