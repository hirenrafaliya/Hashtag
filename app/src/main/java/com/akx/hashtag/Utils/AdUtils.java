package com.akx.hashtag.Utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Keep;

import com.akx.hashtag.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

@Keep
public class AdUtils {

    public static InterstitialAd interstitialAd;

    public static void initializeInterstitialAd(Context context) {
        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(context.getString(R.string.interstitial_ad));
        interstitialAd.loadAd(new AdRequest.Builder().addTestDevice(context.getString(R.string.test_device_id)).build());
    }

    public static void reInitInterstitialAd(Context context) {
        if (interstitialAd == null)
            initializeInterstitialAd(context);
        else if (!interstitialAd.isLoading() && !interstitialAd.isLoaded())
            initializeInterstitialAd(context);
    }

    public static void showInterstitialAd(Context context, AdListener adListener) {
        if (interstitialAd == null) {
            initializeInterstitialAd(context);
        } else {
            if (interstitialAd.isLoaded()) {
                if (adListener != null) interstitialAd.setAdListener(adListener);
                interstitialAd.show();
            } else if (!interstitialAd.isLoading()) {
                initializeInterstitialAd(context);
            }
        }
    }

    public static void forceInterstitialAd(Context context, AdListener adListener) {
        if (AdUtils.isInterstitialAdLoaded()) {
            interstitialAd.setAdListener(adListener);
            interstitialAd.show();
        } else {
            reInitInterstitialAd(context);
            interstitialAd.setAdListener(adListener);
        }
    }

    public static boolean isInterstitialAdLoaded() {
        if (interstitialAd == null) return false;
        else return interstitialAd.isLoaded();
    }
}
