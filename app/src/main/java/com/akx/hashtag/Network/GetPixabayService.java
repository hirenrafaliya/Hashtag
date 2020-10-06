package com.akx.hashtag.Network;


import com.akx.hashtag.Model.Pixabay;

import androidx.annotation.Keep;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

@Keep
public interface GetPixabayService {

    String API_KEY="6587334-0f355ad7dc985e0e3a314a686";

    @GET("api/")
    Call<Pixabay> getAllPhotos(@Query("key") String key,@Query("page") int page,@Query("category") String category);

}
