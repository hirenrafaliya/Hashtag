package com.akx.hashtag.Adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.akx.hashtag.Model.Photo;
import com.akx.hashtag.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.Keep;

@Keep
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    ImageAdapterListener listener;

    Context context;
    List<Photo> photoList;

    public ImageAdapter(Context context, List<Photo> photoList) {
        this.context = context;
        this.photoList=photoList;
        if(context instanceof ImageAdapterListener){
            listener=(ImageAdapterListener)context;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try{
            listener.lastChildOfImageAdapter(position);
        }catch (Exception e)
        {e.printStackTrace();}


        Glide.with(context)
                .load(photoList.get(position).getLargeImageURL())
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        ValueAnimator animator=ValueAnimator.ofInt(holder.cardParentImage.getMeasuredHeight(),resource.getIntrinsicHeight());
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                int val=(int)valueAnimator.getAnimatedValue();
                                ViewGroup.LayoutParams layoutParams=holder.cardParentImage.getLayoutParams();
                                layoutParams.height=val;
                                holder.cardParentImage.setLayoutParams(layoutParams);
                            }
                        });
                        animator.setDuration(400).start();
                        holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                listener.imageSelected(resource);
                            }
                        });
                        return false;
                    }
                })
                .into(holder.imgPhoto);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgPhoto;
        CardView cardParentImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto=itemView.findViewById(R.id.imgPhoto);
            cardParentImage=itemView.findViewById(R.id.cardImageParent);
        }
    }

    public void updatePhotoList(List<Photo> photoList){
        int size=this.photoList.size();
        this.photoList=null;
        this.photoList=photoList;
        notifyItemRangeInserted(this.photoList.size()-size, this.photoList.size());
    }

    public interface ImageAdapterListener{
        void lastChildOfImageAdapter(int postion);
        void imageSelected(Drawable drawable);
    }

}
