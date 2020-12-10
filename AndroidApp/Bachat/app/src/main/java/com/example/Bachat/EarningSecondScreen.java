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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
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
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class EarningSecondScreen extends AppCompatActivity {

    private static final String TAG = "SecondScreen";
    //defined for contacts//
    static final int PICK_CONTACT = 1;
    String st;
    final private int REQUEST_MULTIPLE_PERMISSIONS = 124;
    //end of contacts definition
    //defined for currency
    public static BreakIterator data;
    List<String> keysList;
    List<String> RateList;
    String baseCurr;
    String toCurr;
    double BaseValue;
    public static int posconversion;
    public static int negconversion;
    public static int posrates;
    public static int negrates;
    String resultrates = null;
    String resultconversion = null;
    DatabaseHelper myDb;
    //end of currency definition

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_screen);
        myDb = myDb = new DatabaseHelper(this);

        Log.d(TAG, "profile object : " + HomeFragment.current_profile);
        Log.d(TAG, "onCreate:Started Earning SecondScreen");
        Log.d("Time zone", "=" + Calendar.getInstance().getTime());
        getSupportActionBar().setTitle("Income");
        getSupportActionBar().setSubtitle("Choose Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //ListView mListView = (ListView) findViewById(R.id.theGrid);
        GridView mListView = (GridView) findViewById(R.id.theGrid);


        //code for contacts
        //AccessContact();

        /*Button contacts = (Button) findViewById(R.id.btnContact);
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });*/




        //end of code for contacts






        //code for currency conversion
        Button convert = (Button) findViewById(R.id.btnConvert);
        // this is the currency in which the user is currently entering amount
        baseCurr= "USD";
        //this is the currency which the users selects as default currency for his use (assuming euro here, will be set by the user in the app , all his transactions will be in this currency by default)
        toCurr = "EUR";
        //this is the amount of money he has entered as being spent in the base currency
        BaseValue = 100;

        try {

           Boolean check = haveNetworkConnection();
           if (check) {
               Log.d(" connection test output is : " +check,"; internet working" );
               resultrates = loadConvTypes();
               while (resultrates == null){

               }

                if (EarningSecondScreen.posrates== 1){
                   Log.d("on result check for rates", "everything is ok, we fetched live rates");
                }
                else if (EarningSecondScreen.negrates == 1){
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

            convert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                            if (EarningSecondScreen.posconversion == 1){
                                Log.d("on result check", "everything is ok, values converted from live rates");
                            }
                            else if (EarningSecondScreen.negconversion == 1){
                                Log.d("on result check", "we are facing some issues getting results at the moment, and now will proceed to use saved values from the database");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(EarningSecondScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(EarningSecondScreen.this, "Please Enter a Value to Convert..", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        //end of code for currency conversion


        //Create the Person objects

        Category earningcategory1 = new Category("Salary", "drawable://" + R.drawable.salary_icon);
        Category earningcategory2 = new Category("Business", "drawable://" + R.drawable.business_icon);
        Category earningcategory3 = new Category("Gifts", "drawable://" + R.drawable.gifts_icon_2);
        Category earningcategory4 = new Category("Bonus", "drawable://" + R.drawable.bonus_icon);
        Category earningcategory5 = new Category("Interest", "drawable://" + R.drawable.interest_icon);
        Category earningcategory6 = new Category("Cashback", "drawable://" + R.drawable.cashback_icon);;
        Category earningcategory7= new Category("Refund", "drawable://" + R.drawable.refund_icon);
        Category earningcategory8 = new Category("Donation", "drawable://" + R.drawable.donations_income_icon);
        Category earningcategory9 = new Category("Insurance", "drawable://" + R.drawable.insurance_payout_icon);
        Category earningcategory10 = new Category("Credit Points", "drawable://" + R.drawable.credit_points_icon);
        Category earningcategory11 = new Category("Extra Income", "drawable://" + R.drawable.extra_income_icon);
        Category earningcategory12 = new Category("Others", "drawable://" + R.drawable.other_income_icon);



        //Add the Person objects to an ArrayList
        final ArrayList<Category> earningcategoryList = new ArrayList<>();
        earningcategoryList.add(earningcategory1);
        earningcategoryList.add(earningcategory2);
        earningcategoryList.add(earningcategory3);
        earningcategoryList.add(earningcategory4);
        earningcategoryList.add(earningcategory5);
        earningcategoryList.add(earningcategory6);
        earningcategoryList.add(earningcategory7);
        earningcategoryList.add(earningcategory8);
        earningcategoryList.add(earningcategory9);
        earningcategoryList.add(earningcategory10);
        earningcategoryList.add(earningcategory11);
        earningcategoryList.add(earningcategory12);


        CategoryListAdapter adapter = new CategoryListAdapter(this, R.layout.adapter_view_layout, earningcategoryList);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.d(TAG, "onItemClick:you clicked on a list item  " + peopleList.get(position).getName());
                //toast.makeText(SecondScreen.this, "you clicked on " + peopleList.get(position).getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EarningSecondScreen.this, EarningThirdScreen.class);
                String toadd =  earningcategoryList.get(position).getName();
                intent.putExtra("fromEarningSecond",toadd);
                startActivity(intent);
            }
        });

        FloatingActionButton fab_home= (FloatingActionButton)  findViewById(R.id.fab_home);
        fab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EarningSecondScreen.this, MainActivity.class);
                startActivity(intent);

            }
        });


    }

    //Method for chcecking if internet connection is available
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

    //This is the url for all the currencies compared to base currency
    String url1 = "https://api.exchangeratesapi.io/latest?base="+ baseCurr;
    //This is the url for conversion rates to app specific converison (we want conversion rates against toCurr)
    //By app specific i mean that , as we will provide conversion functionality to only a select few currencies in out spinner, we d not need to fetch data for all the currencies
    String url = "https://api.exchangeratesapi.io/latest?base="+ toCurr+"&&symbols=USD,GBP,INR";

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

            EarningSecondScreen.this.runOnUiThread(new Runnable() {
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
        //This is the url for all the currencies compared to base currency
        String url1 = "https://api.exchangeratesapi.io/latest?base="+ baseCurr;
        //This is the url for conversion rates to app specific converison (we want conversion rates against toCurr)
        //By app specific i mean that , as we will provide conversion functionality to only a select few currencies in out spinner, we d not need to fetch data for all the currencies
        String url = "https://api.exchangeratesapi.io/latest?base="+ toCurr+"&&symbols=USD,GBP,INR";

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
                EarningSecondScreen.this.runOnUiThread(new Runnable() {
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
        EarningSecondScreen.posconversion =1;
        return true;
    }
    public boolean setNegResponseConversion(){
        EarningSecondScreen.negconversion =1;
        return true;
    }
    public boolean resetPosResponseConversion(){
        EarningSecondScreen.posconversion=0;
        return true;
    }

    public boolean resetNegResponseConversion(){
        EarningSecondScreen.negconversion=0;
        return true;
    }

    public boolean setPosResponseRates(){
        EarningSecondScreen.posrates =1;
        return true;
    }
    public boolean setNegResponseRates(){
        EarningSecondScreen.negrates= 1;
        return true;
    }
    public boolean resetPosResponseRates(){
        EarningSecondScreen.posrates =0;
        return true;
    }
    public boolean resetNegResponseRates(){
        EarningSecondScreen.negrates =0;
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
                        StringBuilder sb = new StringBuilder();
                        StringBuilder sb1 = new StringBuilder();
                        String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String contactName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
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

                        //myDb.storeEarningContacts(contactName,sb.toString(), sb1.toString());

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
        new AlertDialog.Builder(EarningSecondScreen.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    //end of method for contacts
}





