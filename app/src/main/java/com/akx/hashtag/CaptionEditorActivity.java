package com.akx.hashtag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.akx.hashtag.Adapter.ImageAdapter;
import com.akx.hashtag.Adapter.TextAdapter;
import com.akx.hashtag.Fragment.AdjustFragment;
import com.akx.hashtag.Fragment.CaptionFragment;
import com.akx.hashtag.Fragment.TextCollectionFragment;
import com.akx.hashtag.Fragment.ImageFragment;
import com.akx.hashtag.Fragment.TextFragment;
import com.akx.hashtag.Network.Utils;
import com.akx.hashtag.Utils.AdShowUtils;
import com.akx.hashtag.Utils.AdUtils;
import com.akx.hashtag.Utils.DataUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CaptionEditorActivity extends AppCompatActivity implements ImageAdapter.ImageAdapterListener, TextFragment.TextListener, TextAdapter.TextAdapterListener {


    private RelativeLayout layoutImgParent;
    private ImageView imgBackground;
    private EditText edtCaption;
    private LinearLayout layoutAdjust, layoutCaption, layoutImages, layoutSave;

    private FragmentManager fragmentManager;

    public static Fragment fragment;
    ImageFragment imageFragment;
    public static TextFragment textFragment;
    AdjustFragment adjustFragment;
    List<String> fragmentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caption_editor);

        initializeViews();

        if (AdShowUtils.reqInterShow(this, 1))
            AdUtils.showInterstitialAd(this, null);


        layoutAdjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(fragment instanceof AdjustFragment) || fragment == null) {
                    fragment = adjustFragment;
                    replaceFragment(fragment);
                } else {
                    adjustFragment.remove();
                    fragment = null;
                }
            }
        });

        layoutImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(fragment instanceof ImageFragment) || fragment == null) {
                    fragment = imageFragment;
                    replaceFragment(fragment);
                } else {
                    imageFragment.remove();
                    fragment = null;
                }
            }
        });

        layoutCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragment instanceof TextCollectionFragment) {
                    fragmentManager.beginTransaction().remove(fragment).commit();
                    fragment = null;
                    return;
                }
                if (!(fragment instanceof TextFragment) || fragment == null) {
                    fragment = textFragment;
                    replaceFragment(fragment);
                } else {
                    textFragment.remove();
                    fragment = null;
                }
            }
        });

        edtCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragment instanceof TextCollectionFragment) {
                    fragmentManager.beginTransaction().remove(fragment).commit();
                    fragment = null;
                    return;
                }
                if (!(fragment instanceof TextFragment) || fragment == null) {
                    fragment = textFragment;
                    replaceFragment(fragment);
                } else {
                    textFragment.remove();
                    fragment = null;
                }
            }
        });

        layoutSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutImgParent.setDrawingCacheEnabled(true);
                edtCaption.setVisibility(View.INVISIBLE);
                edtCaption.setVisibility(View.VISIBLE);
                DataUtils.bitmapImg = layoutImgParent.getDrawingCache();


                startActivity(new Intent(CaptionEditorActivity.this, SaveCaptionImageActivity.class));
            }
        });

    }

    private void initializeViews() {
        layoutImgParent = findViewById(R.id.layoutImgParent);
        imgBackground = findViewById(R.id.imgBackgound);
        layoutAdjust = findViewById(R.id.layoutAdjust);
        edtCaption = findViewById(R.id.edtCaption);
        layoutCaption = findViewById(R.id.layoutCaption);
        layoutImages = findViewById(R.id.layoutImages);
        layoutSave = findViewById(R.id.layoutSave);

        imageFragment = new ImageFragment(imgBackground);
        textFragment = new TextFragment(edtCaption);
        adjustFragment = new AdjustFragment(edtCaption);
        fragmentManager = getSupportFragmentManager();
        fragmentList = new ArrayList<>();


        if (!DataUtils.captionList.isEmpty()) {
            DataUtils.text = DataUtils.captionList.get(0);
            edtCaption.setText(DataUtils.text);
        } else {
            edtCaption.setText("Enter your text here...");
        }

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        try {
            fragmentManager.beginTransaction().replace(R.id.layoutFragment, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (AdShowUtils.reqInterShow(this, 2))
            AdUtils.showInterstitialAd(this, null);
    }

    @Override
    public void lastChildOfImageAdapter(int postion) {
        if (postion == DataUtils.photoList.size() - 1) {
            Log.d("TAGER", "updating list");
            imageFragment.getPhotos();
        }
    }

    @Override
    public void onTextChanged(String text) {
        DataUtils.text = text;
        edtCaption.setText(DataUtils.text);
    }

    @Override
    public void onTextSelected(String text) {
        edtCaption.setText(DataUtils.text);
    }

    @Override
    public void imageSelected(Drawable drawable) {
        imgBackground.setImageDrawable(drawable);
        imageFragment.remove();
        fragment = null;
    }
}
