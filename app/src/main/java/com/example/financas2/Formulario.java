package com.example.financas2;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Formulario extends AppCompatActivity {

    Basedados basedados;
    Cursor c;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_formulario);

        basedados = new Basedados(this);

        Intent intent_atual = getIntent();
        id = intent_atual.getIntExtra("id_do_movimento", 0);

        SimpleDateFormat formato_pt = new SimpleDateFormat("dd-MM-yyyy");
        Calendar data = Calendar.getInstance();

        EditText et_data = findViewById(R.id.data);
        et_data.setText(formato_pt.format(data.getTime()));

        initSpinner();

        RadioButton rbReceita = findViewById(R.id.rb_receita);
        rbReceita.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    TextView categoria = findViewById(R.id.categoria);
                    categoria.setVisibility(View.GONE);
                    Spinner categorias = findViewById(R.id.categorias);
                    categorias.setVisibility(View.GONE);
                } else {
                    TextView categoria = findViewById(R.id.categoria);
                    categoria.setVisibility(View.VISIBLE);
                    Spinner categorias = findViewById(R.id.categorias);
                    categorias.setVisibility(View.VISIBLE);
                }
            }
        });


        Button adicionar = findViewById(R.id.b_add_alt);
        adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                concluirMovimento();
            }
        });

        if (id != 0) {
            //editar
            mostraInfo(id);
            adicionar.setText(getResources().getText(R.string.modificar));
        }
    }

    private void initSpinner() {
        final Spinner sp = findViewById(R.id.categorias);
        c = basedados.consultaLeitura("Select nome from categorias");
        ArrayList<String> nomes_cat = new ArrayList<String>();

        if (c.getCount() > 0) {
            c.moveToFirst();
            int catidx = c.getColumnIndex("nome");
            do {
                nomes_cat.add(c.getString(catidx));
            } while (c.moveToNext() != false);

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nomes_cat);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp.setAdapter(adapter);

        c.close();
    }

    public void mostraInfo(int id){

        c = basedados.consultaLeitura("select * from movimentos where _id = " + id);
        c.moveToFirst();

        String nome = c.getString(c.getColumnIndex("descricao"));
        EditText et_nome = findViewById(R.id.nome);
        et_nome.setText(nome);

        String cat = c.getString(c.getColumnIndex("categoria"));
        Spinner sp_cat = findViewById(R.id.categorias);

        int i = 0;
        while (!sp_cat.getItemAtPosition(i).toString().equalsIgnoreCase(cat)) {
            i++;
        }
        sp_cat.setSelection(i);


        double valor = c.getDouble(c.getColumnIndex("valor"));
        EditText et_val = findViewById(R.id.val);
        RadioButton rb;
        if (valor < 0) {
            rb = findViewById(R.id.rb_despesa);
            et_val.setText(String.valueOf(valor).substring(1));
        }
        else {
            rb = findViewById(R.id.rb_receita);
            et_val.setText(String.valueOf(valor));
        }
        rb.performClick();


        String data = c.getString(c.getColumnIndex("data"));
        EditText et_data = findViewById(R.id.data);
        et_data.setText(data);

    }

    public void concluirMovimento(){
        EditText et_nome = findViewById(R.id.nome);
        String nome = et_nome.getText().toString();

        Spinner sp_categorias = findViewById(R.id.categorias);
        String cat = sp_categorias.getSelectedItem().toString();

        EditText et_valor = findViewById(R.id.val);
        double valor = 0;
        if (et_valor.getText().length() > 0) {
            valor = Double.parseDouble(et_valor.getText().toString());
        }

        EditText et_data = findViewById(R.id.data);
        String data = null;
        try {
            data = Basedados.dateToBD(et_data.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        RadioGroup rg_tipo = findViewById(R.id.rgtipo);
        switch (rg_tipo.getCheckedRadioButtonId()) {
            case R.id.rb_despesa:
                valor *= -1;
                break;
        }

        if(rg_tipo.getCheckedRadioButtonId() == R.id.rb_despesa)
            valor *= -1;


        String sqlcmd = id==0 ? "insert" : "update";

        basedados.consultaEscrita(sqlcmd+" into movimentos (descricao, categoria, valor, data) values ('" + nome + "', '" + cat + "', " + valor + ", '" + data + "');");
        Toast.makeText(Formulario.this, "Movimento inserido", Toast.LENGTH_SHORT).show();

        finish();
    }
}
