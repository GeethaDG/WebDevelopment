package com.example.Bachat;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class SetThresholdFragment extends Fragment {
    DatabaseHelper myDb;
    View view;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myDb= new DatabaseHelper(getActivity());
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.threshold_show_all_screen, container, false);
        //Log.d(TAG, "onCreate: Threshold View Screen");
        ListView listshowthreshold = (ListView) view.findViewById(R.id.listViewThreshold);

        Cursor res = myDb.getAllDataThreshold();
        if (res.getCount() == 0) {
            //message
            showMessage("error", "no data in table");
             return view;}

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append(res.getString(0) + "," + res.getString(1) + "\n");

        }

        //Log.d(TAG, " "+ buffer.toString());

        /*Button homebutton = (Button) view.findViewById(R.id.btnGoToMain);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });*/

        String[] bufferdata  = buffer.toString().split("\n");
        final ArrayList<ThresholdListItem> ThresholdListItems = new ArrayList<>();
        //Log.d(TAG, ""+ bufferdata.length);
        for(int i=0; i< bufferdata.length; i++){
            String[] listindex  = bufferdata[i].split(",");
            ThresholdListItem item = new ThresholdListItem(listindex[0],listindex[1]);
            ThresholdListItems.add(item);
        }
        ThresholdViewAllAdapter adapter = new ThresholdViewAllAdapter(getActivity(), R.layout.threshold_view_all_layout, ThresholdListItems);
        listshowthreshold.setAdapter(adapter);

        listshowthreshold.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.d(TAG, "onItemClick:you clicked on a list item  " + peopleList.get(position).getName());
                //toast.makeText(SecondScreen.this, "you clicked on " + peopleList.get(position).getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), EditThresholdScreen.class);
                String toeditcategory =  ThresholdListItems.get(position).getCategory();
                String toeditamount =  ThresholdListItems.get(position).getAmount();
                String toedit = toeditcategory+","+toeditamount;
                intent.putExtra("toeditfromthresholdviewscreen", toedit);
                startActivity(intent);
                /*String toeditcategory =  ThresholdListItems.get(position).getCategory();
                String toeditamount =  ThresholdListItems.get(position).getAmount();
                String toedit = toeditcategory+","+toeditamount;
                Bundle bundle = new Bundle();
                bundle.putString("toeditfromthresholdviewscreen", toedit);
                ThresholdScreenFragment thresholdscreenfragment=new ThresholdScreenFragment();
                thresholdscreenfragment.setArguments(bundle);
                fragmentManager = getChildFragmentManager();
                fragmentTransaction= fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, new SetThresholdFragment());
                fragmentTransaction.commit();*/
            }
        });
        return view;

    }
    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

}


