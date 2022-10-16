package com.linkdevelpment.newsfeeds.core.presentation.extentions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import com.linkdevelpment.newsfeeds.R
import es.dmoral.toasty.Toasty

fun Context.showToast(message: String, connectionError: Boolean=false){
    if (connectionError){
        Toasty.error(this, getString(R.string.check_internet_connections), Toast.LENGTH_LONG, true).show()

    }else{
        Toasty.error(this,message, Toast.LENGTH_LONG, true).show()
    }
    Log.d("error",message)
}

fun Context.showGenericAlertDialog(message: String, errorCode: Int=0){
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton("ok"){ dialog, _ ->
            dialog.dismiss()
        }
    }.show()
}