package com.example.Bachat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FourthScreen extends AppCompatActivity {
    private static final String TAG = "FourthScreen";
    public String fromThird,mop,currency;
    DatePicker picker;
    DatabaseHelper myDb;
    public String category;
    public String subcategory;
    public String modeofpayment;
    public String amount;
    public String addedDate;
    public String addedNote;
    public String datePicked;
    static final int PICK_CONTACT = 1;
    String st;
    final private int REQUEST_MULTIPLE_PERMISSIONS = 124;
    StringBuilder sb = new StringBuilder();
    StringBuilder sb1 = new StringBuilder();
    String contactName;
    String notecheck;
    EditText editnote;
    String originalnotecheck;
    Button addnote;

    int dayOfMonth;

    //defined for currency
    public static BreakIterator data;
    List<String> keysList;
    List<String> RateList;
    String baseCurr;
    String toCurr;
    double BaseValue;
    public String currSelected;
    public static int posconversion;
    public static int negconversion;
    public static int posrates;
    public static int negrates;
    double convertedAmout;
    String resultrates = null;
    String resultconversion = null;
    //end of currency definition

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fourth_screen);
        myDb = new DatabaseHelper(this);
        getSupportActionBar().setTitle("Expense");
        getSupportActionBar().setSubtitle("Add Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Button convert = (Button) findViewById(R.id.btnConvert);

        convert.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                Toast.makeText(FourthScreen.this, "The entered currency has been converted to "+ convertedAmout + " " + HomeFragment.default_currency.substring(0,3), Toast.LENGTH_SHORT).show();
            }

        });


        //code for custom notes
        addnote =(Button) findViewById(R.id.btnAddNote);
        editnote = (EditText) findViewById(R.id.editTextNote2);
        addnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notecheck = editnote.getText().toString();
                originalnotecheck = notecheck;
                String b= originalnotecheck.replaceAll("`"," ");
                String c= b.replaceAll("~"," ");
                String d = c.replaceAll("//"," ");


                //String emptystring = ".";
                //SpannableStringBuilder spannablestring = new SpannableStringBuilder(notecheck);
                SpannableStringBuilder spannablestring = new SpannableStringBuilder(d);
                StyleSpan normalspan = new StyleSpan(Typeface.NORMAL);
                StyleSpan boldspan = new StyleSpan(Typeface.BOLD);
                StyleSpan italicsspan = new StyleSpan(Typeface.ITALIC);
                UnderlineSpan underlinespan = new UnderlineSpan();

                //spannablestring.setSpan(normalspan,0,emptystring.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

                //spannablestring.insert(emptystring.length(),notecheck);

                spannablestring.setSpan(normalspan,0,spannablestring.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                int boldstart = notecheck.indexOf("`");
                int boldend = notecheck.indexOf("`",boldstart+1);

                if (boldstart != -1 && boldend != -1){
                    String bold= notecheck.substring(boldstart+1,boldend);
                    //spannablestring.replace(boldstart,boldend+1,bold);
                    spannablestring.setSpan(boldspan,boldstart,boldend,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }


                int italicsstart = notecheck.indexOf("~");
                int italicsend = notecheck.indexOf("~",italicsstart+1);

                if(italicsstart != -1 && italicsend != -1 ){
                    String italics = notecheck.substring(italicsstart+1,italicsend);
                    //spannablestring.replace(italicsstart,italicsend+1,italics);
                    spannablestring.setSpan(italicsspan,italicsstart,italicsend,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                }


                int underlinestart = notecheck.indexOf("//");
                int underlineend = notecheck.indexOf("//",underlinestart+1);

                if (underlinestart != -1 && underlineend != -1){
                    String underline= notecheck.substring(underlinestart+1,underlineend);
                    //spannablestring.replace(underlinestart,underlineend+1,underline);
                    spannablestring.setSpan(underlinespan,underlinestart,underlineend,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }


                editnote.setText(spannablestring);
            }

        });

        //The following code is for the text spinner to enable the selection of the mode of payment
        Spinner modeSpinner = (Spinner) findViewById(R.id.spnModes);
        ArrayAdapter<CharSequence> modeAdapter = ArrayAdapter.createFromResource(this,R.array.Modeofpayment,android.R.layout.simple_list_item_1);
        //ModeofPayment is the list specified in strings.xml
        modeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        modeSpinner.setAdapter(modeAdapter);
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                String mode = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(),mode+" chosen",Toast.LENGTH_LONG).show();
                mop = mode;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //code for contacts
        Button addContacts = (Button) findViewById(R.id.buttonAddContact);
        addContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessContact();
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        Button remContacts = (Button) findViewById(R.id.buttonRemContact);
        remContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contactName != null){
                    contactName = "";
                    sb.setLength(0);
                    sb1.setLength(0);
                    Toast.makeText(FourthScreen.this, "The contact has been removed", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(FourthScreen.this, "You have not added a contact", Toast.LENGTH_SHORT).show();
                }

            }
        });
        //end of code for contacts

        //The following code is for the text spinner to enable the selection of the currency
        Spinner currencySpinner = (Spinner) findViewById(R.id.spnCurrency);
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
            @SuppressLint("LongLogTag")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                String curr = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(),mode+" chosen",Toast.LENGTH_LONG).show();
                currency = curr;
                currSelected = curr;
                Double passedConverted = convertedAmout;
                EditText editamount = (EditText) findViewById(R.id.editTextAmount2);
                String amountcheck = editamount.getText().toString();

                if (amountcheck.length() == 0) {
                    Toast.makeText(FourthScreen.this, "Please enter amount", Toast.LENGTH_SHORT).show();
                }
                else if(Float.parseFloat(amountcheck) >= 99999999f)
                {
                    Toast.makeText(FourthScreen.this, "Please enter amount which is smaller than 99999999", Toast.LENGTH_SHORT).show();
                }
                else if(Float.parseFloat(amountcheck) < 0f)
                {
                    Toast.makeText(FourthScreen.this, "Please enter amount which is greater than 0", Toast.LENGTH_SHORT).show();
                }
                else if(Float.parseFloat(amountcheck) >= 99999999f || Float.parseFloat(amountcheck) < 0f)
                {

                }
                else if(Float.parseFloat(amountcheck) < 99999999f  && Float.parseFloat(amountcheck) >= 0f  && !(amountcheck.length() == 0))
                    //Conversion
                    if(!currSelected.equals(HomeFragment.default_currency)){
                        // this is the currency in which the user is currently entering amount

                        baseCurr= currency.substring(0,3);
                        Log.d(TAG, "onClick: baseCurr :" + baseCurr);
                        //this is the currency which the users selects as default currency for his use (assuming euro here, will be set by the user in the app , all his transactions will be in this currency by default)


                        toCurr = HomeFragment.default_currency.substring(0,3);
                        Log.d(TAG, "onClick: toCurrency  " + toCurr);
                        //this is the amount of money he has entered as being spent in the base currency
                        BaseValue = Float.parseFloat(editamount.getText().toString());

                        try {

                            Boolean check = haveNetworkConnection();
                            if (check) {
                                Log.d(" connection test output is : " +check,"; internet working" );
                                resultrates = loadConvTypes();
                                while (resultrates == null){

                                }

                                if (EarningThirdScreen.posrates== 1){
                                    Log.d("on result check for rates", "everything is ok, we fetched live rates");
                                }
                                else if (EarningThirdScreen.negrates == 1){
                                    Log.d("on result check for rates", "we are facing some issues getting results at the moment");
                                }
                            }
                            else{
                                Log.d( " connection test output is : " +check,  "; There seems to be an some connection issues,  please check that you are connected to the internet"  );
                                //some code for fetching old rates from the internal database for conversion.
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(//!edtEuroValue.getText().toString().isEmpty()//
                                true)
                        {

                            //define a spinner to choose the currency for payment, toCurr
                            // get the value of amount from the edit tet for amount, BaseValue

                            //String toCurr = toCurrency.getSelectedItem().toString();
                            //double BaseValue = Double.valueOf(edtEuroValue.getText().toString());
                            //Toast.makeText(MainActivity.this, "Please Wait..", Toast.LENGTH_SHORT).show();
                            try {
                                resultconversion=convertCurrency(toCurr, BaseValue);
                                while (resultconversion == null){

                                }
                                if (EarningThirdScreen.posconversion == 1){
                                    Log.d("on result check", "everything is ok, values converted from live rates");
                                }
                                else if (EarningThirdScreen.negconversion == 1){
                                    Log.d("on result check", "we are facing some issues getting results at the moment, and now will proceed to use saved values from the database");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(FourthScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(FourthScreen.this, "Please Enter a Value to Convert..", Toast.LENGTH_SHORT).show();
                        }

                    }

                //end of code for currency conversion



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button addButton = (Button) findViewById(R.id.btnAddTransaction);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView theTextViewDate = (TextView) findViewById(R.id.textViewDate);
                picker = (DatePicker) findViewById(R.id.editTextDate2);
                TextView theTextViewNote = (TextView) findViewById(R.id.textViewNote);
                EditText editnote = (EditText) findViewById(R.id.editTextNote2);
                TextView theTextViewAmount = (TextView) findViewById(R.id.textViewAmount);
                EditText editamount = (EditText) findViewById(R.id.editTextAmount2);

                //Check for contact
                if(contactName == null || contactName.equals("")){
                    contactName = "";
                    sb.setLength(0);
                    sb.append("");
                    sb1.setLength(0);
                    sb1.append("");
                }
                //myDb.storeExpenseContacts(contactName,sb.toString(), sb1.toString());


                String date = picker.getYear() + "/" + (picker.getMonth() + 1) + "/" + picker.getDayOfMonth();


                dayOfMonth = picker.getDayOfMonth();
                if(String.valueOf(dayOfMonth).length() == 1){

                    datePicked = picker.getYear() + "/" + (picker.getMonth() + 1) + "/0" + picker.getDayOfMonth();
                    Log.d(TAG, "onClick: inside dayOfMonth loop: "+ datePicked);
                }else
                {datePicked = picker.getYear() + "/" + (picker.getMonth() + 1) + "/" + picker.getDayOfMonth();}
                Log.d(TAG, "onClick: dayOfMonth " + picker.getDayOfMonth());
                String notecheck = editnote.getText().toString();
                String note;
                String checker = editnote.getText().toString();
                if (checker.length() == 0) {
                    note = "Not Set";
                } else {
                    note = originalnotecheck;
                }

                String amountcheck = editamount.getText().toString();
                if (amountcheck.isEmpty()) {
                    Toast.makeText(FourthScreen.this, "Please enter amount", Toast.LENGTH_SHORT).show();
                }
                else if(Float.parseFloat(amountcheck) >= 99999999.99)
                {
                    Toast.makeText(FourthScreen.this, "Please enter amount which is smaller than 99999999", Toast.LENGTH_SHORT).show();
                }
                else if(Float.parseFloat(amountcheck) < 0f)
                {
                    Toast.makeText(FourthScreen.this, "Please enter amount which is greater than 0", Toast.LENGTH_SHORT).show();
                }
                else if(Float.parseFloat(amountcheck) >= 99999999f || Float.parseFloat(amountcheck) < 0f )
                {

                }

                else {
                    amount = amountcheck;
                    fromThird = getIntent().getStringExtra("fromThird");
                    String toadd = fromThird + "," + mop + "," + amount + "," + datePicked + "," + note;
                    Log.d(TAG, "onClick: toadd is " + toadd);
                    String result[] = toadd.split(",");
                    category = result[0];
                    subcategory = result[1];
                    modeofpayment = result[2];
                    amount = result[3];
                    addedDate = result[4];
                    addedNote = result[5];
                    boolean isInserted = myDb.storeExpenseTransaction(category, subcategory, modeofpayment, amount, addedDate, addedNote, currency, contactName,sb.toString(), sb1.toString(),convertedAmout);
                    if (isInserted = true)
                        Toast.makeText(FourthScreen.this, "data inserted", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(FourthScreen.this, "not inserted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FourthScreen.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        FloatingActionButton fab_home= (FloatingActionButton)  findViewById(R.id.fab_home);
        fab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FourthScreen.this, MainActivity.class);
                startActivity(intent);

            }
        });

    }

    //Method for checking if internet connection is available
    private boolean haveNetworkConnection() {
        Log.d("haveNetworkConnection:", " stared");
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        Log.d( "haveNetworkConnection:","now returning values");
        return haveConnectedWifi || haveConnectedMobile;
    }

    //methods for currency conversion
    public String loadConvTypes() throws IOException {
        String url;
        //This is the url for all the currencies compared to base currency
        String url1 = "https://api.exchangeratesapi.io/latest?base="+ baseCurr;
        //This is the url for conversion rates to app specific converison (we want conversion rates against toCurr)
        //By app specific i mean that , as we will provide conversion functionality to only a select few currencies in out spinner, we d not need to fetch data for all the currencies
        Log.d(TAG, "loadConvTypes: toCurr before if" + toCurr);
        if(!(toCurr.equals("EUR"))){
            url = "https://api.exchangeratesapi.io/latest?base="+ toCurr+"&&symbols=USD,GBP,INR,EUR";
        }else{
            url = "https://api.exchangeratesapi.io/latest?&&symbols=USD,GBP,INR";
        }


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String mMessage = e.getMessage().toString();
                //Log.w("failure Response", mMessage);
                //Toast.makeText(EarningSecondScreen.this, mMessage, Toast.LENGTH_SHORT).show();
                setNegResponseRates();
                resetPosResponseRates();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String mMessage = response.body().string();

                FourthScreen.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(MainActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject obj = new JSONObject(mMessage);
                            JSONObject  b = obj.getJSONObject("rates");

                            Iterator keysToCopyIterator = b.keys();
                            keysList = new ArrayList<String>();
                            RateList = new ArrayList<String>();

                            while (keysToCopyIterator.hasNext()) {
                                String key = (String) keysToCopyIterator.next();
                                //Log.d(TAG, "string key is:" + key);
                                String RateToCopy = b.getString(key);
                                //Log.d(TAG, "string rate to copy is: " + RateToCopy);

                                keysList.add(key);
                                RateList.add(RateToCopy);
                            }
                            //code to print out the available currencies
                            //Log.d("key list size"," : " + keysList.size());
                            for(int i = 0; i < keysList.size(); i++) {
                                Log.d("", " " + i+ ". " + keysList.get(i)+ ", rate is: " + RateList.get(i));
                            }
                            setPosResponseRates();
                            resetNegResponseRates();
                            // ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, keysList);
                            //toCurrency.setAdapter(spinnerArrayAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

        });
        return "complete";
    }


    public String convertCurrency(final String toCurr, final double baseValue) throws IOException {
        String url;
        //This is the url for all the currencies compared to base currency
        String url1 = "https://api.exchangeratesapi.io/latest?base="+ baseCurr;
        //This is the url for conversion rates to app specific converison (we want conversion rates against toCurr)
        //By app specific i mean that , as we will provide conversion functionality to only a select few currencies in out spinner, we d not need to fetch data for all the currencies
        if(!toCurr.equals("EUR")){
            url = "https://api.exchangeratesapi.io/latest?base="+ toCurr+"&&symbols=USD,GBP,INR,EUR";
        }else{
            url = "https://api.exchangeratesapi.io/latest?&&symbols=USD,GBP,INR";
        }
        Log.d(TAG, "convertCurrency: toCurrency is " + toCurr);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String mMessage = e.getMessage().toString();
                //Log.w("failure Response", mMessage);
                //Toast.makeText(EarningSecondScreen.this, mMessage, Toast.LENGTH_SHORT).show();
                setNegResponseConversion();
                resetPosResponseConversion();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String mMessage = response.body().string();
                FourthScreen.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(MainActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject obj = new JSONObject(mMessage);
                            JSONObject  b = obj.getJSONObject("rates");
                            String val = b.getString(baseCurr);

                            double output = baseValue/Double.valueOf(val);

                            //logging the value of conversion
                            Log.d("converting amount", ": " + baseValue + " from : " + baseCurr + " to user's default currency , which is : " +toCurr);
                            Log.d("converted output is", " = " + output);
                            convertedAmout = output;
                            setPosResponseConversion();
                            resetNegResponseConversion();

                            //textView.setText(String.valueOf(output));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        return "complete";
    }

    public boolean setPosResponseConversion(){
        FourthScreen.posconversion =1;
        return true;
    }
    public boolean setNegResponseConversion(){
        FourthScreen.negconversion =1;
        return true;
    }
    public boolean resetPosResponseConversion(){
        FourthScreen.posconversion=0;
        return true;
    }

    public boolean resetNegResponseConversion(){
        FourthScreen.negconversion=0;
        return true;
    }

    public boolean setPosResponseRates(){
        FourthScreen.posrates =1;
        return true;
    }
    public boolean setNegResponseRates(){
        FourthScreen.negrates= 1;
        return true;
    }
    public boolean resetPosResponseRates(){
        FourthScreen.posrates =0;
        return true;
    }
    public boolean resetNegResponseRates(){
        FourthScreen.negrates =0;
        return true;
    }


    //methods for contacts
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                   /* if (c.moveToFirst()) {
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        try {
                            if (hasPhone.equalsIgnoreCase("1")) {
                                Cursor phones = getContentResolver().query(
                                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                        null, null);
                                phones.moveToFirst();
                                String cNumber = phones.getString(phones.getColumnIndex("data1"));
                                System.out.println("number is:" + cNumber);
                                Log.d(TAG, "number is:" + cNumber);
                                //txtphno.setText("Phone Number is: "+cNumber);
                            }
                            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            //txtname.setText("Name is: "+name);
                            System.out.println("name is:" + name);
                            Log.d(TAG, "name is:" + name);
                        }*/

                    while(c.moveToNext())
                    {
                        String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        contactName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        try{
                            //the below cursor will give you details for multiple contacts
                            Cursor emailCursor = getBaseContext().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                    new String[]{id}, null);
                            // continue till this cursor reaches to all emails  which are associated with a contact in the contact list
                            while (emailCursor.moveToNext())
                            {
                                int emailType         = emailCursor.getInt(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                                //String isStarred        = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED));
                                String email    = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                                //you will get all emails according to it's type as below switch case.
                                //Logs.e will print the phone number along with the name in DDMS. you can use these details where ever you want.

                                switch (emailType)
                                {
                                    case ContactsContract.CommonDataKinds.Email.TYPE_MOBILE:
                                        sb1.append(email+ " , ");
                                        Log.e(  " TYPE_MOBILE", " TYPE_MOBILE" + email + " ,");
                                        break;
                                    case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                                        sb1.append(email+ " , ");
                                        Log.e(" TYPE_HOME", "TYPE_HOME " + email+ " ,");
                                        break;
                                    case ContactsContract.CommonDataKinds.Email.TYPE_WORK:
                                        sb1.append(email+ " , ");
                                        Log.e(" TYPE_WORK", " TYPE_WORK" + email+ " ,");
                                        break;
                                    case ContactsContract.CommonDataKinds.Email.TYPE_OTHER:
                                        sb1.append(email+ " , ");
                                        Log.e(" TYPE_OTHER", " TYPE_OTHER" + email+ " ,");
                                        break;
                                    case ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM:
                                        sb1.append(email+ " , ");
                                        Log.e(" TYPE_CUSTOM", " TYPE_CUSTOM" + email+ " ,");
                                        break;

                                }

                            }


                            emailCursor.close();
                        }

                        catch (Exception ex)
                        {
                            Log.d(TAG, "onActivityResult: inside catch block ," + ex.getMessage());
                            //st.getMessage();
                        }
                        Log.d("allnumbers", " " + sb.toString());
                        Log.d("contact name is: ", " "+ contactName );


                        if (Integer.parseInt(c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                            try{
                                //the below cursor will give you details for multiple contacts
                                Cursor pCursor = getBaseContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                                        new String[]{id}, null);
                                // continue till this cursor reaches to all phone numbers which are associated with a contact in the contact list
                                while (pCursor.moveToNext())
                                {
                                    int phoneType         = pCursor.getInt(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                                    //String isStarred        = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED));
                                    String phoneNo    = pCursor.getString(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    //you will get all phone numbers according to it's type as below switch case.
                                    //Logs.e will print the phone number along with the name in DDMS. you can use these details where ever you want.

                                    switch (phoneType)
                                    {
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                            sb.append(phoneNo+ " , ");
                                            Log.e(  " TYPE_MOBILE", " TYPE_MOBILE" + phoneNo + " ,");
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                            sb.append(phoneNo+ " , ");
                                            Log.e(" TYPE_HOME", "TYPE_HOME " + phoneNo+ " ,");
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                            sb.append(phoneNo+ " , ");
                                            Log.e(" TYPE_WORK", " TYPE_WORK" + phoneNo+ " ,");
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
                                            sb.append(phoneNo+ " , ");
                                            Log.e(" TYPE_WORK_MOBILE", " TYPE_WORK_MOBILE" + phoneNo+ " ,");
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                                            sb.append(phoneNo+ " , ");
                                            Log.e(" TYPE_OTHER", " TYPE_OTHER" + phoneNo+ " ,");
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_RADIO:
                                            sb.append(phoneNo+ " , ");
                                            Log.e(" TYPE_Radio", " TYPE_Radio" + phoneNo+ " ,");
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD:
                                            sb.append(phoneNo+ " , ");
                                            Log.e(" TYPE_TTY_TDD", " TYPE_TTY_TDD" + phoneNo+ " ,");
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER:
                                            sb.append(phoneNo+ " , ");
                                            Log.e(" TYPE_WORK_PAGER", " TYPE_WORK_PAGER" + phoneNo+ " ,");
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                                            sb.append(phoneNo+ " , ");
                                            Log.e(" TYPE_FAX_WORK", " TYPE_FAX_WORK" + phoneNo+ " ,");
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                                            sb.append(phoneNo+ " , ");
                                            Log.e(" TYPE_FAX_HOME", " TYPE_FAX_HOME" + phoneNo+ " ,");
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
                                            sb.append(phoneNo+ " , ");
                                            Log.e(" TYPE_PAGER", " TYPE_PAGER" + phoneNo+ " ,");
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM:
                                            sb.append(phoneNo+ " , ");
                                            Log.e(" TYPE_CUSTOM", " TYPE_CUSTOM" + phoneNo+ " ,");
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK:
                                            sb.append(phoneNo+ " , ");
                                            Log.e(" TYPE_CALLBACK", " TYPE_CALLBACK" + phoneNo+ " ,");
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_CAR:
                                            sb.append(phoneNo+ " , ");
                                            Log.e(" TYPE_CAR", " TYPE_CAR" + phoneNo+ " ,");
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN:
                                            sb.append(phoneNo+ " , ");
                                            Log.e(" TYPE_COMPANY_MAIN", " TYPE_COMPANY_MAIN" + phoneNo+ " ,");
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_ISDN:
                                            sb.append(phoneNo+ " , ");
                                            Log.e(" TYPE_ISDN", " TYPE_ISDN" + phoneNo+ " ,");
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN:
                                            sb.append(phoneNo+ " , ");
                                            Log.e(" TYPE_MAIN", " TYPE_MAIN" + phoneNo+ " ,");
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_MMS:
                                            sb.append(phoneNo+ " , ");
                                            Log.e(" TYPE_MMS", " TYPE_MMS" + phoneNo+ " ,");
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX:
                                            sb.append(phoneNo+ " , ");
                                            Log.e(" TYPE_OTHER_FAX", " TYPE_OTHER_FAX" + phoneNo+ " ,");
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_TELEX:
                                            sb.append(phoneNo+ " , ");
                                            Log.e(" TYPE_TELEX", " TYPE_TELEX" + phoneNo+ " ,");
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT:
                                            sb.append(phoneNo+ " , ");
                                            Log.e(" TYPE_ASSISTANT", " TYPE_ASSISTANT" + phoneNo+ " ,");
                                            break;
                                        default:
                                            break;
                                    }

                                }


                                pCursor.close();
                            }

                            catch (Exception ex)
                            {
                                Log.d(TAG, "onActivityResult: inside catch block ," + ex.getMessage());
                                //st.getMessage();
                            }

                        Log.d("contact name is: ", " "+ contactName );
                        Log.d("allnumbers", " " + sb.toString());
                        Log.d("allemails", " " + sb1.toString());



                    }
                }
                break;
        }
    }

    private void AccessContact()
    {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("Read Contacts");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS))
            permissionsNeeded.add("Write Contacts");
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_MULTIPLE_PERMISSIONS);
            return;
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);

            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(FourthScreen.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
  /*  public void AddData (Button btn){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDb.insertData(category, subcategory, modeofpayment, amount, addedDate, addedNote);
                if (isInserted = true)
                    Toast.makeText(FourthScreen.this, "data inserted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(FourthScreen.this, "not inserted", Toast.LENGTH_SHORT).show();

            }
        });
    }*/

}