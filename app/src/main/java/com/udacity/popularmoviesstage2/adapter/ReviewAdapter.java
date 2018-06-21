package com.udacity.popularmoviesstage2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.popularmoviesstage2.R;
import com.udacity.popularmoviesstage2.model.Review;

import java.util.List;

/**
 * Created by hansend on 6/15/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {
    private List<Review> reviewList;
    private final Context context;

    public ReviewAdapter(@NonNull Context context,
                        List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @Override
    public ReviewAdapter.ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup,
                                                                  int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.review_list_item, viewGroup, false);

        return new ReviewAdapter.ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewAdapterViewHolder reviewAdapterViewHolder,
                                 int position) {
        Review review = reviewList.get(position);
        reviewAdapterViewHolder.setReviewAuthor(review.getAuthor());
        reviewAdapterViewHolder.setReviewContent(review.getContent());
    }

    @Override
    public int getItemCount() {
        if (null == reviewList) return 0;
        return reviewList.size();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        final TextView reviewAuthor;
        final TextView reviewContent;

        public ReviewAdapterViewHolder(View view) {
            super(view);

            reviewAuthor = (TextView) view.findViewById(R.id.textview_review_author);
            reviewContent = (TextView) view.findViewById(R.id.textview_review_content);
        }

        public void setReviewAuthor(String text) {
            reviewAuthor.setText(text);
        }

        public void setReviewContent(String text) {
            reviewContent.setText(text);
        }
    }
}
