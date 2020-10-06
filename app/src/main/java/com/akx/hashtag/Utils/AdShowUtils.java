package com.akx.hashtag.Utils;

import android.content.Context;

import androidx.annotation.Keep;

@Keep
public class AdShowUtils {

    private static int interPresetCount = 0;
    static int interShowCount = 3;

    public static boolean reqInterShow(Context context,int count) {
        try {
            interPresetCount=interPresetCount+count;
            if (interPresetCount >= interShowCount) {
                interPresetCount = 0;
                return true;
            } else if (interPresetCount >= interShowCount - 2) {
                AdUtils.reInitInterstitialAd(context);
            }
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public static boolean isInterMainShown = false;

}
