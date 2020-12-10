package com.example.Bachat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class EditThresholdScreen extends AppCompatActivity {
    //String fromThresholdCategory;
    String toeditfromthresholdviewscreen;
    int flag = 0;
    EditText Threshold;
    String value;
    String[] splitstring;
    public Button removeButton;
    DatabaseHelper myDb;
    private static final String TAG = "ThresholdScreen";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        myDb = new DatabaseHelper(this);
        //fromThresholdCategory = getIntent().getStringExtra("fromThresholdCategory");
        toeditfromthresholdviewscreen = getIntent().getStringExtra("toeditfromthresholdviewscreen");;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.threshold_screen);
        TextView showcat = (TextView)findViewById(R.id.textViewThresholdCategory);
        Threshold = (EditText) findViewById(R.id.editTextThreshold);
        //getSupportActionBar().setTitle("Set Threshold");

        splitstring= toeditfromthresholdviewscreen.split(",");
        showcat.setText(splitstring[0]);
        //showcat.setText(fromThresholdCategory);
        //Threshold.setText("0");
        if(splitstring[1].equals("Not Set")) {
            Threshold.setText("0");
        }
        else{
            Threshold.setText(splitstring[1]);
        }


        final Button setThreshold = (Button) findViewById(R.id.btnSetThreshold);
        setThreshold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Float.parseFloat(Threshold.getText().toString()) >= 99999999f)
                {
                    Toast.makeText(EditThresholdScreen.this, "Please enter amount which is smaller than 99999999", Toast.LENGTH_SHORT).show();
                }
                if(Float.parseFloat(Threshold.getText().toString()) < 0f)
                {
                    Toast.makeText(EditThresholdScreen.this, "Please enter amount which is greater than 0", Toast.LENGTH_SHORT).show();
                }
                else if(Threshold.getText().toString().isEmpty())
                {
                    Toast.makeText(EditThresholdScreen.this, "Please enter amount ", Toast.LENGTH_SHORT).show();
                }
                else if(Float.parseFloat(Threshold.getText().toString()) < 99999999f &&  Float.parseFloat(Threshold.getText().toString()) >= 0f )
                {
                    setThreshold();
                }

            }
        });

        removeButton =(Button)findViewById(R.id.btnRemoveThreshold);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeThreshold();
            }
        });

        ImageButton back = (ImageButton) findViewById(R.id.btn_Back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditThresholdScreen.this, MainActivity.class);
                intent.putExtra("open_set_threshold", "Open Threshold View");
                startActivity(intent);
            }
        });

        FloatingActionButton fab_home= (FloatingActionButton)  findViewById(R.id.fab_home);
        fab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditThresholdScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    void setThreshold(){
        value=Threshold.getText().toString();
        if(value.length()<1) {
            Log.d(TAG, "value.length < 1");
            String threshold = "Not Set";
            //boolean isUpdate = myDb.updateThreshold(fromThresholdCategory,threshold);
            boolean isUpdate = myDb.updateThreshold(splitstring[0],threshold);

            Log.d(TAG, "update amount is : "+threshold);
            if(isUpdate == true)
                flag=1;
                //Toast.makeText(ThresholdScreen.this,"Threshold Set",Toast.LENGTH_LONG).show();
            //else
                //Toast.makeText(ThresholdScreen.this,"Threshold not Set",Toast.LENGTH_LONG).show();

        }
        else{

            //boolean isUpdate = myDb.updateThreshold(fromThresholdCategory,value);
            boolean isUpdate = myDb.updateThreshold(splitstring[0],value);

            Log.d(TAG, "update amount is : "+value);
            if(isUpdate == true)
                Log.d(TAG, "setThreshold: update ok");
                flag=1;
                //Toast.makeText(ThresholdScreen.this,"Threshold Set",Toast.LENGTH_LONG).show();
            //else
                //Toast.makeText(ThresholdScreen.this,"Threshold not Set",Toast.LENGTH_LONG).show();
        }
       if(flag == 1){
           Log.d(TAG, "flag set to 1");

           Intent intent = new Intent(EditThresholdScreen.this, MainActivity.class);
           intent.putExtra("open_set_threshold", "Open Threshold View");
           startActivity(intent);
       }
    }

    void removeThreshold(){
        Log.d(TAG, "removeThreshold: ");
            String threshold = "Not Set";

            //boolean isUpdate = myDb.updateThreshold(fromThresholdCategory,threshold);
            boolean isUpdate = myDb.updateThreshold(splitstring[0],threshold);

            Log.d(TAG, "removed amount is : "+threshold);
            if(isUpdate == true)
                flag=1;
        if(flag == 1){
            Log.d(TAG, "flag set to 1");

            Intent intent = new Intent(EditThresholdScreen.this, MainActivity.class);
            intent.putExtra("open_set_threshold", "Open Threshold View");
            startActivity(intent);
        }
    }

}
