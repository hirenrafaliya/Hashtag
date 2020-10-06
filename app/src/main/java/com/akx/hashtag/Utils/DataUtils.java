package com.akx.hashtag.Utils;

import android.graphics.Bitmap;
import android.widget.EditText;

import com.akx.hashtag.Model.Caption;
import com.akx.hashtag.Model.Photo;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Keep;

@Keep
public class DataUtils {

    public static List<String> captionList=new ArrayList<>();

    public static List<Photo> photoList=new ArrayList<>();

    public static Bitmap bitmapImg;

    //edtCaption string
    public static String text;

}
