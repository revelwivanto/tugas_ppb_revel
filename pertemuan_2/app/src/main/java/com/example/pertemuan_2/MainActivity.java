package com.example.pertemuan_2;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editBil1, editBil2;
    private TextView textHasil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editBil1 = findViewById(R.id.editTextBil1);
        editBil2 = findViewById(R.id.editTextBil2);
        textHasil = findViewById(R.id.textViewHasil);
    }

    public void operasiTambah(View view) {
        operasi('+');
    }

    public void operasiKurang(View view) {
        operasi('-');
    }

    public void operasiKali(View view) {
        operasi('*');
    }

    public void operasiBagi(View view) {
        operasi('/');
    }

    private void operasi(char op) {
        float bil1, bil2, hasil = 0;

        if (editBil1.getText().toString().isEmpty() || editBil2.getText().toString().isEmpty()) {
            textHasil.setText("Masukkan bilangan terlebih dahulu");
            return;
        }

        bil1 = Float.parseFloat(editBil1.getText().toString());
        bil2 = Float.parseFloat(editBil2.getText().toString());

        switch (op) {
            case '+':
                hasil = bil1 + bil2;
                break;
            case '-':
                hasil = bil1 - bil2;
                break;
            case '*':
                hasil = bil1 * bil2;
                break;
            case '/':
                if (bil2 == 0) {
                    textHasil.setText("Tidak dapat membagi dengan 0");
                    return;
                }
                hasil = bil1 / bil2;
                break;
        }

        textHasil.setText(bil1 + " " + op + " " + bil2 + " = " + hasil);
    }
}