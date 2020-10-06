package com.akx.hashtag.Network;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.akx.hashtag.Adapter.CaptionAdapter;
import com.akx.hashtag.Model.Caption;
import com.akx.hashtag.Utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Keep;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@Keep
public class LoadShayShay extends AsyncTask {

    List<String> captionList=new ArrayList<>();
    Context context;
    boolean isOk=false;
    CaptionAdapter captionAdapter;
    ProgressBar progressLoad;

    public LoadShayShay(Context context,CaptionAdapter captionAdapter, ProgressBar progressBar){
        this.context=context;
        this.captionAdapter=captionAdapter;
        this.progressLoad=progressBar;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        Log.d("TAGER", "LoadShayShay doInBackground called");
        GetCaptionService captionService= RetrofitClientInstance.getShayShayInstance().create(GetCaptionService.class);

        Call<Caption> call=captionService.getAllCaption();

        call.enqueue(new Callback<Caption>() {
            @Override
            public void onResponse(Call<Caption> call, Response<Caption> response) {
                Log.d("TAGER", response.code()+"");
                if(response.isSuccessful()){
                    Caption caption=response.body();
                    captionList=caption.getCaptionList();
                    hideProgress();
                    isOk=true;
                    onPostExecute(null);
                    return;
                }
                else {
                    hideProgress();
                    isOk=false;
                    return;
                }
            }

            @Override
            public void onFailure(Call<Caption> call, Throwable t) {
                hideProgress();
                isOk=false;
            }
        });
        return null;
    }

    void hideProgress(){
        Activity activity=(Activity)context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressLoad.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onPostExecute(Object o) {
        Log.d("TAGER", "isOk left");
        if(isOk){
            Log.d("TAGER", "OnPostExecite called");
            DataUtils.captionList.addAll(captionList);
            captionAdapter.notifyItemRangeInserted(DataUtils.captionList.size()-captionList.size(),DataUtils.captionList.size());
        }
    }
}
