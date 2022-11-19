package com.example.etask;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RewardVh extends RecyclerView.ViewHolder{

    public TextView txt_name, txt_coins, txt_option, txtQuantity, txtAvailableStock;
    public LinearLayout stockContent;
    public ImageView txt_imageUrl;
    public CardView view_rewards;
    public RewardVh(@NonNull View itemView) {
        super(itemView);

        view_rewards = itemView.findViewById(R.id.view_rewards);
        txt_imageUrl = itemView.findViewById(R.id.img_rewards);
        stockContent = itemView.findViewById(R.id.stock_content);
        txt_name = itemView.findViewById(R.id.txt_rewardName);
        txtQuantity = itemView.findViewById(R.id.txt_rewardQuantity);
        txtAvailableStock = itemView.findViewById(R.id.txt_availableStock);
        txt_coins = itemView.findViewById(R.id.txt_rewardCoins);
        txt_option = itemView.findViewById(R.id.txt_option_reward);

    }
}
