package com.example.Bachat;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditUserScreen extends AppCompatActivity{
    String updatedname;
    String updatedmail;
    String originalname;
    String originalmail;
    DatabaseHelper myDb;
    private static final String TAG = "EditUser";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_screen);
        final EditText editname = (EditText) findViewById(R.id.editTextEditName);
        final EditText editmail = (EditText) findViewById(R.id.editTextEditMail);
        Button confirm = (Button) findViewById(R.id.btnConfirm);


        myDb = new DatabaseHelper(this);

        Cursor res = myDb.getAllDataUser();
        StringBuffer sb= new StringBuffer();
        StringBuffer sb2= new StringBuffer();
        while (res.moveToNext())
        {
            sb.append(res.getString(0));
            sb2.append(res.getString(2));
        }

        //code for testing
        Button setprofile = (Button) findViewById(R.id.buttonSetProfile);
        Button addprofile = (Button) findViewById(R.id.buttonAddProfile);
        Button setprofiledefault = (Button) findViewById(R.id.ButtonSetProfileDefault);

        addprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.insertProfile("Office");
            }
        });

        setprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Before calling set profile, the current profile is " + HomeFragment.current_profile);
                myDb.setCurrentProfiles("Office");
                //Log.d(TAG, "onClick: the changed current profile is " + HomeFragment.current_profile);
            }
        });
        setprofiledefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Before calling set profile, the current profile is " + HomeFragment.current_profile);
                myDb.setCurrentProfiles("Default");
                //Log.d(TAG, "onClick: the changed current profile is " + HomeFragment.current_profile);
            }
        });

        //The following code is for the text spinner to enable the selection of the currency
        Spinner currencySpinner = (Spinner) findViewById(R.id.editCurrency);
        ArrayAdapter<CharSequence> currencyAdapter = ArrayAdapter.createFromResource(this,R.array.Currencies,android.R.layout.simple_list_item_1);
        //Currencies is the list specified in strings.xml
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        currencySpinner.setAdapter(currencyAdapter);
        String compareValue = HomeFragment.default_currency;
        if (compareValue != null) {
            int spinnerPosition = currencyAdapter.getPosition(compareValue);
            currencySpinner.setSelection(spinnerPosition);
        }
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                String curr = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "onItemSelected: currency value is :" + curr);
                myDb.setDefaultCurrency(curr);
                HomeFragment.default_currency = curr;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //end testing here


        originalname = sb.toString();
        originalmail = sb2.toString();
        editname.setText(originalname);
        editmail.setText(originalmail);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedname = editname.getText().toString();
                updatedmail = editmail.getText().toString().toLowerCase();

                 if(updatedname.isEmpty()){
                    Toast.makeText(EditUserScreen.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                 }
                 else if(updatedmail.isEmpty()){
                    Toast.makeText(EditUserScreen.this, "Please enter an e-mail address", Toast.LENGTH_SHORT).show();
                 }
                 else if(!updatedmail.contains("@") || !updatedmail.contains(".")){
                     Toast.makeText(EditUserScreen.this, "Please enter a valid e-mail address", Toast.LENGTH_SHORT).show();
                 }
                 else{
                     updatedname = editname.getText().toString();
                     updatedmail = editmail.getText().toString().toLowerCase();

                     boolean res = myDb.updateDataUser(originalname,updatedname,updatedmail);
                     if(res){
                         Log.d(TAG, " userdata updated ");
                     }
                     else{
                         Log.d(TAG, "userdata update failed");
                     }
                     Intent intent = new Intent(EditUserScreen.this, MainActivity.class);
                     intent.putExtra("settings_screen", "settings screen");
                     startActivity(intent);
                 }
            }
        });

        ImageButton back = (ImageButton) findViewById(R.id.btn_Back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditUserScreen.this, MainActivity.class);
                intent.putExtra("settings_screen", "settings screen");
                startActivity(intent);
            }
        });

        FloatingActionButton fab_home= (FloatingActionButton)  findViewById(R.id.fab_home);
        fab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditUserScreen.this, MainActivity.class);
                startActivity(intent);

            }
        });


    }
}
