package com.akx.hashtag.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.akx.hashtag.Adapter.ImageAdapter;
import com.akx.hashtag.CaptionEditorActivity;
import com.akx.hashtag.Model.Photo;
import com.akx.hashtag.Model.Pixabay;
import com.akx.hashtag.Network.GetPixabayService;
import com.akx.hashtag.Network.RetrofitClientInstance;
import com.akx.hashtag.R;
import com.akx.hashtag.Utils.DataUtils;
import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {

    View view;

    RecyclerView recyclerView;
    RelativeLayout layoutParentImage,layoutSelect;
    ImageAdapter imageAdapter;
    List<Photo> photoList;
    ImageView imgBack,imgBackground;


    int page=1;

    public ImageFragment(ImageView imgBackground){
        this.imgBackground=imgBackground;
    }

    public ImageFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_image, container, false);

        initializeViews();
        if (DataUtils.photoList.isEmpty()) {
            getPhotos();
        } else {
            initializeRecyclerView(DataUtils.photoList);
        }


        layoutSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 4);

            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove();
            }
        });

        return view;
    }

    private void initializeViews() {
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutParentImage=view.findViewById(R.id.layoutParentImage);
        photoList=new ArrayList<>();
        layoutSelect=view.findViewById(R.id.layoutSelect);
        imgBack=view.findViewById(R.id.imgBack);

        Animation animation= AnimationUtils.loadAnimation(getContext(), R.anim.anim_editor_translate);
        layoutParentImage.startAnimation(animation);
    }


    boolean isLoading=false;
    public void getPhotos() {
        try{
            if(!isLoading){
                Log.d("TAGER", "getPhotos: ");
                isLoading=true;
                GetPixabayService pixabayService = RetrofitClientInstance.getPixabayInstance().create(GetPixabayService.class);

                Call<Pixabay> call = pixabayService.getAllPhotos(GetPixabayService.API_KEY,page,"backgrounds");

                call.enqueue(new Callback<Pixabay>() {
                    @Override
                    public void onResponse(Call<Pixabay> call, Response<Pixabay> response) {
                        isLoading=false;
                        if (response.isSuccessful()) {
                            page++;
                            photoList=response.body().getPhotos();
                            DataUtils.photoList.addAll(photoList);
                            if (imageAdapter == null) {
                                initializeRecyclerView(DataUtils.photoList);
                            } else {
                                Log.d("TAGER","dataset changing" );
                                imageAdapter.notifyItemRangeInserted(DataUtils.photoList.size()-photoList.size(),DataUtils.photoList.size());
                            }
                        } else {
                            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Pixabay> call, Throwable t) {
                        isLoading=false;
                        Toast.makeText(getActivity(), "Failed to get", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        catch (Exception e){
            isLoading=false;
            e.printStackTrace();
        }
    }

    private void initializeRecyclerView(List<Photo> photoList) {
        try
        {
            imageAdapter = new ImageAdapter(getContext(), photoList);
            recyclerView.setItemViewCacheSize(40);
            recyclerView.setAdapter(imageAdapter);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            OverScrollDecoratorHelper.setUpOverScroll(recyclerView,OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if(requestCode==4 && resultCode==RESULT_OK && data!=null){
                remove();
                imgBackground.setImageURI(data.getData());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void remove() {
        Animation animation= AnimationUtils.loadAnimation(getContext(), R.anim.anim_editor_translate_reverse);
        layoutParentImage.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(ImageFragment.this).commit();
                CaptionEditorActivity.fragment=null;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
