package com.akx.hashtag;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.ScriptC;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.akx.hashtag.Network.Utils;
import com.akx.hashtag.Utils.AdShowUtils;
import com.akx.hashtag.Utils.AdUtils;
import com.akx.hashtag.Utils.DataUtils;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaveCaptionImageActivity extends AppCompatActivity {

    ImageView imgDisplay,imgBack,imgHome;
    RelativeLayout layoutSave,layoutShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_caption_image);


        if (AdShowUtils.reqInterShow(this, 3))
            AdUtils.showInterstitialAd(this, null);

        initializeViews();

        layoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TedPermission.with(SaveCaptionImageActivity.this)
                        .setPermissionListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                sendBitmap(DataUtils.bitmapImg);
                            }

                            @Override
                            public void onPermissionDenied(List<String> deniedPermissions) {
                                Toast.makeText(SaveCaptionImageActivity.this, "Can't save the Image", Toast.LENGTH_SHORT).show();

                            }
                        }).setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();
            }
        });

        layoutSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TedPermission.with(SaveCaptionImageActivity.this)
                        .setPermissionListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                if (AdShowUtils.reqInterShow(SaveCaptionImageActivity.this, 2))
                                    AdUtils.showInterstitialAd(SaveCaptionImageActivity.this, null);
                                saveBitmap(DataUtils.bitmapImg);
                            }

                            @Override
                            public void onPermissionDenied(List<String> deniedPermissions) {
                                Toast.makeText(SaveCaptionImageActivity.this, "Can't save the Image", Toast.LENGTH_SHORT).show();

                            }
                        }).setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();

            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SaveCaptionImageActivity.this,MainActivity.class);
                startActivity(intent);
                finish();

                if (AdShowUtils.reqInterShow(SaveCaptionImageActivity.this, 2))
                    AdUtils.showInterstitialAd(SaveCaptionImageActivity.this, null);
            }
        });

    }

    private void initializeViews() {
        imgDisplay=findViewById(R.id.imgDisplay);
        layoutSave=findViewById(R.id.layoutSave);
        layoutShare=findViewById(R.id.layoutShare);
        imgBack=findViewById(R.id.imgBack);
        imgHome=findViewById(R.id.imgHome);

        imgDisplay.setImageBitmap(DataUtils.bitmapImg);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void saveBitmap(Bitmap bitmap) {

        Toast.makeText(this, "Saving....", Toast.LENGTH_SHORT).show();
        try {
            File file, f = null;

            file = new File(Environment.getExternalStorageDirectory() + "/Hash");
            if (!file.exists()) {
                file.mkdirs();
            }

            String str = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
            str = "hash_" + str + ".png";
            f = new File(file.getAbsolutePath() + "/" + str);
            FileOutputStream ostream = new FileOutputStream(f);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            ostream.close();

            MediaScannerConnection.scanFile(SaveCaptionImageActivity.this,
                    new String[]{f.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });

            Toast.makeText(SaveCaptionImageActivity.this, "Saved Successfully", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(SaveCaptionImageActivity.this, "Something went wrong \n "+e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void sendBitmap(Bitmap bitmap) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/png");
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "title");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);
        OutputStream outstream;
        try {
            outstream = getContentResolver().openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outstream);
            outstream.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share Image"));

        if (AdShowUtils.reqInterShow(SaveCaptionImageActivity.this, 1))
            AdUtils.showInterstitialAd(SaveCaptionImageActivity.this, null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (AdShowUtils.reqInterShow(SaveCaptionImageActivity.this, 2))
            AdUtils.showInterstitialAd(SaveCaptionImageActivity.this, null);
    }
}
