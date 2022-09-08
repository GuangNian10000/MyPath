package com.my.path.app.util

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import jp.wasabeef.glide.transformations.BlurTransformation

object ImageUtil {

    /**
     * 高斯模糊
     * */
    fun Context.loadGaussianImage(url: String, imageView: ImageView) {
        
        
        //加载背景，
        Glide.with(this)
            .load(url)
            .skipMemoryCache(true)//跳过内存缓存
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(14, 80)))
            .into(imageView)
    }

    fun Context.loadGaussianImage(url: Int, imageView: ImageView) {
        //加载背景，
        Glide.with(this)
            .load(url)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(14, 80)))
            .into(imageView)
    }

    fun Context.loadGardenImage(url: String, imageView: ImageView) {
        Glide.with(this)
            .asBitmap()
          //  .placeholder(R.drawable.ic_user_null)
            .load(url)
            .skipMemoryCache(true)//跳过内存缓存
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .centerCrop()
            .into(object : BitmapImageViewTarget(imageView) {
                override fun setResource(resource: Bitmap?) {
                    val circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(resources, resource)
                    circularBitmapDrawable.isCircular = true
                    imageView.setImageDrawable(circularBitmapDrawable)
                }
            })
    }


    fun Context.loadPicImage(url: String, imageView: ImageView) {
        Glide.with(this)
            .load(url)
            .into(imageView)
    }

    /**
     * 加载圆角图片
     * @param context
     * @param url
     * @param imageView
     * @param radius 圆角大小
     */
    fun Context.loadRoundImage(url: String, imageView: ImageView, radius: Int) {
        val requestOptions = RequestOptions()
            .priority(Priority.HIGH)
            .dontAnimate()
            .skipMemoryCache(true)//跳过内存缓存
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .transforms(CenterCrop(), RoundedCorners(radius))
        Glide.with(this)
            .load(url)
            .apply(requestOptions)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    fun Context.loadRoundImage(url: Int, imageView: ImageView, radius: Int) {
        val requestOptions = RequestOptions()
            .priority(Priority.HIGH)
            .dontAnimate()
            .skipMemoryCache(true)//跳过内存缓存
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .transforms(CenterCrop(), RoundedCorners(radius))
        Glide.with(this)
            .load(url)
            .apply(requestOptions)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }
}