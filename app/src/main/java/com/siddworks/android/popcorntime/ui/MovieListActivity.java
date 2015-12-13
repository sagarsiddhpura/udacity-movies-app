package com.siddworks.android.popcorntime.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.siddworks.android.popcorntime.R;
import com.siddworks.android.popcorntime.data.Api;
import com.siddworks.android.popcorntime.data.MovieGridViewAdapter;
import com.siddworks.android.popcorntime.data.MovieRecyclerViewAdapter;
import com.siddworks.android.popcorntime.model.Movie;
import com.siddworks.android.popcorntime.model.MovieWrapper;
import com.siddworks.android.popcorntime.ui.widgets.AutofitRecyclerView;
import com.siddworks.android.popcorntime.util.CommonUtil;
import com.siddworks.android.popcorntime.util.Constants;
import com.siddworks.android.popcorntime.util.JsonUtils;
import com.siddworks.android.popcorntime.util.Key;
import com.siddworks.android.popcorntime.util.Log;
import com.siddworks.android.popcorntime.util.PopcornTimeUtil;
import com.siddworks.android.popcorntime.util.PrefUtils;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    public boolean mTwoPane;
    private ArrayList<Movie> movies;
    private static final String TAG = "MovieListActivity";
    private boolean isLoggingEnabled = true;
    private AutofitRecyclerView mMoviesRV;
    private TextView mEmptyTextView;
    private GridView mMoviesGridView;
    private RelativeLayout mLoadingView;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mMoviesRV = (AutofitRecyclerView) findViewById(R.id.movie_list);
        mMoviesGridView = (GridView) findViewById(R.id.movies_gridview);
        assert mMoviesRV != null;
        setupRecyclerView(mMoviesRV);
        mEmptyTextView = (TextView) findViewById(R.id.empty_TextView);
        mLoadingView = (RelativeLayout) findViewById(R.id.loadingView);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        showMovies();
    }

    private void showMovies() {
        String moviesSortType = PrefUtils.getMoviesSortType(this);
        android.util.Log.d(TAG, "showMovies() called with moviesSortType:" + moviesSortType);
        if(moviesSortType.equals(Constants.SORT_BY_POPULAR)) {
            showPopularMovies();
        } else {
            showHighestRatedMovies();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movies_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_set_sort_by) {
            View viewById = findViewById(R.id.action_set_sort_by);
            View actionView = item.getActionView();
            showSortByMenu(viewById);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSortByMenu(View actionView) {
        PopupMenu popup = new PopupMenu(this, actionView);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_sort_by, popup.getMenu());
        popup.show();
    }

    private void showHighestRatedMovies() {
//        Log.d(isLoggingEnabled, TAG, "showHighestRatedMovies");
        toggleLoadingStatus(true);

        Api api = PopcornTimeUtil.getApi();

        Observable<MovieWrapper> highestRatedMoviesNetworkObs = api.getHighestRatedMovies(Key.getKey(), Constants.MIN_VOTE_COUNT, Constants.SORT_BY_RATED);
        highestRatedMoviesNetworkObs.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieWrapper>() {
                    @Override
                    public void onCompleted() {
                        Log.d(isLoggingEnabled, TAG, "highestRatedMoviesNetworkObs.onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(isLoggingEnabled, TAG, "highestRatedMoviesNetworkObs.onError  e:" + e);
                        e.printStackTrace();

                        toggleLoadingStatus(false);
                        if(movies == null) {
                            refreshAdapter(movies);
                        }

                        Snackbar snackbar = Snackbar
                                .make(mCoordinatorLayout, "Error connecting to internet",
                                        Snackbar.LENGTH_LONG)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        showMovies();
                                    }
                                });

                        snackbar.show();
                    }

                    @Override
                    public void onNext(MovieWrapper movieWrapper) {
                        Log.d(isLoggingEnabled, TAG, "highestRatedMoviesNetworkObs.onNext  movieWrapper:" + movieWrapper);

                        ArrayList<Movie> moviesNetwork = movieWrapper.getMovies();
                        if (moviesNetwork != null) {
                            // Save the network data to preferences
                            String json = JsonUtils.makeJson(movieWrapper);
                            if (json != null) {
                                Log.d(isLoggingEnabled, TAG, "saving to pref json:" + json);
                                PrefUtils.saveHighestRatedMovies(MovieListActivity.this, json);
                            }
                        }
                    }
                });

        Observable<MovieWrapper> highestRatedMoviesPrefObs = Observable.just(null);
        String highestRatedMoviesJson = PrefUtils.getHighestRatedMovies(MovieListActivity.this);
        Log.d(isLoggingEnabled, TAG, "from pref highestRatedMoviesJson:" + highestRatedMoviesJson);
        if (highestRatedMoviesJson != null) {
            MovieWrapper movieWrapper = (MovieWrapper) JsonUtils.makeObject(
                    highestRatedMoviesJson, MovieWrapper.class);
            highestRatedMoviesPrefObs = Observable.just(movieWrapper);
        }

        Observable<MovieWrapper> highestRatedMoviesAllSources = Observable
                .concat(highestRatedMoviesPrefObs, highestRatedMoviesNetworkObs)
                .takeFirst(new Func1<MovieWrapper, Boolean>() {
                    @Override
                    public Boolean call( MovieWrapper movieWrapper ) {
                        return movieWrapper != null;
                    }
                });

        highestRatedMoviesAllSources.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieWrapper>() {
                    @Override
                    public void onCompleted() {
                        Log.d(isLoggingEnabled, TAG, "highestRatedMoviesAllSources.onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(isLoggingEnabled, TAG, "highestRatedMoviesAllSources.onError  e:" + e);
                    }

                    @Override
                    public void onNext(MovieWrapper movieWrapper) {
                        Log.d(isLoggingEnabled, TAG, "highestRatedMoviesAllSources.onNext  movieWrapper:" + movieWrapper);

                        if (movieWrapper != null) {
                            ArrayList<Movie> moviesNetwork = movieWrapper.getMovies();
                            if (moviesNetwork != null) {
                                // Recycler view is empty. Refresh view
                                movies = moviesNetwork;
                                refreshAdapter(movies);
                            }
                        }
                    }
                });

    }

    private void showPopularMovies() {
        Log.d(isLoggingEnabled, TAG, "showPopularMovies");
        toggleLoadingStatus(true);
        Api api = PopcornTimeUtil.getApi();

        Observable<MovieWrapper> popularMoviesNetworkObs = api.getPopularMovies(Key.getKey(), Constants.MIN_VOTE_COUNT, Constants.SORT_BY_POPULAR);
        popularMoviesNetworkObs.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieWrapper>() {
                    @Override
                    public void onCompleted() {
                        Log.d(isLoggingEnabled, TAG, "popularMoviesNetworkObs.onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(isLoggingEnabled, TAG, "popularMoviesNetworkObs.onError  e:" + e);
                        e.printStackTrace();

                        toggleLoadingStatus(false);
                        if(movies == null) {
                            refreshAdapter(movies);
                        }

                        Snackbar snackbar = Snackbar
                                .make(mCoordinatorLayout, "Error connecting to internet",
                                        Snackbar.LENGTH_LONG)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        showMovies();
                                    }
                                });

                        snackbar.show();
                    }

                    @Override
                    public void onNext(MovieWrapper movieWrapper) {
                        Log.d(isLoggingEnabled, TAG, "popularMoviesNetworkObs.onNext  movieWrapper:" + movieWrapper);

                        ArrayList<Movie> moviesNetwork = movieWrapper.getMovies();
                        if (moviesNetwork != null) {
                            // Save the network data to preferences
                            String json = JsonUtils.makeJson(movieWrapper);
                            if (json != null) {
                                PrefUtils.savePopularMovies(MovieListActivity.this, json);
                            }
                        }
                    }
                });

        Observable<MovieWrapper> popularMoviesPrefObs = Observable.just(null);
        String popularMoviesJson = PrefUtils.getPopularMovies(MovieListActivity.this);
//        Log.d(isLoggingEnabled, TAG, " popularMoviesJson:" + popularMoviesJson);
        if (popularMoviesJson != null) {
            MovieWrapper movieWrapper = (MovieWrapper) JsonUtils.makeObject(
                    popularMoviesJson, MovieWrapper.class);
            popularMoviesPrefObs = Observable.just(movieWrapper);
        }

        Observable<MovieWrapper> popularMoviesAllSources = Observable
                .concat(popularMoviesPrefObs, popularMoviesNetworkObs)
                .takeFirst(new Func1<MovieWrapper, Boolean>() {
                    @Override
                    public Boolean call( MovieWrapper movieWrapper ) {
                        return movieWrapper != null;
                    }
                });

        popularMoviesAllSources.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieWrapper>() {
                    @Override
                    public void onCompleted() {
                        Log.d(isLoggingEnabled, TAG, "popularMoviesAllSources.onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(isLoggingEnabled, TAG, "popularMoviesAllSources.onError  e:" + e);
                    }

                    @Override
                    public void onNext(MovieWrapper movieWrapper) {
                        Log.d(isLoggingEnabled, TAG, "popularMoviesAllSources.onNext  movieWrapper:" + movieWrapper);

                        if (movieWrapper != null) {
                            ArrayList<Movie> moviesNetwork = movieWrapper.getMovies();
                            if (moviesNetwork != null) {
                                // Recycler view is empty. Refresh view
                                movies = moviesNetwork;
                                refreshAdapter(movies);
                            }
                        }
                    }
                });

    }

    @UiThread
    private void refreshAdapter(ArrayList<Movie> movies) {
        android.util.Log.d(TAG, "refreshAdapter() called. movies:"+movies);

        toggleLoadingStatus(false);

        if (mMoviesRV != null) {
            if (movies == null || movies.size() == 0) {
                CommonUtil.setVisiblity(mEmptyTextView, View.VISIBLE);
                CommonUtil.setVisiblity(mMoviesRV, View.GONE);
                CommonUtil.setVisiblity(mMoviesGridView, View.GONE);
            } else {
                CommonUtil.setVisiblity(mEmptyTextView, View.GONE);
                CommonUtil.setVisiblity(mMoviesRV, View.VISIBLE);
                CommonUtil.setVisiblity(mMoviesGridView, View.VISIBLE);

                if (mMoviesRV.getAdapter() == null) {
                    mMoviesRV.setAdapter(new MovieRecyclerViewAdapter(this, movies));
                } else {
                    MovieRecyclerViewAdapter adapter = (MovieRecyclerViewAdapter) mMoviesRV.getAdapter();
                    adapter.clearAll();
                    adapter.addAll(movies);
                    adapter.notifyDataSetChanged();
                }

                if (mMoviesGridView.getAdapter() == null) {
                    mMoviesGridView.setAdapter(new MovieGridViewAdapter(this, movies));
                } else {
//                    mMoviesGridView.setAdapter(new MovieGridViewAdapter(this, movies));
                    MovieGridViewAdapter adapter = (MovieGridViewAdapter) mMoviesGridView.getAdapter();
                    adapter.clearData();
                    adapter.addAll(movies);
                    adapter.notifyDataSetChanged();
                }


            }
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
//        mMoviesRV.addItemDecoration(new MarginDecoration(this));
//        mMoviesRV.setLayoutManager(new GridLayoutManager(this, 4));
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_by_popular:
                PrefUtils.setMoviesSortType(this, Constants.SORT_BY_POPULAR);
                showMovies();
                break;
            case R.id.action_sort_by_rated:
                PrefUtils.setMoviesSortType(this, Constants.SORT_BY_RATED);
                showMovies();
                break;
        }
        return false;
    }

    void toggleLoadingStatus(boolean showLoading) {
        if(showLoading) {
            CommonUtil.setVisiblity(mLoadingView, View.VISIBLE);
        } else {
            CommonUtil.setVisiblity(mLoadingView, View.GONE);
        }
    }
}
