package com.example.financas2;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Basedados {
    private Conexao conexaobd;
    private SQLiteDatabase bd;
    private String[] cats;

    private class Conexao extends SQLiteOpenHelper {
        public Conexao(Context context) {
            super(context, "basedados", null, 1);

            Resources res = context.getResources();
            cats = new String[] {
                    res.getString(R.string.educacao),
                    res.getString(R.string.alimentacao),
                    res.getString(R.string.casa),
                    res.getString(R.string.carro),
                    res.getString(R.string.divertimento),
                    res.getString(R.string.tecnologia),
                    res.getString(R.string.pessoal)
            };
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("create table movimentos (_id integer primary key, descricao varchar (100), categoria varchar (100), valor real, data varchar (100) )");
            db.execSQL("create table categorias (_id integer primary key, nome varchar (100))");

            for(int i = 0; i < cats.length; i++) {
                db.execSQL("insert into categorias (nome) values ('" + cats[i] + "')");
            }

            /*
            db.execSQL("insert into movimentos (descricao, categoria, valor, data) values ('faculdade', 'Educação', -1800, '2015-05-28')");
            db.execSQL("insert into movimentos (descricao, categoria, valor, data) values ('ordenado', 'null', 5000, '2015-06-01')");
            db.execSQL("insert into movimentos (descricao, categoria, valor, data) values ('embraiagem', 'Carro', -250, '2015-06-08')");
            db.execSQL("insert into movimentos (descricao, categoria, valor, data) values ('venda olx', 'null', 120, '2015-05-24')");
            db.execSQL("insert into movimentos (descricao, categoria, valor, data) values ('renda', 'Casa', -700, '2015-06-01')");
            */
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    /**
     * Converte uma data do formato base de dados (aaaa-mm-dd) para o
     * formato português (dd-mm-aaaa)
     *
     * @param date_bd data em base de dados (aaaa-mm-dd)
     * @return Devolve a data em formato português (dd-mm-aaaa)
     * @throws java.text.ParseException
     */

    public static String dateToPT(String date_bd) throws ParseException {
        SimpleDateFormat formato_bd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formato_pt = new SimpleDateFormat("dd-MM-yyyy");

        Date newdate = formato_bd.parse(date_bd);

        return formato_pt.format(newdate);
    }

    /**
     * Converte uma data do formato português (dd-mm-aaaa) para o
     * formato base de dados (aaaa-mm-dd)
     *
     * @param date_pt data em português (dd-mm-aaaa)
     * @return Devolve a data em formato base de dados (aaaa-mm-dd)
     * @throws ParseException
     */

    public static String dateToBD(String date_pt) throws ParseException {
        SimpleDateFormat formato_bd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formato_pt = new SimpleDateFormat("dd-MM-yyyy");

        Date newdate = formato_pt.parse(date_pt);

        return formato_bd.format(newdate);
    }


    public Basedados(Context contexto_inicial) {
        conexaobd = new Conexao(contexto_inicial);
        bd = conexaobd.getWritableDatabase();
    }

    public void fechar() {
        conexaobd.close();
    }

    public void consultaEscrita(String consulta) {
        bd.execSQL(consulta);
    }

    public Cursor consultaLeitura(String consulta) {
        return bd.rawQuery(consulta, null);
    }

}
