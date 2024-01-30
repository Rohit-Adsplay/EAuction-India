package com.eauctionindia

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yodo1.mas.Yodo1Mas
import com.yodo1.mas.banner.Yodo1MasBannerAdSize
import com.yodo1.mas.banner.Yodo1MasBannerAdView
import com.yodo1.mas.error.Yodo1MasError
import com.yodo1.mas.helper.model.Yodo1MasAdBuildConfig
import com.yodo1.mas.interstitial.Yodo1MasInterstitialAd
import com.yodo1.mas.interstitial.Yodo1MasInterstitialAdListener
import com.yodo1.mas.rewardedinterstitial.Yodo1MasRewardedInterstitialAd
import com.yodo1.mas.rewardedinterstitial.Yodo1MasRewardedInterstitialAdListener
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), Yodo1MasInterstitialAdListener,
    Yodo1MasRewardedInterstitialAdListener {

    var url: String = "https://www.auction.drcars.in/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Yodo1Mas.getInstance().setCCPA(true);
        Yodo1Mas.getInstance().setCOPPA(false);
        Yodo1Mas.getInstance().setGDPR(true);

        val configBuilder = Yodo1MasAdBuildConfig.Builder().enableUserPrivacyDialog(true)
        Yodo1Mas.getInstance().setAdBuildConfig(configBuilder.build())

        Yodo1Mas.getInstance().initMas(this, "IvTERPqr9b", object : Yodo1Mas.InitListener {
            override fun onMasInitSuccessful() {
                /*Toast.makeText(
                    this@MainActivity,
                    "[Yodo1 Mas] Successful initialization",
                    Toast.LENGTH_SHORT
                ).show()*/
            }

            override fun onMasInitFailed(error: Yodo1MasError) {
                //  Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })

        Yodo1MasRewardedInterstitialAd.getInstance().setAdListener(this)
        Yodo1MasRewardedInterstitialAd.getInstance().autoDelayIfLoadFail = true
        Yodo1MasInterstitialAd.getInstance().setAdListener(this)
        Yodo1MasInterstitialAd.getInstance().autoDelayIfLoadFail = true

        Yodo1MasRewardedInterstitialAd.getInstance().loadAd(this@MainActivity)
        Yodo1MasInterstitialAd.getInstance().loadAd(this@MainActivity)

        val webView: WebView = findViewById(R.id.webView)

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);

        webView.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                this@MainActivity.setTitle("Loading...")
                this@MainActivity.setProgress(progress * 100)
                if (progress == 100) this@MainActivity.setTitle("E Auctions India")
            }
        })

        webView.loadUrl(url)
        bannerAd()

        Handler(Looper.getMainLooper()).postDelayed({
            interstitialAd()
        }, 60000)

        Handler(Looper.getMainLooper()).postDelayed({
            rewardedAd()
        }, 300000)
    }


    fun bannerAd() {
        val bannerAdView: Yodo1MasBannerAdView = findViewById(R.id.banner)
        bannerAdView.loadAd()
    }

    fun interstitialAd() {
        Log.d("YODOADD", "interstitialAd: ShowHit")
        val isLoaded = Yodo1MasInterstitialAd.getInstance().isLoaded()
        if (isLoaded) Yodo1MasInterstitialAd.getInstance().showAd(this@MainActivity)
    }

    fun rewardedAd() {
        Log.d("YODOADD", "rewardedAd: ShowHit")
        val isLoaded = Yodo1MasRewardedInterstitialAd.getInstance().isLoaded()
        if (isLoaded) Yodo1MasRewardedInterstitialAd.getInstance().showAd(this@MainActivity)
    }

    fun rewardedDelayedAd() {
        Yodo1MasRewardedInterstitialAd.getInstance().loadAd(this@MainActivity)
        Log.d("YODOADD", "rewardedDelayedAd: Called")
        Handler(Looper.getMainLooper()).postDelayed({
            Log.d("YODOADD", "rewardedDelayedAd: ShowHit")
            Yodo1MasRewardedInterstitialAd.getInstance().showAd(this@MainActivity)
        }, 300000)
    }

    fun interstitialDelayedAd() {
        Yodo1MasInterstitialAd.getInstance().loadAd(this@MainActivity)
        Log.d("YODOADD", "interstitiaDelayedlAd: Called")
        Handler(Looper.getMainLooper()).postDelayed({
            Log.d("YODOADD", "interstitiaDelayedlAd: ShowHit")
            Yodo1MasInterstitialAd.getInstance().showAd(this@MainActivity)
        }, 120000)
    }

    //Interstital Handlers
    override fun onInterstitialAdLoaded(ad: Yodo1MasInterstitialAd?) {
        Log.d("MYYODOAD", "onInterstitialAdLoaded: Loaded")
    }

    override fun onInterstitialAdFailedToLoad(ad: Yodo1MasInterstitialAd?, error: Yodo1MasError) {
        Log.d("MYYODOAD", "onInterstitialAdLoaded: Failed Loaded")
        Handler(Looper.getMainLooper()).postDelayed({
            Yodo1MasInterstitialAd.getInstance().loadAd(this@MainActivity)
        }, 15000)
    }

    override fun onInterstitialAdOpened(ad: Yodo1MasInterstitialAd?) {
        Log.d("MYYODOAD", "onInterstitialAdLoaded: Opened")
    }

    override fun onInterstitialAdFailedToOpen(ad: Yodo1MasInterstitialAd?, error: Yodo1MasError) {
        Log.d("MYYODOAD", "onInterstitialAdLoaded: Failed Open")
    }

    override fun onInterstitialAdClosed(ad: Yodo1MasInterstitialAd?) {
        Log.d("MYYODOAD", "onInterstitialAdLoaded: Closed")
        interstitialDelayedAd()
    }


    //Rewarded Handlers
    override fun onRewardedInterstitialAdLoaded(ad: Yodo1MasRewardedInterstitialAd?) {
        Log.d("MYYODOAD", "onRewardedInterstitialAdLoaded: Loaded")
    }

    override fun onRewardedInterstitialAdFailedToLoad(
        ad: Yodo1MasRewardedInterstitialAd?, error: Yodo1MasError
    ) {
        Log.d("MYYODOAD", "onRewardedInterstitialAdLoaded: Failed Load")
        Handler(Looper.getMainLooper()).postDelayed({
            Yodo1MasRewardedInterstitialAd.getInstance().loadAd(this@MainActivity)
        }, 15000)
    }

    override fun onRewardedInterstitialAdOpened(ad: Yodo1MasRewardedInterstitialAd?) {
        Log.d("MYYODOAD", "onRewardedInterstitialAdLoaded: Opened")
    }

    override fun onRewardedInterstitialAdFailedToOpen(
        ad: Yodo1MasRewardedInterstitialAd?, error: Yodo1MasError
    ) {
        Log.d("MYYODOAD", "onRewardedInterstitialAdLoaded: Failed Open")
    }

    override fun onRewardedInterstitialAdClosed(ad: Yodo1MasRewardedInterstitialAd?) {
        Log.d("MYYODOAD", "onRewardedInterstitialAdLoaded: Closed")
        rewardedDelayedAd()
    }

    override fun onRewardedInterstitialAdEarned(ad: Yodo1MasRewardedInterstitialAd?) {
        Log.d("MYYODOAD", "onRewardedInterstitialAdLoaded: Earned Reward")
    }
}