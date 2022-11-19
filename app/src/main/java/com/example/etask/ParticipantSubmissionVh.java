package com.example.etask;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ParticipantSubmissionVh extends RecyclerView.ViewHolder{
    public TextView txt_full_name, txtLocation, txt_statement;
    public ImageView txt_imageUrlStart, txt_imageUrlDone;
    public Button approve_task, disapprove;
    public ParticipantSubmissionVh(@NonNull View itemView) {
        super(itemView);

        txt_full_name = itemView.findViewById(R.id.participant_name);
        txt_imageUrlStart = itemView.findViewById(R.id.taskImageViewStart);
        txt_imageUrlDone = itemView.findViewById(R.id.taskImageViewDone);
        txtLocation = itemView.findViewById(R.id.participant_location);
        txt_statement = itemView.findViewById(R.id.participant_statement);
        approve_task = itemView.findViewById(R.id.approve_task);
        disapprove = itemView.findViewById(R.id.disapprove_task);

    }
}
