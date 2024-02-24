package com.app.astrotalk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.astrotalk.R;
import com.app.astrotalk.listeners.OnCategoryItemClick;
import com.app.astrotalk.model.AllCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserProfileAdapter extends RecyclerView.Adapter<UserProfileAdapter.ViewHolder> {

    public List<AllCategory> userProfiles = new ArrayList<>();
    public List<AllCategory> filterList = new ArrayList<>();
    private Context context;

    private OnCategoryItemClick onCategoryItemClick;

    public UserProfileAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<AllCategory> userProfiles, OnCategoryItemClick onCategoryItemClick) {
        this.userProfiles = userProfiles;
        this.onCategoryItemClick = onCategoryItemClick;
        filterList.clear();
        filterList.addAll(this.userProfiles);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_list, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AllCategory userProfile = userProfiles.get(position);

        // Set data to views
        holder.txtName.setText(userProfile.getName());
        holder.txtAstroType.setText(userProfile.getAstroType());
        holder.txtExp.setText("Exp: " + userProfile.getAstroExp());
        holder.txtLang.setText(userProfile.getAstroLang());
        holder.ivProfile.setImageResource(userProfile.getImageD());
        // Handle button click if needed
        holder.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCategoryItemClick.onItemClick(holder.getAdapterPosition(), userProfiles.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return userProfiles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfile;
        TextView txtName, txtAstroType, txtExp, txtLang;
        Button btnChat;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            txtName = itemView.findViewById(R.id.txtName);
            txtAstroType = itemView.findViewById(R.id.txtAstroType);
            txtExp = itemView.findViewById(R.id.txtExp);
            txtLang = itemView.findViewById(R.id.txtLang);
            btnChat = itemView.findViewById(R.id.btnChat);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filter(String charText) {
        this.userProfiles.clear();
        // Assume no data is found initially
        if (charText.toLowerCase(Locale.getDefault()).length() == 0) {
            this.userProfiles.addAll(filterList);
            // Set data found to true when the search text is empty
        } else {
            for (AllCategory wp : filterList) {
                if (wp.getName().toLowerCase().contains(charText.toLowerCase(Locale.getDefault()))) {
                    this.userProfiles.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
