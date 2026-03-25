package com.zozi.kittyfacts.data.remote.api

import com.zozi.kittyfacts.data.remote.dto.KittyFactDto
import retrofit2.http.GET

interface KittyFactApi {
    @GET("fact")
    suspend fun getRandomFact(): KittyFactDto
}