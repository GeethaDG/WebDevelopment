package com.example.Bachat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class ExportActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    ArrayList<ListItem> expenseItems;
    ArrayList<ListItem> incomeItems;
    private static final String TAG = "ExportActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.export_from_screen);
        myDb = new DatabaseHelper(this);
    }

    public void exportExpense(View view){
        //defining the data
        StringBuilder expenseData = new StringBuilder();
        StringBuilder data = new StringBuilder();
        expenseData.append("CATEGORY, SUBCATEGORY, MOP, AMOUNT, DATE(YYYY/MM/DD), NOTE");
        Cursor res = myDb.getAllData();
        Log.d(TAG, "res for exportData" + res);
        ArrayList<String> listItem = new ArrayList<>();
        if (res.getCount() == 0) {
            showMessage("Oops", "Looks like you have no data to export");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            String item = new String();
            expenseData.append("\n" + res.getString(1) + "," + res.getString(2) + ", " + res.getString(3) + "," + res.getString(4) + "," + res.getString(5));
        }

        try {
            //writing data to csv
            FileOutputStream out = openFileOutput("Expenses.csv", Context.MODE_PRIVATE);
            Log.d(TAG, "exportExpense: inside try\n" + expenseData.toString());
            out.write((expenseData.toString()).getBytes(Charset.forName("UTF-8")));
            out.close();

            //exporting
            Context context = getApplicationContext();
            File fileLocation = new File(getFilesDir(), "Expenses.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.Bachat.fileprovider", fileLocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Expense Details from Bachat");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Send Email"));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void exportEarning(View view){
        //defining the data
        StringBuilder earningData = new StringBuilder();
        earningData.append("CATEGORY, MOP, AMOUNT, DATE(YYYY/MM/DD), NOTE");
        Cursor res = myDb.getAllDataEarning();
        ArrayList<String> listItem = new ArrayList<>();
        if (res.getCount() == 0) {
            showMessage("Oops", "Looks like you have no data to export");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            earningData.append("\n" + res.getString(1) + "," + res.getString(2) + ", " + res.getString(3) + "," + res.getString(4));
        }

        try {
            FileOutputStream out = openFileOutput("Earnings.csv", Context.MODE_PRIVATE);
            Log.d(TAG, "exportEarning: "+ earningData.toString());
            out.write((earningData.toString()).getBytes(Charset.forName("UTF-8")));
            out.close();

            //exporting
            Context context = getApplicationContext();
            File fileLocation = new File(getFilesDir(), "Earnings.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.Bachat.fileprovider", fileLocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Income Details from Bachat");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Send Email"));
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
