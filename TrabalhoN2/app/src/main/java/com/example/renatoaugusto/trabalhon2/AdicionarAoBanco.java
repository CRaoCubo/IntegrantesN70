package com.example.renatoaugusto.trabalhon2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;


public class AdicionarAoBanco {

    private SQLiteDatabase Sql;

    public AdicionarAoBanco(SQLiteDatabase Sql) {

        this.Sql = Sql;
    }

    public ArrayAdapter<Entidades> buscaCompromissos(Context c) {

        ArrayAdapter<Entidades> adpDados = new ArrayAdapter<Entidades>(c, android.R.layout.simple_list_item_1);

        Cursor cursor = Sql.query("AGENDA", null, null, null, null, null, null);

        if (cursor.getCount() > 0) {

            cursor.moveToFirst();// Posiciona no primeiro registro

            do {

                Entidades etd = new Entidades();
                etd.setId(cursor.getLong(0));
                etd.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
                etd.setData(cursor.getString(cursor.getColumnIndex("DATA")));
                etd.setLocal(cursor.getString(cursor.getColumnIndex("LOCAL")));
                etd.setDescricao(cursor.getString(cursor.getColumnIndex("DESCRICAO")));
                etd.setParticipantes(cursor.getString(cursor.getColumnIndex("PARTICIPANTES")));
                etd.setTipo(cursor.getString(cursor.getColumnIndex("TIPO")));
                adpDados.add(etd);

            } while (cursor.moveToNext());
        }

        return adpDados;
    }


    public ArrayAdapter<EntidadesCanceladas> buscaCompromissosCancelados(Context c) {

        ArrayAdapter<EntidadesCanceladas> adpDados = new ArrayAdapter<EntidadesCanceladas>(c, android.R.layout.simple_list_item_1);

        Cursor cursor = Sql.query("AGENDACANCELADA", null, null, null, null, null, null);

        if (cursor.getCount() > 0) {

            cursor.moveToFirst();// Posiciona no primeiro registro

            do {

                EntidadesCanceladas etd = new EntidadesCanceladas();
                etd.setId(cursor.getLong(0));
                etd.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
                adpDados.add(etd);

            } while (cursor.moveToNext());
        }

        return adpDados;
    }


    public ArrayAdapter<Entidades> buscaTipo(Context c) {

        ArrayAdapter<Entidades> adpDados = new ArrayAdapter<Entidades>(c, android.R.layout.simple_list_item_1);

        Cursor cursor = Sql.query("TIPO", null, null, null, null, null, null);

        if (cursor.getCount() > 0) {

            cursor.moveToFirst();// Posiciona no primeiro registro

            do {

                Entidades etd = new Entidades();
                etd.setId(cursor.getLong(0));
                etd.setAdicionarTipo(cursor.getString(cursor.getColumnIndex("NOMETIPO")));
                adpDados.add(etd);

            } while (cursor.moveToNext());
        }

        return adpDados;
    }


    public void insereCompromisso(Entidades entidade) {

        ContentValues values = new ContentValues();
        values.put("NOME", entidade.getNome());
        values.put("DATA", entidade.getData());
        values.put("LOCAL", entidade.getLocal());
        values.put("DESCRICAO", entidade.getDescricao());
        values.put("PARTICIPANTES", entidade.getParticipantes());
        values.put("TIPO", entidade.getTipo());
        Sql.insertOrThrow("AGENDA", null, values);

    }

    public void cancelarCompromisso(EntidadesCanceladas entidadesCanceladas, long id) {

        Sql.delete("AGENDA", "ID = ?", new String[]{String.valueOf(id)});//Deleta a tarefa da lista de compromissos ativos
        ContentValues values = new ContentValues();
        values.put("NOME", entidadesCanceladas.getNome());
        Sql.insertOrThrow("AGENDACANCELADA", null, values);

    }

    public void alteraCompromisso(Entidades entidade, long id) {

        ContentValues values = new ContentValues();
        values.put("NOME", entidade.getNome());
        values.put("DATA", entidade.getData());
        values.put("LOCAL", entidade.getLocal());
        values.put("DESCRICAO", entidade.getDescricao());
        values.put("PARTICIPANTES", entidade.getParticipantes());
        values.put("TIPO", entidade.getTipo());
        Sql.update("AGENDA", values, " ID = ? ", new String[]{String.valueOf(id)});
    }

    public void insereNovoTipo(Entidades entidade) {

        ContentValues values = new ContentValues();
        values.put("NOMETIPO", entidade.getAdicionarTipo());
        Sql.insertOrThrow("TIPO", null, values);

    }
}
