package com.example.suraj.olaplaystudio;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.support.v4.widget.SwipeRefreshLayout;

import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import android.view.Menu;
import android.support.v7.widget.SearchView;
import android.support.v4.view.MenuItemCompat;
import android.app.SearchManager;
import android.widget.EditText;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.Spanned;


import android.view.ViewGroup;
import android.view.MenuInflater;

import com.example.suraj.olaplaystudio.util.FavList;
import com.example.suraj.olaplaystudio.util.MusicItem;
import com.example.suraj.olaplaystudio.util.RecyclerViewScrollListener;
import com.example.suraj.olaplaystudio.util.RequestInterface;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicPlayer#newInstance} factory method to
 * create an instance of this fragment.
 */


public class MusicPlayer extends Fragment {


    private RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefreshRecyclerList;
    private RecyclerViewAdapter mAdapter;
    private RecyclerViewScrollListener scrollListener;
    private View mCoverView;
    private View mTitleView;
    private View mTimeView;
    private View mDurationView;
    private View seekBar;
    private View mFabView;
    private TextView counter;
    private MusicItem musicItem;
    private boolean isPlaying=false;
    private MediaPlayer mediaPlayer;
    private boolean initialStage = true;

    private ArrayList<MusicItem> modelList = new ArrayList<>();
    private int length;

    private AVLoadingIndicatorView progressDialog;
    public MusicPlayer() {
        // Required empty public constructor
    }


    public static MusicPlayer newInstance() {
        MusicPlayer fragment = new MusicPlayer();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    private Handler mHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        findViews(view);

        return view;

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ArrayList<Integer> favList=new ArrayList<>();
        List<FavList> favLists= SQLite.select().
                from(FavList.class).queryList();
        if(favLists.size()>0){
            for (FavList fav :
                    favLists) {
                favList.add(fav.getPos());
            }

            RecyclerViewAdapter.selectedPositions=null;
            RecyclerViewAdapter.selectedPositions=favList;
        }




        setAdapter();




        swipeRefreshRecyclerList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // Do  stuff on refresh
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (swipeRefreshRecyclerList.isRefreshing())
                            swipeRefreshRecyclerList.setRefreshing(false);
                    }
                }, 5000);

            }
        });

        mFabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleMusic();


            }
        });
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(mediaPlayer != null){
                    if(isPlaying){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        ((SeekBar)seekBar).setProgress(mCurrentPosition);
                    }

                }
                mHandler.postDelayed(this, 1000);
            }
        });


        mCoverView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isPlaying) {

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                            new Pair<>(mCoverView, ViewCompat.getTransitionName(mCoverView)),
                            new Pair<>(mTimeView, ViewCompat.getTransitionName(mTimeView)),
                            new Pair<>(mDurationView, ViewCompat.getTransitionName(mDurationView)),
                            new Pair<>(mFabView, ViewCompat.getTransitionName(mFabView)));


                    Bundle data = new Bundle();
                    data.putSerializable("Music", musicItem);
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtras(data);
                    ActivityCompat.startActivity(getActivity(), intent, options.toBundle());

                }

            }
        });

    }


    private void findViews(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefreshRecyclerList = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_recycler_list);
        mCoverView = view.findViewById(R.id.cover);
        mTitleView = view.findViewById(R.id.title);
        mTimeView = view.findViewById(R.id.time);
        mDurationView = view.findViewById(R.id.duration);
        mFabView = view.findViewById(R.id.fab);
        seekBar=view.findViewById(R.id.seekbar);
        counter=view.findViewById(R.id.counter);
        ((SeekBar)seekBar).getProgressDrawable().setColorFilter(0xFFFFFFFF, PorterDuff.Mode.SRC_ATOP);
        ((SeekBar)seekBar).getThumb().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
        progressDialog=view.findViewById(R.id.avi);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);

        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.action_search));

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(getActivity().SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        //changing edittext color
        EditText searchEdit = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        searchEdit.setTextColor(Color.WHITE);
        searchEdit.setHintTextColor(Color.WHITE);
        searchEdit.setBackgroundColor(Color.TRANSPARENT);
        searchEdit.setHint("Search");

        InputFilter[] fArray = new InputFilter[2];
        fArray[0] = new InputFilter.LengthFilter(40);
        fArray[1] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                for (int i = start; i < end; i++) {

                    if (!Character.isLetterOrDigit(source.charAt(i)))
                        return "";
                }


                return null;


            }
        };
        searchEdit.setFilters(fArray);
        View v = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        v.setBackgroundColor(Color.TRANSPARENT);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<MusicItem> filterList = new ArrayList<MusicItem>();
                if (s.length() > 0) {
                    for (int i = 0; i < modelList.size(); i++) {
                        if (modelList.get(i).getSong().toLowerCase().contains(s.toString().toLowerCase())) {
                            filterList.add(modelList.get(i));
                            mAdapter.updateList(filterList);
                        }
                    }

                } else {
                    mAdapter.updateList(modelList);
                }
                return false;
            }
        });

    }


    private void setAdapter() {

        loadMusicFromWeb();


        scrollListener = new RecyclerViewScrollListener() {

            public void onEndOfScrollReached(RecyclerView rv) {

//                Toast.makeText(getActivity(), "End of the RecyclerView reached. Do your pagination stuff here", Toast.LENGTH_SHORT).show();

                scrollListener.disableScrollListener();
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
          /*
             Note: The below two methods should be used wisely to handle the pagination enable and disable states based on the use case.
                     1. scrollListener.disableScrollListener(); - Should be called to disable the scroll state.
                     2. scrollListener.enableScrollListener(); - Should be called to enable the scroll state.
          */


    }

    void loadMusicFromWeb() {

        if(isOnline()){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://starlord.hackerearth.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RequestInterface requestInterface = retrofit.create(RequestInterface.class);
            Call<ArrayList<MusicItem>> call = requestInterface.getJSon();


            progressDialog.setVisibility(View.VISIBLE);
            progressDialog.show();
            call.enqueue(new Callback<ArrayList<MusicItem>>() {
                @Override
                public void onResponse(Call<ArrayList<MusicItem>> call, Response<ArrayList<MusicItem>> response) {
                    modelList = response.body();
                    mAdapter = new RecyclerViewAdapter(getActivity(), modelList,counter);

                    recyclerView.setHasFixedSize(true);
                    progressDialog.hide();
                    progressDialog.setVisibility(View.GONE);

                    musicItem = modelList.get(0);
                    loadImage((ImageView) mCoverView, musicItem);
                    String songTitle=musicItem.getSong()+" - "+musicItem.getArtist();
                    ((TextView)mTitleView).setText(songTitle);


                    // use a linear layout manager
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);


                    recyclerView.setAdapter(mAdapter);


                    mAdapter.SetOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position, final MusicItem model) {

                            //handle item click events here
                            String songTitle=model.getSong()+" - "+model.getArtist();
                            musicItem=model;
                            ((TextView)mTitleView).setText(songTitle);
                            Picasso.with(getActivity()).load(model.getCover()).into((ImageView) mCoverView);
                            progressDialog.setVisibility(View.VISIBLE);
                            play(model.getSongUrl());


                        }
                    });

                }

                @Override
                public void onFailure(Call<ArrayList<MusicItem>> call, Throwable t) {
                    Log.d("Failed", t.getLocalizedMessage());

                }
            });


        }else {

            try {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

                alertDialog.setTitle("Info");
                alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();

                    }
                });

                alertDialog.show();
            }
            catch(Exception e)
            {
                Log.d("Alert" ,"Show Dialog: "+e.getMessage());
            }
        }


    }

    boolean redirectedUrl = false;

    public void loadImage(final ImageView imageView, final MusicItem model) {
        // Webview is used beacuse the url is redirecting and not loading image.

        WebView webView = new WebView(getActivity());
        webView.loadUrl(model.getCover());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                Picasso.with(getActivity())
                        .load(request.getUrl()).into((ImageView) imageView);
                redirectedUrl = true;
                return super.shouldOverrideUrlLoading(view, request);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                Picasso.with(getActivity())
                        .load(url).
                        error(R.drawable.ic_image_black_24dp)
                        .fit()
                        .into((ImageView) imageView);
                redirectedUrl = true;
                model.setCover_image(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        if (!redirectedUrl) {
            Picasso.with(getActivity())
                    .load(model.getSongUrl()).
                    error(R.drawable.ic_image_black_24dp)
                    .fit()
                    .into((ImageView) imageView);
            redirectedUrl = false;
        }

    }

    void toggleMusic(){
        if(initialStage){
            initialStage=false;
            play(musicItem.getSongUrl());
            progressDialog.setVisibility(View.VISIBLE);
            return;
        }
        if (!isPlaying)
        {
                mediaPlayer.seekTo(length);
                mediaPlayer.start();
            ((FloatingActionButton)mFabView).setImageResource(R.drawable.ic_pause_black_24dp);
                isPlaying=true;
            progressDialog.setVisibility(View.VISIBLE);
        }
        else
        {
            progressDialog.setVisibility(View.GONE);
            stop();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer=null;
        }
    }

    void stop(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            ((FloatingActionButton)mFabView).setImageResource(R.drawable.ic_play_arrow_black_24dp);
            length=mediaPlayer.getCurrentPosition();
            isPlaying=false;
        }
    }


    void play(String url){



        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
            stop();
            mediaPlayer=null;
            ((FloatingActionButton)mFabView).setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }


            mediaPlayer = new MediaPlayer();
            try {

                mediaPlayer.reset(); // new one

                mediaPlayer.setDataSource(url);

                //mp.prepareAsync();

                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                mediaPlayer.prepare(); // don't use prepareAsync for mp3 playback

                mediaPlayer.start();



                ((FloatingActionButton)mFabView).setImageResource(R.drawable.ic_pause_black_24dp);

                // String songTitle = songsList.get(songIndex).get("songTitle");
                // songTitleLabel.setText(songTitle);

                ((SeekBar)seekBar).setMax(mediaPlayer.getDuration()/1000);
                ((TextView)mDurationView).setText(""+mediaPlayer.getDuration()/1000);


            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        isPlaying = mediaPlayer.isPlaying();

    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(getActivity(), "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
