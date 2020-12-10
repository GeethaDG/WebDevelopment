package com.example.Bachat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    TextView forget;
    Button login;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
//show sign up activity
            startActivity(new Intent(LoginActivity.this, SplashscreenActivity.class));
//startActivity(new Intent(MainActivity.this, SetupScreen.class));
        }


        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();
        myDb = new DatabaseHelper(this);
        setContentView(R.layout.login_screen);
        username = (EditText) findViewById(R.id.editTextLoginUsername);
        password= (EditText) findViewById(R.id.editTextLoginPassword);
        login= (Button) findViewById(R.id.btnLogin);
        forget = (TextView) findViewById(R.id.textViewForgotPassword);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                String pass = password.getText().toString();
                int result = checkuser(name,pass);

                if (result == -100){
                    Toast.makeText(LoginActivity.this,"No such user", Toast.LENGTH_SHORT).show();

                }
                else if( result == -1){
                    Toast.makeText(LoginActivity.this,"Incorrect Password", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(LoginActivity.this,"Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });


    }

    public int checkuser(String a, String b) {
        int result = myDb.checkLogin(a, b);
        return result;
    }

}
