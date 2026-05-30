package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
  private val requestPermissionLauncher = registerForActivityResult(
    ActivityResultContracts.RequestPermission()
  ) { isGranted: Boolean ->
    // Handle permission outcome if needed
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        != PackageManager.PERMISSION_GRANTED) {
      requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    setContent {
      Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        GrammarQuestWebView(
          modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        )
      }
    }
  }
}

@Composable
fun GrammarQuestWebView(modifier: Modifier = Modifier) {
  AndroidView(
    modifier = modifier,
    factory = { context ->
      WebView(context).apply {
        settings.run {
          javaScriptEnabled = true
          domStorageEnabled = true
          databaseEnabled = true
          mediaPlaybackRequiresUserGesture = false
          allowFileAccess = true
        }
        webViewClient = WebViewClient()
        webChromeClient = object : WebChromeClient() {
          override fun onPermissionRequest(request: PermissionRequest?) {
            try {
              request?.grant(request.resources)
            } catch (e: Exception) {
              e.printStackTrace()
            }
          }
        }
        loadUrl("file:///android_asset/index.html")
      }
    }
  )
}
