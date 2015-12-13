package com.siddworks.android.popcorntime.ui;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.siddworks.android.popcorntime.R;
import com.siddworks.android.popcorntime.model.Movie;
import com.siddworks.android.popcorntime.ui.widgets.SquareImageView;
import com.siddworks.android.popcorntime.util.Constants;
import com.siddworks.android.popcorntime.util.JsonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {

    private ArrayList<Movie> movies;
    private Movie movie;
    private Toolbar toolbar;
    private ImageView mPosterImage;
    private SquareImageView mbackground;
    private View scrollView;
    private ImageView mPosterImage2;
    private boolean isLoggingEnabled = true;
    private static final String TAG = "MovieDetailFragment";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(Constants.MOVIE_JSON)) {
            // Load the content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            String json = getArguments().getString(Constants.MOVIE_JSON);
            movie = (Movie) JsonUtils.makeObject(json, Movie.class);

            AppCompatActivity activity = (AppCompatActivity) this.getActivity();
            toolbar = (Toolbar) activity.findViewById(R.id.detail_toolbar);
            if (activity != null) {
                activity.setSupportActionBar(toolbar);
            }
            if (toolbar != null) {
                toolbar.setTitle(movie.getTitle());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (movie != null) {
            scrollView = rootView.findViewById(R.id.scroll);
            mPosterImage = (ImageView) rootView.findViewById(R.id.poster_image_placeholder);
            mPosterImage2 = (ImageView) rootView.findViewById(R.id.poster_image2);

            mbackground = (SquareImageView) rootView.findViewById(R.id.background);

            Picasso.with(getActivity())
                    .load(movie.getFullBackdropPath())
                    .placeholder(R.drawable.watermark)
                    .into(mbackground);
            applyPalette(mbackground);

//            Picasso.with(getActivity())
//                    .load(movie.getFullPosterPath())
//                    .into(mPosterImage);
//
            Picasso.with(getActivity())
                    .load(movie.getFullPosterPath())
                    .placeholder(R.drawable.watermark)
                    .into(mPosterImage2);

            ((TextView) rootView.findViewById(R.id.title)).setText(movie.getTitle());
            ((TextView) rootView.findViewById(R.id.movie_detail)).setText(movie.getOverview());

            String voteAverage = String.format("%1$2.1f", movie.getVoteAverage());

            ((TextView) rootView.findViewById(R.id.rating)).setText(voteAverage);
            ((TextView) rootView.findViewById(R.id.release_date)).setText(movie.getReleaseDate());
            ((TextView) rootView.findViewById(R.id.popularity_value)).setText(String.valueOf(movie.getPopularity()));
            ((TextView) rootView.findViewById(R.id.vote_count_value)).setText(String.valueOf(movie.getVoteCount()));
//            ((TextView) rootView.findViewById(R.id.user_rating_value)).setText(String.valueOf(movie.getUserRating()));
            ((TextView) rootView.findViewById(R.id.original_language_value)).setText(movie.getOriginalLanguage());
            ((TextView) rootView.findViewById(R.id.revenue_value)).setText(String.valueOf(movie.getRevenue()));
        }

        return rootView;
    }

    private void applyPalette(ImageView image) {
        // Palette library to be implemented in stage 2
        int primaryDark = getResources().getColor(R.color.colorPrimaryDark);
        toolbar.setBackgroundColor(primaryDark);
        initScrollFade(image);
    }

    private void updateBackground(View view) {
        int primaryDark = getResources().getColor(R.color.colorPrimaryDark);
        int primary = getResources().getColor(R.color.colorPrimary);
        int lightMutedColor = getResources().getColor(R.color.colorAccent);
        int mutedColor = getResources().getColor(R.color.colorAccent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            RippleDrawable ripple = (RippleDrawable) view.getBackground();
            GradientDrawable rippleBackground = (GradientDrawable) ripple.getDrawable(0);
            rippleBackground.setColor(lightMutedColor);
            ripple.setColor(ColorStateList.valueOf(mutedColor));
        } else {
            StateListDrawable drawable = (StateListDrawable) view.getBackground();
            drawable.setColorFilter(mutedColor, PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void initScrollFade(final ImageView image) {
        setComponentsStatus(scrollView, image);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                setComponentsStatus(scrollView, image);
            }
        });
    }

    private void setComponentsStatus(View scrollView, ImageView image) {
//        Log.d(isLoggingEnabled, TAG, " scrollView:" + scrollView.getScrollY());

        int scrollY = scrollView.getScrollY();
        image.setTranslationY(-scrollY / 2);
        ColorDrawable background = (ColorDrawable) toolbar.getBackground();
        int padding = scrollView.getPaddingTop();
        double alpha = (1 - (((double) padding - (double) scrollY) / (double) padding)) * 255.0;
        alpha = alpha < 0 ? 0 : alpha;
        alpha = alpha > 255 ? 255 : alpha;

        background.setAlpha((int) alpha);

        float scrollRatio = (float) (alpha / 255f);
        int titleColor = getAlphaColor(Color.WHITE, scrollRatio);
        toolbar.setTitleTextColor(titleColor);
    }

    private int getAlphaColor(int color, float scrollRatio) {
        return Color.argb((int) (scrollRatio * 255f), Color.red(color), Color.green(color), Color.blue(color));
    }

    private void restablishActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().getReturnTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    toolbar.setTitleTextColor(Color.WHITE);
                    toolbar.getBackground().setAlpha(255);
                }

                @Override
                public void onTransitionStart(Transition transition) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onTransitionPause(Transition transition) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onTransitionResume(Transition transition) {
                    // TODO Auto-generated method stub

                }
            });
        }
    }

}
