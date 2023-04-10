package com.example.majoreader.ui.animeServers


import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.*
import android.webkit.*
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.majoreader.MainActivity
import com.example.majoreader.R

import im.delight.android.webview.AdvancedWebView

class AnimeWatchFragment : Fragment() {
    private var activity: MainActivity? = null
    private var dataLoad: String? = null
    private var source: String? = null
    private var videoPlayer: WebView? = null

    private inner class ChromeClient : WebChromeClient() {
        private var mCustomView: View? = null
        private var mCustomViewCallback: CustomViewCallback? = null
        private var mOriginalOrientation = 0
        private var mOriginalSystemUiVisibility = 0
        override fun onHideCustomView() {
            val window = activity?.window
            val decorView = window!!.decorView as FrameLayout
            decorView.removeView(mCustomView)
            mCustomView = null
            window.decorView.systemUiVisibility = mOriginalSystemUiVisibility
            activity!!.requestedOrientation = mOriginalOrientation
            val callback = mCustomViewCallback
            callback!!.onCustomViewHidden()
            mCustomViewCallback = null
        }
        override fun onShowCustomView(view: View, callback: CustomViewCallback) {
            if (mCustomView != null) {
                onHideCustomView()
                return
            }
            mCustomView = view
            mOriginalSystemUiVisibility = activity!!.window.decorView.systemUiVisibility
            mOriginalOrientation = activity!!.requestedOrientation
            mCustomViewCallback = callback
            val decorView = activity!!.window.decorView as FrameLayout
            decorView.addView(mCustomView, FrameLayout.LayoutParams(-1, -1))
            activity!!.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }


    private fun pixelToDp(pixel: Int): Int {
        val metrics = resources.displayMetrics
        return (pixel.toFloat() / metrics.density) as Int
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        source = requireArguments().getString("data")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root: View = inflater.inflate(R.layout.servers_player, container, false)
        videoPlayer = root.findViewById(R.id.iframe) as AdvancedWebView

        videoPlayer!!.webChromeClient = ChromeClient()
        val DesktopAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36"
        videoPlayer!!.settings.apply {
            javaScriptEnabled = true
            pluginState = WebSettings.PluginState.ON
            builtInZoomControls = true
            allowFileAccess = true
            loadWithOverviewMode = true
            javaScriptCanOpenWindowsAutomatically = false
            domStorageEnabled = true
            userAgentString = DesktopAgent
            allowUniversalAccessFromFileURLs = true
        }


        videoPlayer?.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                if (savedInstanceState == null) {
                    videoPlayer?.post {
                        val width = if (Build.VERSION.SDK_INT >= 13) {
                            val size = Point()
                            activity?.windowManager?.defaultDisplay?.getSize(size)
                            size.x
                        } else {
                            activity?.windowManager?.defaultDisplay?.width ?: 0
                        }
                        val height = if (Build.VERSION.SDK_INT >= 13) {
                            val size = Point()
                            activity?.windowManager?.defaultDisplay?.getSize(size)
                            size.y
                        } else {
                            activity?.windowManager?.defaultDisplay?.height ?: 0
                        }
                        val params = videoPlayer!!.layoutParams
                        params.width = width
                        params.height = height
                        videoPlayer!!.layoutParams = params
                        dataLoad = "<iframe target=\"_top\" src =\" $source\" width =\" ${pixelToDp(width - 20)}\" height =\" ${pixelToDp(height / 2)}\" allowFullScreen></iframe>"
                        (videoPlayer!! as AdvancedWebView).loadHtml(dataLoad, "text/html", "utf-8")
                    }
                }
            }
        }



        activity = requireActivity() as MainActivity
        activity?.window?.setFlags(1024, 1024)

        return root
    }

    override fun onPause() {
        super.onPause()
        videoPlayer!!.onPause()
    }

    override fun onDestroy() {
        videoPlayer!!.destroy()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        videoPlayer!!.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        videoPlayer!!.saveState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            videoPlayer!!.restoreState(savedInstanceState)
        }
    }

}