package tech.kidzania.banamex.cajero

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.app.Activity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * Contenedor a pantalla completa de la aplicación web "Banamex Cajero".
 * La página está diseñada para un escenario de 1920x1080 (horizontal) y se
 * escala sola dentro del WebView.
 */
class MainActivity : Activity() {

    private lateinit var webView: WebView
    private var showingOffline = false
    private var connectivityManager: ConnectivityManager? = null

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            // Al recuperar la conexión, reintenta automáticamente si estamos en la pantalla offline.
            runOnUiThread { if (showingOffline) loadCajero() }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        webView = WebView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.WHITE)
            overScrollMode = View.OVER_SCROLL_NEVER
            isVerticalScrollBarEnabled = false
            isHorizontalScrollBarEnabled = false
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                cacheMode = WebSettings.LOAD_DEFAULT
                allowFileAccess = false
                allowContentAccess = false
                mediaPlaybackRequiresUserGesture = false
                setSupportZoom(false)
                builtInZoomControls = false
                displayZoomControls = false
            }
            webViewClient = CajeroClient()
        }
        setContentView(webView)

        connectivityManager = getSystemService(ConnectivityManager::class.java)
        connectivityManager?.registerDefaultNetworkCallback(networkCallback)

        if (savedInstanceState == null) {
            loadCajero()
        } else {
            webView.restoreState(savedInstanceState)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemBars()
    }

    private fun hideSystemBars() {
        WindowInsetsControllerCompat(window, window.decorView).apply {
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsetsCompat.Type.systemBars())
        }
    }

    private fun loadCajero() {
        showingOffline = false
        webView.loadUrl(BuildConfig.CAJERO_URL)
    }

    private fun showOffline() {
        showingOffline = true
        webView.loadUrl("file:///android_asset/offline.html")
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Modo kiosco: solo navega hacia atrás dentro de la web; nunca cierra la app por accidente.
        if (webView.canGoBack() && !showingOffline) webView.goBack()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }

    override fun onPause() {
        webView.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onDestroy() {
        connectivityManager?.unregisterNetworkCallback(networkCallback)
        (webView.parent as? ViewGroup)?.removeView(webView)
        webView.destroy()
        super.onDestroy()
    }

    private inner class CajeroClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            val uri = request.url
            if (uri.scheme == "app" && uri.host == "retry") {
                loadCajero()
                return true
            }
            if (isInternal(uri)) return false
            // Enlaces externos se abren en el navegador del sistema.
            if (uri.scheme == "https" || uri.scheme == "http") {
                runCatching { startActivity(Intent(Intent.ACTION_VIEW, uri)) }
            }
            return true
        }

        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            if (request.isForMainFrame && !isOfflinePage(request.url)) showOffline()
        }

        override fun onReceivedHttpError(
            view: WebView,
            request: WebResourceRequest,
            errorResponse: android.webkit.WebResourceResponse
        ) {
            if (request.isForMainFrame && errorResponse.statusCode >= 500) showOffline()
        }

        private fun isInternal(uri: Uri): Boolean =
            uri.scheme == "file" || uri.host == Uri.parse(BuildConfig.CAJERO_URL).host

        private fun isOfflinePage(uri: Uri): Boolean =
            uri.scheme == "file" && uri.path == "/android_asset/offline.html"
    }
}
