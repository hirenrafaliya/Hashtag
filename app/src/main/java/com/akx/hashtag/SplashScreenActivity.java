package com.akx.hashtag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.akx.hashtag.Utils.AdUtils;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.xml.sax.SAXParseException;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Animation animation= AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.anim_item_click);
        ImageView imgIcon=findViewById(R.id.imgIcon);

        imgIcon.startAnimation(animation);


        RequestConfiguration requestConfiguration
                = new RequestConfiguration.Builder()
                .setTestDeviceIds(Collections.singletonList(getString(R.string.test_device_id)))
                .build();
        MobileAds.setRequestConfiguration(requestConfiguration);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                AdUtils.reInitInterstitialAd(SplashScreenActivity.this);
            }
        });


        CountDownTimer countDownTimer=new CountDownTimer(800,800) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Intent intent=new Intent(SplashScreenActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.anim_fade_exit, R.anim.anim_fade_enter);
            }
        }.start();
    }
}
