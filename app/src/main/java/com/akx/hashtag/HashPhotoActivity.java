package com.akx.hashtag;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akx.hashtag.Model.Quote;
import com.akx.hashtag.Network.GetQuoteService;
import com.akx.hashtag.Network.RetrofitClientInstance;
import com.akx.hashtag.Network.Utils;
import com.akx.hashtag.Utils.AdShowUtils;
import com.akx.hashtag.Utils.AdUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HashPhotoActivity extends AppCompatActivity {

    String caption,tags;

    List<ImageLabel> hashList;

    ImageView imgPhoto,imgBack,imgRefresh;
    TextView txtCaption,txtTags,txtTitle;
    RelativeLayout layoutRefreshCaption,layoutCopyCaption,layoutShareTags,layoutCopyTags;


    CardView cardPhoto,cardCaption,cardTags;
    Button btnSelect;


    Animation animInfiniteRotate;
    Animation animClick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hash_photo);

        initializeViews();

        if(Utils.uriPhoto!=null){
            getLables(Utils.uriPhoto);
            imgPhoto.setImageURI(Utils.uriPhoto);
            imgPhoto.getLayoutParams().height=RelativeLayout.LayoutParams.WRAP_CONTENT;
        }

        getCaption();

        setListeners();

    }

    private void initializeViews(){
        imgPhoto=findViewById(R.id.imgPhoto);
        txtCaption=findViewById(R.id.txtCaption);
        txtTags=findViewById(R.id.txtTags);
        imgBack=findViewById(R.id.imgBack);
        txtTitle=findViewById(R.id.txtTitle);
        layoutRefreshCaption=findViewById(R.id.layoutRefreshCaption);
        layoutCopyCaption=findViewById(R.id.layoutCopyCaption);
        layoutCopyTags=findViewById(R.id.layoutCopyTags);
        layoutShareTags=findViewById(R.id.layoutShareTags);
        imgRefresh=findViewById(R.id.imgRefresh);
        cardCaption=findViewById(R.id.cardParentCaption);
        cardPhoto=findViewById(R.id.cardParentPhoto);
        cardTags=findViewById(R.id.cardParentHashtag);
        btnSelect=findViewById(R.id.btnSelect);

        hashList=new ArrayList<>();

        animateViews();

        initializeBannerAd();

        MobileAds.initialize(this,getString(R.string.app_id));

        CountDownTimer countDownTimer=new CountDownTimer(800,800) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if (AdShowUtils.reqInterShow(HashPhotoActivity.this, 3))
                    AdUtils.showInterstitialAd(HashPhotoActivity.this, null);
            }
        }.start();


    }

    private void setListeners(){

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation=AnimationUtils.loadAnimation(HashPhotoActivity.this, R.anim.anim_item_click);
                btnSelect.startAnimation(animation);

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select your photo"), 1);


            }
        });




        layoutCopyTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animClick=AnimationUtils.loadAnimation(HashPhotoActivity.this, R.anim.anim_item_click);
                layoutCopyTags.startAnimation(animClick);
                Utils.copyToClipboard(txtTags.getText().toString(), HashPhotoActivity.this);

                if (AdShowUtils.reqInterShow(HashPhotoActivity.this, 1))
                    AdUtils.showInterstitialAd(HashPhotoActivity.this, null);
            }
        });

        layoutCopyCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animClick=AnimationUtils.loadAnimation(HashPhotoActivity.this, R.anim.anim_item_click);
                layoutCopyCaption.startAnimation(animClick);
                Utils.copyToClipboard(txtCaption.getText().toString(), HashPhotoActivity.this);

                if (AdShowUtils.reqInterShow(HashPhotoActivity.this, 1))
                    AdUtils.showInterstitialAd(HashPhotoActivity.this, null);
            }
        });

        layoutShareTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animClick=AnimationUtils.loadAnimation(HashPhotoActivity.this, R.anim.anim_item_click);
                layoutShareTags.startAnimation(animClick);
                Utils.shareText(txtTags.getText().toString(), HashPhotoActivity.this, "Hashtags");

                if (AdShowUtils.reqInterShow(HashPhotoActivity.this, 2))
                    AdUtils.showInterstitialAd(HashPhotoActivity.this, null);
            }
        });

        layoutRefreshCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                animClick=AnimationUtils.loadAnimation(HashPhotoActivity.this, R.anim.anim_item_click);
                layoutRefreshCaption.startAnimation(animClick);
                getCaption();

                if (AdShowUtils.reqInterShow(HashPhotoActivity.this, 1))
                    AdUtils.showInterstitialAd(HashPhotoActivity.this, null);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initializeBannerAd(){
        MobileAds.initialize(this,getString(R.string.app_id));
        AdView adView=findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().addTestDevice(getString(R.string.test_device_id)).build());
    }



    private void getCaption(){

        layoutRefreshCaption.setEnabled(false);
        animInfiniteRotate=AnimationUtils.loadAnimation(HashPhotoActivity.this, R.anim.anim_rotate_infinite);
        imgRefresh.startAnimation(animInfiniteRotate);

        GetQuoteService quoteService= RetrofitClientInstance.getQuotesInstance().create(GetQuoteService.class);

        Call<Quote> call=quoteService.getQuotes("en");

        call.enqueue(new Callback<Quote>() {
            @Override
            public void onResponse(Call<Quote> call, Response<Quote> response) {
                if(response.isSuccessful()){
                    Quote quote=response.body();
                    caption=quote.getQuote();
                    txtCaption.setText(caption);
                    layoutRefreshCaption.setEnabled(true);
                    imgRefresh.clearAnimation();
                }
                else {
                    layoutRefreshCaption.setEnabled(true);
                    txtCaption.setText(caption);
                    imgRefresh.clearAnimation();
                }

            }

            @Override
            public void onFailure(Call<Quote> call, Throwable t) {
                layoutRefreshCaption.setEnabled(true);
                txtCaption.setText("");
                imgRefresh.clearAnimation();
                Toast.makeText(HashPhotoActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getLables(Uri uri){
        try {
            InputImage inputImage=InputImage.fromFilePath(this, uri);

            ImageLabeler labeler= ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
            labeler.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                @Override
                public void onSuccess(List<ImageLabel> imageLabels) {
                    hashList=imageLabels;
                    StringBuilder stringBuilder=new StringBuilder();
                    for(int i=0;i<hashList.size();i++){
                        stringBuilder.append("#"+hashList.get(i).getText()+" ");
                    }
                    tags=stringBuilder.toString();
                    txtTags.setText(tags);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    txtTags.setText("");
                    Toast.makeText(HashPhotoActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void animateViews(){
        Animation[] animations=new Animation[3];
        for(int i=0;i<animations.length;i++){
            animations[i]=AnimationUtils.loadAnimation(HashPhotoActivity.this, R.anim.anim_item_fade);
            animations[i].setStartOffset((i+1)*100);
        }
        cardPhoto.startAnimation(animations[0]);
        cardCaption.startAnimation(animations[1]);
        cardTags.startAnimation(animations[2]);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Utils.uriPhoto = data.getData();
            Intent intent = new Intent(HashPhotoActivity.this, HashPhotoActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(0,0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (AdShowUtils.reqInterShow(HashPhotoActivity.this, 3))
            AdUtils.showInterstitialAd(HashPhotoActivity.this, null);
    }
}
