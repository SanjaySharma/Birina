package com.birina.bsecure.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;




/**
 * Created by vikash on 4/9/15.
 */
public class RestClient {

    private APIServices apiServices;
    private final String BASE_URL = "http://birinagroup.com/birina-b-secure/back-office/";


    public RestClient() {

        OkHttpClient.Builder httpClient = null;
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            httpClient = new OkHttpClient.Builder()
                    .addInterceptor(httpLoggingInterceptor);


        OkHttpClient client = httpClient.build();


        Gson gson = new GsonBuilder().setLenient().create();

       /* Gson gson = new GsonBuilder()
                .create();*/



        Retrofit retrofit =   new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        apiServices = retrofit.create(APIServices.class);
    }

    public APIServices getApiService() {
        return apiServices;
    }






}
