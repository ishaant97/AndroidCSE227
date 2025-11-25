package com.example.androidcse227.Unit6

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.androidcse227.R


class WebView2 : AppCompatActivity() {

    private lateinit var webView : WebView
    private lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_web_view2)

        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)


        val settings = webView.settings

        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.allowContentAccess = true
        settings.loadsImagesAutomatically = true

        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()

        webView.loadUrl("https://ums.lpu.in/lpuums/LoginNew.aspx")

        webView.webViewClient=object: WebViewClient(){
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progressBar.visibility= View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar.visibility=View.GONE
                Toast.makeText(this@WebView2, "Page Loaded", Toast.LENGTH_LONG).show()
            }
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?){
                Toast.makeText(this@WebView2, "Error! $error", Toast.LENGTH_LONG).show()
            }
        }

        webView.webChromeClient=object: WebChromeClient(){
            override fun onReceivedTitle(view: WebView?, title: String?) {
                supportActionBar?.title=title
            }
        }

        // MODERN BACK HANDLING
        onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (webView.canGoBack()) {
                        webView.goBack()
                    } else {
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        )

    }
}