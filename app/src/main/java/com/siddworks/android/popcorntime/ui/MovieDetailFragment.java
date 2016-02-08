package com.siddworks.android.popcorntime.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.siddworks.android.popcorntime.R;
import com.siddworks.android.popcorntime.data.Api;
import com.siddworks.android.popcorntime.data.MovieContract;
import com.siddworks.android.popcorntime.data.ReviewsGridViewAdapter;
import com.siddworks.android.popcorntime.data.TrailersGridViewAdapter;
import com.siddworks.android.popcorntime.model.Movie;
import com.siddworks.android.popcorntime.model.ReviewsApiResult;
import com.siddworks.android.popcorntime.model.ReviewsApiResultWrapper;
import com.siddworks.android.popcorntime.model.VideosApiResult;
import com.siddworks.android.popcorntime.model.VideosApiResultWrapper;
import com.siddworks.android.popcorntime.ui.widgets.SquareImageView;
import com.siddworks.android.popcorntime.util.CommonUtil;
import com.siddworks.android.popcorntime.util.Constants;
import com.siddworks.android.popcorntime.util.JsonUtils;
import com.siddworks.android.popcorntime.util.Key;
import com.siddworks.android.popcorntime.util.Log;
import com.siddworks.android.popcorntime.util.PopcornTimeUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ArrayList<Movie> movies;
    private Movie movie;
    private Toolbar toolbar;
    private SquareImageView mbackground;
    private View scrollView;
    private ImageView mPosterImage;
    private boolean isLoggingEnabled = true;
    private static final String TAG = "MovieDetailFragment";
    private View rootView;
    private String movie_Id;
    private boolean isFavourite;

    private static ContentValues movieValues;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
        movieValues=new ContentValues();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(Constants.DETAIL_LOADER, null, this);
        getLoaderManager().initLoader(Constants.TRAILER_LOADER, null, this);
        getLoaderManager().initLoader(Constants.REVIEW_LOADER, null, this);
        getLoaderManager().initLoader(Constants.GENRE_LOADER, null, this);
        getLoaderManager().initLoader(Constants.FAVOURITE_LOADER, null, this);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments().containsKey(Constants.MOVIE_JSON)) {
            // Load the content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            String json = getArguments().getString(Constants.MOVIE_JSON);
            movie = (Movie) JsonUtils.makeObject(json, Movie.class);

            AppCompatActivity activity = (AppCompatActivity) this.getActivity();
            toolbar = (Toolbar) activity.findViewById(R.id.detail_toolbar);
            if (activity != null && toolbar != null) {
                activity.setSupportActionBar(toolbar);
                toolbar.setTitle(movie.getTitle());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (movie != null) {
            populateContract(movie);
            movie_Id = String.valueOf(movie.getId());

            scrollView = rootView.findViewById(R.id.scroll);
            mPosterImage = (ImageView) rootView.findViewById(R.id.poster_image);
            mbackground = (SquareImageView) rootView.findViewById(R.id.background);

            Picasso.with(getActivity())
                    .load(movie.getFullBackdropPath())
                    .placeholder(R.drawable.watermark)
                    .into(mbackground);
            applyPalette(mbackground);

            Picasso.with(getActivity())
                    .load(movie.getFullPosterPath())
                    .placeholder(R.drawable.watermark)
                    .into(mPosterImage);

            ((TextView) rootView.findViewById(R.id.title)).setText(movie.getTitle());
            ((TextView) rootView.findViewById(R.id.movie_detail)).setText(movie.getOverview());

            String voteAverage = String.format("%1$2.1f", movie.getVoteAverage());

            ((TextView) rootView.findViewById(R.id.rating)).setText(voteAverage);
            ((TextView) rootView.findViewById(R.id.release_date)).setText(movie.getReleaseDate());
            ((TextView) rootView.findViewById(R.id.popularity_value)).setText(String.valueOf(movie.getPopularity()));
            ((TextView) rootView.findViewById(R.id.vote_count_value)).setText(String.valueOf(movie.getVoteCount()));
            ((TextView) rootView.findViewById(R.id.original_language_value)).setText(movie.getOriginalLanguage());
            ((TextView) rootView.findViewById(R.id.revenue_value)).setText(String.valueOf(movie.getRevenue()));

            // Load videos and reviews
            int id = movie.getId();
            loadVideos(id);
            loadReviews(id);

            // setup Favourite
            final ImageButton favButton = (ImageButton) rootView.findViewById(R.id.favButton);
            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFavourite) {
                        getActivity().getContentResolver().delete(MovieContract.Favourites.buildMoviesUriWithMovieId(movie_Id), null, null);
                        Toast.makeText(getContext(), "REMOVED FROM FAVOURITES!", Toast.LENGTH_SHORT).show();
                        isFavourite = false;
                        refreshFavButton();
                    } else {
                        getActivity().getContentResolver().insert(MovieContract.Favourites.buildMovieUri(), movieValues);
                        Toast.makeText(getContext(), "ADDED TO FAVOURITES!", Toast.LENGTH_SHORT).show();
                        isFavourite = true;
                        refreshFavButton();
                    }
                }
            });
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movies_detail, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_share_trailer) {

            if (movie != null) {
                if(movie.getTrailers() == null || movie.getTrailers().size() == 0) {
                    CommonUtil.showToast(getActivity(), "This movie does not have trailer to share");
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, "Checkout this awesome movie " +
                            movie.getTitle() + ": " + movie.getTrailers().get(0).getVideoUrl() );
                    intent.setType("text/plain");
                    startActivity(Intent.createChooser(intent, "Sharing Movie Trailer:"));
                }
            } else {
                CommonUtil.showToast(getActivity(), "Error sharing trailer");
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshFavButton() {
        Log.d(isLoggingEnabled, TAG, "refreshFavButton isFavourite:" + isFavourite);

        final ImageButton favButton = (ImageButton) rootView.findViewById(R.id.favButton);
        if(isFavourite) {
            favButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_full));
        } else {
            favButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border));
        }
    }

    private void populateContract(Movie movie) {
        if (movieValues.size()==0){
            movieValues.put(MovieContract.Movies.PAGE, "1");
            movieValues.put(MovieContract.Movies.POSTER_PATH, movie.getPosterPath());
            movieValues.put(MovieContract.Movies.ADULT, String.valueOf(movie.isAdult()));
            movieValues.put(MovieContract.Movies.OVERVIEW, movie.getOverview());
            movieValues.put(MovieContract.Movies.RELEASE_DATE, movie.getReleaseDate());
            movieValues.put(MovieContract.Movies.MOVIE_ID, movie.getId());
            movieValues.put(MovieContract.Movies.ORIGINAL_TITLE, movie.getOriginalTitle());
            movieValues.put(MovieContract.Movies.ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
            movieValues.put(MovieContract.Movies.TITLE, movie.getTitle());
            movieValues.put(MovieContract.Movies.BACKDROP_PATH, movie.getBackdropPath());
            movieValues.put(MovieContract.Movies.POPULARITY, movie.getPopularity());
            movieValues.put(MovieContract.Movies.VOTE_COUNT, movie.getVoteCount());
            movieValues.put(MovieContract.Movies.VOTE_AVERAGE, movie.getVoteAverage());
            movieValues.put(MovieContract.Movies.FAVOURED, "1");
        }
    }

    private void loadVideos(final int id) {

        final GridView trailersGridView = (GridView) rootView.findViewById(R.id.trailers_gridview);
        final TextView trailersStatusTextView = (TextView) rootView.findViewById(R.id.trailers_status_TextView);
        final TextView loadingTrailersTextView = (TextView) rootView.findViewById(R.id.loading_trailers_TextView);
        final ProgressBar trailersProgressBar = (ProgressBar) rootView.findViewById(R.id.trailers_progress_bar);
        trailersGridView.setColumnWidth(500);

        Api api = PopcornTimeUtil.getApi();
        Observable<VideosApiResultWrapper> videosObs = api
                .getVideosForMovie(String.valueOf(id), Key.getKey());
        videosObs.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VideosApiResultWrapper>() {
                    @Override
                    public void onCompleted() {
                        Log.d(isLoggingEnabled, TAG, "videosObs.onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(isLoggingEnabled, TAG, "videosObs.onError  e:" + e);
                        e.printStackTrace();

                        trailersStatusTextView.setText("Error loading Trailers");
                        CommonUtil.setVisiblity(trailersGridView, View.GONE);
                        CommonUtil.setVisiblity(loadingTrailersTextView, View.GONE);
                        CommonUtil.setVisiblity(trailersProgressBar, View.GONE);
                        CommonUtil.setVisiblity(trailersStatusTextView, View.VISIBLE);
                    }

                    @Override
                    public void onNext(VideosApiResultWrapper videosApiResultWrapper) {
//                        Log.d(isLoggingEnabled, TAG, "videosObs.onNext" +
//                                " videosApiResultWrapper:" + JsonUtils.makeJson(videosApiResultWrapper));

                        CommonUtil.setVisiblity(trailersGridView, View.VISIBLE);
                        CommonUtil.setVisiblity(loadingTrailersTextView, View.GONE);
                        CommonUtil.setVisiblity(trailersProgressBar, View.GONE);
                        CommonUtil.setVisiblity(trailersStatusTextView, View.GONE);

                        ArrayList<VideosApiResult> trailers = videosApiResultWrapper.getVideos();

                        Log.d(isLoggingEnabled, TAG, "trailers:" + JsonUtils.makeJson(trailers));

                        // No trailers
                        if (trailers.size() == 0) {
                            trailersStatusTextView.setText("No Trailers Available");
                            CommonUtil.setVisiblity(trailersGridView, View.GONE);
                            CommonUtil.setVisiblity(trailersStatusTextView, View.VISIBLE);
                            return;
                        } else {
                            movie.setTrailers(trailers);
                            // Set trailers in grid view
                            final TrailersGridViewAdapter trailersGridViewAdapter =
                                    new TrailersGridViewAdapter(getActivity(), trailers);
                            trailersGridView.setAdapter(trailersGridViewAdapter);
                            trailersGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    trailersGridViewAdapter.onClick(getActivity(), position);
                                }
                            });
                        }
                    }
                });
    }

    private void loadReviews(int id) {
        final GridView reviewsGridView = (GridView) rootView.findViewById(R.id.reviews_gridview);
        final TextView reviewsStatusTextView = (TextView) rootView.findViewById(R.id.reviews_status_TextView);
        final TextView loadingReviewsTextView = (TextView) rootView.findViewById(R.id.loading_reviews_TextView);
        final ProgressBar reviewsProgressBar = (ProgressBar) rootView.findViewById(R.id.reviews_progress_bar);
        reviewsGridView.setColumnWidth(500);

        Api api = PopcornTimeUtil.getApi();
        Observable<ReviewsApiResultWrapper> reviewsObs = api
                .getReviewsForMovie(String.valueOf(id), Key.getKey());
        reviewsObs.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ReviewsApiResultWrapper>() {
                    @Override
                    public void onCompleted() {
                        Log.d(isLoggingEnabled, TAG, "reviewsObs.onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(isLoggingEnabled, TAG, "reviewsObs.onError  e:" + e);
                        e.printStackTrace();

                        reviewsStatusTextView.setText("Error loading Reviews");
                        CommonUtil.setVisiblity(reviewsGridView, View.GONE);
                        CommonUtil.setVisiblity(loadingReviewsTextView, View.GONE);
                        CommonUtil.setVisiblity(reviewsProgressBar, View.GONE);
                        CommonUtil.setVisiblity(reviewsStatusTextView, View.VISIBLE);
                    }

                    @Override
                    public void onNext(ReviewsApiResultWrapper reviewsApiResultWrapper) {
//                        Log.d(isLoggingEnabled, TAG, "reviewsObs.onNext" +
//                                " reviewsApiResultWrapper:" + JsonUtils.makeJson(reviewsApiResultWrapper));

                        CommonUtil.setVisiblity(reviewsGridView, View.VISIBLE);
                        CommonUtil.setVisiblity(loadingReviewsTextView, View.GONE);
                        CommonUtil.setVisiblity(reviewsProgressBar, View.GONE);
                        CommonUtil.setVisiblity(reviewsStatusTextView, View.GONE);

                        ArrayList<ReviewsApiResult> reviews = reviewsApiResultWrapper.getReviews();

                        Log.d(isLoggingEnabled, TAG, "reviews:" + JsonUtils.makeJson(reviews));

                        // No reviews
                        if (reviews.size() == 0) {
                            reviewsStatusTextView.setText("No Reviews Available");
                            CommonUtil.setVisiblity(reviewsGridView, View.GONE);
                            CommonUtil.setVisiblity(reviewsStatusTextView, View.VISIBLE);
                            return;
                        } else {
                            // Set reviews in grid view
                            final ReviewsGridViewAdapter reviewsGridViewAdapter = new ReviewsGridViewAdapter(getActivity(), reviews);
                            reviewsGridView.setAdapter(reviewsGridViewAdapter);
                        }
                    }
                });
    }

    private void applyPalette(ImageView image) {
        // Palette library to be implemented in stage 2
        int primaryDark = getResources().getColor(R.color.colorPrimaryDark);
        if (toolbar != null) {
            toolbar.setBackgroundColor(primaryDark);
        }
        try{
            initScrollFade(image);
        } catch (Exception e) {
        }
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

    private void reEstablishActionBar() {
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != movie_Id) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            switch (id) {
                case Constants.DETAIL_LOADER:
                    return new CursorLoader(
                            getActivity(),
                            MovieContract.Movies.buildMoviesUriWithMovieId(movie_Id),
                            Constants.MOVIE_COLUMNS,
                            null,
                            null,
                            null
                    );
                case Constants.FAVOURITE_LOADER:
                    return new CursorLoader(
                            getActivity(),
                            MovieContract.Favourites.buildMovieUri(),
                            Constants.FAVOURITE_MOVIE_COLUMNS,
                            null,
                            null,
                            null
                    );
                case Constants.TRAILER_LOADER:
                    return new CursorLoader(
                            getActivity(),
                            MovieContract.Trailers.buildMoviesUriWithMovieId(movie_Id),
                            Constants.TRAILER_COLUMNS,
                            null,
                            null,
                            null
                    );
                case Constants.REVIEW_LOADER:
                    return new CursorLoader(
                            getActivity(),
                            MovieContract.Reviews.buildMoviesUriWithMovieId(movie_Id),
                            Constants.REVIEW_COLUMNS,
                            null,
                            null,
                            null
                    );
                case Constants.GENRE_LOADER:
                    return new CursorLoader(
                            getActivity(),
                            MovieContract.Genres.buildMoviesUriWithMovieId(movie_Id),
                            Constants.GENRE_COLUMNS,
                            null,
                            null,
                            null
                    );
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
        if (!data.moveToFirst()) { return; }
        switch (loader.getId()) {
//            case DETAIL_LOADER:
//                defaultShow();
//                orgLang = data.getString(COL_ORIGINAL_LANG);
//
//                orgTitle = data.getString(COL_ORIGINAL_TITLE);
//                ((TextView) rootView.findViewById(R.id.orgTitle))
//                        .setText(orgTitle);
//
//                overview = data.getString(COL_OVERVIEW);
//                ((TextView) rootView.findViewById(R.id.overview))
//                        .setText(overview);
//
//                relDate = data.getString(COL_RELEASE_DATE);
//
//                ((TextView) rootView.findViewById(R.id.relDate))
//                        .setText("Release Date: "+relDate);
//
//                postURL = data.getString(COL_POSTER_PATH);
//                ImageView poster = (ImageView) rootView.findViewById(R.id.poster);
//                Picasso
//                        .with(getActivity())
//                        .load(postURL)
//                        .transform(new RoundedCornersTransformation(10, 10))
//                        .fit()
//                        .into(poster);
//
//                movieId = data.getString(COL_MOVIE_ID);
//                popularity = data.getString(COL_POPULARITY);
//                double pop=Double.parseDouble(popularity);
//                popularity=String.valueOf((double)Math.round(pop*10d)/10d) ;
//
//                ((TextView) rootView.findViewById(R.id.popularity))
//                        .setText("Popularity : " + popularity);
//
//                votAvg = data.getString(COL_VOTE_AVERAGE);
//                double vote=Double.parseDouble(votAvg);
//                votAvg=String.valueOf((double)Math.round(vote*10d)/10d) ;
//                ((TextView) rootView.findViewById(R.id.vote))
//                        .setText(votAvg);
//                backdropURL=data.getString(COL_BACKDROP_PATH);
//                final ImageView backdrop = (ImageView) rootView.findViewById(R.id.backdropImg);
//                Picasso
//                        .with(getActivity())
//                        .load(backdropURL)
//                        .fit()
//                        .centerCrop()
//                        .networkPolicy(NetworkPolicy.OFFLINE)
//                        .into(backdrop, new Callback() {
//                            @Override
//                            public void onSuccess() {
//                            }
//
//                            @Override
//                            public void onError() {
//                                Picasso
//                                        .with(getActivity())
//                                        .load(backdropURL)
//                                        .fit()
//                                        .centerCrop()
//                                        .error(R.mipmap.ic_launcher)
//                                        .into(backdrop, new Callback() {
//                                            @Override
//                                            public void onSuccess() {
//                                            }
//
//                                            @Override
//                                            public void onError() {
//                                                Log.v("Error Loading Images", "'");
//                                            }
//                                        });
//                            }
//                        });
//                backdrop.setAdjustViewBounds(true);
//
//                final String downloaded= data.getString(COL_DOWNLOADED);
//                showed=data.getString(COL_SHOWED);
//                FloatingActionButton show;
//                if (showed.equalsIgnoreCase("1")) {
//                    rootView.findViewById(R.id.hide).setVisibility(View.VISIBLE);
//                    show = (FloatingActionButton) rootView.findViewById(R.id.hide);
//                    show();
//                } else {
//                    rootView.findViewById(R.id.show).setVisibility(View.VISIBLE);
//                    show = (FloatingActionButton) rootView.findViewById(R.id.show);
//                    hide();
//                }
//                show.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        ContentValues sh = new ContentValues();
//                        if (showed.equalsIgnoreCase("1")) {
//                            sh.put(MovieContract.Movies.SHOWED, "0");
//                            Toast.makeText(getContext(), "TRAILERS and REVIEWS hidden!", Toast.LENGTH_SHORT).show();
//                            hide();
//                        } else {
//                            sh.put(MovieContract.Movies.SHOWED, "1");
//                            if (downloaded.equalsIgnoreCase("0")){
//                                if(Utility.hasNetworkConnection(getActivity())) {
//                                    updateMovieList();
//                                    sh.put(MovieContract.Movies.DOWNLOADED, "1");
//                                    Toast.makeText(getContext(), "TRAILERS and REVIEWS shown!", Toast.LENGTH_SHORT).show();
//                                    show();
//                                }else{
//                                    Toast.makeText(getContext(), "Network Not Available!", Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        }
//                        getContext().getContentResolver().update(
//                                MovieContract.Movies.CONTENT_URI.buildUpon().appendPath(movieId).build(),
//                                sh, null, new String[]{movieId});
//                    }
//                });
//
//                FloatingActionButton play= (FloatingActionButton) rootView.findViewById(R.id.play);
//
//                play.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (downloaded.equalsIgnoreCase("0")){
//                            ContentValues sh = new ContentValues();
//                            if(Utility.hasNetworkConnection(getActivity())) {
//                                updateMovieList();
//                                sh.put(MovieContract.Movies.DOWNLOADED, "1");
//                                getContext().getContentResolver().update(
//                                        MovieContract.Movies.CONTENT_URI.buildUpon().appendPath(movieId).build(),
//                                        sh, null, new String[]{movieId});
//                                Toast.makeText(getContext(), "Click One More Time to Play!", Toast.LENGTH_SHORT).show();
//                            }else{
//                                Toast.makeText(getContext(), "Network Not Available!", Toast.LENGTH_LONG).show();
//                            }
//                        }else{
//                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(playTrailer));
//                            startActivity(intent);
//                        }
//                    }
//                });
//                FloatingActionButton share = (FloatingActionButton) rootView.findViewById(R.id.share);
//                share.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (downloaded.equalsIgnoreCase("0")) {
//                            ContentValues sh = new ContentValues();
//                            if(Utility.hasNetworkConnection(getActivity())) {
//                                updateMovieList();
//                                sh.put(MovieContract.Movies.DOWNLOADED, "1");
//                                getContext().getContentResolver().update(
//                                        MovieContract.Movies.CONTENT_URI.buildUpon().appendPath(movieId).build(),
//                                        sh, null, new String[]{movieId});
//                                Toast.makeText(getContext(), "Click One More Time to Share!", Toast.LENGTH_SHORT).show();
//                            }else{
//                                Toast.makeText(getContext(), "Network Not Available!", Toast.LENGTH_LONG).show();
//                            }
//                        } else {
//                            Intent intent = new Intent();
//                            intent.setAction(Intent.ACTION_SEND);
//                            intent.putExtra(Intent.EXTRA_TEXT,
//                                    orgTitle + "Watch : " + playTrailer + MOVIE_SHARE_HASHTAG);
//                            intent.setType("text/plain");
//                            startActivity(Intent.createChooser(intent, getString(R.string.share_links)));
//                        }
//                    }
//                });
//                if (movieValues.size()==0){
//                    movieValues.put(MovieContract.Movies.PAGE, data.getString(COL_PAGE));
//                    movieValues.put(MovieContract.Movies.POSTER_PATH,postURL );
//                    movieValues.put(MovieContract.Movies.ADULT,data.getString(COL_ADULT) );
//                    movieValues.put(MovieContract.Movies.OVERVIEW,overview );
//                    movieValues.put(MovieContract.Movies.RELEASE_DATE,relDate );
//                    movieValues.put(MovieContract.Movies.MOVIE_ID,movie_Id );
//                    movieValues.put(MovieContract.Movies.ORIGINAL_TITLE,orgTitle );
//                    movieValues.put(MovieContract.Movies.ORIGINAL_LANGUAGE,orgLang );
//                    movieValues.put(MovieContract.Movies.TITLE,data.getString(COL_TITLE) );
//                    movieValues.put(MovieContract.Movies.BACKDROP_PATH,backdropURL );
//                    movieValues.put(MovieContract.Movies.POPULARITY,popularity );
//                    movieValues.put(MovieContract.Movies.VOTE_COUNT,data.getString(COL_VOTE_COUNT) );
//                    movieValues.put(MovieContract.Movies.VOTE_AVERAGE, votAvg);
//                    movieValues.put(MovieContract.Movies.FAVOURED, "1");
//                    movieValues.put(MovieContract.Movies.SHOWED, data.getString(COL_SHOWED));
//                    movieValues.put(MovieContract.Movies.DOWNLOADED, data.getString(COL_DOWNLOADED));
//                    movieValues.put(MovieContract.Movies.SORT_BY, data.getString(COL_SORT_BY));
//                }
//                break;
            case Constants.FAVOURITE_LOADER:
                isFavourite = false;
                if (data.moveToFirst()) {
                    do {
                        String movieId = data.getString(Constants.COL_MOVIE_ID);
                        if (movieId != null && movieId.equalsIgnoreCase(movie_Id)){
                            isFavourite = true;
                        }
                    }
                    while (data.moveToNext());
                }
                refreshFavButton();
                break;
//            case TRAILER_LOADER:
//                int iter=0;
//                if (data.moveToFirst()) {
//                    do {
//                        trailerListAdapter.swapCursor(data);
//                        iter++;
//                        if(iter==1){
//                            playTrailer+=data.getString(DetailActivityFragment.COL_TRAILER_SOURCE);
//                        }
//                    }
//                    while (data.moveToNext());
//                }
//                break;
//            case REVIEW_LOADER:
//                Log.e(LOG_TAG,"Review");
//                if (data.moveToFirst()) {
//                    do {
//                        reviewListAdapter.swapCursor(data);
//                    }
//                    while (data.moveToNext());
//                }
//                break;
//            case GENRE_LOADER:
//                if (data.moveToFirst()) {
//                    do {
//                        if(genre.length()<9)
//                            genre+=data.getString(COL_GENRE_NAME)+" ";
//                        genreListAdapter.swapCursor(data);
//                    }
//                    while (data.moveToNext());
//                    TextView genreNames=((TextView) rootView.findViewById(R.id.genreNames));
//                    genreNames.setText(genre);
//                }
//                break;
            default:
//                throw new UnsupportedOperationException("Unknown Loader");
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
//        trailerListAdapter.swapCursor(null);
//        reviewListAdapter.swapCursor(null);
//        genreListAdapter.swapCursor(null);
    }}
