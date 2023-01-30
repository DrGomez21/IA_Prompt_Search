package com.diegogomez.iaimagesearch

import android.app.DownloadManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.diegogomez.iaimagesearch.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter : LexicartAdapter
    private val iaImageCretaed = mutableListOf<LexicartData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.svImage.setOnQueryTextListener(this)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = LexicartAdapter(
            information = iaImageCretaed,
            onClickListener = { lexicartData -> onCopiedPressed(lexicartData) },
            onClickDownloader = { lexicartData -> downloadImage(lexicartData) }
        )
        binding.rvImages.layoutManager = LinearLayoutManager(this)
        binding.rvImages.adapter = adapter
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://lexica.art/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun searchByWord(query:String) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java).getImageFromLexicart("search?q=$query")
            val lexicart = call.body()

            // Accedemos al hilo principal.
            runOnUiThread {
                if (call.isSuccessful) {
                    // Log.d("GODIE", lexicart.toString())
                    val imagenes = lexicart?.lexicarData ?: emptyList()
                    iaImageCretaed.clear()
                    iaImageCretaed.addAll(imagenes)
                    adapter.notifyDataSetChanged()
                } else {
                    showError()
                }
                hideKeyboard()
            }
        }
    }

    private fun showError() {
        Toast.makeText(this, "Ocurrió un error :(", Toast.LENGTH_SHORT).show()
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.viewRoot.windowToken, 0)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) {
            searchByWord(query.lowercase())
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun onCopiedPressed(lexicart: LexicartData) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip : ClipData = ClipData.newPlainText("Copiado", lexicart.prompt)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Copiado al portapapeles", Toast.LENGTH_SHORT).show()
    }

    private fun downloadImage(lexicart: LexicartData) {
        val url = lexicart.src
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("File")
            .setDescription("Descargando archivo")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DCIM, System.currentTimeMillis().toString())
        val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)
    }

}