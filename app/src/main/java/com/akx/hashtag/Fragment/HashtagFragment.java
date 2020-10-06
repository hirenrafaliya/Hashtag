package com.akx.hashtag.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akx.hashtag.Adapter.HashtagAdapter;
import com.akx.hashtag.MainActivity;
import com.akx.hashtag.Model.Hashtag;
import com.akx.hashtag.R;
import com.akx.hashtag.Utils.AdShowUtils;
import com.akx.hashtag.Utils.AdUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HashtagFragment extends Fragment {

    public static String TAG="TAGER";

    View view;

    RecyclerView mRecyclerView;
    FirebaseFirestore firebaseFirestore;
    List<Hashtag> hashtagList;
    ProgressBar progressBar;
    TextView txtStatus;

    public HashtagFragment(){}


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hashtag, null);

        initializeViews();

        getHashtags();

        Log.d(TAG, "onCreateView: ");

        return view;
    }

    private void initializeViews(){
        mRecyclerView=view.findViewById(R.id.recyclerView);
        progressBar=view.findViewById(R.id.progressBar);
        txtStatus=view.findViewById(R.id.txtStatus);

        firebaseFirestore=FirebaseFirestore.getInstance();
        hashtagList=new ArrayList<>();
    }

    private void getHashtags(){

        progressBar.setVisibility(View.VISIBLE);
        txtStatus.setVisibility(View.VISIBLE);txtStatus.setText("Loading...");

        firebaseFirestore.collection("Hashtag").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    Log.d(TAG, "onSuccess: ");
                    List<DocumentSnapshot> dsList=queryDocumentSnapshots.getDocuments();

                    for(DocumentSnapshot snapshot : dsList){
                        Hashtag hashtag=snapshot.toObject(Hashtag.class);
                        String s=hashtag.getTags();
                        s=s.replace(" ","").replace("#"," #").replaceFirst(" ", "");
                        hashtag.setTags(s);
                        hashtagList.add(hashtag);
                    }
                    initializeRecyclerView();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                txtStatus.setText("Something went wrong \n Click to Retry");
                txtStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getHashtags();
                    }
                });
            }
        });
    }

    private void initializeRecyclerView(){

        progressBar.setVisibility(View.GONE);
        txtStatus.setVisibility(View.GONE);

        CountDownTimer timer=new CountDownTimer(800,800) {
            @Override
            public void onTick(long l) {
                if(!AdShowUtils.isInterMainShown){
                    if(AdUtils.isInterstitialAdLoaded()){
                        AdUtils.showInterstitialAd(getContext(),null);
                        AdShowUtils.isInterMainShown=true;
                    }
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();

        mRecyclerView.setHasFixedSize(true);
        HashtagAdapter hashtagAdapter=new HashtagAdapter(getContext(), hashtagList);
        mRecyclerView.setAdapter(hashtagAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        OverScrollDecoratorHelper.setUpOverScroll(mRecyclerView,OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
    }

}
