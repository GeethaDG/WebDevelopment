package com.example.Bachat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EarningEditUpdateScreen extends AppCompatActivity {
    DatabaseHelper myDb;
    private static final String TAG = "EarningEditScreen";
    public String fromShowAll;
    public String id;
    String[] splitstring;

    public Button updateButton;
    public String updatedea;
    public String updateded;
    public String updatedn;
    DatePickerDialog picker;
    public Button addnote;
    public String notecheck;
    public String originalnotecheck;

    //Additional
    public String updatedMop;
    Spinner editSpinnerMOP;
    StringBuffer currEntered;
    StringBuffer modeEntered;
    String updatedCurr;
    StringBuffer emailEntered;
    StringBuffer phoneEntered;
    StringBuffer conEntered;
    String contactName;
    StringBuilder sb = new StringBuilder();
    StringBuilder sb1 = new StringBuilder();
    final private int REQUEST_MULTIPLE_PERMISSIONS = 124;
    static final int PICK_CONTACT = 1;
    EditText editContact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = new DatabaseHelper(this);
        Log.d(TAG, "onCreate: EditScreen created");
        setContentView(R.layout.earning_edit_update_screen);
        // TextView editid =(TextView) findViewById(R.id.textViewEditId);
        TextView editcategory =(TextView) findViewById(R.id.textViewEditCategory);
        final EditText editamount = (EditText) findViewById(R.id.editTextEditAmount);
        final EditText editdate = (EditText) findViewById(R.id.editTextEditDate);
        final EditText editnote = (EditText) findViewById(R.id.editTextEditNote);
        editdate.setInputType(InputType.TYPE_NULL);
        editdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(EarningEditUpdateScreen.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                editdate.setText(  year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        fromShowAll =getIntent().getStringExtra("toeditfromearningshowall");
        splitstring= fromShowAll.split(",");
        id= splitstring[0];
        editcategory.setText(splitstring[1]);
        editamount.setText(splitstring[2]);
        editdate.setText(splitstring[3]);


        Cursor resMode = myDb.getEarningMop(splitstring[0]);
        modeEntered= new StringBuffer();
        while (resMode.moveToNext())
        {
            modeEntered.append(resMode.getString(0));
        }

        //Spinner setting for editing mode of payment
        editSpinnerMOP = (Spinner) findViewById(R.id.spnEditMOP);
        ArrayAdapter<CharSequence> modeAdapter = ArrayAdapter.createFromResource(this,R.array.Modeofpayment,android.R.layout.simple_list_item_1);

        //ModeofPayment is the list specified in strings.xml
        modeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        editSpinnerMOP.setAdapter(modeAdapter);
        String compareValueMOP = modeEntered.toString();
        if (compareValueMOP != null) {
            int spinnerPosition = modeAdapter.getPosition(compareValueMOP);
            editSpinnerMOP.setSelection(spinnerPosition);
        }

        //To set currency
        Cursor resCurr = myDb.getCurrencyEntered(splitstring[0]);
        currEntered= new StringBuffer();
        while (resCurr.moveToNext())
        {
            currEntered.append(resCurr.getString(0));
        }

        //The following code is for the text spinner to enable the selection of the currency
        Spinner currencySpinner = (Spinner) findViewById(R.id.spnCurr);
        ArrayAdapter<CharSequence> currencyAdapter = ArrayAdapter.createFromResource(this,R.array.Currencies,android.R.layout.simple_list_item_1);
        //Currencies is the list specified in strings.xml
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        currencySpinner.setAdapter(currencyAdapter);
        String compareValueCurr = currEntered.toString();
        if (compareValueCurr != null) {
            int spinnerPosition = currencyAdapter.getPosition(compareValueCurr);
            currencySpinner.setSelection(spinnerPosition);
        }
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                updatedCurr = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //To set contactname
        Cursor resCon = myDb.getContactName(splitstring[0]);
        conEntered= new StringBuffer();
        while (resCon.moveToNext())
        {
            conEntered.append(resCon.getString(0));
        }

        editContact = (EditText) findViewById(R.id.editTextContact);
        if(conEntered.toString().equals(""))
        {
            editContact.setHint("Click here to add a contact");
        }else{
            editContact.setText(conEntered.toString());
            contactName = conEntered.toString();
        }

        //To Add a contact
        editContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessContact();
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);

            }
        });

        //To Remove Contact
        Button buttonRemContact = (Button) findViewById(R.id.btnRemContact);
        buttonRemContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editContact.getText().clear();
                editContact.setHint("Click here to add a contact");
            }
        });


        //To set contactphone
        Cursor resPhone = myDb.getContactPhone(splitstring[0]);
        phoneEntered= new StringBuffer();
        while (resPhone.moveToNext())
        {
            phoneEntered.append(resPhone.getString(0));
        }


        //To set contactemail
        Cursor resEmail = myDb.getContactEmail(splitstring[0]);
        emailEntered= new StringBuffer();
        while (resEmail.moveToNext())
        {
            emailEntered.append(resEmail.getString(0));
        }


        Cursor res = myDb.getNoteEarning(splitstring[0]);
        StringBuffer note= new StringBuffer();
        while (res.moveToNext())
        {
            note.append(res.getString(0));
        }
        // editnote.setText(note.toString());

        //code for custom notes
        notecheck = note.toString();
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
        Log.d(TAG, "" + editnote + " " +  spannablestring );


        addnote =(Button) findViewById(R.id.btnEditAddNote);

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





        updateButton = (Button) findViewById(R.id.btnUpdate);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedea = editamount.getText().toString();
                updatedMop = editSpinnerMOP.getSelectedItem().toString();
                updateded = editdate.getText().toString();
                updatedn = originalnotecheck;

                if(updatedea.length() == 0)
                {
                    Toast.makeText(EarningEditUpdateScreen.this, "Please enter amount ", Toast.LENGTH_SHORT).show();
                }
                else if(Float.parseFloat(updatedea) >= 99999999f)
                {
                    Toast.makeText(EarningEditUpdateScreen.this, "Please enter amount which is smaller than 99999999", Toast.LENGTH_SHORT).show();
                }
                else if(Float.parseFloat(updatedea) < 0f)
                {
                    Toast.makeText(EarningEditUpdateScreen.this, "Please enter amount which is greater than 0", Toast.LENGTH_SHORT).show();
                }

                else if(Float.parseFloat(updatedea) >=0f && Float.parseFloat(updatedea) < 99999999f  && !(updatedea.length() == 0))
                {
                    UpdateData();
                }
                //Intent intent =new Intent(EarningEditScreen.this, EarningShowAllScreen.class);
                //startActivity(intent);
            }
        });

        FloatingActionButton fab_home= (FloatingActionButton)  findViewById(R.id.fab_home);
        fab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EarningEditUpdateScreen.this, MainActivity.class);
                startActivity(intent);

            }
        });

        ImageButton back = (ImageButton) findViewById(R.id.btn_Back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EarningEditUpdateScreen.this, MainActivity.class);
                intent.putExtra("open_view_all_income", "Open view all income screen");
                startActivity(intent);
            }
        });

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
                        editContact.setText(contactName);



                    }
                }
                break;
        }
    }


    //Methods for adding contact
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
        new AlertDialog.Builder(EarningEditUpdateScreen.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }






    public void UpdateData() {
        // updateButton.setOnClickListener(
        //   new View.OnClickListener() {
        // @Override
        //public void onClick(View v) {
        boolean isUpdate = myDb.updateDataEarning(id,splitstring[1],updatedMop,updatedea,updateded,updatedn,updatedCurr,contactName,sb.toString(), sb1.toString());
        Log.d(TAG, "update string is : "+updatedea+" "+updateded);
        if(isUpdate == true) {
            Toast.makeText(EarningEditUpdateScreen.this, "Data Update", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(EarningEditUpdateScreen.this, MainActivity.class);
            intent.putExtra("open_view_all_income", "Open view all income screen");
            startActivity(intent);
        }
        else {
            Toast.makeText(EarningEditUpdateScreen.this, "Data not Updated", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(EarningEditUpdateScreen.this, MainActivity.class);
            intent.putExtra("open_view_all_income", "Open view all income screen");
            startActivity(intent);
        }
        //}
        //  }
        //);
    }

    //private method to set default value for MOP spinner
    public int getIndex(Spinner spinner, String myString) {
        Log.d(TAG, "getIndex: CompareValue passing in" + myString);
        Log.d(TAG, "getIndex: Spinner value getting from position 2 " + spinner.getItemAtPosition(2));
        Log.d(TAG, "getIndex: Spinner value getting from position 2 converted to string " + spinner.getItemAtPosition(2).toString());
        Log.d(TAG, "getIndex: Comparison result " + spinner.getItemAtPosition(2).toString().equalsIgnoreCase(myString));
        for (int i = 0; i < spinner.getCount(); i++) {
            String adjust = " " + spinner.getItemAtPosition(i).toString();
            if (adjust.equalsIgnoreCase(myString)) {
                Log.d(TAG, "getIndex: Value will be " + i);
                return i;
            }
        }

        return 0;
    }
}
