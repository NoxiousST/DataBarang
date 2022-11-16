package com.test.databarang;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseHandler dbHandler;
    ArrayList<Barang> recyclerDataArrayList;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        fab = findViewById(R.id.floating_action_button);
        recyclerView = findViewById(R.id.barangRV);

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TambahActivity.class);
            startActivity(intent);
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(dMessageReceiver, new IntentFilter("delete"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerDataArrayList = new ArrayList<>();
        dbHandler = new DatabaseHandler(MainActivity.this);
        recyclerDataArrayList = dbHandler.readAllBarang();

        linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewAdapter(recyclerDataArrayList, this);
        recyclerView.setAdapter(adapter);

    }

    private final BroadcastReceiver dMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int xpos = intent.getIntExtra("pos", 0);
            recyclerDataArrayList.remove(xpos);
            adapter.notifyItemRemoved(xpos);
            adapter.notifyItemRangeChanged(xpos, recyclerDataArrayList.size());
        }
    };

}
