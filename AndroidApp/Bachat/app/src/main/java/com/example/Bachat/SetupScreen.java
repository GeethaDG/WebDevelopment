package com.example.Bachat;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SetupScreen extends AppCompatActivity {

    private static int SETUP_SCREEN = 999999999;
    private static final String TAG = "SetupScreen";
    public String store;
    DatabaseHelper myDb;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started user screen");
        myDb= new DatabaseHelper(this);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.setup_screen);

        Log.d(TAG, "onCreate:  put layout of setupscreen");
        final EditText username = (EditText) findViewById(R.id.editTextEditName);
        final EditText usermail = (EditText) findViewById(R.id.editTextEditMail);
        final EditText userpassword = (EditText) findViewById(R.id.editTextEditPassword);
        final EditText userpasswordconfirm = (EditText) findViewById(R.id.editTextEditPasswordConfirm);
        final EditText userquestion = (EditText) findViewById(R.id.editTextEditQuestion);
        final EditText useranswer = (EditText) findViewById(R.id.editTextEditAnswer);
        Button proceed = (Button) findViewById(R.id.btnConfirm);


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                String password = userpassword.getText().toString();
                String confirmpassword = userpasswordconfirm.getText().toString();
                String question = userquestion.getText().toString();
                String answer = useranswer.getText().toString();
                String mail = usermail.getText().toString();

                if (username.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(SetupScreen.this, "please enter your name", Toast.LENGTH_SHORT).show();
                }

                if (usermail.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(SetupScreen.this, "please enter your e-mail", Toast.LENGTH_SHORT).show();
                }
                if (userpassword.getText().toString().isEmpty())
                {
                    Toast.makeText(SetupScreen.this, "please enter a password", Toast.LENGTH_SHORT).show();
                }
                if (confirmpassword.isEmpty())
                {
                    Toast.makeText(SetupScreen.this, "please confirm your password", Toast.LENGTH_SHORT).show();
                }
                if (question.isEmpty())
                {
                    Toast.makeText(SetupScreen.this, "please center a question of your choice", Toast.LENGTH_SHORT).show();
                }
                if (answer.isEmpty())
                {
                    Toast.makeText(SetupScreen.this, "please answer the selected question", Toast.LENGTH_SHORT).show();
                }

                if(!password.equals(confirmpassword)) {

                    Toast.makeText(SetupScreen.this, "password and confirm password do not match", Toast.LENGTH_SHORT).show();

                }
                if(name.toLowerCase().isEmpty() || mail.toLowerCase().isEmpty() || password.isEmpty() || confirmpassword.isEmpty() || question.isEmpty() || answer.isEmpty() || !password.equals(confirmpassword)) {


                }
                else if(!mail.contains("@") || !mail.contains(".")){
                    Toast.makeText(SetupScreen.this, "Please enter a valid e-mail address", Toast.LENGTH_SHORT).show();
                }
                else{
                    String mailtolower=mail.toLowerCase();

                boolean inserted = myDb.insertDataUser(name,password, mailtolower, question, answer);
                Log.d(TAG, "onClick: " + inserted);
                Intent intent = new Intent(SetupScreen.this,MainActivity.class);
                startActivity(intent);
                finish();}

            }
        });
        new Handler().postDelayed(new Runnable () {
            @Override
            public void run() {
                Intent intent = new Intent(SetupScreen.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SETUP_SCREEN);


    }
}
