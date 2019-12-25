package com.example.financas2;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;

public class NovaCategoriaActivity extends AppCompatActivity {

    Basedados basedados;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lista_categorias);


        basedados = new Basedados(this);

        Button adicionar = findViewById(R.id.add_nova_cat);
        adicionar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                TextView caixa_de_nova_categoria = findViewById(R.id.caixa_categoria);
                basedados.consultaEscrita ("insert into categorias (nome) values ('"+caixa_de_nova_categoria.getText().toString()+"')");
                finish();

            }
        });

        Intent intent_atual = getIntent();
        final int id = intent_atual.getIntExtra("id_da_categoria", 0);

        if (id != 0) {
            //editar
            mostraInfo(id);

            adicionar.setText(getResources().getText(R.string.alt_nova_cat));
            adicionar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EditText et_nome = findViewById(R.id.caixa_categoria);
                    String nome = et_nome.getText().toString();


                    basedados.consultaEscrita("update categorias SET nome = '"+ nome + "' where _id = "+id);
                    finish();
                }
            });

        }


    }

    public void mostraInfo(int id){

        c = basedados.consultaLeitura("select * from categorias where _id = " + id);
        c.moveToFirst();

        String nome = c.getString(c.getColumnIndex("nome"));
        EditText et_nome = findViewById(R.id.caixa_categoria);
        et_nome.setText(nome);




    }
}
