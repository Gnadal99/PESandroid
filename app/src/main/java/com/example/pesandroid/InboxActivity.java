package com.example.pesandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class InboxActivity extends AppCompatActivity   {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // your code
            this.finishAffinity();
        }

        return super.onKeyDown(keyCode, event);
    }

    public void back(View view) {
        SharedPreferences preferences = getSharedPreferences("MySharedPref", 0);
        preferences.edit().remove("mail").apply();
        preferences.edit().remove("password").apply();

        finish();
    }
}