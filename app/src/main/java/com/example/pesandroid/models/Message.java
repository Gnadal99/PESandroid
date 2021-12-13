package com.example.pesandroid.models;

public class Message {

    public String title;

    public String body;

    public Message(String title, String body) {
        this.title = title;
        this.body = body;
    }

    @Override
    public String toString() {
        return "\n     {" +
                "\n          \"title\": \"" + title + "\"," +
                "\n          \"body\": \"" + body + '\"' +
                "\n     }\n";
    }
}