package com.androrubin.genesis.Remote

import com.androrubin.genesis.Model.MyPlaces
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface GoogleAPIService {

    @GET
    fun getNearbyPlaces(@Url url:String):Call<MyPlaces>
}