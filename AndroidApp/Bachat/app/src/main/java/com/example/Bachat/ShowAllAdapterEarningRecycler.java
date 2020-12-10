package com.example.Bachat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShowAllAdapterEarningRecycler extends RecyclerView.Adapter<ShowAllAdapterEarningRecycler.EarningViewHolder> {
    ArrayList<EarningListItem> mEarningList;
    private OnItemClickListener mListener;


    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener =listener;
    }

    public class EarningViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        TextView mode;
        TextView amount;
        TextView date;
        ImageButton menu;


        public EarningViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mode = itemView.findViewById(R.id.textViewCategoryEarning);
            amount = itemView.findViewById(R.id.textViewAmountEarning);
            date = itemView.findViewById(R.id.textViewDateEarning);
            //menu = itemView.findViewById(R.id.imageButtonMenu);
            //menu.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }

        }

        private void showPopupMenu(View view){
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.pop_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_popup_edit:


                    ShowAllExpenseFragment.a = 1;

                    return true;
                case R.id.action_popup_repeat:
                    ShowAllExpenseFragment.a = 2;
                    Log.d("inside action repeat", "onMenuItemClick: youclicked on item repeat");

                    break;
                default:
                    return false;
                }
            return false;
         }
    }



    public ShowAllAdapterEarningRecycler(ArrayList<EarningListItem> earningList) {
        mEarningList = earningList;
    }

    @NonNull
    @Override
    public EarningViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.earning_show_all_recycler_item_layout, parent, false);
        EarningViewHolder evh = new EarningViewHolder(v,mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull EarningViewHolder holder, int position) {
        EarningListItem currentItem = mEarningList.get(position);

        holder.mode.setText(currentItem.getMode());
        holder.amount.setText(currentItem.getAmount());
        holder.date.setText(currentItem.getDate());
    }

    @Override
    public int getItemCount() {
        return mEarningList.size();
    }
}
