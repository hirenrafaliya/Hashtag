package com.akx.hashtag.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akx.hashtag.Model.Hashtag;
import com.akx.hashtag.Network.Utils;
import com.akx.hashtag.R;
import com.akx.hashtag.Utils.AdShowUtils;
import com.akx.hashtag.Utils.AdUtils;

import java.util.List;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

@Keep
public class HashtagAdapter extends RecyclerView.Adapter<HashtagAdapter.ViewHolder> {

    Context context;
    List<Hashtag> hashtagList;




    public HashtagAdapter(Context context, List<Hashtag> hashtagList) {
        this.context = context;
        this.hashtagList = hashtagList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hashtag, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtTitle.setText(hashtagList.get(position).getTitle());
        holder.txtTags.setText(hashtagList.get(position).getTags());

        Animation animation= AnimationUtils.loadAnimation(context, R.anim.anim_item_fade);
        holder.cardParent.startAnimation(animation);

    }

    @Override
    public int getItemCount() {
        return hashtagList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle,txtTags;
        CardView cardParent;
        RelativeLayout layoutShare,layoutCopy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTags=itemView.findViewById(R.id.txtTags);
            txtTitle=itemView.findViewById(R.id.txtTitle);
            cardParent=itemView.findViewById(R.id.cardParent);
            layoutCopy=itemView.findViewById(R.id.layoutCopy);
            layoutShare=itemView.findViewById(R.id.layoutShare);

            layoutCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animation animation= AnimationUtils.loadAnimation(context, R.anim.anim_item_click);
                    layoutCopy.startAnimation(animation);
                    Utils.copyToClipboard(hashtagList.get(getAdapterPosition()).getTags(),context);

                    if (AdShowUtils.reqInterShow(context, 2))
                        AdUtils.showInterstitialAd(context, null);
                }
            });

            layoutShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animation animation= AnimationUtils.loadAnimation(context, R.anim.anim_item_click);
                    layoutShare.startAnimation(animation);
                    Utils.shareText(hashtagList.get(getAdapterPosition()).getTags(),context,"Caption");

                    if (AdShowUtils.reqInterShow(context, 2))
                        AdUtils.showInterstitialAd(context, null);
                }
            });
        }
    }
}
