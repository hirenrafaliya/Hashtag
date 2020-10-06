package com.akx.hashtag.Network;

import com.akx.hashtag.Model.Quote;

import androidx.annotation.Keep;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

@Keep
public interface GetQuoteService {

    @Headers({
            "x-rapidapi-host:quotes15.p.rapidapi.com",
            "x-rapidapi-key:d4b9b5f2a0msh949eb19d98bb788p102208jsnee015fbb5022"
    })
    @GET("quotes/random/")
    Call<Quote> getQuotes(@Query("language_code") String lang);

}
