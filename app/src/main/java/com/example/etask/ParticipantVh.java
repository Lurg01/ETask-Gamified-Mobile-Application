package com.example.etask;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticipantVh extends RecyclerView.ViewHolder {
    public TextView txt_fName, txt_lName, txt_email, txtOption, totalCoins, txtDatetime;
    public CircleImageView participantImage;
    public ParticipantVh(@NonNull View itemView) {
        super(itemView);

        participantImage = itemView.findViewById(R.id.participant_img);
        txt_fName = itemView.findViewById(R.id.txt_fName);
        txt_lName = itemView.findViewById(R.id.txt_lName);
        txt_email = itemView.findViewById(R.id.txt_email);
        txtDatetime = itemView.findViewById(R.id.txt_datetime);
        totalCoins = itemView.findViewById(R.id.get_totalCoins_for_leaderboard);
        txtOption = itemView.findViewById(R.id.txt_option_participant);


    }

}
