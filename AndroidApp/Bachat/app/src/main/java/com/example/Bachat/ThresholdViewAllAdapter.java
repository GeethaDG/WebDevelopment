package com.example.Bachat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ThresholdViewAllAdapter extends ArrayAdapter<ThresholdListItem> {
    private static final String TAG = "ThresholdViewAllAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    private static class ViewHolder {
        TextView category;
        TextView amount;
    }

    public ThresholdViewAllAdapter(Context context, int resource, ArrayList<ThresholdListItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the  information
        String category = getItem(position).getCategory();
        String amount = getItem(position).getAmount();

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ThresholdViewAllAdapter.ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ThresholdViewAllAdapter.ViewHolder();
            holder.category = (TextView) convertView.findViewById(R.id.textViewCategory);
            holder.amount = (TextView) convertView.findViewById(R.id.textViewAmount);

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ThresholdViewAllAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        //holder.id.setText(id);
        holder.category.setText(category);
        holder.amount.setText(amount);


        return convertView;
    }

}
