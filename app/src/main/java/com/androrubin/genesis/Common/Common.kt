package com.androrubin.genesis.Common

import com.androrubin.genesis.Remote.GoogleAPIService
import com.androrubin.genesis.Remote.RetrofitClient

object Common {

    private val GOOGLE_API_URL ="https://maps.googleapis.com/"
    val googleAPIService:GoogleAPIService
    get()=RetrofitClient.getClient(GOOGLE_API_URL).create(GoogleAPIService::class.java)
}