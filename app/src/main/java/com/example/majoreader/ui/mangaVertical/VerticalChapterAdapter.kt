package com.example.majoreader.ui.mangaVertical


import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.provider.Settings.Global

import com.example.majoreader.MainActivity
import com.example.majoreader.ImageData
import android.widget.TableLayout
import androidx.recyclerview.widget.RecyclerView

import android.view.ViewGroup
import android.view.LayoutInflater
import android.util.DisplayMetrics
import android.util.Log
import android.view.View

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders

import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.Headers
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.majoreader.R


class VerticalChapterAdapter(
    private val mainActivity: MainActivity,
    var imageList: ImageData,
    var buttons: TableLayout,
) : RecyclerView.Adapter<VerticalChapterAdapter.ListItemHolder>() {

    private val handler = Handler(Looper.getMainLooper())
    inner class ListItemHolder(view: View) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        var imageView: ImageView
        var overidedSizes = false
        override fun onClick(p0: View) {
            if (buttons.visibility == View.VISIBLE) {
                buttons.visibility = View.INVISIBLE
            } else {
                buttons.visibility = View.VISIBLE
            }
        }

        init {
            imageView = view.findViewById<View>(R.id.imageContainer) as ImageView
            view.isClickable = true
            view.setOnClickListener(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.image_container, parent, false)
        return ListItemHolder( itemView)
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        Log.i("IS VIEW EXISTENT?" , (holder.imageView!=null).toString())
        var data: String? = imageList.images[position]
        val displayMetrics = DisplayMetrics()
        val windowManager = mainActivity.windowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        Log.i("REFERER",imageList.referer)
        holder.setIsRecyclable(false)
        Glide.with(holder.imageView.context)
            .asBitmap()
            .load(GlideUrl(data, LazyHeaders.Builder().addHeader("referer", imageList.referer).build() as Headers))
            .placeholder(R.drawable.loading).dontAnimate()
            .diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).centerInside()
            .listener(object : RequestListener<Bitmap> {
                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
//
                        holder.imageView.scaleType= ImageView.ScaleType.FIT_START
                        val aspectRatio = resource!!.width.toFloat() / resource.height.toFloat()
                        val width = holder.imageView.context.resources.displayMetrics.widthPixels
                        holder.imageView.layoutParams.width = width
                        holder.imageView.layoutParams.height = (width / aspectRatio).toInt()


//                    handler.post{
//                            Glide.with(holder.imageView.context).clear(holder.imageView)
//                            Glide.with(holder.imageView.context).load(resource).into(holder.imageView)
//                        }
//                    }


                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return imageList.images.size
    }

    fun pixelToDp(pixel: Int): Int {
        val metrics: DisplayMetrics = mainActivity.resources.displayMetrics
        return (pixel.toFloat() / metrics.density) as Int
    }
}