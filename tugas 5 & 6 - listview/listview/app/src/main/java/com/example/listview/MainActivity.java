package com.example.listview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView lv;
    private ArrayAdapter<String> kontak;
    private ImageView add;
    private kontakAdapter kAdapter;
    private SQLiteDatabase dbku;
    private SQLiteOpenHelper dbopen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.listView);
        lv.setOnItemClickListener(this);

        ArrayList<kontak> listKontak = new ArrayList<kontak>();
        kAdapter = new kontakAdapter(this,0,listKontak);

        lv.setAdapter(kAdapter);

        Button badd = (Button) findViewById(R.id.add_button);
        Button bdelete = (Button) findViewById(R.id.delete_button);
        Button bsearch = (Button) findViewById(R.id.search_button);
        Button bexplore = (Button) findViewById(R.id.explore_button);
        EditText search = (EditText) findViewById(R.id.search_bar);

        badd.setOnClickListener(this);
        bdelete.setOnClickListener(this);
        bsearch.setOnClickListener(this);
        bexplore.setOnClickListener(this);

        dbopen = new SQLiteOpenHelper(this,"kontak.db",null,1) {
            @Override
            public void onCreate(SQLiteDatabase db) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };

        dbku = dbopen.getWritableDatabase();
        dbku.execSQL("create table if not exists kontak(id INTEGER PRIMARY KEY AUTOINCREMENT, nama TEXT, nohp TEXT);");
        ambil_data();
    }

    private void add_item(String nm, String hp) {
        ContentValues datanya = new ContentValues();
        datanya.put("nama", nm);
        datanya.put("nohp", hp);

        // Omit the ID column when inserting, as it will be auto-generated
        long newRowId = dbku.insert("kontak", null, datanya);
        // Check if the insertion was successful
        if (newRowId != -1) {
            // Create a new kontak object with the retrieved data
            kontak newKontak = new kontak((int) newRowId, nm, hp);
            // Add the new kontak to the adapter
            kAdapter.add(newKontak);
        } else {
            // Handle the case where insertion failed
            Toast.makeText(this, "Failed to insert data", Toast.LENGTH_SHORT).show();
        }
    }

    private void update_item(Integer id, String nmNew, String hpNew) {
        ContentValues datanya = new ContentValues();
        datanya.put("nama", nmNew);
        datanya.put("nohp", hpNew);

        // Define the WHERE clause to specify which entry to update
        String whereClause = "id=?";
        String[] whereArgs = {String.valueOf(id)};

        // Update the entry in the database
        dbku.update("kontak", datanya, whereClause, whereArgs);

        // Clear the adapter data
        kAdapter.clear();

        // Notify the ListView that the data has changed
        kAdapter.notifyDataSetChanged();

        ambil_data();

        Toast.makeText(this,"Data terupdate", Toast.LENGTH_SHORT).show();
    }

    private void insertKontak(Integer id, String nm, String hp) {
        kontak newKontak = new kontak(id, nm,hp);
        kAdapter.add(newKontak);
    }

    private void ambil_data() {
        Cursor cur = dbku.rawQuery("select id, nama, nohp from kontak",null);
        Toast.makeText(this,cur.getCount() + " kontak ditemukan.", Toast.LENGTH_LONG).show();

        int i = 0;
        if (cur.getCount() > 0) cur.moveToFirst();
        while (i < cur.getCount()) {
            insertKontak(cur.getInt(cur.getColumnIndex("id")), cur.getString(cur.getColumnIndex("nama")), cur.getString(cur.getColumnIndex("nohp")));
            cur.moveToNext();
            i++;
        }
    }

    private void hapus_data() {
        Cursor cur = dbku.rawQuery("drop table kontak",null);
        Toast.makeText(this,"Seluruh data terhapus", Toast.LENGTH_SHORT).show();

        // Clear the adapter data
        kAdapter.clear();

        // Notify the ListView that the data has changed
        kAdapter.notifyDataSetChanged();
    }


    public void onClick(View v){
        // Append the appropriate number or operator based on which button was clicked
        if (v.getId() == R.id.add_button) {
          tambah_data();
        } else if (v.getId() == R.id.delete_button) {
           hapus_data();
        } else if (v.getId() == R.id.search_button) {
            cari_data();
        } else if (v.getId() == R.id.explore_button) {
            kAdapter.clear();
            ambil_data();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Retrieve the data for the clicked item from your data structure
        kontak clickedKontak = (kontak) kAdapter.getItem(position);

        // Extract the name and number from the clicked kontak object
        Integer idKontak = clickedKontak.getId();
        String nama = clickedKontak.getNama();
        String noHp = clickedKontak.getNoHp();

        // Create and show the dialog with the data pre-filled
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Edit Kontak");

        // Inflate the layout for the dialog
        View vEdit = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_kontak, null);

        // Find EditText fields in the dialog layout
        final EditText nm = (EditText) vEdit.findViewById(R.id.etNama);
        final EditText hp = (EditText) vEdit.findViewById(R.id.etHP);

        // Set the EditText fields with the data from the clicked kontak
        nm.setText(nama);
        hp.setText(noHp);

        // Set the dialog view
        builder.setView(vEdit);

        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                update_item(idKontak, nm.getText().toString(), hp.getText().toString());
                Toast.makeText(getBaseContext(), "Kontak berhasil diperbarui", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Hapus kontak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hapusData(idKontak, nama.toString(), noHp.toString());
                Toast.makeText(getBaseContext(), "Kontak berhasil dihapus", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });


        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Show the dialog
        builder.show();

        // Perform the desired action with the retrieved data
        // For example, display the name and number in a toast message
        //Toast.makeText(MainActivity.this, "Name: " + nama + ", Number: " + noHp, Toast.LENGTH_SHORT).show();
    }

    private void tambah_data() {
        AlertDialog.Builder buat = new AlertDialog.Builder(this);
        buat.setTitle("Add Kontak");
        View vAdd = LayoutInflater.from(this).inflate(R.layout.add_kontak,null);
        final EditText nm = (EditText) vAdd.findViewById(R.id.etNama);
        final EditText hp = (EditText) vAdd.findViewById(R.id.etHP);

        buat.setView(vAdd);
        buat.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                add_item(nm.getText().toString(), hp.getText().toString());
                Toast.makeText(getBaseContext(), "Data tersimpan", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        buat.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        buat.show();
    }

    private void cari_data() {
        EditText search = (EditText) findViewById(R.id.search_bar);
        String searchTerm = search.getText().toString();

        // Define the query with placeholders for the search term
        String query = "SELECT id, nama, nohp FROM kontak WHERE nama LIKE ? OR nohp LIKE ?";
        String[] selectionArgs = {"%" + searchTerm + "%", "%" + searchTerm + "%"};

        kAdapter.clear();

        Cursor cur = dbku.rawQuery(query, selectionArgs);

        int i = 0;
        if (cur.getCount() > 0) cur.moveToFirst();
        while (i < cur.getCount()) {
            insertKontak(cur.getInt(cur.getColumnIndex("id")), cur.getString(cur.getColumnIndex("nama")), cur.getString(cur.getColumnIndex("nohp")));
            cur.moveToNext();
            i++;
        }
    }

    private void hapusData(Integer id, String nama, String nohp) {
        // Define the query with placeholders for the search term
        String query = "DELETE FROM kontak WHERE id = ?";
        String[] selectionArgs = {id.toString()};

        Cursor cur = dbku.rawQuery(query, selectionArgs);

        int i = 0;
        if (cur.getCount() > 0) cur.moveToFirst();
        while (i < cur.getCount()) {
            insertKontak(cur.getInt(cur.getColumnIndex("id")), cur.getString(cur.getColumnIndex("nama")), cur.getString(cur.getColumnIndex("nohp")));
            cur.moveToNext();
            i++;
        }
    }

}