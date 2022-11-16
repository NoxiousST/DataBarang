package com.test.databarang;

public class Barang {
    private String kdBrg;
    private String nmBrg;
    private int hrgBeli;
    private int hrgJual;
    private int stok;
    public Barang() {
    }
    public Barang(String kdBrg, String nmBrg, int hrgBeli, int hrgJual, int stok) {
        this.kdBrg= kdBrg;
        this.nmBrg= nmBrg;
        this.hrgBeli = hrgBeli;
        this.hrgJual = hrgJual;
        this.stok = stok;
    }
    public String getKdBrg() {return kdBrg;}
    public void setKdBrg(String kdBrg) {this.kdBrg= kdBrg;}

    public String getNmBrg() {return nmBrg;}
    public void setNmBrg(String nmBrg) {this.nmBrg= nmBrg;}

    public int getHrgBeli() {return hrgBeli;}
    public void setHrgBeli(int hrgBeli) {this.hrgBeli= hrgBeli;}

    public int getHrgJual() {return hrgJual;}
    public void setHrgJual(int hrgJual) {this.hrgJual= hrgJual;}

    public int getStok() {return stok;}
    public void setStok(int stok) {this.stok= stok;}
}
