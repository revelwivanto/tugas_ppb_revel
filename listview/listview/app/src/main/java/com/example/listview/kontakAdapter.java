package com.example.listview;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class kontakAdapter extends ArrayAdapter<kontak> {
    private static class ViewHolder {
        TextView nama;
        TextView nohp;
    }

    public kontakAdapter(Context context, int resource, List<kontak> objects) {
        super(context, resource, objects);
    }

    public View getView(int position, View ConvertView, ViewGroup parent) {
        kontak dtkontak=getItem(position);
        ViewHolder viewKontak;

        if (ConvertView == null) {
            viewKontak = new ViewHolder();
            ConvertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user,parent,false);
            viewKontak.nama = (TextView) ConvertView.findViewById(R.id.tNama);
            viewKontak.nohp = (TextView) ConvertView.findViewById(R.id.tNoHP);

            ConvertView.setTag(viewKontak);

            //Button btn = (Button) ConvertView.findViewById(R.id.edit);
            //btn.setTag(position);
            //btn.setOnClickListener(this);
        } else {
            viewKontak = (ViewHolder) ConvertView.getTag();
        }

        viewKontak.nama.setText(dtkontak.getNama());
        viewKontak.nohp.setText(dtkontak.getNoHp());
        return ConvertView;
    }
}
