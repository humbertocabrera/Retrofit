package edu.iest.retrofit.network

import edu.iest.retrofit.models.ImagenRandom
import edu.iest.retrofit.models.ListaBreed
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("breeds/image/random")
    fun imagenAleatoria() : Call<ImagenRandom>

    @GET("breeds/{raza}/images")
    fun listaImagenesDePerrosPorRaza (@Path("raza") raza : String) : Call<ListaBreed>
    //Si raza = "chihuahua" url seria breed/chihuahua/images
    // Si raza = "pastor" url seria breed/pastor/images
}