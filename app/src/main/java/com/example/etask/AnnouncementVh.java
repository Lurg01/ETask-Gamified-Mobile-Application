package com.example.etask;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AnnouncementVh extends RecyclerView.ViewHolder{
    public TextView txt_title,txt_desc, txt_option;
    public AnnouncementVh(@NonNull View itemView) {
        super(itemView);

        txt_title = itemView.findViewById(R.id.txt_title1);
        txt_desc = itemView.findViewById(R.id.txt_desc1);
        txt_option = itemView.findViewById(R.id.txt_option_ans);


    }
}
