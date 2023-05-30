package com.androrubin.genesis.chatBot

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface RetrofitAPI {

    @GET
    fun getMessage(@Url url: String?): Call<MsgModal?>?
}
