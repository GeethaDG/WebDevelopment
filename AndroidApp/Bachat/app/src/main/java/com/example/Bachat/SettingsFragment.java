package com.example.Bachat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {
    View view;
    ImageButton namebutton;
    ImageButton securitybutton;
    //ImageButton subcategorybutton;
    TextView nametext;
    TextView securitytext;
    //TextView subcategorytext;


    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.import_to_screen);
        view = inflater.inflate(R.layout.settings, container, false);
        namebutton = view.findViewById(R.id.Name_Button);
        securitybutton = view.findViewById(R.id.Security_Button);

        nametext = view.findViewById(R.id.Name);
        securitytext = view.findViewById(R.id.Security);


        namebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditUserScreen.class);
                startActivity(intent);
            }
        });

        nametext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditUserScreen.class);
                startActivity(intent);
            }
        });
        securitybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditSecurityScreen.class);
                startActivity(intent);
            }
        });

        securitytext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditSecurityScreen.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
