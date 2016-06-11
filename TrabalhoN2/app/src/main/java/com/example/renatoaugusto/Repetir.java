package com.example.renatoaugusto;

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
import android.widget.*;

import com.example.renatoaugusto.sqlite.AcessoBanco;
import com.example.renatoaugusto.sqlite.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Repetir extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private static Date dataInicialDoEvento, dataFinalDoEvento;
    private static String novoNome, novaDescricao, novaData, novoLocal, novosParticipantes, novoTipo;
    final AcessoBanco db = new AcessoBanco(this);

    private Spinner spn_repetir;
    private ArrayAdapter adpRepeticao;
    private Button bt_cancelar_repeticao, bt_concluir_repeticao;
    private RadioButton rdApos, rdData, rdSempre;
    private EditText edtInicioEm, edt_finalizar_por_repeticoes, edt_finalizar_por_data, edtRepetirCada;
    private TextView txtSetarRepeticoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repetir);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        spn_repetir = (Spinner) findViewById(R.id.spn_repetir);
        bt_cancelar_repeticao = (Button) findViewById(R.id.bt_cancelar_repeticao);
        bt_concluir_repeticao = (Button) findViewById(R.id.bt_concluir_repeticao);
        edtInicioEm = (EditText) findViewById(R.id.edtInicioEm);
        edtRepetirCada = (EditText) findViewById(R.id.edtRepetirCada);
        txtSetarRepeticoes = (TextView) findViewById(R.id.txtSetarRepeticoes);
        edt_finalizar_por_repeticoes = (EditText) findViewById(R.id.edt_finalizar_por_repeticoes);
        edt_finalizar_por_data = (EditText) findViewById(R.id.edt_finalizar_por_data);
        rdApos = (RadioButton) findViewById(R.id.rdOcorrencia);
        rdData = (RadioButton) findViewById(R.id.rdData);
        rdSempre = (RadioButton) findViewById(R.id.rdSempre);

        edt_finalizar_por_repeticoes.setVisibility(View.INVISIBLE);//EditText invisivel
        edt_finalizar_por_data.setVisibility(View.INVISIBLE);//EditText invisivel
        txtSetarRepeticoes.setVisibility(View.INVISIBLE);//Text invisivel

        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.containsKey("nome")) {

            novoNome = bundle.getString("nome");
            novaDescricao = bundle.getString("descricao");
            novaData = bundle.getString("data");
            novoLocal = bundle.getString("local");
            novosParticipantes = bundle.getString("participantes");
            novoTipo = bundle.getString("tipo");
            edtInicioEm.setText(novaData);
        }

        bt_concluir_repeticao.setOnClickListener(this);
        bt_cancelar_repeticao.setOnClickListener(this);
        rdApos.setOnClickListener(this);
        rdData.setOnClickListener(this);
        rdSempre.setOnClickListener(this);
        edtRepetirCada.setOnClickListener(this);
        edt_finalizar_por_repeticoes.setOnFocusChangeListener(this);

        adpRepeticao = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpRepeticao.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_repetir.setAdapter(adpRepeticao);

        adpRepeticao.add("Diariamente");
        adpRepeticao.add("Semanalmente");
        adpRepeticao.add("Mensalmente");
        adpRepeticao.add("Anualmente");


        exibeDataListener listener = new exibeDataListener();
        edt_finalizar_por_data.setOnClickListener(listener);
        edt_finalizar_por_data.setOnFocusChangeListener(listener);

    }

    @Override
    public void onClick(View v) {


        if (v == bt_cancelar_repeticao) {
            Intent it = new Intent(this, Cadastrar.class);
            startActivity(it);
        }

        if (v == bt_concluir_repeticao) {
            Intent it = new Intent(this, MenuPrincipal.class);

            boolean RadioButtonSelecionado = RadioButtonSelecionado();

            if (RadioButtonSelecionado == false) {
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("ALERTA!");
                builder.setMessage("Não foi definida uma data de término para o compromisso.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                builder.show();

            } else if (TextUtils.isEmpty(edtRepetirCada.getText().toString()))
                mensagem();

            else {
                verificarRadioButton();
                startActivity(it);
            }
        }

        if (v == rdSempre) {
            edt_finalizar_por_data.setVisibility(View.INVISIBLE);
            edt_finalizar_por_repeticoes.setVisibility(View.INVISIBLE);
        }

        if (v == rdApos) {
            edt_finalizar_por_repeticoes.setVisibility(View.VISIBLE);
            edt_finalizar_por_data.setVisibility(View.INVISIBLE);
        }

        if (v == rdData) {
            edt_finalizar_por_data.setVisibility(View.VISIBLE);
            edt_finalizar_por_repeticoes.setVisibility(View.INVISIBLE);
        }

        if (v == edtRepetirCada || v == rdSempre || v == rdApos || v == rdData || v == bt_concluir_repeticao) {

            txtSetarRepeticoes.setVisibility(View.VISIBLE);

            if (spn_repetir.getSelectedItemPosition() == 0) {
                txtSetarRepeticoes.setText("dias");
            } else if (spn_repetir.getSelectedItemPosition() == 1) {
                txtSetarRepeticoes.setText("semanas");
            } else if (spn_repetir.getSelectedItemPosition() == 2) {
                txtSetarRepeticoes.setText("meses");
            } else if (spn_repetir.getSelectedItemPosition() == 3) {
                txtSetarRepeticoes.setText("anos");
            }
        }
    }

    private void mensagem() {

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("ALERTA!");
        builder.setMessage("Não foi informado uma frequencia de repetições.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        builder.show();
    }

    public boolean RadioButtonSelecionado() {

        boolean RadioButtonSelecionado = false;

        if (rdSempre.isChecked()) {
            return RadioButtonSelecionado = true;

        } else if (rdApos.isChecked()) {
            return RadioButtonSelecionado = true;

        } else if (rdData.isChecked()) {
            return RadioButtonSelecionado = true;
        }
        return RadioButtonSelecionado = false;
    }


//Veririfica qual radio Button foi selecionado =======================================================================================================================

    public void verificarRadioButton() {

        if (rdSempre.isChecked()) {
            InserirRepeticoesSempre();

        } else if (rdApos.isChecked()) {
            InserirRepeticoesPorOcorrencia();

        } else if (rdData.isChecked()) {
            InserirRepeticoesPorData();
        }
    }


    public void InserirRepeticoesSempre() {

        int quantidadeDeOcorrencias = 3;
        int quantidadeDeRepeticoes = Integer.valueOf(edtRepetirCada.getText().toString());

        db.open();

        Date dataInicial = null;
        long id_repetir = 0;
        int i = 0;

        String formatDataInicial = edtInicioEm.getText().toString();
        SimpleDateFormat sd = new SimpleDateFormat(("dd/MM/yyyy"));

        try {
            dataInicial = sd.parse(formatDataInicial);
        } catch (ParseException e) {
            e.printStackTrace();
        }


//Caso o evento seja diario ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        if (spn_repetir.getSelectedItemPosition() == 0) {


            for (i = 0; i < quantidadeDeOcorrencias; i++) {

                String dataFormatada = sd.format(dataInicial);
                Inserir(id_repetir, dataFormatada); //Insere no banco (na primeira inserção o id_repetição estara vazio).

                Cursor c = db.getCompromissos(); //busca compromissos no banco


                //se o id_repetição for igual a zero, seta o valor do id nele
                if (id_repetir == 0) {

                    if (c.moveToFirst()) {
                        do {
                            id_repetir = c.getLong(0);
                        } while (c.moveToNext());
                    }
                }

                //Na primeira inserção o id_repeticao ficara vazio, então entra nessa condição para setar valor nele
                if (c.moveToFirst()) {
                    do {

                        if (c.getLong(1) == 0) {

                            String nomeAlterar = c.getString(2);
                            String dataAlterar = c.getString(3);
                            String localAlterar = c.getString(4);
                            String descricaoAlterar = c.getString(5);
                            String participantesAlterar = c.getString(6);
                            long canceladoAlterar = 0;

                            db.alterarCompromisso(id_repetir, nomeAlterar, dataAlterar, localAlterar, descricaoAlterar, participantesAlterar, canceladoAlterar);
                        }

                    } while (c.moveToNext());
                }
                dataInicial.setDate(dataInicial.getDate() + quantidadeDeRepeticoes);
            }


//Caso o evento seja semanal ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        } else if (spn_repetir.getSelectedItemPosition() == 1) {


            for (i = 0; i < quantidadeDeOcorrencias; i++) {

                String dataFormatada = sd.format(dataInicial);
                Inserir(id_repetir, dataFormatada);

                Cursor c = db.getCompromissos();

                if (id_repetir == 0) {

                    if (c.moveToFirst()) {
                        do {
                            id_repetir = c.getLong(0);
                        } while (c.moveToNext());
                    }
                }


                if (c.moveToFirst()) {
                    do {

                        if (c.getLong(1) == 0) {

                            String nomeAlterar = c.getString(2);
                            String dataAlterar = c.getString(3);
                            String localAlterar = c.getString(4);
                            String descricaoAlterar = c.getString(5);
                            String participantesAlterar = c.getString(6);
                            long canceladoAlterar = 0;

                            db.alterarCompromisso(id_repetir, nomeAlterar, dataAlterar, localAlterar, descricaoAlterar, participantesAlterar, canceladoAlterar);
                        }

                    } while (c.moveToNext());
                }
                dataInicial.setDate(dataInicial.getDate() + (7 * quantidadeDeRepeticoes));
            }


//Caso o evento seja mensal ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        } else if (spn_repetir.getSelectedItemPosition() == 2) {


            for (i = 0; i < quantidadeDeOcorrencias; i++) {

                String dataFormatada = sd.format(dataInicial);
                Inserir(id_repetir, dataFormatada);

                Cursor c = db.getCompromissos();

                if (id_repetir == 0) {

                    if (c.moveToFirst()) {
                        do {
                            id_repetir = c.getLong(0);
                        } while (c.moveToNext());
                    }
                }


                if (c.moveToFirst()) {
                    do {

                        if (c.getLong(1) == 0) {

                            String nomeAlterar = c.getString(2);
                            String dataAlterar = c.getString(3);
                            String localAlterar = c.getString(4);
                            String descricaoAlterar = c.getString(5);
                            String participantesAlterar = c.getString(6);
                            long canceladoAlterar = 0;

                            db.alterarCompromisso(id_repetir, nomeAlterar, dataAlterar, localAlterar, descricaoAlterar, participantesAlterar, canceladoAlterar);
                        }

                    } while (c.moveToNext());
                }
                dataInicial.setMonth(dataInicial.getMonth() + quantidadeDeRepeticoes);
            }


//Caso o evento seja anual ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        } else if (spn_repetir.getSelectedItemPosition() == 3) {


            for (i = 0; i < quantidadeDeOcorrencias; i++) {

                String dataFormatada = sd.format(dataInicial);
                Inserir(id_repetir, dataFormatada);

                Cursor c = db.getCompromissos();

                if (id_repetir == 0) {

                    if (c.moveToFirst()) {
                        do {
                            id_repetir = c.getLong(0);
                        } while (c.moveToNext());
                    }
                }


                if (c.moveToFirst()) {
                    do {

                        if (c.getLong(1) == 0) {

                            String nomeAlterar = c.getString(2);
                            String dataAlterar = c.getString(3);
                            String localAlterar = c.getString(4);
                            String descricaoAlterar = c.getString(5);
                            String participantesAlterar = c.getString(6);
                            long canceladoAlterar = 0;

                            db.alterarCompromisso(id_repetir, nomeAlterar, dataAlterar, localAlterar, descricaoAlterar, participantesAlterar, canceladoAlterar);
                        }

                    } while (c.moveToNext());
                }
                dataInicial.setYear(dataInicial.getYear() + quantidadeDeRepeticoes);
            }
        }
        db.close();
        Toast.makeText(this, "Compromisso Cadastrado!", Toast.LENGTH_SHORT).show();
    }

// Radio Button = Terminar repetição inserindo quantidade de ocorrencias =====================================================================================================

    public void InserirRepeticoesPorOcorrencia() {

        int quantidadeDeOcorrencias = Integer.valueOf(edt_finalizar_por_repeticoes.getText().toString());
        int quantidadeDeRepeticoes = Integer.valueOf(edtRepetirCada.getText().toString());

        db.open();

        Date dataInicial = null;
        long id_repetir = 0;
        int i = 0;

        String formatDataInicial = edtInicioEm.getText().toString();
        SimpleDateFormat sd = new SimpleDateFormat(("dd/MM/yyyy"));

        try {
            dataInicial = sd.parse(formatDataInicial);
        } catch (ParseException e) {
            e.printStackTrace();
        }


//Caso o evento seja diario ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        if (spn_repetir.getSelectedItemPosition() == 0) {


            for (i = 0; i < quantidadeDeOcorrencias; i++) {

                String dataFormatada = sd.format(dataInicial);
                Inserir(id_repetir, dataFormatada);

                Cursor c = db.getCompromissos();

                if (id_repetir == 0) {

                    if (c.moveToFirst()) {
                        do {
                            id_repetir = c.getLong(0);
                        } while (c.moveToNext());
                    }
                }


                if (c.moveToFirst()) {
                    do {

                        if (c.getLong(1) == 0) {

                            String nomeAlterar = c.getString(2);
                            String dataAlterar = c.getString(3);
                            String localAlterar = c.getString(4);
                            String descricaoAlterar = c.getString(5);
                            String participantesAlterar = c.getString(6);
                            long canceladoAlterar = 0;

                            db.alterarCompromisso(id_repetir, nomeAlterar, dataAlterar, localAlterar, descricaoAlterar, participantesAlterar, canceladoAlterar);
                        }

                    } while (c.moveToNext());
                }
                dataInicial.setDate(dataInicial.getDate() + quantidadeDeRepeticoes);
            }


//Caso o evento seja semanal ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        } else if (spn_repetir.getSelectedItemPosition() == 1) {


            for (i = 0; i < quantidadeDeOcorrencias; i++) {

                String dataFormatada = sd.format(dataInicial);
                Inserir(id_repetir, dataFormatada);

                Cursor c = db.getCompromissos();

                if (id_repetir == 0) {

                    if (c.moveToFirst()) {
                        do {
                            id_repetir = c.getLong(0);
                        } while (c.moveToNext());
                    }
                }


                if (c.moveToFirst()) {
                    do {

                        if (c.getLong(1) == 0) {

                            String nomeAlterar = c.getString(2);
                            String dataAlterar = c.getString(3);
                            String localAlterar = c.getString(4);
                            String descricaoAlterar = c.getString(5);
                            String participantesAlterar = c.getString(6);
                            long canceladoAlterar = 0;

                            db.alterarCompromisso(id_repetir, nomeAlterar, dataAlterar, localAlterar, descricaoAlterar, participantesAlterar, canceladoAlterar);
                        }

                    } while (c.moveToNext());
                }
                dataInicial.setDate(dataInicial.getDate() + (7 * quantidadeDeRepeticoes));
            }


//Caso o evento seja mensal ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        } else if (spn_repetir.getSelectedItemPosition() == 2) {


            for (i = 0; i < quantidadeDeOcorrencias; i++) {

                String dataFormatada = sd.format(dataInicial);
                Inserir(id_repetir, dataFormatada);

                Cursor c = db.getCompromissos();

                if (id_repetir == 0) {

                    if (c.moveToFirst()) {
                        do {
                            id_repetir = c.getLong(0);
                        } while (c.moveToNext());
                    }
                }


                if (c.moveToFirst()) {
                    do {

                        if (c.getLong(1) == 0) {

                            String nomeAlterar = c.getString(2);
                            String dataAlterar = c.getString(3);
                            String localAlterar = c.getString(4);
                            String descricaoAlterar = c.getString(5);
                            String participantesAlterar = c.getString(6);
                            long canceladoAlterar = 0;

                            db.alterarCompromisso(id_repetir, nomeAlterar, dataAlterar, localAlterar, descricaoAlterar, participantesAlterar, canceladoAlterar);
                        }

                    } while (c.moveToNext());
                }
                dataInicial.setMonth(dataInicial.getMonth() + quantidadeDeRepeticoes);
            }


//Caso o evento seja anual ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        } else if (spn_repetir.getSelectedItemPosition() == 3) {


            for (i = 0; i < quantidadeDeOcorrencias; i++) {

                String dataFormatada = sd.format(dataInicial);
                Inserir(id_repetir, dataFormatada);

                Cursor c = db.getCompromissos();

                if (id_repetir == 0) {

                    if (c.moveToFirst()) {
                        do {
                            id_repetir = c.getLong(0);
                        } while (c.moveToNext());
                    }
                }


                if (c.moveToFirst()) {
                    do {

                        if (c.getLong(1) == 0) {

                            String nomeAlterar = c.getString(2);
                            String dataAlterar = c.getString(3);
                            String localAlterar = c.getString(4);
                            String descricaoAlterar = c.getString(5);
                            String participantesAlterar = c.getString(6);
                            long canceladoAlterar = 0;

                            db.alterarCompromisso(id_repetir, nomeAlterar, dataAlterar, localAlterar, descricaoAlterar, participantesAlterar, canceladoAlterar);
                        }

                    } while (c.moveToNext());
                }
                dataInicial.setYear(dataInicial.getYear() + quantidadeDeRepeticoes);
            }
        }
        db.close();
        Toast.makeText(this, "Compromisso Cadastrado!", Toast.LENGTH_SHORT).show();
    }


// Radio Button = Terminar repetição inserindo data final===========================================================================================

    public void InserirRepeticoesPorData() {

        db.open();

        int quantidadeDeRepeticoes = Integer.valueOf(edtRepetirCada.getText().toString());
        Date dataInicial = null, dataFinal = null;
        int resultado;
        long id_repetir = 0;

        String formatDataInicial = edtInicioEm.getText().toString();
        String formatDataFinal = edt_finalizar_por_data.getText().toString();
        SimpleDateFormat sd = new SimpleDateFormat(("dd/MM/yyyy"));

        try {
            dataInicial = sd.parse(formatDataInicial);
            dataFinal = sd.parse(formatDataFinal);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Caso o evento seja diario==========================
        if (spn_repetir.getSelectedItemPosition() == 0) {

            resultado = dataInicial.compareTo(dataFinal);

            while (resultado == -1 || resultado == 0) {

                String dataFormatada = sd.format(dataInicial);

                Inserir(id_repetir, dataFormatada);

                Cursor c = db.getCompromissos();

                if (id_repetir == 0) {

                    if (c.moveToFirst()) {
                        do {

                            id_repetir = c.getLong(0);

                        } while (c.moveToNext());
                    }
                }


                if (c.moveToFirst()) {
                    do {

                        if (c.getLong(1) == 0) {

                            String nomeAlterar = c.getString(2);
                            String dataAlterar = c.getString(3);
                            String localAlterar = c.getString(4);
                            String descricaoAlterar = c.getString(5);
                            String participantesAlterar = c.getString(6);
                            long canceladoAlterar = 0;

                            db.alterarCompromisso(id_repetir, nomeAlterar, dataAlterar, localAlterar, descricaoAlterar, participantesAlterar, canceladoAlterar);
                        }

                    } while (c.moveToNext());
                }


                dataInicial.setDate(dataInicial.getDate() + quantidadeDeRepeticoes);
                resultado = dataInicial.compareTo(dataFinal);
            }

            //Caso o evento seja semanal===========================
        } else if (spn_repetir.getSelectedItemPosition() == 1) {

            resultado = dataInicial.compareTo(dataFinal);

            while (resultado == -1 || resultado == 0) {

                String dataFormatada = sd.format(dataInicial);

                Inserir(id_repetir, dataFormatada);

                Cursor c = db.getCompromissos();

                if (id_repetir == 0) {

                    if (c.moveToFirst()) {
                        do {

                            id_repetir = c.getLong(0);

                        } while (c.moveToNext());
                    }
                }


                if (c.moveToFirst()) {
                    do {

                        if (c.getLong(1) == 0) {

                            String nomeAlterar = c.getString(2);
                            String dataAlterar = c.getString(3);
                            String localAlterar = c.getString(4);
                            String descricaoAlterar = c.getString(5);
                            String participantesAlterar = c.getString(6);
                            long canceladoAlterar = 0;

                            db.alterarCompromisso(id_repetir, nomeAlterar, dataAlterar, localAlterar, descricaoAlterar, participantesAlterar, canceladoAlterar);
                        }

                    } while (c.moveToNext());
                }


                dataInicial.setDate(dataInicial.getDate() + (7 * quantidadeDeRepeticoes));
                resultado = dataInicial.compareTo(dataFinal);
            }

            //Caso o evento seja mensal============================
        } else if (spn_repetir.getSelectedItemPosition() == 2) {


            resultado = dataInicial.compareTo(dataFinal);

            while (resultado == -1 || resultado == 0) {

                String dataFormatada = sd.format(dataInicial);

                Inserir(id_repetir, dataFormatada);

                Cursor c = db.getCompromissos();

                if (id_repetir == 0) {

                    if (c.moveToFirst()) {
                        do {

                            id_repetir = c.getLong(0);

                        } while (c.moveToNext());
                    }
                }


                if (c.moveToFirst()) {
                    do {

                        if (c.getLong(1) == 0) {

                            String nomeAlterar = c.getString(2);
                            String dataAlterar = c.getString(3);
                            String localAlterar = c.getString(4);
                            String descricaoAlterar = c.getString(5);
                            String participantesAlterar = c.getString(6);
                            long canceladoAlterar = 0;

                            db.alterarCompromisso(id_repetir, nomeAlterar, dataAlterar, localAlterar, descricaoAlterar, participantesAlterar, canceladoAlterar);
                        }

                    } while (c.moveToNext());
                }


                dataInicial.setMonth(dataInicial.getMonth() + quantidadeDeRepeticoes);
                resultado = dataInicial.compareTo(dataFinal);
            }

            //Caso o evento seja anual ============================
        } else if (spn_repetir.getSelectedItemPosition() == 3) {


            resultado = dataInicial.compareTo(dataFinal);

            while (resultado == -1 || resultado == 0) {

                String dataFormatada = sd.format(dataInicial);

                Inserir(id_repetir, dataFormatada);

                Cursor c = db.getCompromissos();

                if (id_repetir == 0) {

                    if (c.moveToFirst()) {
                        do {

                            id_repetir = c.getLong(0);

                        } while (c.moveToNext());
                    }
                }


                if (c.moveToFirst()) {
                    do {

                        if (c.getLong(1) == 0) {

                            String nomeAlterar = c.getString(2);
                            String dataAlterar = c.getString(3);
                            String localAlterar = c.getString(4);
                            String descricaoAlterar = c.getString(5);
                            String participantesAlterar = c.getString(6);
                            long canceladoAlterar = 0;

                            db.alterarCompromisso(id_repetir, nomeAlterar, dataAlterar, localAlterar, descricaoAlterar, participantesAlterar, canceladoAlterar);
                        }

                    } while (c.moveToNext());
                }

                dataInicial.setYear(dataInicial.getYear() + quantidadeDeRepeticoes);
                resultado = dataInicial.compareTo(dataFinal);
            }
        }

        db.close();
        Toast.makeText(this, "Compromisso Cadastrado!", Toast.LENGTH_SHORT).show();
    }

    public void Inserir(long id_repetir, String novaData) {
        db.insereCompromisso(id_repetir, novoNome, novaData, novoLocal, novaDescricao, novosParticipantes, novoTipo);
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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (v == edt_finalizar_por_repeticoes) {
            edt_finalizar_por_repeticoes.setText(null);
        }

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

            dataInicialDoEvento = data;

            SimpleDateFormat sd = new SimpleDateFormat(("dd/MM/yyyy"));
            String dataFormatada = sd.format(data);

            edt_finalizar_por_data.setText(dataFormatada);

        }
    }
}
