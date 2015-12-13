package com.siddworks.android.popcorntime.data;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.siddworks.android.popcorntime.R;
import com.siddworks.android.popcorntime.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.List;

/**
 * Created by SIDD on 12-Dec-15.
 */
public class MovieGridViewAdapter extends BaseAdapter {

    private final Activity mContext;
    private final List<Movie> mMovies;
    private boolean isLoggingEnabled = true;
    private static final String TAG = "MovieGridViewAdapter";

    public MovieGridViewAdapter(Activity context, List<Movie> movies) {
        mContext = context;
        mMovies = movies;
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Movie getItem(int position) {
        if (position >= 0 && position < mMovies.size()) {
            return mMovies.get(position);
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

        Movie movie = getItem(position);
        if (movie == null) {
            return null;
        }

        View row = convertView;
        ViewHolder holder = null;
        if (convertView == null) {
            row = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_content, parent, false);

            holder = new ViewHolder();
            holder.mPosterView = (ImageView)row.findViewById(R.id.poster);
            row.setTag(holder);
        } else {
            holder = (ViewHolder)row.getTag();
        }

        Picasso.with(mContext)
                .load(mMovies.get(position)
                        .getFullPosterPath())
                .into(holder.mPosterView );

        return row;
    }

    public void addAll(Collection<Movie> xs) {
        mMovies.addAll(xs);
        notifyDataSetChanged();
    }

    public void clearData() {
        mMovies.clear();
    }

    public List<Movie> getItems() {
        return mMovies;
    }

    public class ViewHolder {
        public View mView;
        public ImageView mPosterView;
        public Movie mItem;
    }
}
