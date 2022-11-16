package com.test.databarang;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private static final Locale locale = new Locale("id", "ID");
    private final ArrayList<Barang> barangDataArrayList;
    private final Context mcontext;
    DatabaseHandler dbHandler;
    Intent dlt = new Intent("delete");
    NumberFormat format = NumberFormat.getCurrencyInstance(locale);

    public RecyclerViewAdapter(ArrayList<Barang> recyclerDataArrayList, Context mcontext) {
        this.barangDataArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlayout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        format.setMaximumFractionDigits(0);
        Resources res = holder.itemView.getContext().getResources();
        Barang recyclerData = barangDataArrayList.get(position);
        holder.kodeTV.setText(res.getString(R.string.kb) + recyclerData.getKdBrg());
        holder.namaTV.setText(recyclerData.getNmBrg());
        holder.beliTV.setText(res.getString(R.string.hb) + format.format(recyclerData.getHrgBeli()));
        holder.jualTV.setText(res.getString(R.string.hj) + format.format(recyclerData.getHrgJual()));
        holder.stokTV.setText(res.getString(R.string.st) + String.valueOf(recyclerData.getStok()));

        holder.contextMenu.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(mcontext, holder.contextMenu);
            popup.inflate(R.menu.context_menu);
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.upd:
                        Intent i = new Intent(mcontext, UpdateActivity.class);
                        i.putExtra("kode", recyclerData.getKdBrg());
                        i.putExtra("nama", recyclerData.getNmBrg());
                        i.putExtra("beli", recyclerData.getHrgBeli());
                        i.putExtra("jual", recyclerData.getHrgJual());
                        i.putExtra("stok", recyclerData.getStok());
                        mcontext.startActivity(i);
                        return true;
                    case R.id.dlt:
                        dbHandler = new DatabaseHandler(mcontext);
                        dbHandler.deleteRow(recyclerData.getKdBrg());
                        dlt.putExtra("pos", position);
                        LocalBroadcastManager.getInstance(mcontext).sendBroadcast(dlt);
                        return true;
                    default:
                        return false;
                }
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return barangDataArrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private final TextView kodeTV, namaTV, beliTV, jualTV, stokTV;
        private final TextView contextMenu;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            kodeTV = itemView.findViewById(R.id.kode);
            namaTV = itemView.findViewById(R.id.nama);
            beliTV = itemView.findViewById(R.id.beli);
            jualTV = itemView.findViewById(R.id.jual);
            stokTV = itemView.findViewById(R.id.stok);
            contextMenu = itemView.findViewById(R.id.contextMenu);
        }
    }
}
