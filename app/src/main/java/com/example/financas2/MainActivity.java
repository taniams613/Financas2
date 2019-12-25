package com.example.financas2;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.*;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    ListView movimentos;
    public Cursor c;
    public Basedados bd;
    public int estado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        /*
        String idioma = sp.getString("idioma_preferido", "");
        atualizarLocale(idioma);
        */
        setContentView(R.layout.activity_main);

        movimentos = findViewById(R.id.lista);

        /* Acesso à base de dados,
         * leitura dos movimentos e
         * escrita dos valores nos itens da lista
         */



        String temp = sp.getString("filtro_inicial", "");
        System.out.println(temp);

        switch (estado) {
            case 0:
                filtro_semana();
                break;
            case 1:
                filtro_mes();
                break;
            case 2:
                filtro_todos();
                break;
            default:
                filtro_todos();
        }

        movimentos.setAdapter(new ListAdapter(this, c, 0));

        Button adicionar_movimento = findViewById(R.id.adicionar_movimento);
        adicionar_movimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Formulario.class);
                startActivity(intent);
            }
        });


    }

    public void atualizarLocale(String idioma){

        Locale locale = new Locale(idioma);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

    }

    public void onStart () {
        super.onStart();
/*
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String idioma = sp.getString("idioma_preferido", "");
        atualizarLocale(idioma);*/

        registerForContextMenu(movimentos);
        atualizarLista("");
    }



    public void atualizarLista(String extra){
        bd = new Basedados(this);

        c = bd.consultaLeitura("select sum(valor) as total from movimentos " + extra);
        c.moveToFirst();

        TextView total = findViewById(R.id.saldo);
        int valorTotal = c.getInt(c.getColumnIndex("total"));
        total.setText(valorTotal + " €");

        c = bd.consultaLeitura("select * from movimentos " + extra);
        c.moveToFirst();

        movimentos.setAdapter(new ListAdapter(this, c, 0));

    }

    public void onCreateContextMenu (ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        c.moveToPosition(info.position);

        String nome = c.getString(c.getColumnIndex("descricao"));
        int valor = c.getInt(c.getColumnIndex("valor"));

        menu.setHeaderTitle(nome + " " + valor + "€");

        menu.add(Menu.NONE,0, Menu.NONE, getResources().getText(R.string.editar));
        menu.add(Menu.NONE, 1, Menu.NONE, getResources().getText(R.string.eliminar));

    }

    public boolean onContextItemSelected(MenuItem item) {

        int movimento = c.getInt(c.getColumnIndex("_id"));

        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Formulario.class);
                intent.putExtra("id_do_movimento", movimento);
                startActivity(intent);

                break;
            case 1:

                bd.consultaEscrita("delete from movimentos where _id = " + movimento);
                atualizarLista("");

                break;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.filtro_semana:
                filtro_semana();
                return true;
            case R.id.filtro_mes:
                filtro_mes();
                return true;
            case R.id.filtro_todos:
                filtro_todos();
                return true;
            case R.id.settings:
                intent = new Intent();
                intent.setClass(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.categorias:
                intent = new Intent();
                intent.setClass(MainActivity.this, CategoriasActivity.class);
                startActivity(intent);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void filtro_semana() {
        atualizarLista("where data >= date('now', '-6 days', 'weekday 1') order by _id desc");
        estado = 0;
    }

    private void filtro_mes() {
        atualizarLista("where data >= date('now', 'start of month') order by _id desc");
        estado = 1;
    }

    private void filtro_todos() {
        atualizarLista("");
        estado = 2;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("estado", estado);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        estado = savedInstanceState.getInt("estado");

        if (estado > -1) {
            switch (estado) {
                case 0:
                    filtro_semana();
                    break;
                case 1:
                    filtro_mes();
                    break;
                case 2:
                    filtro_todos();
                    break;
                default:
                    filtro_todos();
            }
        }
    }
}
