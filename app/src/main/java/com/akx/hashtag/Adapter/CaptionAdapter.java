package com.akx.hashtag.Adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.akx.hashtag.HashPhotoActivity;
import com.akx.hashtag.Network.Utils;
import com.akx.hashtag.R;
import com.akx.hashtag.Utils.AdShowUtils;
import com.akx.hashtag.Utils.AdUtils;

import java.util.List;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.internal.Util;

@Keep
public class CaptionAdapter extends RecyclerView.Adapter<CaptionAdapter.ViewHolder> {

    Context context;
    List<String> captionList;

    Animation animation;

    public CaptionAdapter(Context context, List<String> captionList) {
        this.context = context;
        this.captionList = captionList;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_caption, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtCaption.setText(captionList.get(position));

        animation=AnimationUtils.loadAnimation(context, R.anim.anim_item_fade);
        holder.cardParent.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return captionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtCaption;
        CardView cardParent;
        RelativeLayout layoutShare,layoutCopy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCaption=itemView.findViewById(R.id.txtCaption);
            cardParent=itemView.findViewById(R.id.cardParent);
            layoutCopy=itemView.findViewById(R.id.layoutCopy);
            layoutShare=itemView.findViewById(R.id.layoutShare);

//            animation=AnimationUtils.loadAnimation(context, R.anim.anim_item_fade);
//            cardParent.startAnimation(animation);

            layoutCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animation animation= AnimationUtils.loadAnimation(context, R.anim.anim_item_click);
                    layoutCopy.startAnimation(animation);
                    Utils.copyToClipboard(captionList.get(getAdapterPosition()),context);

                    if (AdShowUtils.reqInterShow(context, 2))
                        AdUtils.showInterstitialAd(context, null);
                }
            });

            layoutShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animation animation= AnimationUtils.loadAnimation(context, R.anim.anim_item_click);
                    layoutShare.startAnimation(animation);
                    Utils.shareText(captionList.get(getAdapterPosition()),context,"Caption");

                    if (AdShowUtils.reqInterShow(context, 2))
                        AdUtils.showInterstitialAd(context, null);
                }
            });

        }
    }

    public void updateCaptionList(List<String> captionList){
        Log.d("TAGER", "updateCptionList called");
        this.captionList.addAll(captionList);
        notifyItemRangeInserted(this.captionList.size()-captionList.size(),this.captionList.size());
//        notifyDataSetChanged();
    }
}
