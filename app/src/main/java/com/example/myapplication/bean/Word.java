package com.example.myapplication.bean;

/**
 * Created by 解奕鹏 on 2018/5/20.
 */

public class Word {
    public static String s;
    private String key;
    private StringBuffer ps;//音标
    private StringBuffer pron;//发音
    private StringBuffer pos;//词性
    private StringBuffer acceptation;//词性注释
    private StringBuffer orig;//例句
    private StringBuffer trans;//例句翻译

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public StringBuffer getPs() {
        return ps;
    }

    public void setPs(StringBuffer ps) {
        this.ps = ps;
    }

    public StringBuffer getPron() {
        return pron;
    }

    public void setPron(StringBuffer pron) {
        this.pron = pron;
    }

    public StringBuffer getPos() {
        return pos;
    }

    public void setPos(StringBuffer pos) {
        this.pos = pos;
    }

    public StringBuffer getAcceptation() {
        return acceptation;
    }

    public void setAcceptation(StringBuffer acceptation) {
        this.acceptation = acceptation;
    }

    public StringBuffer getOrig() {
        return orig;
    }

    public void setOrig(StringBuffer orig) {
        this.orig = orig;
    }

    public StringBuffer getTrans() {
        return trans;
    }

    public void setTrans(StringBuffer trans) {
        this.trans = trans;
    }
}
