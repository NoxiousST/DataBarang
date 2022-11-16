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
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;
public class UpdateActivity extends AppCompatActivity {

    private static final Locale locale = new Locale("id", "ID");
    TextInputLayout kdBrg, nmBrg, hrgBeli, hrgJual, stok;
    TextInputEditText kdBrgTxt, nmBrgTxt, hrgBeliTxt, hrgJualTxt, stokTxt;
    Button btnUpdate;
    String vKode, vNama, vStok;
    NumberFormat format = NumberFormat.getCurrencyInstance(locale);
    BigDecimal vHrgBeli, vHargaJual;
    int vBeli, vJual, valueBeli, valueJual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        final DatabaseHandler db = new DatabaseHandler(this);
        kdBrg = findViewById(R.id.kdBrg);
        nmBrg = findViewById(R.id.nmBrg);
        hrgBeli = findViewById(R.id.hrgBeli);
        hrgJual = findViewById(R.id.hrgJual);
        stok = findViewById(R.id.stok);
        kdBrgTxt = findViewById(R.id.kdBrgTxt);
        nmBrgTxt = findViewById(R.id.nmBrgTxt);
        hrgBeliTxt = findViewById(R.id.hrgBeliTxt);
        hrgJualTxt = findViewById(R.id.hrgJualTxt);
        stokTxt = findViewById(R.id.stokTxt);
        btnUpdate = findViewById(R.id.btnUpdate);

        vKode = getIntent().getStringExtra("kode");
        vNama = getIntent().getStringExtra("nama");
        vBeli = getIntent().getIntExtra("beli", 0);
        vJual = getIntent().getIntExtra("jual", 0);
        vStok = String.valueOf(getIntent().getIntExtra("stok", 0));

        format.setMaximumFractionDigits(0);
        kdBrgTxt.setText(vKode);
        nmBrgTxt.setText(vNama);
        hrgBeliTxt.setText(format.format(vBeli));
        hrgJualTxt.setText(format.format(vJual));
        stokTxt.setText(vStok);

        btnUpdate.setOnClickListener(v -> {
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


            if (TextUtils.isEmpty(kode)) {
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
                db.updateMethod(kode, nama, valueBeli, valueJual, Integer.parseInt(istok));
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
