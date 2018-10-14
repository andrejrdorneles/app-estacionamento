package br.com.cwi.cwiestacionamento.utils

import android.widget.ImageView
import com.squareup.picasso.Picasso

fun ImageView.loadImage(url: String?) {
    url?.let {
        Picasso.with(context).load(it).into(this)
    }
}