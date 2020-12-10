package com.example.Bachat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener//,HomeFragment.onFragmentBtnSelected
{
    private static final String TAG = "MainActivity";
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    TextView toolbar_title_set;
    public String firstDate;
    public String secondDate;
    public String category;
    public String modeOfPayment;
    public String a;
    public String showAll;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: MainActivity Started" );


        Log.d("Time zone", "=" + Calendar.getInstance().getTime());

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
//show sign up activity
            startActivity(new Intent(MainActivity.this, SplashscreenActivity.class));
//startActivity(new Intent(MainActivity.this, SetupScreen.class));
        }


        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();

        String view_threshold_fragment = getIntent().getStringExtra("open_set_threshold");
        Log.d(TAG, "onCreate: "+view_threshold_fragment);
        String view_Expense_fragment = getIntent().getStringExtra("open_view_all_expenses");
        Log.d(TAG, "onCreate: "+view_Expense_fragment);
        String view_Income_fragment = getIntent().getStringExtra("open_view_all_income");
        Log.d(TAG, "onCreate: "+view_Expense_fragment);
        String view_Import_Export_fragment = getIntent().getStringExtra("Import_Export_Screen");
        String view_Settings_fragment = getIntent().getStringExtra("settings_screen");
        String view_filter_fragment = getIntent().getStringExtra("FilterScreen");

        if(view_threshold_fragment!=null){
            view_threshold_fragment="Open Threshold View";
        }
        else if(view_Expense_fragment!=null){
            view_Expense_fragment="Open Expense View";
            Log.d(TAG, "onCreate:" + view_Expense_fragment);
        }
        else if (view_Income_fragment != null) {
            view_Income_fragment="Open Income View";
            Log.d(TAG, "onCreate:" + view_Income_fragment);
        }
        else if (view_Import_Export_fragment != null){
            view_Import_Export_fragment = "Open Import Export Screen";
        }
        else if(view_Settings_fragment!=null){
            view_Settings_fragment="Open Settings Fragment";
        }
        else if(view_filter_fragment != null){
            view_filter_fragment = "ShowAllExpenseFragment";
        }
        else{
            view_threshold_fragment="Open Home Fragment";
            view_Expense_fragment="Open Home Fragment";
            view_Import_Export_fragment="Open Home Fragment";
            view_filter_fragment = "Open Home Fragment";
        }
        //onSaveInstanceState(savedInstanceState,view_fragment);
        //Declaring all the components of Navigation Menu
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar_title_set=findViewById(R.id.toolbar_title);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        //For the toggle animation as the user open navigation menu
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();



        if(savedInstanceState == null) {
            if(view_threshold_fragment == "Open Threshold View"){
                toolbar_title_set.setText("Threshold");
                //toolbar_title_set_2.setText("Threshold");
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new SetThresholdFragment()).commit();
                view_threshold_fragment=null;
            }
            else if( view_Expense_fragment=="Open Expense View" || view_Income_fragment=="Open Income View"){
                toolbar_title_set.setText("History");

                //toolbar_title_set_2.setText("History");
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new TransactionHistory()).commit();
                view_Expense_fragment=null;
            }
            else if(view_Import_Export_fragment=="Open Import Export Screen"){
                toolbar_title_set.setText("Import/Export");
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new ImportExportFragment()).commit();
                view_Import_Export_fragment=null;
            }
            else if(view_Settings_fragment=="Open Settings Fragment"){
                toolbar_title_set.setText("Settings");
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new SettingsFragment()).commit();
                view_Settings_fragment=null;
            }
            else if(view_filter_fragment.equals("ShowAllExpenseFragment")){
                toolbar_title_set.setText("Test");
                Bundle bundle = new Bundle();
                bundle.putString("Filter_options",a);
                ShowAllExpenseFragment showAllExpenseFragment = new ShowAllExpenseFragment();
                showAllExpenseFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, showAllExpenseFragment).commit();
                a = null;
                view_filter_fragment = null;
            }
            else
                {
                toolbar_title_set.setText("Bachat");
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new HomeFragment()).commit();
            }
        }
    }


    //Navigation Menu Items on click fragment loading
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.menuHome:
                toolbar_title_set.setText("Bachat");
                //toolbar_title_set_2.setText("");
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new HomeFragment()).commit();
                break;
            case R.id.menuThreshold:
                toolbar_title_set.setText("Threshold");
                //toolbar_title_set_2.setText("Threshold");
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new SetThresholdFragment()).commit();
                break;
            case R.id.menuTransactionHistory:
                toolbar_title_set.setText("History");
                //toolbar_title_set_2.setText("History");
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new TransactionHistory()).commit();
                break;
            case R.id.menuImportExport:
                toolbar_title_set.setText("Import/Export");
                //toolbar_title_set_2.setText("Import/Export");
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new ImportExportFragment()).commit();
                break;
            case R.id.menuSettings:
                toolbar_title_set.setText("Settings");
                //toolbar_title_set_2.setText("Settings");
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new SettingsFragment()).commit();
                break;
            case R.id.menuAbout:
                //toolbar_title_set.setText("About");
                //toolbar_title_set_2.setText("About");
                //getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new ImportExport()).commit();
                Toast.makeText(this, "Basic Prototype", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuGraphs:
                toolbar_title_set.setText("Graphs");
                //toolbar_title_set_2.setText("About");
                //getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new ImportExport()).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new graph_fragment()).commit();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

/*@Override
public boolean onCreateOptionsMenu(Menu menu) {
MenuInflater inflater=getMenuInflater();
inflater.inflate(R.menu.option_menu,menu);
return true;
}

@Override
public boolean onOptionsItemSelected(@NonNull MenuItem item) {
switch (item.getItemId()) {
case R.id.menuSettings:
getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,
new Settings()).commit();
return true;
case R.id.menu_1:
Toast.makeText(this, "Menu 1 selected", Toast.LENGTH_SHORT).show();
return true;
case R.id.menu_2:
Toast.makeText(this, "Menu 2 selected", Toast.LENGTH_SHORT).show();
return true;
default:
return super.onOptionsItemSelected(item);
}
}*/
}

/*public Bundle onSaveInstanceState(Bundle savedInstanceState,String view_fragment) {
super.onSaveInstanceState(savedInstanceState);
// Save UI state changes to the savedInstanceState.
// This bundle will be passed to onCreate if the process is
// killed and restarted.
if(view_fragment == null){
return savedInstanceState;
}
else{
savedInstanceState.putString("MyString", view_fragment);
return savedInstanceState;
}
}

/*@Override
public void onButtonSelected() {
Toast.makeText(this, "Fragment Button is working", Toast.LENGTH_SHORT).show();
}

/*
// DatabaseHelper myDb = null;
private static final String TAG = "MainActivity";

@Override
protected void onCreate(Bundle savedInstanceState) {
//Test Commit
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);
Log.d(TAG, "onCreate: MainActivity Started" );
// myDb= new DatabaseHelper(this);

TextView showallearning = (TextView) findViewById(R.id.textViewShowAllEarning);
showallearning.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent intent = new Intent(MainActivity.this,EarningShowAllScreen.class);
startActivity(intent);
}
});
TextView showallexpense = (TextView) findViewById(R.id.textViewShowAllExpense);
showallexpense.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent intent = new Intent(MainActivity.this,ShowAllScreen.class);
startActivity(intent);
}
});

Button threshold = (Button) findViewById(R.id.btnGoToSetThreshold);
threshold.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent intent = new Intent(MainActivity.this, ThresholdViewScreen.class);
startActivity(intent);
}
});

/* Button viewthreshold = (Button) findViewById(R.id.btnGoToViewThreshold);
viewthreshold.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent intent = new Intent(MainActivity.this, ThresholdCategoryScreen.class);
startActivity(intent);
}
});*/
//addding here again because one was already existing
/*ImageView plusImage = (ImageView) findViewById(R.id.plusImage);
int imageResource = getResources().getIdentifier("@drawable/plus", null, this.getPackageName());
plusImage.setImageResource(imageResource);
plusImage.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Log.d(TAG, "onClick: you clicked on the plus image");
//Toast.makeText(MainActivity.this, "image clicked", Toast.LENGTH_SHORT).show();
Intent intent = new Intent(MainActivity.this, EarningSecondScreen.class);
startActivity(intent);
}
});
ImageView minusImage = (ImageView) findViewById(R.id.minusImage);
int imageResourcea = getResources().getIdentifier("@drawable/minus", null, this.getPackageName());
minusImage.setImageResource(imageResourcea);
minusImage.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Log.d(TAG, "onClick: you clicked on the minus image");
//Toast.makeText(MainActivity.this, "image clicked", Toast.LENGTH_SHORT).show();
Intent intent = new Intent(MainActivity.this, SecondScreen.class);
startActivity(intent);
}
});
ImageView convertImage = (ImageView) findViewById(R.id.convertImage);
int imageResourceb = getResources().getIdentifier("@drawable/convert", null, this.getPackageName());
convertImage.setImageResource(imageResourceb);
convertImage.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Log.d(TAG, "onClick: you clicked on the convert image");
//Toast.makeText(MainActivity.this, "image clicked", Toast.LENGTH_SHORT).show();
Intent intent = new Intent(MainActivity.this, ImportToScreen.class);
startActivity(intent);
}
});
}*/

