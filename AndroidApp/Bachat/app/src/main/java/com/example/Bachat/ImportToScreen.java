/*package com.example.Bachat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ImportToScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_to_screen);
        ImageView earnimport =(ImageView) findViewById(R.id.imageViewEarning);
        ImageView expenseimport =(ImageView) findViewById(R.id.imageViewExpense);

        earnimport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ImportToScreen.this,EarningImportScreen.class);
                startActivity(intent);
            }
        });
        expenseimport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImportToScreen.this, ImportScreen.class);
                startActivity(intent);
            }
        });

    }
}*/
