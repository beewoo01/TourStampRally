package com.sdin.tourstamprally.model;

public class AlramState {
    private int idx;
    private String user_phone;
    private int email_Alram;
    private int push_Alram;
    private int sms_Alram;

    public AlramState() {
    }

    public AlramState( String user_phone, int email_Alram, int push_Alram, int sms_Alram) {
        this.user_phone = user_phone;
        this.email_Alram = email_Alram;
        this.push_Alram = push_Alram;
        this.sms_Alram = sms_Alram;
    }

    public AlramState(int idx, String user_phone, int email_Alram, int push_Alram, int sms_Alram) {
        this.idx = idx;
        this.user_phone = user_phone;
        this.email_Alram = email_Alram;
        this.push_Alram = push_Alram;
        this.sms_Alram = sms_Alram;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public int getEmail_Alram() {
        return email_Alram;
    }

    public void setEmail_Alram(int email_Alram) {
        this.email_Alram = email_Alram;
    }

    public int getPush_Alram() {
        return push_Alram;
    }

    public void setPush_Alram(int push_Alram) {
        this.push_Alram = push_Alram;
    }

    public int getSms_Alram() {
        return sms_Alram;
    }

    public void setSms_Alram(int sms_Alram) {
        this.sms_Alram = sms_Alram;
    }
}
