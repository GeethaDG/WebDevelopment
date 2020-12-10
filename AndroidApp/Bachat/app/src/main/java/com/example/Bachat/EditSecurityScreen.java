package com.example.Bachat;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditSecurityScreen extends AppCompatActivity {
    Button updatepassword;
    Button updateqa;
    EditText newquestion;
    EditText newanswer;
    EditText newpassword;
    EditText newpasswordconfirm;
    TextView question;
    TextView answer;
    TextView password;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.security_screen);
        myDb = new DatabaseHelper(this);

        newquestion = (EditText) findViewById(R.id.editTextNewQuestion);
        newanswer = (EditText) findViewById(R.id.editTextNewAnswer);
        newpassword = (EditText) findViewById(R.id.editTextNewPassword);
        newpasswordconfirm = (EditText) findViewById(R.id.editTextNewPasswordConfirm);
        question = (TextView) findViewById(R.id.textViewCurrentQuestion);
        answer = (TextView) findViewById(R.id.textViewCurrentAnswer);
        password = (TextView) findViewById(R.id.textViewCurrentPassword);
        updateqa = (Button) findViewById(R.id.btnUpdateQA);
        updatepassword = (Button) findViewById(R.id.btnUpdatePassword);


        Cursor res = myDb.getAllDataUser();
        while(res.moveToNext()){
            question.setText(res.getString(5));
            answer.setText(res.getString(6));
            password.setText(res.getString(1));
        }


        updateqa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedq = newquestion.getText().toString();
                String updateda = newanswer.getText().toString();
                if (updatedq.isEmpty() && updateda.isEmpty()) {
                    Toast.makeText(EditSecurityScreen.this, "please mention both Q and A", Toast.LENGTH_SHORT).show();
                }
                if (updatedq.isEmpty() && !updateda.isEmpty()) {
                    Toast.makeText(EditSecurityScreen.this, "please mention the question for the updated answer", Toast.LENGTH_SHORT).show();
                }
                if (updateda.isEmpty() && !updatedq.isEmpty()) {
                    Toast.makeText(EditSecurityScreen.this, "please mention the answer for the updated question", Toast.LENGTH_SHORT).show();
                }else {
                    myDb.updateQAUser(updatedq, updateda);
                }

            }
        });
        updatepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedp = newpassword.getText().toString();
                String updatedpc = newpasswordconfirm.getText().toString();
                if (updatedp.isEmpty()) {
                    Toast.makeText(EditSecurityScreen.this, "please enter a new password", Toast.LENGTH_SHORT).show();
                }
                if (updatedpc.isEmpty()) {
                    Toast.makeText(EditSecurityScreen.this, "please confirm your password", Toast.LENGTH_SHORT).show();
                }
                if (!updatedpc.equals(updatedp)) {
                    Toast.makeText(EditSecurityScreen.this, "the passwords do not match", Toast.LENGTH_SHORT).show();
                }
                if (updatedp.isEmpty() || updatedpc.isEmpty() || !updatedpc.equals(updatedpc)) {

                } else {
                    Cursor res = myDb.getAllDataUser();
                    StringBuffer sb = new StringBuffer();
                    while (res.moveToNext()) {
                        sb.append(res.getString(0));
                    }
                    String username = sb.toString();
                    Log.d("checking user name", "passed username in method is : " + username);
                    myDb.updatePasswordUser(updatedp, username);
                    Intent intent = new Intent(EditSecurityScreen.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}

