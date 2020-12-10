package com.example.Bachat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class SecondScreen extends AppCompatActivity {

    private static final String TAG = "SecondScreen";
    @Override

    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_screen);
        Log.d(TAG, "onCreate:Started SecondScreen");


        //ListView mListView = (ListView) findViewById(R.id.theGrid);
        GridView mListView = (GridView) findViewById(R.id.theGrid);
        getSupportActionBar().setTitle("Expense");
        getSupportActionBar().setSubtitle("Choose Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab_home= (FloatingActionButton)  findViewById(R.id.fab_home);
        fab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondScreen.this, MainActivity.class);
                startActivity(intent);

            }
        });



        //Create the Person objects
        /*
        Person john = new Person("John","12-20-1998","Male",
                getResources().getIdentifier("@drawable/cartman_cop", null,this.getPackageName()));
                */

        Category category1 = new Category("Health", "drawable://" + R.drawable.healthcare);
        Category category2 = new Category("Donations", "drawable://" + R.drawable.donations);
        Category category3 = new Category("Bills", "drawable://" + R.drawable.billsicon);
        Category category4 = new Category("Shopping", "drawable://" + R.drawable.shoppingicon);
        Category category5 = new Category("Dining Out", "drawable://" + R.drawable.dinningout);
        Category category6 = new Category("Entertainment", "drawable://" + R.drawable.entertaimenticon);;
        Category category7= new Category("Groceries", "drawable://" + R.drawable.groceries_icon);
        Category category8 = new Category("Pet Care", "drawable://" + R.drawable.petsicon);
        Category category9 = new Category("Transportation", "drawable://" + R.drawable.transportation);
        Category category10 = new Category("Loans", "drawable://" + R.drawable.loansicon);
        Category category11 = new Category("Personal Care", "drawable://" + R.drawable.personalcareicon);
        Category category12= new Category("Miscellaneous", "drawable://" + R.drawable.miscellaneousicon);



        //Add the Person objects to an ArrayList
        final ArrayList<Category> categoryList = new ArrayList<>();
        categoryList.add(category1);
        categoryList.add(category3);
        categoryList.add(category2);
        categoryList.add(category4);
        categoryList.add(category5);
        categoryList.add(category6);
        categoryList.add(category7);
        categoryList.add(category8);
        categoryList.add(category9);
        categoryList.add(category10);
        categoryList.add(category11);
        categoryList.add(category12);


        CategoryListAdapter adapter = new CategoryListAdapter(this, R.layout.adapter_view_layout, categoryList);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.d(TAG, "onItemClick:you clicked on a list item  " + peopleList.get(position).getName());
                //toast.makeText(SecondScreen.this, "you clicked on " + peopleList.get(position).getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SecondScreen.this, ThirdScreen.class);
                String toadd =  categoryList.get(position).getName();
                Log.d(TAG, "onItemClick: "+ toadd);
                intent.putExtra("fromSecond",toadd);
                startActivity(intent);
            }
        });
    }
}





