package com.numan.mobpub.utils_

import android.content.Context
import android.content.SharedPreferences

//SharedPreferences manager class
object SharedPrefs {
    const val app_id = "app_id"
    const val banner_ad_unit_id = "banner_ad_unit_id"
    const val inter_ad_unit_id = "inter_ad_unit_id"
    const val native_ad = "native_ad"
    const val fcm_token = "fcm_token"
    const val ITEM_SIZE = "item_size"

    //SharedPreferences file name
    private const val SHARED_PREFS_FILE_NAME = "name_on_cake_shared_prefs"

    //here you can centralize all your shared prefs keys
    const val SHOWFACE = "showface"
    const val BACKGROUND_IMAGE = "background_image"
    const val AD_INDEX = "ad_index"
    const val SPLASH_AD_DATA = "splash_ad_data"
    var COUNT = "count"
    var video_created_or_not = "video_created_or_not"
    var video_path = "video_path"
    const val screen_hight = "screen_height"
    const val screen_width = "screen_height"
    const val IS_ADS_REMOVED = "is_ads_removed"

    //use to get more App current Url
    const val URL_INDEX = "URL_INDEX"
    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE)
    }

    fun clearPrefs(context: Context) {

        //TODO : Set KEY_IS_LOGIN=false if clear pref
//        if (getBoolean(context, KEY_IS_LOGIN))
        getPrefs(context).edit().clear().commit()
    }

    fun contain(context: Context, key: String?): Boolean {
        return getPrefs(context).contains(key)
    }

    //Save Booleans
    fun savePref(context: Context, key: String?, value: Boolean) {
        getPrefs(context).edit().putBoolean(key, value).commit()
    }

    //Get Booleans
    fun getBoolean(context: Context, key: String?): Boolean {
        return getPrefs(context).getBoolean(key, false)
    }

    //Get Booleans if not found return a predefined default value
    fun getBoolean(context: Context, key: String?, defaultValue: Boolean): Boolean {
        return getPrefs(context).getBoolean(key, defaultValue)
    }

    //Strings
    fun save(context: Context, key: String?, value: String?) {
        getPrefs(context).edit().putString(key, value).commit()
    }

    fun getString(context: Context, key: String?): String? {
        return getPrefs(context).getString(key, "")
    }

    fun getString(context: Context, key: String?, defaultValue: String?): String? {
        return getPrefs(context).getString(key, defaultValue)
    }

    //Integers
    fun save(context: Context, key: String?, value: Int) {
        getPrefs(context).edit().putInt(key, value).commit()
    }

    fun getInt(context: Context, key: String?): Int {
        return getPrefs(context).getInt(key, 0)
    }

    fun getInt(context: Context, key: String?, defaultValue: Int): Int {
        return getPrefs(context).getInt(key, defaultValue)
    }

    //Floats
    fun save(context: Context, key: String?, value: Float) {
        getPrefs(context).edit().putFloat(key, value).commit()
    }

    fun getFloat(context: Context, key: String?): Float {
        return getPrefs(context).getFloat(key, 0f)
    }

    fun getFloat(context: Context, key: String?, defaultValue: Float): Float {
        return getPrefs(context).getFloat(key, defaultValue)
    }

    //Longs
    fun save(context: Context, key: String?, value: Long) {
        getPrefs(context).edit().putLong(key, value).commit()
    }

    fun getLong(context: Context, key: String?): Long {
        return getPrefs(context).getLong(key, 0)
    }

    fun getLong(context: Context, key: String?, defaultValue: Long): Long {
        return getPrefs(context).getLong(key, defaultValue)
    }

    fun removeKey(context: Context, key: String?) {
        getPrefs(context).edit().remove(key).commit()
    }
}