package edu.iest.retrofit

import android.database.MergeCursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import edu.iest.retrofit.models.ImagenRandom
import edu.iest.retrofit.network.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiCall = API().crearServicioAPI()
        val ivPicasso = findViewById<ImageView>(R.id.imagenApi)

        apiCall.imagenAleatoria().enqueue(object : Callback<ImagenRandom> {
            override fun onResponse(call: Call<ImagenRandom>, response: Response<ImagenRandom>) {
                Picasso.get().load(response.body()?.message).into(ivPicasso);
                Log.d("API_PRUEBA", "status es " + response.body()?.status)
                Log.d("API_PRUEBA ", "message es " + response.body()?.message)
//        Aqui hacer lo que necesitemos con los datos
            }

            override fun onFailure(call: Call<ImagenRandom>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "No fue posible conectar a API",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.option_menu_list_images){
            Toast.makeText(this, "OPTION menu 1", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?) : Boolean {
        menuInflater.inflate(R.menu.menu_images, menu)
        return super.onCreateOptionsMenu(menu)
    }
}