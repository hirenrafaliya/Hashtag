package com.akx.hashtag.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.akx.hashtag.Adapter.TextAdapter;
import com.akx.hashtag.CaptionEditorActivity;
import com.akx.hashtag.Model.Caption;
import com.akx.hashtag.Network.GetCaptionService;
import com.akx.hashtag.Network.RetrofitClientInstance;
import com.akx.hashtag.R;
import com.akx.hashtag.Utils.DataUtils;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TextCollectionFragment extends Fragment {


    private ImageView imgBack;

    private View view;
    private FragmentManager fragmentManager;

    RecyclerView recyclerView;
    ProgressBar progressBar,progressLoad;
    RelativeLayout layoutTextCollection;
    TextView txtStatus;

    List<String> captionList;
    LinearLayoutManager linearLayoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_text_collection, container, false);

        initializeViews();

        initializeRecycleView(DataUtils.captionList);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CaptionEditorActivity.fragment=CaptionEditorActivity.textFragment;
                fragmentManager.beginTransaction().replace(R.id.layoutFragment,CaptionEditorActivity.fragment).commit();
            }
        });

        return view;
    }

    private void initializeViews(){
        imgBack=view.findViewById(R.id.imgBack);
        recyclerView=view.findViewById(R.id.recyclerView);
        progressBar=view.findViewById(R.id.progressBar);
        txtStatus=view.findViewById(R.id.txtStatus);
        progressLoad=view.findViewById(R.id.progressLoad);
        layoutTextCollection=view.findViewById(R.id.layoutTextCollection);


        Animation animation= AnimationUtils.loadAnimation(getContext(), R.anim.anim_editor_translate);
        layoutTextCollection.startAnimation(animation);

        fragmentManager=getActivity().getSupportFragmentManager();
    }

    boolean isLoading=false;
    private void loadCaptions(){
        try {
            if(!isLoading){
                progressLoad.setVisibility(View.VISIBLE);
                GetCaptionService captionService= RetrofitClientInstance.getShayShayInstance().create(GetCaptionService.class);

                Call<Caption> call=captionService.getAllCaption();

                call.enqueue(new Callback<Caption>() {
                    @Override
                    public void onResponse(Call<Caption> call, Response<Caption> response) {
                        if(response.isSuccessful()){
                            progressLoad.setVisibility(View.GONE);
                            Caption caption=response.body();
                            captionList=caption.getCaptionList();
                            if(textAdapter!=null)
                            {
                                DataUtils.captionList.addAll(captionList);
                                textAdapter.notifyItemRangeInserted(DataUtils.captionList.size()-captionList.size(),DataUtils.captionList.size());
                            }else {
                                initializeRecycleView(DataUtils.captionList);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Caption> call, Throwable t) {
                    }
                });
            }
        }
        catch (Exception e){
            isLoading=false;
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
        }

    }

    int currentItems,totalItems,scrollOutItems;
    boolean isScrolling=false;
    TextAdapter textAdapter;
    private void initializeRecycleView(List<String> captionList){

        try {
            progressBar.setVisibility(View.GONE);txtStatus.setVisibility(View.GONE);

            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);

            textAdapter=new TextAdapter(getContext(),captionList);
            recyclerView.setAdapter(textAdapter);
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
                    currentItems=linearLayoutManager.getChildCount();
                    totalItems=linearLayoutManager.getItemCount();
                    scrollOutItems=linearLayoutManager.findFirstVisibleItemPosition();

                    if(isScrolling && (currentItems+scrollOutItems==totalItems)){
                        isScrolling=false;
                        loadCaptions();
                    }
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


}
