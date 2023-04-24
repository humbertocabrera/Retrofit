package edu.iest.retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.iest.retrofit.adapters.DogsAdapter
import edu.iest.retrofit.models.ImagenRandom
import edu.iest.retrofit.models.ListaBreed
import edu.iest.retrofit.models.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var spinner : Spinner
    private lateinit var tvUrl : TextView
    private val URL_KEY = "url"

    private var url : String = ""
    private var urlPreferences : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvUrl = findViewById(R.id.tvUrl)
        spinner = findViewById(R.id.spinner)

        val ivPicasso = findViewById<ImageView>(R.id.ivPicasso)

        val arrayList = ArrayList<String>()
        arrayList.add("Samoyed")
        arrayList.add("Affenpinscher")
        arrayList.add("Briard")

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList)

        spinner.setAdapter(arrayAdapter);

        spinner.onItemSelectedListener = this

        val apiCall = API().crearServicioAPI()

        apiCall.imagenAleatoria().enqueue(object : Callback<ImagenRandom> {
            override fun onResponse(call: Call<ImagenRandom>, response: Response<ImagenRandom>) {
                Picasso.get().load(response.body()?.message).into(ivPicasso)
                Log.d("API_PRUEBA", "status es " + response.body()?.status)
                Log.d("API_PRUEBA ", "message es " + response.body()?.message)

                url = response.body()?.message!!
                Picasso.get().load(url).into(ivPicasso)
                tvUrl.text = url

                val sharedPreferences  = getSharedPreferences("PERSISTENCIA", MODE_PRIVATE)
                val preferencesEditor = sharedPreferences.edit()
                preferencesEditor.putString(URL_KEY,url)
                preferencesEditor.apply()

            }

            override fun onFailure(call: Call<ImagenRandom>, t: Throwable) {
                val sharedPreferences  = getSharedPreferences("PERSISTENCIA", MODE_PRIVATE)

                urlPreferences = sharedPreferences.getString(URL_KEY,"").toString()
                Log.d("urlpref", urlPreferences)


                Picasso.get().load(urlPreferences).into(ivPicasso)
                Toast.makeText(
                    this@MainActivity,
                    "No fue posible conectar a API",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        spinner = findViewById<Spinner>(R.id.spinner)

        if(item.itemId == R.id.option_menu_list_images){
            Toast.makeText(this, "OPTION menu 1", Toast.LENGTH_SHORT).show()
            val apiCall = API().crearServicioAPI()
            apiCall.listaImagenesDePerrosPorRaza("hound").enqueue(object: Callback<ListaBreed>{
                override fun onResponse(call: Call<ListaBreed>, response: Response<ListaBreed>) {
                    //nuestra logica
                    val dogs = response.body()?.message///array
                    Log.d("PRUEBAS", "Status de la respuesta ${response.body()?.status}")
                    if (dogs != null){
                        for (dog in dogs){
                            Log.d("PRUEBAS", "Perro es $dog")
                        }
                    }
                }

                override fun onFailure(call: Call<ListaBreed>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "No fue posible conectar a API",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?) : Boolean {
        menuInflater.inflate(R.menu.menu_images, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val raza: String = parent?.getItemAtPosition(position).toString()
        Log.d("raza", raza)
        // creamos servicios de API
        val apiCall = API().crearServicioAPI()

        apiCall.listaImagenesDePerrosPorRaza(raza.lowercase()).enqueue(object : Callback<ListaBreed>{
            override fun onResponse(call: Call<ListaBreed>, response: Response<ListaBreed>) {
                // guardamos la lista de links en la variable dogs
                val dogs = response.body()?.message
                Log.d("PRUEBAS", "Status de la respuesta ${response.body()?.status}")

                // si la respuesta no fue null entonces
                if(dogs!=null) {
                    // usamos un linearLayout para guardar nuestras imagenes
                    val linearLayoutManager = LinearLayoutManager(
                        this@MainActivity,
                        LinearLayoutManager.HORIZONTAL, false
                    )
                    // idenficamos nuestro recycler en la vista
                    val recycler = findViewById<RecyclerView>(R.id.recyclerDogs)

                    // le agregamos un LinearLayout al recycler
                    recycler.layoutManager = linearLayoutManager

                    // usamos como adaptador para el recycler nuestra clase DogsAdapter y le pasamos los links guardados en la variable Dogs y el contexto
                    recycler.adapter = DogsAdapter(dogs,this@MainActivity)
                }
            }

            override fun onFailure(call: Call<ListaBreed>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("PREFERENCIAS ON SAVE", "onSaveInstanceState")
        outState.putString(URL_KEY, urlPreferences)
        outState?.run {
            putString(URL_KEY, urlPreferences)
        }
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        if(TextUtils.isEmpty(urlPreferences)){
            val sharedPreferences  = getSharedPreferences("PERSISTENCIA", MODE_PRIVATE)
            urlPreferences = sharedPreferences.getString(URL_KEY,"").toString()
        }
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}