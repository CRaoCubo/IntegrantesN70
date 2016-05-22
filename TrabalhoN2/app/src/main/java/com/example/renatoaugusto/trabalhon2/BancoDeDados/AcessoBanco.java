package com.example.renatoaugusto.trabalhon2.BancoDeDados;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class AcessoBanco extends SQLiteOpenHelper {


    public AcessoBanco(Context c) {

        super(c, "COMPROMISSOS", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        StringBuilder builder = new StringBuilder(); //Concatenar o Script
        builder.append("CREATE TABLE IF NOT EXISTS AGENDA ( ");
        builder.append("ID                 INTEGER       NOT NULL ");
        builder.append("PRIMARY KEY AUTOINCREMENT, ");
        builder.append("NOME               VARCHAR (100), ");
        builder.append("DATA               VARCHAR (100), ");
        builder.append("LOCAL              VARCHAR (100), ");
        builder.append("DESCRICAO          VARCHAR (100), ");
        builder.append("PARTICIPANTES      VARCHAR (100), ");
        builder.append("TIPO               VARCHAR (100) ");
        builder.append("); ");

        db.execSQL(builder.toString());

        StringBuilder build = new StringBuilder(); //Concatenar o Script
        build.append("CREATE TABLE IF NOT EXISTS TIPO ( ");
        build.append("ID                 INTEGER       NOT NULL ");
        build.append("PRIMARY KEY AUTOINCREMENT, ");
        build.append("NOMETIPO           VARCHAR (100) ");
        build.append("); ");

        db.execSQL(build.toString());


    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
