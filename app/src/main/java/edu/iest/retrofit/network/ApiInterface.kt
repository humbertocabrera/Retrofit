package edu.iest.retrofit.network

import edu.iest.retrofit.models.ImagenRandom
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("breeds/image/random")
    fun imagenAleatoria() : Call<ImagenRandom>
}