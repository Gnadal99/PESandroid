package com.example.pesandroid.models;

import java.util.Date;

public class Mail {

    public Message message;
    public String mail;
    public Date date;

    public Mail(Message message, String mail, Date date) {
        this.message = message;
        this.mail = mail;
        this.date = date;
    }
}