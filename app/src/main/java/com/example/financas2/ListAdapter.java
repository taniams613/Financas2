package com.example.financas2;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;

public class ListAdapter extends CursorAdapter {
    public ListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.lista_movimentos, parent, false);
        bindView(v, context, cursor);

        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView seta = view.findViewById(R.id.img_seta);
        double valor_movimento = cursor.getDouble(cursor.getColumnIndex("valor"));

        if (valor_movimento > 0) {
            seta.setImageResource(R.drawable.seta_verde);
        } else {
            seta.setImageResource(R.drawable.seta_vermelha);
        }


        TextView descricao = view.findViewById(R.id.descricao);
        descricao.setText(cursor.getString(cursor.getColumnIndex("descricao")));

        TextView data = view.findViewById(R.id.data);
        try {
            data.setText(Basedados.dateToPT(cursor.getString(cursor.getColumnIndex("data"))));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView valor = view.findViewById(R.id.valor);
        valor.setText(String.valueOf(valor_movimento));

        TextView tv_categoria = view.findViewById(R.id.mostra_categoria);
        if (valor_movimento < 0) {
            tv_categoria.setText(cursor.getString(cursor.getColumnIndex("categoria")));
        }
    }
}
