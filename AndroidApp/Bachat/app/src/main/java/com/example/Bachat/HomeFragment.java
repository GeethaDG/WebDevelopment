package com.example.Bachat;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {
    //private onFragmentBtnSelected listener;
    //private onFragmentBtnSelected listener2;
    private static final String TAG = "HomeFragment";
    Button imageButton;
    Button imageButton2;
    Button test_button_2;
    DatabaseHelper myDb;
    public static String username;
    public static String current_profile;
    public static String default_currency;
    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntries;
    ArrayList PieEntryLabels;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        myDb = new DatabaseHelper(getActivity());
        Calendar currentTime = Calendar.getInstance();
        Log.d("Time zone", "= " + currentTime.HOUR + " " + currentTime.AM);
        String time = String.valueOf(currentTime.getTime());
        Log.d(TAG, "onCreateView: " + time);
        String compare[] = time.split(" ");
        Log.d(TAG, "onCreateView: " + compare[3]);
        String hour[] = compare[3].split(":");
        int tocheck = Integer.parseInt(hour[0]);
        Log.d(TAG, "onCreateView: " + hour[0]);
        Cursor res = myDb.getAllDataUser();
        StringBuffer sb = new StringBuffer();
        while (res.moveToNext()) {
                sb.append(res.getString(0));
        }
        username = sb.toString();

            Cursor profile = myDb.getCurrentProfiles();
            StringBuffer sb2 = new StringBuffer();
            Log.d(TAG, "onCreateView: " + sb2.toString());
            while (profile.moveToNext()) {
                sb2.append(profile.getString(0));
            }


            current_profile = sb2.toString();

        Cursor curr = myDb.getDefaultCurrency();
        StringBuffer currBuffer= new StringBuffer();
        Log.d(TAG, "onCreateView: "+ currBuffer.toString());
        while (curr.moveToNext()) {
            currBuffer.append(curr.getString(0));
        }

        default_currency = currBuffer.toString();
        Log.d(TAG, "HomeFragment: default currency: " + default_currency);

            Toast.makeText(getActivity(), current_profile, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "User: " + username);

            TextView greetings = (TextView) view.findViewById(R.id.greetings);
            if (tocheck >= 5 && tocheck < 12) {
                greetings.setText("Good Morning " + username + " !!");
            } else if (tocheck >= 12 && tocheck < 18) {
                greetings.setText("Good Afternoon " + username + " !!");
            } else if (tocheck >= 18 && tocheck < 24) {
                greetings.setText("Good Evening " + username + " !!");
            } else {
                greetings.setText("Night Night " + username + " !!");
            }

            imageButton = view.findViewById(R.id.Add_Income);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //listener.onButtonSelected();
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Adding Income", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.show();
                    Intent intent = new Intent(getActivity(), EarningSecondScreen.class);
                    startActivity(intent);
                }
            });
            imageButton2 = view.findViewById(R.id.Add_Expense);
            imageButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //listener.onButtonSelected();
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Adding Expense", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.show();
                    Intent intent = new Intent(getActivity(), SecondScreen.class);
                    startActivity(intent);
                }
            });


            //Graph View

            Cursor Income = myDb.getEarningAmountMain();
            Cursor Expense = myDb.getExpenseAmountMain();
            pieChart = view.findViewById(R.id.piechart);
            ImageView imageView = (ImageView) view.findViewById(R.id.action_image);
            TextView noTransactionPrompt_1 = (TextView) view.findViewById(R.id.prompt_no_transactions);
            TextView noTransactionPrompt_2 = (TextView) view.findViewById(R.id.prompt_no_transactions_2);
            Float pie_Expense = 0.f;
            Float pie_Income = 0.f;
            if (Expense.moveToFirst() && Expense.getString(0) != null) {
                Log.d(TAG, "onCreateView: Expense Block");
                do {
                    Log.d(TAG, "onCreateView: Expense Block 2");
                    pie_Expense = Float.valueOf(Expense.getString(0));
                } while (Expense.moveToNext());

                Expense.close();
            }


            if (Income.moveToFirst() && Income.getString(0) != null) {
                Log.d(TAG, "onCreateView: Income Block");
                do {
                    Log.d(TAG, "onCreateView: Income Block 2");
                    pie_Income = Float.valueOf(Income.getString(0));
                } while (Income.moveToNext());
                Income.close();
            }


            if (pie_Income == 0.f && pie_Expense == 0.f) {
                Log.d(TAG, "Inside if Loop income fucked");
                imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.pie_chart));
                noTransactionPrompt_1.setText("Oops! You haven't added \ntransactions this month");
                noTransactionPrompt_2.setText("Add transactions to see your chart ");
                pieChart.setVisibility(view.INVISIBLE);
            }
            else {
                pieChart.setVisibility(view.VISIBLE);
                imageView.setVisibility(view.INVISIBLE);
                noTransactionPrompt_1.setVisibility(view.INVISIBLE);
                noTransactionPrompt_2.setVisibility(view.INVISIBLE);
                pieEntries = new ArrayList();
                ArrayList<Integer> colors = new ArrayList<>();
                float netBalance = pie_Income - pie_Expense;
                Log.d(TAG, "test" + pie_Income.toString() + pie_Expense.toString());
                if(pie_Income==0f){
                    pieEntries.add(new PieEntry(pie_Expense, "EXPENSE"));
                    colors.add(ContextCompat.getColor(getActivity(), R.color.Expense));
                }
                else if (pie_Expense==0f){
                    pieEntries.add(new PieEntry(pie_Income, "INCOME"));
                    colors.add(ContextCompat.getColor(getActivity(), R.color.Income));

                }
                else {
                    pieEntries.add(new PieEntry(pie_Income, "INCOME"));
                    pieEntries.add(new PieEntry(pie_Expense, "EXPENSE"));
                    colors.add(ContextCompat.getColor(getActivity(), R.color.Income));
                    colors.add(ContextCompat.getColor(getActivity(), R.color.Expense));

                }
                pieChart.setRotationAngle(135f);
                pieDataSet = new PieDataSet(pieEntries, "");
                pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);
                pieDataSet.setValueLinePart1OffsetPercentage(30.f);
                pieDataSet.setValueLinePart1Length(0.5f);
                pieDataSet.setValueLinePart2Length(.20f);
                pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                pieDataSet.setColors(colors);
                pieDataSet.setSliceSpace(0f);
                pieDataSet.setDrawValues(false);
                pieChart.setEntryLabelColor(Color.BLACK);
                pieChart.setEntryLabelTextSize(20f);
                //pieDataSet.setValueTextColor(Color.BLACK);
                //pieDataSet.setValueTextSize(15f);
                pieChart.setTransparentCircleAlpha(0);
                pieChart.setCenterTextSize(15f);
                pieChart.setCenterText("Net Balance\n" + netBalance);
                pieChart.setRotationEnabled(false);
                pieChart.getLegendRenderer();
                pieChart.getLegend().setEnabled(false);
                pieChart.getDescription().setEnabled(false);
                pieChart.setTouchEnabled(false);
            }
            return view;
        }
}
