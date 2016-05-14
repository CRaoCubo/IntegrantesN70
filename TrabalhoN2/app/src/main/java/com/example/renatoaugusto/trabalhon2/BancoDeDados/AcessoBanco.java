package com.example.renatoaugusto.trabalhon2.BancoDeDados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Renato Augusto on 14/05/2016.
 */
public class AcessoBanco {


    public static final String CODIGO = "codigo";
    public static final String NOME = "nome";
    private static final String NOME_BD = "empresa";
    private static final String NOME_TABELA = "pessoas";

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


//Novo Compromisso ---------------------------------------------------------------------------------

    public long insereCompromisso(String name)
    {
        ContentValues values = new ContentValues();
        values.put(NOME, name);
        return db.insert(NOME_TABELA, null, values);
    }
}
