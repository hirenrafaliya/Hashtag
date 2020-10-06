package com.akx.hashtag;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akx.hashtag.Adapter.PagerViewAdapter;
import com.akx.hashtag.Network.CheckForUpdates;
import com.akx.hashtag.Network.CloudMessaging;
import com.akx.hashtag.Network.Utils;
import com.akx.hashtag.Utils.AdShowUtils;
import com.akx.hashtag.Utils.AdUtils;
import com.akx.hashtag.Utils.DataUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "TAGER";


    ViewPager mViewPager;
    RelativeLayout mLayoutHashtag, mLayoutCaption, layoutCaptionEditor, layoutAutoHash;
    TextView txtHashtag, txtCaption;
    ImageView imgHashtag, imgCaption, imgPromote;
    FirebaseAnalytics firebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        RequestConfiguration requestConfiguration
                = new RequestConfiguration.Builder()
                .setTestDeviceIds(Collections.singletonList(getString(R.string.test_device_id)))
                .build();
        MobileAds.setRequestConfiguration(requestConfiguration);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                AdUtils.reInitInterstitialAd(MainActivity.this);
            }
        });

//        MediationTestSuite.launch(this);

        initializeViews();

        initializeViewPager();

        try {
            new CheckForUpdates(MainActivity.this, imgPromote).execute();
            new CloudMessaging(this).execute();
        } catch (Exception e) {
        }

        layoutAutoHash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "generate_click");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "generate_click");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button");
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_item_click);
                layoutAutoHash.startAnimation(animation);

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select your photo"), 1);
            }
        });

        layoutCaptionEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_item_click);
                layoutCaptionEditor.startAnimation(animation);
                if (DataUtils.captionList.size() != 0) {
                    Intent intent = new Intent(MainActivity.this, CaptionEditorActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Wait until captions are loading", Toast.LENGTH_SHORT).show();
                }

            }
        });


        CountDownTimer timer = new CountDownTimer(1200, 800) {
            @Override
            public void onTick(long l) {
                if (!AdShowUtils.isInterMainShown) {
                    if (AdUtils.isInterstitialAdLoaded()) {
                        AdUtils.showInterstitialAd(MainActivity.this, null);
                        AdShowUtils.isInterMainShown = true;
                    }
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();

    }

    boolean isLoadAd = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (isLoadAd)
            AdUtils.reInitInterstitialAd(this);
    }

    private void initializeViews() {
        layoutAutoHash = findViewById(R.id.layoutAutoHash);
        mViewPager = findViewById(R.id.viewPager);
        mLayoutCaption = findViewById(R.id.layoutCaption);
        mLayoutHashtag = findViewById(R.id.layoutHashtag);
        txtHashtag = findViewById(R.id.txtHashtag);
        imgPromote = findViewById(R.id.imgPromote);
        txtCaption = findViewById(R.id.txtCaption);
        imgHashtag = findViewById(R.id.imgHashtag);
        imgCaption = findViewById(R.id.imgCaption);
        layoutCaptionEditor = findViewById(R.id.layoutCaptionEditor);


        MobileAds.initialize(this, getString(R.string.app_id));
    }

    private void initializeViewPager() {
        PagerViewAdapter pagerViewAdapter = new PagerViewAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerViewAdapter);

        setPageHashtag();

        mLayoutHashtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPageHashtag();
                mViewPager.setCurrentItem(0);
            }
        });

        mLayoutCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPageCaption();
                mViewPager.setCurrentItem(1);
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (!AdShowUtils.isInterMainShown) {
                    if (AdUtils.isInterstitialAdLoaded()) {
                        AdUtils.showInterstitialAd(MainActivity.this, null);
                        AdShowUtils.isInterMainShown = true;
                    }
                }

                if (position == 0) {
                    setPageHashtag();
                } else if (position == 1) {
                    setPageCaption();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    private void setPageHashtag() {
        mLayoutCaption.setBackground(null);

        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_item_click);
        mLayoutHashtag.startAnimation(animation);

        mLayoutHashtag.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_navigation));

        imgHashtag.setImageResource(R.drawable.ic_hashtag_white);
        imgCaption.setImageResource(R.drawable.ic_caption);

        txtHashtag.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryWhite));
        txtCaption.setTextColor(ContextCompat.getColor(this, R.color.colorSecondaryBlack));
    }

    private void setPageCaption() {
        mLayoutHashtag.setBackground(null);

        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_item_click);
        mLayoutCaption.startAnimation(animation);

        mLayoutCaption.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_navigation));

        imgHashtag.setImageResource(R.drawable.ic_hashtag);
        imgCaption.setImageResource(R.drawable.ic_caption_white);

        txtHashtag.setTextColor(ContextCompat.getColor(this, R.color.colorSecondaryBlack));
        txtCaption.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryWhite));


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Utils.uriPhoto = data.getData();
            Intent intent = new Intent(MainActivity.this, HashPhotoActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (AdUtils.isInterstitialAdLoaded()) {
            AdUtils.showInterstitialAd(this, new AdListener() {
                @Override
                public void onAdClosed() {
                    showExitDialog();
                }
            });
            isLoadAd = false;
        } else {
            exit();
        }
    }

    private void exit() {
        try {
            if (dialog != null)
                if (dialog.isShowing())
                    dialog.dismiss();
            finishAffinity();
//            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Dialog dialog;

    private void showExitDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow() != null) {
            ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
            dialog.getWindow().setBackgroundDrawable(colorDrawable);
        }
        dialog.setContentView(R.layout.dialog_layout);
        dialog.setCancelable(true);

        TextView txtDialogTitle, txtDialogDesc;
        Button btnDialogPostive, btnDialogNegative;

        txtDialogTitle = dialog.findViewById(R.id.txtDialogTitle);
        txtDialogDesc = dialog.findViewById(R.id.txtDialogDesc);
        btnDialogPostive = dialog.findViewById(R.id.btnDialogPositive);
        btnDialogNegative = dialog.findViewById(R.id.btnDialogNegative);
        ImageView imgAnimation = dialog.findViewById(R.id.imgAnimation);
        imgAnimation.setVisibility(View.GONE);

        txtDialogTitle.setText("Are you sure you want to exit ?");
        txtDialogDesc.setVisibility(View.GONE);
        btnDialogNegative.setText("Cancel");
        btnDialogPostive.setText("Exit");

        dialog.show();

        btnDialogNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnDialogPostive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        });
    }

}
