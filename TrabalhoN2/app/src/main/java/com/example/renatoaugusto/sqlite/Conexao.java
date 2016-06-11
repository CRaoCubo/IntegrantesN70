package com.example.renatoaugusto.sqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class Conexao extends SQLiteOpenHelper {

    private static final String TAG = "DBAdapter";

    private static final String NOME_BD = "agenda";
    private static final int DATABASE_VERSION = 1;


    Conexao(Context context) {
        super(context, NOME_BD, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        StringBuilder builder = new StringBuilder(); //Concatenar o Script
        builder.append("create table compromissos ( ");
        builder.append("codigo integer not null ");
        builder.append("primary key autoincrement, ");
        builder.append("codigo_repeticao integer, ");
        builder.append("nome text, ");
        builder.append("data text, ");
        builder.append("local text, ");
        builder.append("descricao text, ");
        builder.append("participantes text, ");
        builder.append("tipo text, ");
        builder.append("cancelado text ");
        builder.append("); ");

        db.execSQL(builder.toString());

        StringBuilder build = new StringBuilder(); //Concatenar o Script
        build.append("create table tipos ( ");
        build.append("codigo_tipo integer not null ");
        build.append("primary key autoincrement, ");
        build.append("nome_tipo text ");
        build.append("); ");

        db.execSQL(build.toString());

        StringBuilder b = new StringBuilder(); //Concatenar o Script
        b.append("create table tipos_aux ( ");
        b.append("codigo_tipo_aux integer not null ");
        b.append("primary key autoincrement, ");
        b.append("nome_tipo_aux text ");
        b.append("); ");

        db.execSQL(b.toString());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Atualizando banco de dados da versão " + oldVersion + " para "
                + newVersion + ". Todos os dados serão destruidos.");
        db.execSQL("DROP TABLE IF EXISTS compromissos");
        onCreate(db);
    }
}
