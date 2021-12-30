package com.example.pesandroid.models;

public class Mail {

    public Message message;
    public String mail;
    public String date;

    public Mail(Message message, String mail, String date) {
        this.message = message;
        this.mail = mail;
        this.date = date;
    }
}