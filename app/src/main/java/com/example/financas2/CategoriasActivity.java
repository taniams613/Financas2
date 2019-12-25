package com.example.financas2;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


public class CategoriasActivity extends AppCompatActivity {

    public Cursor c;
    Basedados bd;
    ListView lista_de_categorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_categorias);


        bd = new Basedados(this);

        atualizarCategorias();

        Button add_cat = (findViewById(R.id.b_add_alt_cat));
        add_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CategoriasActivity.this, NovaCategoriaActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onStart () {
        super.onStart();
        registerForContextMenu(lista_de_categorias);
        atualizarCategorias();
    }

    public void atualizarCategorias(){
        c = bd.consultaLeitura("select * from categorias");
        c.moveToFirst();
        String[] categorias = new String[c.getCount()];

        int i = 0;
        if(c.getCount() > 0){

            do {
                categorias[i] = c.getString(c.getColumnIndex("nome"));
                i++;
            }
            while(c.moveToNext() == true);

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.item_categoria,R.id.nome_cat, categorias);
        lista_de_categorias = findViewById(R.id.lista_categorias);
        lista_de_categorias.setAdapter(adapter);

    }

    public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        c.moveToPosition(info.position);

        String nome = c.getString(c.getColumnIndex("nome"));

        menu.add(Menu.NONE,0, Menu.NONE, getResources().getText(R.string.editar));
        menu.add(Menu.NONE, 1, Menu.NONE, getResources().getText(R.string.eliminar));

    }

    public boolean onContextItemSelected(MenuItem item) {

        int categoria = c.getInt(c.getColumnIndex("_id"));

        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent();
                intent.setClass(CategoriasActivity.this, NovaCategoriaActivity.class);
                intent.putExtra("id_da_categoria", categoria);
                startActivity(intent);

                break;
            case 1:

              bd.consultaEscrita("delete from categorias where _id = " + categoria);
              atualizarCategorias();
              break;
        }
        return false;
    }
}
