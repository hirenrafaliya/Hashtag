package com.akx.hashtag.Network;

import androidx.annotation.Keep;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Keep
public class RetrofitClientInstance {

    static Retrofit retrofitShayShay;
    static Retrofit retrofitQuotes;
    static Retrofit retrofitPixabay;

    public static Retrofit getShayShayInstance(){
        if(retrofitShayShay==null){
            retrofitShayShay=new retrofit2.Retrofit.Builder()
                    .baseUrl("https://shayshay.p.rapidapi.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofitShayShay;
    }

    public static Retrofit getQuotesInstance(){
        if(retrofitQuotes==null){
            retrofitQuotes=new retrofit2.Retrofit.Builder()
                    .baseUrl("https://quotes15.p.rapidapi.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitQuotes;
    }

    public static Retrofit getPixabayInstance(){
        if(retrofitPixabay==null){
            retrofitPixabay=new retrofit2.Retrofit.Builder()
                    .baseUrl("https://pixabay.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitPixabay;
    }

}
