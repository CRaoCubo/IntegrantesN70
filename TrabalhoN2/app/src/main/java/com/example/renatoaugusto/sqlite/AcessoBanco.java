package com.example.renatoaugusto.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class AcessoBanco {

    public static final String CODIGO = "codigo";
    public static final String CODIGO_REPETICAO = "codigo_repeticao";
    public static final String NOME = "nome";
    public static final String DATA = "data";
    public static final String LOCAL = "local";
    public static final String DESCRICAO = "descricao";
    public static final String PARTICIPANTES = "participantes";
    public static final String TIPO = "tipo";
    public static final String CANCELADO = "cancelado";
    public static final String CODIGO_TIPO = "codigo_tipo";
    public static final String CODIGO_TIPO_AUX = "codigo_tipo_aux";
    public static final String NOME_TIPO = "nome_tipo";
    public static final String NOME_TIPO_AUX = "nome_tipo_aux";
    private static final String TABELA_COMPROMISSOS = "compromissos";
    private static final String TABELA_TIPOS = "tipos";
    private static final String TABELA_TIPOS_AUX = "tipos_aux";

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

    public void insereCompromisso(long novoCodigoRepeticao, String novoNome, String novaData, String novoLocal, String novaDescricao, String novosParticipantes, String novoTipo) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(CODIGO_REPETICAO, novoCodigoRepeticao);
        initialValues.put(NOME, novoNome);
        initialValues.put(DATA, novaData);
        initialValues.put(LOCAL, novoLocal);
        initialValues.put(DESCRICAO, novaDescricao);
        initialValues.put(PARTICIPANTES, novosParticipantes);
        initialValues.put(TIPO, novoTipo);
        initialValues.put(CANCELADO, 0);
        db.insert(TABELA_COMPROMISSOS, null, initialValues);
    }


    public long insereNovoTipo(String novoTipo) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(NOME_TIPO, novoTipo);
        return db.insert(TABELA_TIPOS, null, initialValues);
    }

    public long insereNovoTipoAux(String novoTipo) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(NOME_TIPO_AUX, novoTipo);
        return db.insert(TABELA_TIPOS_AUX, null, initialValues);
    }

    public void alterarCompromisso(long id, String nome, String data, String local, String descricao, String participantes, long cancelado) {
        ContentValues args = new ContentValues();
        args.put(CODIGO_REPETICAO, id);
        args.put(NOME, nome);
        args.put(DATA, data);
        args.put(LOCAL, local);
        args.put(DESCRICAO, descricao);
        args.put(PARTICIPANTES, participantes);
        args.put(CANCELADO, cancelado);
        db.update(TABELA_COMPROMISSOS, args, " codigo = ? ", new String[]{String.valueOf(id)});
    }

    public void cancelarCompromisso(long id, String nome, long cancelado) {
        ContentValues args = new ContentValues();
        args.put(NOME, nome + " - CANCELADO");
        args.put(CANCELADO, cancelado);
        db.update(TABELA_COMPROMISSOS, args, " codigo = ? ", new String[]{String.valueOf(id)});
    }

    public boolean expurgarCompromisso(long cod) {
        return db.delete(TABELA_COMPROMISSOS, CODIGO + "=" + cod, null) > 0;
    }

    public boolean deletarTipo(long cod) {
        return db.delete(TABELA_TIPOS, CODIGO_TIPO + "=" + cod, null) > 0;
    }

    public Cursor getCompromissos() {
        return db.query(TABELA_COMPROMISSOS, new String[]{CODIGO, CODIGO_REPETICAO, NOME, DATA, LOCAL, DESCRICAO, PARTICIPANTES, TIPO, CANCELADO}, null, null, null, null, null);
    }

    public Cursor getTipos() {
        return db.query(TABELA_TIPOS, new String[]{CODIGO_TIPO, NOME_TIPO}, null, null, null, null, null);
    }

    public Cursor getTiposAux() {
        return db.query(TABELA_TIPOS_AUX, new String[]{CODIGO_TIPO_AUX, NOME_TIPO_AUX}, null, null, null, null, null);
    }
}

