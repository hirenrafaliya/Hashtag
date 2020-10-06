package com.akx.hashtag.Network;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.contentcapture.ContentCaptureCondition;
import android.widget.Toast;

import com.akx.hashtag.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;

import androidx.annotation.Keep;

@Keep
public class Utils {

    //transfer URI from MainActivity to HashPhotoActivity
    public static Uri uriPhoto;


    //Some static methods ---------------------------------------------------------------------------------

    public static void copyToClipboard(String text, Context context){
        ClipboardManager clipboardManager=(ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData=ClipData.newPlainText("label", text);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(context, "Copied to Clipboard", Toast.LENGTH_SHORT).show();
    }

    public static void shareText(String text, Context context, String type){
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, type);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intent, "Share "+type));
    }

    public static boolean isInternetAvailable(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
