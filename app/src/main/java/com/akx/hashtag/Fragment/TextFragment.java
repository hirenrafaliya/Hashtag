package com.akx.hashtag.Fragment;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.akx.hashtag.CaptionEditorActivity;
import com.akx.hashtag.HashPhotoActivity;
import com.akx.hashtag.Model.Quote;
import com.akx.hashtag.Network.GetQuoteService;
import com.akx.hashtag.Network.RetrofitClientInstance;
import com.akx.hashtag.R;
import com.akx.hashtag.Utils.DataUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TextFragment extends Fragment {

    private View view;

    private EditText edtText;
    private ImageView imgRefresh;
    EditText edtCaption;
    private RelativeLayout layoutRefresh,layoutCollection;
    private TextListener textListener;
    private RelativeLayout layoutParentText;

    public TextFragment(EditText edtCaption){
        this.edtCaption=edtCaption;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_text, container, false);

        initializeViews();


        setEdtCaption();


        layoutRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    refreshCaption();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        layoutCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    edtText.setText("");
                    CaptionEditorActivity.fragment=new TextCollectionFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layoutFragment,CaptionEditorActivity.fragment).commit();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


        return view;
    }

    private void initializeViews(){
        edtText=view.findViewById(R.id.edtCaption);
        layoutRefresh=view.findViewById(R.id.layoutRefresh);
        layoutParentText=view.findViewById(R.id.layoutParentText);
        imgRefresh=view.findViewById(R.id.imgRefresh);
        layoutCollection=view.findViewById(R.id.layoutCollection);
        if(getContext() instanceof TextListener){
            textListener=(TextListener)getContext();
        }



//        edtText.setText(DataUtils.text);


        Animation animation= AnimationUtils.loadAnimation(getActivity(),R.anim.anim_editor_translate);
        layoutParentText.startAnimation(animation);
    }

    @Override
    public void onResume() {
        super.onResume();
        setEdtCaption();
    }


    private void setEdtCaption(){
        edtText.setText(DataUtils.text);
        edtText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("TAGER", "afterTextChanged:");
                if(editable.toString().length()!=0){
                    textListener.onTextChanged(editable.toString());
                }
            }
        });
    }

    private void refreshCaption(){
        layoutRefresh.setEnabled(false);
        Animation animInfiniteRotate=AnimationUtils.loadAnimation(getActivity(), R.anim.anim_rotate_infinite);
        imgRefresh.startAnimation(animInfiniteRotate);

        GetQuoteService quoteService= RetrofitClientInstance.getQuotesInstance().create(GetQuoteService.class);

        Call<Quote> call=quoteService.getQuotes("en");

        call.enqueue(new Callback<Quote>() {
            @Override
            public void onResponse(Call<Quote> call, Response<Quote> response) {
                if(response.isSuccessful()){
                    Quote quote=response.body();
                    DataUtils.text=quote.getQuote();
                    edtText.setText(DataUtils.text);
                    textListener.onTextChanged(DataUtils.text);
                    layoutRefresh.setEnabled(true);
                    imgRefresh.clearAnimation();
                }
                else {
                    layoutRefresh.setEnabled(true);
                    imgRefresh.clearAnimation();
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Quote> call, Throwable t) {
                layoutRefresh.setEnabled(true);
                imgRefresh.clearAnimation();
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface TextListener{
        void onTextChanged(String text);
    }

    public void remove(){
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_editor_translate_reverse);
        layoutParentText.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(TextFragment.this).commit();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
