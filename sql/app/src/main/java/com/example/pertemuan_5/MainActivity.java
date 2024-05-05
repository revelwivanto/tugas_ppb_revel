package com.example.pertemuan_5;

import android.os.Bundle;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText nrp, nama;
    private Button simpan, ambildata, updatedata, deletedata;
    private SQLiteDatabase dbku;
    private SQLiteOpenHelper Opendb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nrp = (EditText)findViewById(R.id.nrp);
        nama = (EditText) findViewById(R.id.nama);
        simpan = (Button) findViewById(R.id.Simpan);
        ambildata = (Button) findViewById(R.id.ambildata);
        updatedata = (Button) findViewById(R.id.updatedata);
        deletedata = (Button) findViewById(R.id.deletedata);
        simpan.setOnClickListener(operasi);
        ambildata.setOnClickListener(operasi);
        updatedata.setOnClickListener(operasi);
        deletedata.setOnClickListener(operasi);
        Opendb = new SQLiteOpenHelper(this, "db.sql", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {}
            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
        };
        dbku = Opendb.getWritableDatabase();
        dbku.execSQL("create table if not exists mhs(nrp TEXT, nama TEXT);");
    }

    @Override
    protected void onStop() {
        dbku.close();
        Opendb.close();
        super.onStop();
    }

    View.OnClickListener operasi = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.Simpan) {
                simpan();
            } else if (id == R.id.ambildata) {
                ambildata();
            } else if (id == R.id.updatedata) {
                update();
            } else if (id == R.id.deletedata) {
                delete();
            }
        }

    };

    private void simpan() {
        String nrpText = nrp.getText().toString().trim();
        String namaText = nama.getText().toString().trim();

        if (nrpText.isEmpty()) {
            Toast.makeText(this, "NRP cannot be empty", Toast.LENGTH_LONG).show();
            return; // Exit the method early if NRP is empty
        }

        ContentValues dataku = new ContentValues();
        dataku.put("nrp", nrpText);
        dataku.put("nama", namaText);
        long result = dbku.insert("mhs", null, dataku);
        if (result != -1) {
            Toast.makeText(this, "Data Tersimpan", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_LONG).show();
        }
    }

    private void update() {
        String nrpText = nrp.getText().toString().trim();
        String namaText = nama.getText().toString().trim();

        if (nrpText.isEmpty()) {
            Toast.makeText(this, "NRP cannot be empty", Toast.LENGTH_LONG).show();
            return; // Exit the method early if NRP is empty
        }

        ContentValues dataku = new ContentValues();
        dataku.put("nama", namaText);
        int rowsAffected = dbku.update("mhs", dataku, "nrp=?", new String[]{nrpText});
        if (rowsAffected > 0) {
            Toast.makeText(this, "Data Terupdate", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Gagal mengupdate data", Toast.LENGTH_LONG).show();
        }
    }


    private void ambildata() {
        Cursor cur = dbku.rawQuery("SELECT * FROM mhs WHERE nrp=?", new String[]{nrp.getText().toString()});
        if (cur.moveToFirst()) {
            int namaIndex = cur.getColumnIndexOrThrow("nama");
            String namaValue = cur.getString(namaIndex);
            Toast.makeText(this, "Data Ditemukan: " + namaValue, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Data Tidak Ditemukan", Toast.LENGTH_LONG).show();
        }
        cur.close();
    }

    private void delete() {
        dbku.delete("mhs", "nrp='" + nrp.getText().toString() + "'", null);
        Toast.makeText(this, "Data Terhapus", Toast.LENGTH_LONG).show();
    }

}