package com.app.astrotalk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.astrotalk.R;
import com.app.astrotalk.model.UserReviewModel;

import java.util.ArrayList;
import java.util.List;

public class UserReviewAdapter extends RecyclerView.Adapter<UserReviewAdapter.ViewHolder> {
    private List<UserReviewModel> userReviewList = new ArrayList<>();
    private Context context;

    public UserReviewAdapter(List<UserReviewModel> userReviewList, Context context) {
        this.userReviewList = userReviewList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserReviewModel userReview = userReviewList.get(position);

        // Set user profile picture (Assuming you have a local resource for the profile picture)
        holder.userProfilePic.setImageResource(userReviewList.get(holder.getAdapterPosition()).getUserProfilePic());

        // Set user name
        holder.userName.setText(userReview.getReviewerName());

        // Set user rating
        holder.ratingBar.setRating(userReview.getRating());

        // Set review text
        holder.reviewText.setText(userReview.getReviewText());

    }

    @Override
    public int getItemCount() {
        return userReviewList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userProfilePic;
        TextView userName;
        RatingBar ratingBar;

        TextView reviewText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userProfilePic = itemView.findViewById(R.id.userProfilePic);
            userName = itemView.findViewById(R.id.userName);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            reviewText = itemView.findViewById(R.id.reviewText);
        }
    }
}
