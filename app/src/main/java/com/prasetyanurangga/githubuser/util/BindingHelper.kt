package com.prasetyanurangga.githubuser.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) { // This methods should not have any return type, = declaration would make it return that object declaration.
    Picasso.with(view.context).load(url).into(view)
}
