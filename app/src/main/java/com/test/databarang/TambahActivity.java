package com.test.databarang;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.util.Objects;

public class TambahActivity extends AppCompatActivity {

    TextInputLayout kdBrg, nmBrg, hrgBeli, hrgJual, stok;
    TextInputEditText kdBrgTxt, hrgBeliTxt, hrgJualTxt;
    Button btnTambah;
    BigDecimal vHrgBeli, vHargaJual;
    int valueBeli, valueJual;
    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);
        kdBrg = findViewById(R.id.kdBrg);
        nmBrg = findViewById(R.id.nmBrg);
        hrgBeli = findViewById(R.id.hrgBeli);
        hrgJual = findViewById(R.id.hrgJual);
        stok = findViewById(R.id.stok);
        kdBrgTxt = findViewById(R.id.kdBrgTxt);
        hrgBeliTxt = findViewById(R.id.hrgBeliTxt);
        hrgJualTxt = findViewById(R.id.hrgJualTxt);
        btnTambah = findViewById(R.id.btnTambah);

        dbHandler = new DatabaseHandler(TambahActivity.this);
        kdBrgTxt.setText(dbHandler.readKode());
        btnTambah.setOnClickListener(v -> {
            kdBrg.setError(null);
            nmBrg.setError(null);
            hrgBeli.setError(null);
            hrgJual.setError(null);
            stok.setError(null);
            View fokus = null;
            boolean cancel = false;

            String kode = String.valueOf(Objects.requireNonNull(kdBrg.getEditText()).getText());
            String nama = String.valueOf(Objects.requireNonNull(nmBrg.getEditText()).getText());
            String beli = String.valueOf(Objects.requireNonNull(hrgBeli.getEditText()).getText());
            String jual = String.valueOf(Objects.requireNonNull(hrgJual.getEditText()).getText());
            String istok = String.valueOf(Objects.requireNonNull(stok.getEditText()).getText());

            if (dbHandler.checkKode(kode)) {
                kdBrg.setError("Kode barang ini sudah ada!");
                fokus = kdBrg;
                cancel = true;
            } else if (TextUtils.isEmpty(kode)) {
                kdBrg.setError("Data tidak boleh kosong!");
                fokus = kdBrg;
                cancel = true;
            } else if (TextUtils.isEmpty(nama)) {
                nmBrg.setError("Data tidak boleh kosong!");
                fokus = nmBrg;
                cancel = true;
            } else if (TextUtils.isEmpty(beli)) {
                hrgBeli.setError("Data tidak boleh kosong!");
                fokus = hrgBeli;
                cancel = true;
            } else if (TextUtils.isEmpty(jual)) {
                hrgJual.setError("Data tidak boleh kosong!");
                fokus = hrgJual;
                cancel = true;
            } else if (TextUtils.isEmpty(istok)) {
                stok.setError("Data tidak boleh kosong!");
                fokus = stok;
                cancel = true;
            }

            if (cancel)fokus.requestFocus();
            else{
                dbHandler.addBarang(kode, nama, valueBeli, valueJual, Integer.parseInt(istok));
                Toast.makeText(TambahActivity.this, "Barang berhasil ditambahkan.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        hrgBeliTxt.addTextChangedListener(new MoneyTextWatcher(hrgBeliTxt) {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                vHrgBeli = MoneyTextWatcher.parseCurrencyValue(hrgBeliTxt.getText().toString());
                valueBeli = (int) vHrgBeli.doubleValue();
            }
        });
        hrgJualTxt.addTextChangedListener(new MoneyTextWatcher(hrgJualTxt) {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                vHargaJual = MoneyTextWatcher.parseCurrencyValue(hrgJualTxt.getText().toString());
                valueJual = (int) vHargaJual.doubleValue();
            }
        });
    }
}