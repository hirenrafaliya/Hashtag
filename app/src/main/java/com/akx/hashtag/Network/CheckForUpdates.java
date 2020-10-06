package com.akx.hashtag.Network;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.akx.hashtag.BuildConfig;
import com.akx.hashtag.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import androidx.cardview.widget.CardView;
import androidx.annotation.Keep;

@Keep
public class CheckForUpdates extends AsyncTask {

    Context context;
    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    ImageView imgPromote;
    String versionName;
    boolean isCancelable, isShowable;

    public CheckForUpdates(Context context,ImageView imgPromote) {
        this.context = context;
        this.imgPromote=imgPromote;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        firebaseFirestore.collection("Utils")
                .document("util")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            try{
                                HashMap<String,Object> updateMap=(HashMap<String,Object>) documentSnapshot.get("update");
                                HashMap<String,Object> promotionMap=(HashMap<String,Object>) documentSnapshot.get("promotion");

                                versionName = (String) updateMap.get("versionName");
                                isCancelable = (boolean) updateMap.get("isCancelable");
                                isShowable = (boolean) updateMap.get("isShowable");

                                if (!BuildConfig.VERSION_NAME.equals(versionName)) {
                                    if (isShowable) {
                                        showUpdationDialog((Activity)context, isCancelable);
                                    }
                                }

                                initializePromotion(promotionMap);

                            }catch (Exception e){}
                        }
                    }
                });
        return null;
    }


    String smallImgUrl;
    String largeImgUrl;
    String title;
    String description;
    String promoUrl;
    String btnText;

    private void initializePromotion(HashMap<String, Object> hashMap) {
        smallImgUrl = (String) hashMap.get("smallImgUrl");
        largeImgUrl = (String) hashMap.get("largeImgUrl");
        title = (String) hashMap.get("title");
        description = (String) hashMap.get("description");
        promoUrl = (String) hashMap.get("promoUrl");
        btnText = (String) hashMap.get("btnText");
        boolean isShowable = (boolean) hashMap.get("isShowable");

        if (isShowable && Utils.isInternetAvailable((Activity) context)) {
            imgPromote.setVisibility(View.VISIBLE);
            Animation animation= AnimationUtils.loadAnimation((Activity) context, R.anim.anim_infinite);
            imgPromote.startAnimation(animation);
            Glide.with((Activity) context)
                    .load(smallImgUrl)
                    .into(imgPromote);
            imgPromote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPromoDialog((Activity) context);
                }
            });
        }

    }

    private void showPromoDialog(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow() != null) {
            ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
            dialog.getWindow().setBackgroundDrawable(colorDrawable);
        }
        dialog.setContentView(R.layout.dialog_promote);
        ImageView imgDialogPromote;
        TextView txtDialogTitle, txtDialogDesc;
        Button btnDialogPostive, btnDialogNegative;
        CardView cardParent;

        imgDialogPromote = dialog.findViewById(R.id.imgPromote);
        txtDialogTitle = dialog.findViewById(R.id.txtDialogTitle);
        txtDialogDesc = dialog.findViewById(R.id.txtDialogDesc);
        btnDialogPostive = dialog.findViewById(R.id.btnDialogPositive);
        btnDialogNegative = dialog.findViewById(R.id.btnDialogNegative);
        dialog.setCancelable(true);
        txtDialogTitle.setText(title);
        txtDialogDesc.setText(description);
        btnDialogPostive.setText(btnText);

        dialog.show();

        Log.d("TAGER", largeImgUrl);

        Glide.with(activity)
                .load(largeImgUrl)
                .into(imgDialogPromote);

        Bundle bundle=new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,"promotion_impression");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,btnText);
        FirebaseAnalytics.getInstance(activity).logEvent("promotion_impression",bundle);

        btnDialogNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                dialog.dismiss();
            }
        });

        btnDialogPostive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,"promote_click");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,btnText);
                FirebaseAnalytics.getInstance(activity).logEvent("promote_click",bundle);
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(promoUrl)));
                dialog.cancel();dialog.dismiss();
            }
        });
    }

    private void showUpdationDialog(final Activity activity, boolean isCancelable) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow() != null) {
            ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
            dialog.getWindow().setBackgroundDrawable(colorDrawable);
        }
        dialog.setContentView(R.layout.dialog_layout);
        TextView txtDialogTitle, txtDialogDesc;
        Button btnDialogPostive, btnDialogNegative;

        txtDialogTitle = dialog.findViewById(R.id.txtDialogTitle);
        txtDialogDesc = dialog.findViewById(R.id.txtDialogDesc);
        btnDialogPostive = dialog.findViewById(R.id.btnDialogPositive);
        btnDialogNegative = dialog.findViewById(R.id.btnDialogNegative);

        if (isCancelable) {
            dialog.setCancelable(true);
            txtDialogTitle.setText("Update available");
            txtDialogDesc.setText("New version of app is available. Would you like to install new version of the app ?");
            btnDialogPostive.setText("Update");
            btnDialogNegative.setText("Later");
        } else {
            dialog.setCancelable(false);
            txtDialogTitle.setText("Update required");
            txtDialogDesc.setText("New version of app is available. Update the app to the latest version to continue.");
            btnDialogPostive.setText("Update");
            btnDialogNegative.setVisibility(View.INVISIBLE);
        }

        dialog.show();

        btnDialogNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                dialog.dismiss();
            }
        });

        btnDialogPostive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.akx.hashtag")));
            }
        });
    }
}
