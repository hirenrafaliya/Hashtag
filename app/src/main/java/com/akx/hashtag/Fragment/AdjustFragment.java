package com.akx.hashtag.Fragment;


import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.akx.hashtag.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdjustFragment extends Fragment {

    View view;

    SeekBar seekBarTextSize,seekBrightness;
    EditText edtCaption;
    RelativeLayout layoutParentAdjust;

    public AdjustFragment(EditText edtCaption){
        this.edtCaption=edtCaption;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_adjust, container, false);

        initializeViews();


        try {
            setSeekBarTextSize();
            setSeekBrightness();
        }
        catch (Exception e){
            e.printStackTrace();
        }


        return view;
    }

    private void initializeViews(){
        seekBarTextSize=view.findViewById(R.id.seekTextSize);
        layoutParentAdjust=view.findViewById(R.id.layoutParentAdjust);
        seekBrightness=view.findViewById(R.id.seekBrightness);

        Animation animation= AnimationUtils.loadAnimation(getContext(), R.anim.anim_editor_translate);
        layoutParentAdjust.startAnimation(animation);
    }

    private void setSeekBarTextSize(){
        seekBarTextSize.setProgress((int)edtCaption.getTextSize()/3);
        seekBarTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                edtCaption.setTextSize(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    int color;
    private void setSeekBrightness(){
        ColorDrawable colorDrawable=(ColorDrawable)edtCaption.getBackground();
        color=colorDrawable.getColor();

        seekBrightness.setProgress(colorDrawable.getAlpha());
        seekBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                color=ColorUtils.setAlphaComponent(color,i);
                edtCaption.setBackgroundColor(color);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    public void remove() {
        Animation animation= AnimationUtils.loadAnimation(getContext(), R.anim.anim_editor_translate_reverse);
        layoutParentAdjust.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(AdjustFragment.this).commit();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

}
