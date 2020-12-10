package com.example.Bachat;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ThirdScreen extends AppCompatActivity {
    private static final String TAG = "ThirdScreen";
    public String fromSecond,addedSubCategory;
    public String toadd;
    Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_screen);

        //Reminder to the user of which Category was chosen by him in previous page
        /*String categoryToShow = "Category chosen: "+ getIntent().getStringExtra("fromSecond");
        TextView categoryView = (TextView) findViewById(R.id.textCategory);
        categoryView.setText(categoryToShow);*/

        getSupportActionBar().setTitle("Expense");
        getSupportActionBar().setSubtitle("Add/Choose Sub-Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Log.d(TAG, "onCreate: "+ categoryToShow);
        //Opening the database to capture the category
        final String selectedCategory = getIntent().getStringExtra("fromSecond");
        final DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();


        //Creating a ListView to populate with data from DB
        final ListView listView = (ListView) findViewById(R.id.subCatList);

        // Creating an Arraylist to populate from the database with the list of sub-categories related to the selectedCategory
        final ArrayList<String> subCategoryList = new ArrayList<>();
        Cursor cursor = databaseAccess.getListContents(selectedCategory);

        if(cursor.getCount()==0){
            Toast.makeText(ThirdScreen.this,"No sub-categories yet for this category",Toast.LENGTH_LONG).show();
        }else{
            while (cursor.moveToNext()){
                subCategoryList.add(cursor.getString(0));
                ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,subCategoryList);
                listView.setAdapter(listAdapter);
            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ThirdScreen.this, FourthScreen.class);
                String subcategoryname = subCategoryList.get(position);
                fromSecond = getIntent().getStringExtra("fromSecond");
                String toadd = fromSecond + "," + subcategoryname;
                addedSubCategory = subcategoryname;
                Log.d(TAG, "onClick: value of added subcategory: "+ addedSubCategory);
                intent.putExtra("fromThird",toadd);
                startActivity(intent);
                Log.d(TAG, "onItemClick: name is "+ subCategoryList.get(position));
                Toast.makeText(ThirdScreen.this,"You have chosen "+ subCategoryList.get(position),Toast.LENGTH_SHORT).show();
            }
        });


        final EditText subcategoryname = (EditText) findViewById(R.id.editText);
        CharSequence hint= "Add new Sub-Category to " + getIntent().getStringExtra("fromSecond") ;
        subcategoryname.setHint(hint);


        Button thirdButton = (Button) findViewById(R.id.btnGoToFourth);
        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdScreen.this, FourthScreen.class);
                /*TextView theTextView = (TextView) findViewById(R.id.textAddSubcat);*/
                fromSecond = getIntent().getStringExtra("fromSecond");
                int flag = 0;
                //EditText subcategoryname = (EditText) findViewById(R.id.editText);
                addedSubCategory = subcategoryname.getText().toString();
                Log.d(TAG, "onClick: subcategoryname is "+flag);
                Log.d(TAG, "onClick: subcategoryname is "+ addedSubCategory);
                toadd = fromSecond + "," + subcategoryname.getText().toString();

                if (addedSubCategory.isEmpty())
                {
                   //databaseAccess.insertListContents(selectedCategory,"None");
                    Toast.makeText(ThirdScreen.this, "Please mention a Sub-Category", Toast.LENGTH_SHORT).show();
                }
                else{
                    databaseAccess.insertListContents(selectedCategory,addedSubCategory);
                    databaseAccess.close();

                    Log.d(TAG, "onClick: passed value is: "+ toadd );
                    intent.putExtra("fromThird",toadd);
                    startActivity(intent);
                }

            }
        });
        Log.d(TAG, "onCreate: insertListContents: the values being passed to fourth are" + selectedCategory + " and " + addedSubCategory);
        //close connection after getting result

        FloatingActionButton fab_home= (FloatingActionButton)  findViewById(R.id.fab_home);
        fab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThirdScreen.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }
}

