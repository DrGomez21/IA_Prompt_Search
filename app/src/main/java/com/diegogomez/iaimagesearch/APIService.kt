package com.diegogomez.iaimagesearch

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {
    @GET
    suspend fun getImageFromLexicart(@Url url:String): Response<PromptResponse>
}