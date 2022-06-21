package com.nasp.countriesapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

object BitmapUtils {

    fun bitmapAssets(context: Context, file: String): Bitmap {
        val inputStream = context.assets.open(file)

        return BitmapFactory.decodeStream(inputStream)
    }
}