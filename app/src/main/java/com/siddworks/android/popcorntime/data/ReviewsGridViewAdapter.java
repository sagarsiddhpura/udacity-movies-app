package com.siddworks.android.popcorntime.data;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.siddworks.android.popcorntime.R;
import com.siddworks.android.popcorntime.model.ReviewsApiResult;
import com.siddworks.android.popcorntime.util.Log;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by SIDD on 12-Dec-15.
 */
public class ReviewsGridViewAdapter extends BaseAdapter {

    private final Activity mContext;
    private final ArrayList<ReviewsApiResult> mTrailers;
    private boolean isLoggingEnabled = true;
    private static final String TAG = "MovieGridViewAdapter";

    public ReviewsGridViewAdapter(Activity context, ArrayList<ReviewsApiResult> movies) {
        mContext = context;
        mTrailers = movies;
    }

    @Override
    public int getCount() {
        return mTrailers.size();
    }

    @Override
    public ReviewsApiResult getItem(int position) {
        if (position >= 0 && position < mTrailers.size()) {
            return mTrailers.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        Log.d(isLoggingEnabled, TAG, " position:" + position);

        ReviewsApiResult trailer = getItem(position);
        if (trailer == null) {
            return null;
        }

        View row = convertView;
        ViewHolder holder = null;
        if (convertView == null) {
            row = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_item_review, parent, false);

            holder = new ViewHolder();
            holder.mCaption = (TextView) row.findViewById(R.id.caption);
            row.setTag(holder);
        } else {
            holder = (ViewHolder)row.getTag();
        }

        ReviewsApiResult review = mTrailers.get(position);
        Log.d(isLoggingEnabled, TAG, " mTrailers.get(position).getSize():" + review);

        holder.mCaption.setText(review.getReview());

        return row;
    }

    public void addAll(Collection<ReviewsApiResult> trailers) {
        mTrailers.addAll(trailers);
        notifyDataSetChanged();
    }

    public void clearData() {
        mTrailers.clear();
    }

    public ArrayList<ReviewsApiResult> getItems() {
        return mTrailers;
    }

    public class ViewHolder {
        public TextView mCaption;
    }
}
