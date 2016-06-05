package com.example.renatoaugusto.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


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

    public AcessoBanco(Context ctx)
    {
        this.context = ctx;
        conector = new Conexao(context);
    }


    public AcessoBanco open() throws SQLException
    {
        db = conector.getWritableDatabase();
        return this;
    }

    public void close()
    {
        conector.close();
    }

    public long insereCompromisso(String novoNome, String novaData, String novoLocal, String novaDescricao, String novosParticipantes)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(NOME, novoNome);
        initialValues.put(DATA, novaData);
        initialValues.put(LOCAL, novoLocal);
        initialValues.put(DESCRICAO, novaDescricao);
        initialValues.put(PARTICIPANTES, novosParticipantes);
        return db.insert(NOME_TABELA, null, initialValues);
    }

    public boolean remmovePessoa(long cod)
    {
        return db.delete(NOME_TABELA, CODIGO + "=" + cod, null) > 0;
    }

    public Cursor getCadastros()
    {
        return db.query(NOME_TABELA, new String[] {CODIGO, NOME, DATA, LOCAL, DESCRICAO, PARTICIPANTES}, null, null, null, null, null);
    }

    public Cursor getPessoa(long cod) throws SQLException
    {
        Cursor mCursor =
                db.query(true, NOME_TABELA, new String[] {CODIGO, NOME}, CODIGO + "=" + cod, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updatePessoa(long cod, String name, String email)
    {
        ContentValues args = new ContentValues();
        args.put(NOME, name);
        return db.update(NOME_BD, args, CODIGO + "=" + cod, null) > 0;
    }

}
