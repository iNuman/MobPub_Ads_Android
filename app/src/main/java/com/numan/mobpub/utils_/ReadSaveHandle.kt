package com.numan.mobpub.utils_

import android.content.Context
import android.widget.Toast

class ReadSaveHandle internal constructor(var context: Context) {
    var myConfigFileName = "yourConfigFileName"
    fun saveAdCount(totalAdsCount: String?) {
        val sharedPreferences = context.getSharedPreferences(myConfigFileName, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(MyConstants.keyForAdCounter, totalAdsCount)
        //editor.putString("Count", count);
        editor.commit()
    }

    fun saveKeyValue(key: String?, value: String?) {
        val sharedPreferences = context.getSharedPreferences(myConfigFileName, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        //editor.putString("Count", count);
        editor.commit()
    }

    fun getKeyValue(key: String?): String? {
        val sharedPreferences = context.getSharedPreferences(myConfigFileName, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, "")
    }

    fun showToastMessage(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}