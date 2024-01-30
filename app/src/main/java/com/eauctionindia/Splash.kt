package com.eauctionindia

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.gms.ads.identifier.AdvertisingIdClient

class Splash : AppCompatActivity() {

    var myGaid: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

     //   fetchGAID()
        Handler(Looper.getMainLooper()).postDelayed({
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, 3000)

    }

    fun fetchGAID() {
        AsyncTask.execute(Runnable {
            try {
                val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(this)
                myGaid = if (adInfo != null) adInfo.id else null
                Log.i("GAID", myGaid!!)
            } catch (e: Exception) {
                val toast = Toast.makeText(this, "error occurred ", Toast.LENGTH_SHORT)
                toast.setGravity(android.R.attr.gravity, 0, 0)
                toast.show()
            }
        })
    }
}