package com.example.renatoaugusto;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.renatoaugusto.sqlite.AcessoBanco;
import com.example.renatoaugusto.sqlite.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MostrarTodos extends AppCompatActivity implements View.OnClickListener {

    final AcessoBanco db = new AcessoBanco(this);

    private TextView txt_inicio, txt_fim, txt_ate;
    private Button bt_inicio, bt_fim, bt_voltarAoMenu, bt_visualizarEntreDatas;
    private static boolean verificarData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_todos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        txt_inicio = (TextView) findViewById(R.id.txt_inicio);
        txt_fim = (TextView) findViewById(R.id.txt_fim);
        txt_ate = (TextView) findViewById(R.id.txt_ate);
        bt_inicio = (Button) findViewById(R.id.bt_inicio);
        bt_fim = (Button) findViewById(R.id.bt_fim);
        bt_voltarAoMenu = (Button) findViewById(R.id.bt_voltarAoMenu);
        bt_visualizarEntreDatas = (Button) findViewById(R.id.bt_visualizarEntreDatas);

        bt_inicio.setOnClickListener(this);
        bt_fim.setOnClickListener(this);
        bt_voltarAoMenu.setOnClickListener(this);
        bt_visualizarEntreDatas.setOnClickListener(this);
    }

    public void Visualizar() {

        boolean camposVazios = VerificarCamposVazios();

        if (camposVazios == false) {

            boolean SemCompromissosNoIntervalo = true;
            String dataInicio = txt_inicio.getText().toString();
            String diaInicio = dataInicio.substring(14, 16);
            String mesInicio = dataInicio.substring(17, 19);
            String anoInicio = dataInicio.substring(20, 24);

            String dataFim = txt_fim.getText().toString();
            String diaFim = dataFim.substring(12, 14);
            String mesFim = dataFim.substring(15, 17);
            String anoFim = dataFim.substring(18, 22);

            db.open();
            Cursor c = db.getCompromissos();

            if (c.moveToFirst()) {
                do {

                    String data = c.getString(3);
                    String dia = data.substring(0, 2);
                    String mes = data.substring(3, 5);
                    String ano = data.substring(6, 10);

                    if ((Integer.valueOf(dia) >= Integer.valueOf(diaInicio) && Integer.valueOf(mes) >= Integer.valueOf(mesInicio) && Integer.valueOf(ano) >= Integer.valueOf(anoInicio))
                            && (Integer.valueOf(dia) <= Integer.valueOf(diaFim) && Integer.valueOf(mes) <= Integer.valueOf(mesFim) && Integer.valueOf(ano) <= Integer.valueOf(anoFim))) {
                        Toast.makeText(this, "Nome: " + c.getString(2) + "\n" + "Data: " + c.getString(3) + "\n" + "Local: " + c.getString(4) + "\n" + "Participantes: " + c.getString(5) + "\n", Toast.LENGTH_SHORT).show();
                        SemCompromissosNoIntervalo = false;
                    }

                } while (c.moveToNext());
            }
            db.close();

            if (SemCompromissosNoIntervalo == true) {
                Toast.makeText(this, "Não há compromissos para este intervalo!", Toast.LENGTH_SHORT).show();
            }
        } else
            CamposVazios();
    }


    // Verifica se todos os campos foram preenchidos

    public boolean VerificarCamposVazios() {
        String VerificaDataInicio = String.valueOf(txt_inicio.getText());
        String VerificaDataFim = String.valueOf(txt_fim.getText());

        if (TextUtils.isEmpty(VerificaDataInicio) ||
                TextUtils.isEmpty(VerificaDataFim)) {

            return true;

        } else {
            return false;
        }
    }

    // Mensagem caso a verificação de Dados Vazios não esteja nas condições
    public void CamposVazios() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ALERTA!");
        builder.setMessage("Informe as 2 datas.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        builder.show();
    }


    @Override
    public void onClick(View v) {


        if (v == bt_inicio) {
            verificarData = true;
            exibeData();
        }

        if (v == bt_fim) {
            verificarData = false;
            exibeData();
        }

        if (v == bt_voltarAoMenu) {
            Intent it = new Intent(this, MenuPrincipal.class);
            startActivity(it);
        }

        if (v == bt_visualizarEntreDatas) {
            Visualizar();
        }

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

    private class SelecionaDataListener implements DatePickerDialog.OnDateSetListener {


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            Date data = calendar.getTime();

            SimpleDateFormat sd = new SimpleDateFormat(("dd/MM/yyyy"));
            String dataFormatada = sd.format(data);

            if (verificarData == true)
                txt_inicio.setText("Data Inicial: " + dataFormatada);

            else {
                txt_ate.setText("até");
                txt_fim.setText("Data Final: " + dataFormatada);
            }
        }
    }
}


