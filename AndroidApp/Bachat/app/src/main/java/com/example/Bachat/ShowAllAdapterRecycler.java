package com.example.Bachat;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.EventLog;
import android.util.EventLog.Event;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShowAllAdapterRecycler extends  RecyclerView.Adapter<ShowAllAdapterRecycler.ExpenseViewHolder>   {
    private static final String TAG = "ShowAllAdapterRecycler";
    private ArrayList<ListItem> mList;
    private OnItemClickListener mListener;


    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener =listener;
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        TextView category;
        TextView subcategory;
        TextView mop;
        TextView amount;
        TextView date;
        CardView card;
        ImageButton menu;

        public ExpenseViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            category = itemView.findViewById(R.id.textViewCategory);
            subcategory = itemView.findViewById(R.id.textViewSubcategory);
            mop = itemView.findViewById(R.id.textViewModeOfPayment);
            amount = itemView.findViewById(R.id.textViewAmount);
            date = itemView.findViewById(R.id.textViewDate);
           // menu = itemView.findViewById(R.id.imageButtonMenu);
            card = itemView.findViewById(R.id.card_view);
           // menu.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                    Log.d("inside adapter", "onClick: " + position);
                    //showPopupMenu(v);
                    Log.d("returner", "onClick: "+ v);
                }
            }
        }



        private void showPopupMenu(View view){
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.pop_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
            ViewGroup vg =(ViewGroup) view;
            for(int i = 0; i< vg.getChildCount();i++){
                View vgchild = vg.getChildAt(i);
                Log.d("adapter view call", "onItemClick: "+ vgchild.toString());
            }

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_popup_edit:



                    ShowAllExpenseFragment.a=1;

                    return true;
                case R.id.action_popup_repeat:
                    ShowAllExpenseFragment.a=2;
                    Log.d("inside action repeat", "onMenuItemClick: youclicked on item repeat" );

                    break;
                default:
                    return false;
                }
            return false;
        }
    }

    public ShowAllAdapterRecycler(ArrayList<ListItem> expenseList) {
        mList = expenseList;
    }

    @NonNull
    @Override
    public ShowAllAdapterRecycler.ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_all_recycler_item_layout, parent, false);
        ShowAllAdapterRecycler.ExpenseViewHolder evh = new ShowAllAdapterRecycler.ExpenseViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ShowAllAdapterRecycler.ExpenseViewHolder holder, int position) {
        ListItem currentItem = mList.get(position);

        holder.category.setText(currentItem.getCategory());
        holder.subcategory.setText(currentItem.getSubcategory());
        holder.mop.setText(currentItem.getModeofpayment());
        holder.amount.setText(currentItem.getAmount());
        holder.date.setText(currentItem.getDate());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: mlist is "+mList);
        return mList.size();
    }
}
