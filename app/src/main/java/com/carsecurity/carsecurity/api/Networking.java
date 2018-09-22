package com.carsecurity.carsecurity.api;

import com.carsecurity.carsecurity.model.DataModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Networking {

    String BASE_URL = "https://api.thingspeak.com/channels/510399/";

    @GET("feeds.json?api_key=E7XHVIY0U4XNI65T&results=2/")
    Call<DataModel> getlocation();
}
