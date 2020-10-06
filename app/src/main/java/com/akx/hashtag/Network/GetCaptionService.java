package com.akx.hashtag.Network;

import com.akx.hashtag.Model.Caption;

import androidx.annotation.Keep;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

@Keep
public interface GetCaptionService {

    @Headers({
            "x-rapidapi-host:shayshay.p.rapidapi.com",
            "x-rapidapi-key:d4b9b5f2a0msh949eb19d98bb788p102208jsnee015fbb5022"
    })
    @GET("random?limit=20")
    Call<Caption> getAllCaption();
}
