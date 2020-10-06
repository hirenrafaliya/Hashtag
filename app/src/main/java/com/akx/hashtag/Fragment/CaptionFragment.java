package com.akx.hashtag.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.akx.hashtag.Adapter.CaptionAdapter;
import com.akx.hashtag.CaptionEditorActivity;
import com.akx.hashtag.Model.Caption;
import com.akx.hashtag.Network.GetCaptionService;
import com.akx.hashtag.Network.LoadShayShay;
import com.akx.hashtag.Network.RetrofitClientInstance;
import com.akx.hashtag.Network.Utils;
import com.akx.hashtag.R;
import com.akx.hashtag.Utils.AdShowUtils;
import com.akx.hashtag.Utils.AdUtils;
import com.akx.hashtag.Utils.DataUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CaptionFragment extends Fragment {

    public static String TAG="TAGER";


    View view;
    RecyclerView recyclerView;
    ProgressBar progressBar,progressLoad;
    TextView txtStatus;

    LinearLayoutManager linearLayoutManager;


    public CaptionFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_caption, container, false);

        initializeViews();

        getCaptions();



        return view;
    }

    private void initializeViews(){
        recyclerView=view.findViewById(R.id.recyclerView);
        progressBar=view.findViewById(R.id.progressBar);
        txtStatus=view.findViewById(R.id.txtStatus);
        progressLoad=view.findViewById(R.id.progressLoad);
    }

    private void getCaptions(){

        progressBar.setVisibility(View.VISIBLE);
        txtStatus.setVisibility(View.VISIBLE);txtStatus.setText("Loading...");

        GetCaptionService captionService= RetrofitClientInstance.getShayShayInstance().create(GetCaptionService.class);

        Call<Caption> call=captionService.getAllCaption();

        call.enqueue(new Callback<Caption>() {
            @Override
            public void onResponse(Call<Caption> call, Response<Caption> response) {
                if(response.isSuccessful()){
                    Caption caption=response.body();
                    DataUtils.captionList=caption.getCaptionList();
                    initializeRecycleView(DataUtils.captionList);
                }
                else {
                    progressBar.setVisibility(View.INVISIBLE);
                    txtStatus.setText("Something went wrong \n Click to Retry");
                    txtStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getCaptions();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Caption> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                txtStatus.setText("Something went wrong \n Click to Retry");
                txtStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getCaptions();
                    }
                });
            }
        });

    }

    int currentItems,totalItems,scrollOutItems;
    boolean isScrolling=false;
    private void initializeRecycleView(List<String> captionList){

        progressBar.setVisibility(View.GONE);txtStatus.setVisibility(View.GONE);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);

        CaptionAdapter captionAdapter=new CaptionAdapter(getContext(), captionList);
        recyclerView.setAdapter(captionAdapter);
        linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView,OverScrollDecoratorHelper.ORIENTATION_VERTICAL);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling=true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("TAGER", "OnScrolled called");
                currentItems=linearLayoutManager.getChildCount();
                totalItems=linearLayoutManager.getItemCount();
                scrollOutItems=linearLayoutManager.findFirstVisibleItemPosition();

                if(isScrolling && (currentItems+scrollOutItems==totalItems)){
                    isScrolling=false;
                    progressLoad.setVisibility(View.VISIBLE);
                    if (AdShowUtils.reqInterShow(getContext(), 2))
                        AdUtils.showInterstitialAd(getContext(), null);
                    LoadShayShay shayShay=new LoadShayShay(getContext(),captionAdapter,progressLoad);
                    shayShay.execute();
                }
            }
        });

    }


}
