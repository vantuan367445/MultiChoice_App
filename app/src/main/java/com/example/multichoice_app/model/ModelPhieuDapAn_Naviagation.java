package com.example.multichoice_app.model;

import java.io.Serializable;

public class ModelPhieuDapAn_Naviagation implements Serializable {
    private  int stt;
    private  boolean dapanA;
    private  boolean dapanB;
    private  boolean dapanC;
    private  boolean dapanD;

    public ModelPhieuDapAn_Naviagation() {
    }

    public ModelPhieuDapAn_Naviagation(int stt, boolean dapanA, boolean dapanB, boolean dapanC, boolean dapanD) {
        this.stt = stt;
        this.dapanA = dapanA;
        this.dapanB = dapanB;
        this.dapanC = dapanC;
        this.dapanD = dapanD;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public boolean isDapanA() {
        return dapanA;
    }

    public void setDapanA(boolean dapanA) {
        this.dapanA = dapanA;
    }

    public boolean isDapanB() {
        return dapanB;
    }

    public void setDapanB(boolean dapanB) {
        this.dapanB = dapanB;
    }

    public boolean isDapanC() {
        return dapanC;
    }

    public void setDapanC(boolean dapanC) {
        this.dapanC = dapanC;
    }

    public boolean isDapanD() {
        return dapanD;
    }

    public void setDapanD(boolean dapanD) {
        this.dapanD = dapanD;
    }
}
