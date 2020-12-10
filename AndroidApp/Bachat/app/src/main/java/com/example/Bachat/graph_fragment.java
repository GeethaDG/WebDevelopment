package com.example.Bachat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class graph_fragment extends Fragment {
    View view;
    private static String TAG = "Graphs";
    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    //ArrayList pieEntries;
    ArrayList PieEntryLabels;
    ArrayList pieEntries;
    DatabaseHelper myDB;
    String sdate = "Start Date";
    String edate = "End Date";
    String StartDate;
    String EndDate;


    private DatePickerDialog.OnDateSetListener mDateSetListener1;
    private DatePickerDialog.OnDateSetListener mDateSetListener2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_graph_layout, container, false);
        pieChart = view.findViewById(R.id.piechart);
        myDB = new DatabaseHelper(getActivity());
        Calendar calendar = Calendar.getInstance();
        int monthDefault=calendar.get(Calendar.MONTH)+1;
        int yearDefault=calendar.get(Calendar.YEAR);
        StartDate=yearDefault + "/" + (monthDefault) + "/" + 1;
        switch(monthDefault){
            case 2 : {if(yearDefault%4==0) EndDate=yearDefault + "/" + (monthDefault) + "/" + 29;
            else EndDate=yearDefault + "/" + (monthDefault) + "/" + 28;}
            case 4 : EndDate=yearDefault + "/" + (monthDefault) + "/" + 30;
            case 6 : EndDate=yearDefault + "/" + (monthDefault) + "/" + 30;
            case 9 : EndDate=yearDefault + "/" + (monthDefault) + "/" + 30;
            case 11 : EndDate=yearDefault + "/" + (monthDefault) + "/" + 30;
            default : EndDate=yearDefault + "/" + (monthDefault) + "/" + 31;
        }

        Log.d(TAG,StartDate + EndDate);

        final TextView startDate=(TextView) view.findViewById(R.id.startDate);
        startDate.setText(StartDate);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "GraphView: Text View");
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_DeviceDefault_Dialog,mDateSetListener1,year,month,day);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                StartDate = year + "/" + (month + 1) + "/" + dayOfMonth;
                Log.d(TAG,"Month is:" + month + 1);
                String showStartDate = dayOfMonth + "-" + getMonth(month + 1) + "-" + year;
                startDate.setText(showStartDate);
                //firstDate = date;
            }
        };


        final TextView endDate=(TextView) view.findViewById(R.id.endDate);
        endDate.setText(EndDate);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "GraphView: Text View");
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_DeviceDefault_Dialog,mDateSetListener2,year,month,day);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                EndDate = year + "/" + (month + 1) + "/" + dayOfMonth;
                Log.d(TAG,"Month is:" + month + 1);
                String showStartDate = dayOfMonth + "-" + getMonth(month + 1) + "-" + year;
                endDate.setText(showStartDate);
                //firstDate = date;
            }
        };

        Cursor categoryExpenseDB = myDB.getExpenseAmountByCategory(StartDate,EndDate);
        ArrayList<Integer> colors = new ArrayList<>();

        ArrayList<Drawable> categoryIcon = new ArrayList<>();
        //Drawable[] categoryIcon={} ;
        ArrayList<Float> categoryExpenseValue = new ArrayList<>();
        //float[] categoryExpenseValue={};
        ArrayList<String> categoryName= new ArrayList<>();
        //String[] categoryName=new String[15];
        ArrayList<Integer> color_array=new ArrayList<>();
        //int[] color_array={};
        float totalExpense=0f;
        int i=0;
        pieEntries = new ArrayList();

        if (categoryExpenseDB.moveToFirst() && categoryExpenseDB.getString(0) != null) {
            do {
                categoryName.add(i,categoryExpenseDB.getString(0));
                categoryExpenseValue.add(i,Float.valueOf(categoryExpenseDB.getString(1)));
                Log.d(TAG,"Value from DB" + categoryName.get(i) + categoryExpenseValue.get(i));
                categoryIcon.add(i,ContextCompat.getDrawable(getActivity(),getIcon(categoryName.get(i))));
                color_array.add(i,ContextCompat.getColor(getActivity(),getColor(categoryName.get(i))));
                totalExpense=totalExpense+categoryExpenseValue.get(i);
                Log.d(TAG,"Total Expense" + totalExpense);
                i++;
            } while (categoryExpenseDB.moveToNext());
            categoryExpenseDB.close();
            color_array.add(i,ContextCompat.getColor(getActivity(),R.color.other));
            categoryIcon.add(i,ContextCompat.getDrawable(getActivity(),R.drawable.other_pie));
        }

        float lessThanFivePercent = 0;
        int color_index=0;

        for(int j=0;j<categoryName.size();j++){
            float temp= ((categoryExpenseValue.get(j) * 100)/totalExpense);
            if(temp>5f){
                pieEntries.add(new PieEntry(categoryExpenseValue.get(j), categoryIcon.get(j)));
                colors.add(color_index, color_array.get(j));
                String a=categoryName.get(j);
                Log.d("Category Test", a + color_index);
                color_index++;
            }
            else{
                lessThanFivePercent = lessThanFivePercent + categoryExpenseValue.get(j);
            }
            //colors.add(color_index,color_array.get(color_array.size()-1));
        }
        if(lessThanFivePercent != 0){
            pieEntries.add(new PieEntry(lessThanFivePercent, categoryIcon.get(categoryIcon.size()-1)));
            Log.d("Category Test", "test" +color_index);
            colors.add(color_index,color_array.get(color_array.size()-1));
        }



        pieDataSet = new PieDataSet(pieEntries,"");
        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        //Log.d("Colors", "color" + colors.get(colors.size()-1));

        //pieDataSet.setValueLinePart1OffsetPercentage(30.f);
        //pieDataSet.setValueLinePart1Length(0.5f);
        //pieDataSet.setValueLinePart2Length(.20f);
        //pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        //pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setColors(colors);
        pieDataSet.setSliceSpace(5f);
        pieDataSet.setDrawValues(false);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(20);
        //pieDataSet.setValueTextColor(Color.BLACK);
        //pieDataSet.setValueTextSize(15f);
        pieChart.setTransparentCircleAlpha(30);
        //pieChart.setCenterTextSize(15f);
        //pieDataSet.setIconsOffset(new MPPointF(0,50)); //without hole radius
        pieDataSet.setIconsOffset(new MPPointF(0,15)); //with hole radius
        pieChart.setRotationEnabled(true);
        pieChart.getLegendRenderer();
        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setHoleRadius(40);
        //pieChart.setTouchEnabled(false);


        GraphCategory GraphCategory1 = new GraphCategory("Health",
                "drawable://" + R.drawable.healthcare,
                getCategoryAmount(myDB, StartDate,EndDate,"Health"),
                ((getCategoryAmount(myDB, StartDate,EndDate,"Health"))/totalExpense));
        GraphCategory GraphCategory2 = new GraphCategory("Donations",
                "drawable://" + R.drawable.donations,
                getCategoryAmount(myDB, StartDate,EndDate,"Donations"),
                ((getCategoryAmount(myDB, StartDate,EndDate,"Donations"))/totalExpense));
        GraphCategory GraphCategory3 = new GraphCategory("Bills",
                "drawable://" + R.drawable.billsicon,
                getCategoryAmount(myDB, StartDate,EndDate,"Bills"),
                ((getCategoryAmount(myDB, StartDate,EndDate,"Bills"))/totalExpense));
        GraphCategory GraphCategory4 = new GraphCategory("Shopping",
                "drawable://" + R.drawable.shoppingicon,
                getCategoryAmount(myDB, StartDate,EndDate,"Shopping"),
                ((getCategoryAmount(myDB, StartDate,EndDate,"Shopping"))/totalExpense));
        GraphCategory GraphCategory5 = new GraphCategory("Dining Out",
                "drawable://" + R.drawable.dinningout,
                getCategoryAmount(myDB, StartDate,EndDate,"Dining out"),
                ((getCategoryAmount(myDB, StartDate,EndDate,"Dining out"))/totalExpense));
        GraphCategory GraphCategory6 = new GraphCategory("Entertainment",
                "drawable://" + R.drawable.entertaimenticon,
                getCategoryAmount(myDB, StartDate,EndDate,"Entertainment"),
                ((getCategoryAmount(myDB, StartDate,EndDate,"Entertainment"))/totalExpense));
        GraphCategory GraphCategory7= new GraphCategory("Groceries",
                "drawable://" + R.drawable.groceries_icon,
                getCategoryAmount(myDB, StartDate,EndDate,"Groceries"),
                ((getCategoryAmount(myDB, StartDate,EndDate,"Groceries"))/totalExpense));
        GraphCategory GraphCategory8 = new GraphCategory("Pet Care",
                "drawable://" + R.drawable.petsicon,
                getCategoryAmount(myDB, StartDate,EndDate,"Pet"),
                ((getCategoryAmount(myDB, StartDate,EndDate,"Pet"))/totalExpense));
        GraphCategory GraphCategory9 = new GraphCategory("Transportation",
                "drawable://" + R.drawable.transportation,
                getCategoryAmount(myDB, StartDate,EndDate,"Transport"),
                ((getCategoryAmount(myDB, StartDate,EndDate,"Transport"))/totalExpense));
        GraphCategory GraphCategory10 = new GraphCategory("Loans",
                "drawable://" + R.drawable.loansicon,
                getCategoryAmount(myDB, StartDate,EndDate,"Loans"),
                ((getCategoryAmount(myDB, StartDate,EndDate,"Loans"))/totalExpense));
        GraphCategory GraphCategory11 = new GraphCategory("Personal Care",
                "drawable://" + R.drawable.personalcareicon,
                getCategoryAmount(myDB, StartDate,EndDate,"Personal Care"),
                ((getCategoryAmount(myDB, StartDate,EndDate,"Personal Care"))/totalExpense));
        GraphCategory GraphCategory12= new GraphCategory("Miscellaneous",
                "drawable://" + R.drawable.miscellaneousicon,
                getCategoryAmount(myDB, StartDate,EndDate,"Miscellaneous"),
                ((getCategoryAmount(myDB, StartDate,EndDate,"Miscellaneous"))/totalExpense));

        final ArrayList<GraphCategory> GraphCategoryList = new ArrayList<>();
        GraphCategoryList.add(GraphCategory1);
        GraphCategoryList.add(GraphCategory3);
        GraphCategoryList.add(GraphCategory2);
        GraphCategoryList.add(GraphCategory4);
        GraphCategoryList.add(GraphCategory5);
        GraphCategoryList.add(GraphCategory6);
        GraphCategoryList.add(GraphCategory7);
        GraphCategoryList.add(GraphCategory8);
        GraphCategoryList.add(GraphCategory9);
        GraphCategoryList.add(GraphCategory10);
        GraphCategoryList.add(GraphCategory11);
        GraphCategoryList.add(GraphCategory12);

        GraphCategoryListAdapter adapter = new GraphCategoryListAdapter(getActivity(), R.layout.graphs_adapter, GraphCategoryList);
        ListView mListView = (ListView) view.findViewById(R.id.categoryList);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.d(TAG, "onItemClick:you clicked on a list item  " + peopleList.get(position).getName());
                //toast.makeText(SecondScreen.this, "you clicked on " + peopleList.get(position).getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ThirdScreen.class);
                String toadd =  GraphCategoryList.get(position).getCategoryName();
                Log.d(TAG, "onItemClick: "+ toadd);
                intent.putExtra("fromSecond",toadd);
                startActivity(intent);
            }
        });

        return view;
    }

    private int getIcon(String Category) {
        switch (Category) {
            case "Health":
                return R.drawable.healthcare_pie;
            case "Donations":
                return R.drawable.donations_pie;
            case "Bills":
                return R.drawable.billsicon_pie;
            case "Shopping":
                return R.drawable.shoppingicon_pie;
            case "Dining Out":
                return R.drawable.dinningout_pie;
            case "Entertainment":
                return R.drawable.entertaimenticon_pie;
            case "Groceries":
                return R.drawable.groceries_icon_pie;
            case "Pet Care":
                return R.drawable.petsicon_pie;
            case "Transportation":
                return R.drawable.transportation_pie;
            case "Loans":
                return R.drawable.loansicon_pie;
            case "Personal Care":
                return R.drawable.personalcareicon_pie;
            case "Miscellaneous":
                return R.drawable.miscellaneousicon_pie;
            default:
                return R.drawable.other;

        }
    }


    private int getIconList(String Category) {
        switch (Category) {
            case "Health":
                return R.drawable.healthcare;
            case "Donations":
                return R.drawable.donations;
            case "Bills":
                return R.drawable.billsicon;
            case "Shopping":
                return R.drawable.shoppingicon;
            case "Dining Out":
                return R.drawable.dinningout;
            case "Entertainment":
                return R.drawable.entertaimenticon;
            case "Groceries":
                return R.drawable.groceries_icon;
            case "Pet Care":
                return R.drawable.petsicon;
            case "Transportation":
                return R.drawable.transportation;
            case "Loans":
                return R.drawable.loansicon;
            case "Personal Care":
                return R.drawable.personalcareicon;
            case "Miscellaneous":
                return R.drawable.miscellaneousicon;
            default:
                return R.drawable.other;

        }
    }

        private int getColor(String Category){
            switch (Category) {
                case "Health":
                    return R.color.healthcare;
                case "Donations":
                    return R.color.donations;
                case "Bills":
                    return R.color.bills;
                case "Shopping":
                    return R.color.shopping;
                case "Dining Out":
                    return  R.color.dinning;
                case "Entertainment":
                    return R.color.entertaiment;
                case "Groceries":
                    return R.color.groceries;
                case "Pet Care":
                    return R.color.pets;
                case "Transportation":
                    return R.color.transportation;
                case "Loans":
                    return R.color.loans;
                case "Personal Care":
                    return R.color.personalcare;
                case "Miscellaneous":
                    return R.color.miscellaneous;
                default:
                    return R.color.other;
            }
        }

    private String getMonth (int month){
        String retMonth;
        Log.d(TAG,"Month in fn:" + month);
        switch (month){
            case 1 : return "Jan";
            case 2 : return "Feb";
            case 3 : return "Mar";
            case 4 : return "Apr";
            case 5 : return "May";
            case 6 : return"Jun";
            case 7 : return "Jul";
            case 8 : return "Aug";
            case 9 : return "Sep";
            case 10 : return "Oct";
            case 11 : return "Nov";
            case 12 : return "Dec";
            default : return "Error";
        }
    }

    private Float getCategoryAmount(DatabaseHelper myDB_M,String startDate_M,String endDate_M, String category_M){
        float amount = 0;
        String a="0";
        Cursor categoryExp = myDB_M.getExpenseAmountByCategory2(startDate_M,endDate_M,category_M);
        if (categoryExp.moveToFirst() && categoryExp.getString(0) != null) {
            do {
              a = categoryExp.getString(0);
            } while (categoryExp.moveToNext());
        }
        amount = Float.valueOf(a);
        return amount;
    }
}