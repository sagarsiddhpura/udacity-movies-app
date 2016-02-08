package com.siddworks.android.popcorntime.data;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.siddworks.android.popcorntime.R;
import com.siddworks.android.popcorntime.model.Movie;
import com.siddworks.android.popcorntime.model.VideosApiResult;
import com.siddworks.android.popcorntime.util.CommonUtil;
import com.siddworks.android.popcorntime.util.Log;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.List;

/**
 * Created by SIDD on 12-Dec-15.
 */
public class TrailersGridViewAdapter extends BaseAdapter {

    private final Activity mContext;
    private final List<VideosApiResult> mTrailers;
    private boolean isLoggingEnabled = true;
    private static final String TAG = "MovieGridViewAdapter";

    public TrailersGridViewAdapter(Activity context, List<VideosApiResult> movies) {
        mContext = context;
        mTrailers = movies;
    }

    @Override
    public int getCount() {
        return mTrailers.size();
    }

    @Override
    public VideosApiResult getItem(int position) {
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

        VideosApiResult trailer = getItem(position);
        if (trailer == null) {
            return null;
        }

        View row = convertView;
        ViewHolder holder = null;
        if (convertView == null) {
            row = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_item_trailer, parent, false);

            holder = new ViewHolder();
            holder.mPoster = (ImageView)row.findViewById(R.id.thumbnail);
            holder.mCaption = (TextView) row.findViewById(R.id.caption);
            holder.mSize = (TextView) row.findViewById(R.id.size);
            row.setTag(holder);
        } else {
            holder = (ViewHolder)row.getTag();
        }

        // TODO: Add placeholder image
        Picasso.with(mContext)
                .load(mTrailers.get(position)
                        .getThumbnailUrl())
                .into(holder.mPoster);

        if(!TextUtils.isEmpty(mTrailers.get(position).getName())) {
            CommonUtil.setVisiblity(holder.mCaption, View.VISIBLE);
            holder.mCaption.setText(mTrailers.get(position).getName());
        } else {
            CommonUtil.setVisiblity(holder.mCaption, View.GONE);
        }
        Log.d(isLoggingEnabled, TAG, " mTrailers.get(position).getSize():" + mTrailers.get(position).getSize());

        if(mTrailers.get(position).getSize() != 0) {
            CommonUtil.setVisiblity(holder.mSize, View.VISIBLE);
            holder.mSize.setText(mTrailers.get(position).getSize()+"p");
        } else {
            CommonUtil.setVisiblity(holder.mSize, View.GONE);
        }

        return row;
    }

    public void addAll(Collection<VideosApiResult> trailers) {
        mTrailers.addAll(trailers);
        notifyDataSetChanged();
    }

    public void clearData() {
        mTrailers.clear();
    }

    public List<VideosApiResult> getItems() {
        return mTrailers;
    }

    public void onClick(Activity activity, int position) {
        VideosApiResult trailer = getItem(position);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getVideoUrl()));
        activity.startActivity(intent);
    }

    public class ViewHolder {
        public ImageView mPoster;
        public TextView mSize;
        public TextView mCaption;
        public Movie mItem;
    }
}
