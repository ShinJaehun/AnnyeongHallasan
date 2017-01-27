package com.shinjaehun.annyeonghallasan;

/**
 * Created by shinjaehun on 2017-01-28.
 */

public class WeatherCondition {
    private String baseDate;
    private String baseTime;
    private int nx;
    private int ny;

    private float t1h;
    private float rn1;
    private float sky;
    private float uuu;
    private float vvv;
    private float reh;
    private float pty;
    private float lgt;
    private float vec;
    private float wsd;

    public WeatherCondition() {
    }

    public String getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(String baseDate) {
        this.baseDate = baseDate;
    }

    public String getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(String baseTime) {
        this.baseTime = baseTime;
    }

    public int getNx() {
        return nx;
    }

    public void setNx(int nx) {
        this.nx = nx;
    }

    public int getNy() {
        return ny;
    }

    public void setNy(int ny) {
        this.ny = ny;
    }

    public float getT1h() {
        return t1h;
    }

    public void setT1h(float t1h) {
        this.t1h = t1h;
    }

    public float getRn1() {
        return rn1;
    }

    public void setRn1(float rn1) {
        this.rn1 = rn1;
    }

    public int getSky() {
        return (int) sky;
    }

    public void setSky(float sky) {
        this.sky = sky;
    }

    public float getUuu() {
        return uuu;
    }

    public void setUuu(float uuu) {
        this.uuu = uuu;
    }

    public float getVvv() {
        return vvv;
    }

    public void setVvv(float vvv) {
        this.vvv = vvv;
    }

    public float getReh() {
        return reh;
    }

    public void setReh(float reh) {
        this.reh = reh;
    }

    public int getPty() {
        return (int) pty;
    }

    public void setPty(float pty) {
        this.pty = pty;
    }

    public int getLgt() {
        return (int) lgt;
    }

    public void setLgt(float lgt) {
        this.lgt = lgt;
    }

    public float getVec() {
        return vec;
    }

    public void setVec(float vec) {
        this.vec = vec;
    }

    public float getWsd() {
        return wsd;
    }

    public void setWsd(float wsd) {
        this.wsd = wsd;
    }

}