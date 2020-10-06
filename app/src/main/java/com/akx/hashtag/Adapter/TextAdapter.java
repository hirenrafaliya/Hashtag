package com.akx.hashtag.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akx.hashtag.Network.Utils;
import com.akx.hashtag.R;
import com.akx.hashtag.Utils.DataUtils;

import java.util.List;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

@Keep
public class TextAdapter extends RecyclerView.Adapter<TextAdapter.ViewHolder> {

    Context context;
    List<String> captionList;
    Animation animation;
    ViewHolder currentHolder;
    TextAdapterListener listener;

    public TextAdapter(Context context, List<String> captionList) {
        this.context = context;
        this.captionList = captionList;

        if(context instanceof TextAdapterListener){
            listener=(TextAdapterListener)context;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtCaption.setText(captionList.get(position));

        if(DataUtils.text.equals(captionList.get(position))){
            currentHolder=holder;
            holder.txtCaption.setTextColor(Color.WHITE);
            holder.layoutParentText.setBackground(context.getResources().getDrawable(R.drawable.bg_gradient_secondary));
        }

        animation=AnimationUtils.loadAnimation(context, R.anim.anim_item_fade);
        holder.cardParent.startAnimation(animation);

        holder.cardParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataUtils.text=captionList.get(position);
                listener.onTextSelected(DataUtils.text);
                if(currentHolder!=null){
                    currentHolder.txtCaption.setTextColor(context.getResources().getColor(R.color.colorPrimaryBlack));
                    currentHolder.layoutParentText.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryWhite));
                }
                holder.txtCaption.setTextColor(Color.WHITE);
                holder.layoutParentText.setBackground(context.getResources().getDrawable(R.drawable.bg_gradient_secondary));
                currentHolder=holder;
            }
        });
    }

    @Override
    public int getItemCount() {
        return captionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtCaption;
        RelativeLayout layoutParentText;
        CardView cardParent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCaption=itemView.findViewById(R.id.txtCaption);
            cardParent=itemView.findViewById(R.id.cardParent);
            layoutParentText=itemView.findViewById(R.id.layoutParentText);

        }
    }

    public void updateCaptionList(List<String> captionList){
        Log.d("TAGER", "updateCptionList called");
        this.captionList.addAll(captionList);
        notifyItemRangeInserted(this.captionList.size()-captionList.size(),this.captionList.size());
//        notifyDataSetChanged();
    }

    public interface TextAdapterListener{
        void onTextSelected(String text);
    }
}
