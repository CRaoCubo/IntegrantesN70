package com.example.renatoaugusto.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;


public class AcessoBanco {


    public static final String CODIGO = "codigo";
    public static final String NOME = "nome";
    public static final String DATA = "data";
    public static final String LOCAL = "local";
    public static final String DESCRICAO = "descricao";
    public static final String PARTICIPANTES = "participantes";
    private static final String NOME_BD = "agenda";
    private static final String NOME_TABELA = "compromissos";

    private final Context context;

    private Conexao conector;
    private SQLiteDatabase db;

    public AcessoBanco(Context ctx) {
        this.context = ctx;
        conector = new Conexao(context);
    }


    public AcessoBanco open() throws SQLException {
        db = conector.getWritableDatabase();
        return this;
    }

    public void close() {
        conector.close();
    }

    public long insereCompromisso(String novoNome, String novaData, String novoLocal, String novaDescricao, String novosParticipantes) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(NOME, novoNome);
        initialValues.put(DATA, novaData);
        initialValues.put(LOCAL, novoLocal);
        initialValues.put(DESCRICAO, novaDescricao);
        initialValues.put(PARTICIPANTES, novosParticipantes);
        return db.insert(NOME_TABELA, null, initialValues);
    }

    public void alterarCompromisso(long id, String nome, String data, String local, String descricao, String participantes) {
        ContentValues args = new ContentValues();
        args.put(NOME, nome);
        args.put(DATA, data);
        args.put(LOCAL, local);
        args.put(DESCRICAO, descricao);
        args.put(PARTICIPANTES, participantes);
        db.update(NOME_TABELA, args, " codigo = ? ", new String[]{String.valueOf(id)});
    }

    public boolean remmovePessoa(long cod) {
        return db.delete(NOME_TABELA, CODIGO + "=" + cod, null) > 0;
    }

    public Cursor getCompromissos() {
        return db.query(NOME_TABELA, new String[]{CODIGO, NOME, DATA, LOCAL, DESCRICAO, PARTICIPANTES}, null, null, null, null, null);
    }

}
