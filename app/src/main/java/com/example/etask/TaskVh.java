package com.example.etask;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class TaskVh extends RecyclerView.ViewHolder{
    public TextView txt_title,txt_time, txt_date, txt_coins, txt_option;
    public CardView view_task;
    public TaskVh(@NonNull View itemView) {
        super(itemView);

        view_task = itemView.findViewById(R.id.view_task);
        txt_title = itemView.findViewById(R.id.txt_title);
        txt_time = itemView.findViewById(R.id.txt_time);
        txt_date = itemView.findViewById(R.id.txt_date);
        txt_coins = itemView.findViewById(R.id.txt_coins);
        txt_option = itemView.findViewById(R.id.txt_option);



    }
}
