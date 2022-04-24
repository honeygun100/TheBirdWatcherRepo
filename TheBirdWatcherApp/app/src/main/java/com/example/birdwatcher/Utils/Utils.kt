package com.example.birdwatcher.Utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.collection.LruCache
import java.io.ByteArrayOutputStream


object Utils {
    private var mMemoryCache = LruCache<String, Bitmap?>(1024)

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

        val cacheSize = maxMemory / 8

        mMemoryCache = object : LruCache<String, Bitmap?>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount / 1024; }
        }
    }

    fun getBytes(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }

    fun getImage(image: ByteArray?): Bitmap? {
        return BitmapFactory.decodeByteArray(image!!, 0, image.size)
    }


    fun getBitmapFromMemCache(key: String): Bitmap? {

        return mMemoryCache[key]
    }

    fun addBitmapToMemoryCache(key: String, bitmap: Bitmap?) {

        if (getBitmapFromMemCache(key) == null || getBitmapFromMemCache(key) != null) {
            mMemoryCache.remove(key)
            mMemoryCache.put(key, bitmap!!)
        }
    }

}
