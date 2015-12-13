package com.siddworks.android.popcorntime.data;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.siddworks.android.popcorntime.R;
import com.siddworks.android.popcorntime.model.Movie;
import com.siddworks.android.popcorntime.ui.MovieDetailActivity;
import com.siddworks.android.popcorntime.ui.MovieDetailFragment;
import com.siddworks.android.popcorntime.ui.MovieListActivity;
import com.siddworks.android.popcorntime.util.Constants;
import com.siddworks.android.popcorntime.util.JsonUtils;
import com.siddworks.android.popcorntime.util.Log;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by SIDD on 28-Nov-15.
 */
public class MovieRecyclerViewAdapter
        extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {

    private MovieListActivity movieListActivity;
    private ArrayList<Movie> mValues;
    private static final String TAG = "MovieRecyclerViewAdapter";
    private boolean isLoggingEnabled = true;

    public MovieRecyclerViewAdapter(MovieListActivity movieListActivity, ArrayList<Movie> items) {
        this.movieListActivity = movieListActivity;
        mValues = items;
    }

    public void clearAll() {
        mValues.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d(isLoggingEnabled, TAG, " item:" + mValues.get(position));

        holder.mItem = mValues.get(position);
        Picasso.with(movieListActivity)
                .load(mValues.get(position).getFullPosterPath())
                .placeholder(R.drawable.watermark)
                .into(holder.mPosterView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = JsonUtils.makeJson(mValues.get(position));
                if (movieListActivity.mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(Constants.MOVIE_JSON, json);
                    MovieDetailFragment fragment = new MovieDetailFragment();
                    fragment.setArguments(arguments);
                    movieListActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra(Constants.MOVIE_JSON, json);

                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void addAll(ArrayList<Movie> movies) {
        if (mValues == null) {
            mValues = new ArrayList<>();
        }
        mValues.addAll(movies);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mPosterView;
        public Movie mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPosterView = (ImageView) view.findViewById(R.id.poster);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }
}
