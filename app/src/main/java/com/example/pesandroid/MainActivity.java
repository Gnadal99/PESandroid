package com.example.pesandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    MainActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.activity = this;
        ProgressBar pgbar = findViewById(R.id.loginProgressBar);
        pgbar.setVisibility(View.INVISIBLE);

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String mail = sh.getString("mail", null);
        String password = sh.getString("password", null);

        if (mail != null && password != null){
            loginWithPreferences(mail, password);
        }
    }

    public void login(View view) {
        new Thread(new Runnable() {

            InputStream stream = null;
            String result = null;
            Handler handler = new Handler();
            public void run() {
                try {
                    //Obtain data
                    EditText mailIn = findViewById(R.id.logineditTextEmailAddress);
                    String mail = mailIn.getText().toString();
                    EditText passwordIn = findViewById(R.id.logineditTextPassword);
                    String password = passwordIn.getText().toString();

                    handler.post(new Runnable() {
                        public void run() {
                            TextView title = findViewById(R.id.loginTitleText);
                            title.setVisibility(View.INVISIBLE);
                            ImageView imagen = findViewById(R.id.loginImageView);
                            imagen.setVisibility(View.INVISIBLE);
                            EditText mailbox = findViewById(R.id.logineditTextEmailAddress);
                            mailbox.setVisibility(View.INVISIBLE);
                            EditText pasbox = findViewById(R.id.logineditTextPassword);
                            pasbox.setVisibility(View.INVISIBLE);
                            Button buto = findViewById(R.id.loginBtn);
                            buto.setVisibility(View.INVISIBLE);
                            TextView textito = findViewById(R.id.loginsignUpText);
                            textito.setVisibility(View.INVISIBLE);
                            TextView textito2 = findViewById(R.id.loginsignUpLink);
                            textito2.setVisibility(View.INVISIBLE);

                            ProgressBar pgbar = findViewById(R.id.loginProgressBar);
                            pgbar.setVisibility(View.VISIBLE);
                        }
                    });

                    //Query
                    String query = String.format("http://10.0.2.2:9000/Android/login?mail=" + mail + "&password=" + password);
                    URL url = new URL(query);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();

                    //Response
                    stream = conn.getInputStream();
                    BufferedReader reader = null;
                    StringBuilder sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    result = sb.toString();

                    //Result processing
                    if(result.equals("0")) {
                        handler.post(new Runnable() {
                            public void run() {
                                SharedPreferences sharedPreferences = activity.getSharedPreferences("MySharedPref", 0);
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                                myEdit.putString("mail", mail);
                                myEdit.putString("password", password);
                                myEdit.apply();

                                Intent intent = new Intent(activity, InboxActivity.class);
                                intent.putExtra("mail", mail);
                                intent.putExtra("password", password);
                                activity.startActivity(intent);

                                TextView title = findViewById(R.id.loginTitleText);
                                title.setVisibility(View.VISIBLE);
                                ImageView imagen = findViewById(R.id.loginImageView);
                                imagen.setVisibility(View.VISIBLE);
                                EditText mailbox = findViewById(R.id.logineditTextEmailAddress);
                                mailbox.setVisibility(View.VISIBLE);
                                EditText pasbox = findViewById(R.id.logineditTextPassword);
                                pasbox.setVisibility(View.VISIBLE);
                                Button buto = findViewById(R.id.loginBtn);
                                buto.setVisibility(View.VISIBLE);
                                TextView textito = findViewById(R.id.loginsignUpText);
                                textito.setVisibility(View.VISIBLE);
                                TextView textito2 = findViewById(R.id.loginsignUpLink);
                                textito2.setVisibility(View.VISIBLE);

                                ProgressBar pgbar = findViewById(R.id.loginProgressBar);
                                pgbar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    else if (result.equals("-1")) {
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(activity.getApplicationContext(), "Mail address does not exist. Try again.", Toast.LENGTH_LONG).show();

                                TextView title = findViewById(R.id.loginTitleText);
                                title.setVisibility(View.VISIBLE);
                                ImageView imagen = findViewById(R.id.loginImageView);
                                imagen.setVisibility(View.VISIBLE);
                                EditText mailbox = findViewById(R.id.logineditTextEmailAddress);
                                mailbox.setVisibility(View.VISIBLE);
                                EditText pasbox = findViewById(R.id.logineditTextPassword);
                                pasbox.setVisibility(View.VISIBLE);
                                Button buto = findViewById(R.id.loginBtn);
                                buto.setVisibility(View.VISIBLE);
                                TextView textito = findViewById(R.id.loginsignUpText);
                                textito.setVisibility(View.VISIBLE);
                                TextView textito2 = findViewById(R.id.loginsignUpLink);
                                textito2.setVisibility(View.VISIBLE);

                                ProgressBar pgbar = findViewById(R.id.loginProgressBar);
                                pgbar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    else if (result.equals("-2")) {
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(activity.getApplicationContext(), "Wrong password. Try again.", Toast.LENGTH_LONG).show();

                                TextView title = findViewById(R.id.loginTitleText);
                                title.setVisibility(View.VISIBLE);
                                ImageView imagen = findViewById(R.id.loginImageView);
                                imagen.setVisibility(View.VISIBLE);
                                EditText mailbox = findViewById(R.id.logineditTextEmailAddress);
                                mailbox.setVisibility(View.VISIBLE);
                                EditText pasbox = findViewById(R.id.logineditTextPassword);
                                pasbox.setVisibility(View.VISIBLE);
                                Button buto = findViewById(R.id.loginBtn);
                                buto.setVisibility(View.VISIBLE);
                                TextView textito = findViewById(R.id.loginsignUpText);
                                textito.setVisibility(View.VISIBLE);
                                TextView textito2 = findViewById(R.id.loginsignUpLink);
                                textito2.setVisibility(View.VISIBLE);

                                ProgressBar pgbar = findViewById(R.id.loginProgressBar);
                                pgbar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    else {
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(activity.getApplicationContext(), "Unexpected error.", Toast.LENGTH_LONG).show();

                                TextView title = findViewById(R.id.loginTitleText);
                                title.setVisibility(View.VISIBLE);
                                ImageView imagen = findViewById(R.id.loginImageView);
                                imagen.setVisibility(View.VISIBLE);
                                EditText mailbox = findViewById(R.id.logineditTextEmailAddress);
                                mailbox.setVisibility(View.VISIBLE);
                                EditText pasbox = findViewById(R.id.logineditTextPassword);
                                pasbox.setVisibility(View.VISIBLE);
                                Button buto = findViewById(R.id.loginBtn);
                                buto.setVisibility(View.VISIBLE);
                                TextView textito = findViewById(R.id.loginsignUpText);
                                textito.setVisibility(View.VISIBLE);
                                TextView textito2 = findViewById(R.id.loginsignUpLink);
                                textito2.setVisibility(View.VISIBLE);

                                ProgressBar pgbar = findViewById(R.id.loginProgressBar);
                                pgbar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void loginWithPreferences(String mail, String password) {
        new Thread(new Runnable() {

            InputStream stream = null;
            String result = null;
            Handler handler = new Handler();
            public void run() {
                try {


                    handler.post(new Runnable() {
                        public void run() {
                            TextView title = findViewById(R.id.loginTitleText);
                            title.setVisibility(View.INVISIBLE);
                            ImageView imagen = findViewById(R.id.loginImageView);
                            imagen.setVisibility(View.INVISIBLE);
                            EditText mailbox = findViewById(R.id.logineditTextEmailAddress);
                            mailbox.setVisibility(View.INVISIBLE);
                            EditText pasbox = findViewById(R.id.logineditTextPassword);
                            pasbox.setVisibility(View.INVISIBLE);
                            Button buto = findViewById(R.id.loginBtn);
                            buto.setVisibility(View.INVISIBLE);
                            TextView textito = findViewById(R.id.loginsignUpText);
                            textito.setVisibility(View.INVISIBLE);
                            TextView textito2 = findViewById(R.id.loginsignUpLink);
                            textito2.setVisibility(View.INVISIBLE);

                            ProgressBar pgbar = findViewById(R.id.loginProgressBar);
                            pgbar.setVisibility(View.VISIBLE);
                        }
                    });

                    //Query
                    String query = String.format("http://10.0.2.2:9000/Android/login?mail=" + mail + "&password=" + password);
                    URL url = new URL(query);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();

                    //Response
                    stream = conn.getInputStream();
                    BufferedReader reader = null;
                    StringBuilder sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    result = sb.toString();

                    //Result processing
                    if(result.equals("0")) {
                        handler.post(new Runnable() {
                            public void run() {
                                SharedPreferences sharedPreferences = activity.getSharedPreferences("MySharedPref", 0);
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                                myEdit.putString("mail", mail);
                                myEdit.putString("password", password);
                                myEdit.apply();

                                Intent intent = new Intent(activity, InboxActivity.class);
                                intent.putExtra("mail", mail);
                                intent.putExtra("password", password);
                                activity.startActivity(intent);

                                TextView title = findViewById(R.id.loginTitleText);
                                title.setVisibility(View.VISIBLE);
                                ImageView imagen = findViewById(R.id.loginImageView);
                                imagen.setVisibility(View.VISIBLE);
                                EditText mailbox = findViewById(R.id.logineditTextEmailAddress);
                                mailbox.setVisibility(View.VISIBLE);
                                EditText pasbox = findViewById(R.id.logineditTextPassword);
                                pasbox.setVisibility(View.VISIBLE);
                                Button buto = findViewById(R.id.loginBtn);
                                buto.setVisibility(View.VISIBLE);
                                TextView textito = findViewById(R.id.loginsignUpText);
                                textito.setVisibility(View.VISIBLE);
                                TextView textito2 = findViewById(R.id.loginsignUpLink);
                                textito2.setVisibility(View.VISIBLE);

                                ProgressBar pgbar = findViewById(R.id.loginProgressBar);
                                pgbar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    else if (result.equals("-1")) {
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(activity.getApplicationContext(), "Mail address does not exist. Try again.", Toast.LENGTH_LONG).show();

                                TextView title = findViewById(R.id.loginTitleText);
                                title.setVisibility(View.VISIBLE);
                                ImageView imagen = findViewById(R.id.loginImageView);
                                imagen.setVisibility(View.VISIBLE);
                                EditText mailbox = findViewById(R.id.logineditTextEmailAddress);
                                mailbox.setVisibility(View.VISIBLE);
                                EditText pasbox = findViewById(R.id.logineditTextPassword);
                                pasbox.setVisibility(View.VISIBLE);
                                Button buto = findViewById(R.id.loginBtn);
                                buto.setVisibility(View.VISIBLE);
                                TextView textito = findViewById(R.id.loginsignUpText);
                                textito.setVisibility(View.VISIBLE);
                                TextView textito2 = findViewById(R.id.loginsignUpLink);
                                textito2.setVisibility(View.VISIBLE);

                                ProgressBar pgbar = findViewById(R.id.loginProgressBar);
                                pgbar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    else if (result.equals("-2")) {
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(activity.getApplicationContext(), "Wrong password. Try again.", Toast.LENGTH_LONG).show();

                                TextView title = findViewById(R.id.loginTitleText);
                                title.setVisibility(View.VISIBLE);
                                ImageView imagen = findViewById(R.id.loginImageView);
                                imagen.setVisibility(View.VISIBLE);
                                EditText mailbox = findViewById(R.id.logineditTextEmailAddress);
                                mailbox.setVisibility(View.VISIBLE);
                                EditText pasbox = findViewById(R.id.logineditTextPassword);
                                pasbox.setVisibility(View.VISIBLE);
                                Button buto = findViewById(R.id.loginBtn);
                                buto.setVisibility(View.VISIBLE);
                                TextView textito = findViewById(R.id.loginsignUpText);
                                textito.setVisibility(View.VISIBLE);
                                TextView textito2 = findViewById(R.id.loginsignUpLink);
                                textito2.setVisibility(View.VISIBLE);

                                ProgressBar pgbar = findViewById(R.id.loginProgressBar);
                                pgbar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    else {
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(activity.getApplicationContext(), "Unexpected error.", Toast.LENGTH_LONG).show();

                                TextView title = findViewById(R.id.loginTitleText);
                                title.setVisibility(View.VISIBLE);
                                ImageView imagen = findViewById(R.id.loginImageView);
                                imagen.setVisibility(View.VISIBLE);
                                EditText mailbox = findViewById(R.id.logineditTextEmailAddress);
                                mailbox.setVisibility(View.VISIBLE);
                                EditText pasbox = findViewById(R.id.logineditTextPassword);
                                pasbox.setVisibility(View.VISIBLE);
                                Button buto = findViewById(R.id.loginBtn);
                                buto.setVisibility(View.VISIBLE);
                                TextView textito = findViewById(R.id.loginsignUpText);
                                textito.setVisibility(View.VISIBLE);
                                TextView textito2 = findViewById(R.id.loginsignUpLink);
                                textito2.setVisibility(View.VISIBLE);

                                ProgressBar pgbar = findViewById(R.id.loginProgressBar);
                                pgbar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void signUp(View view) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        activity.startActivity(intent);
    }
}