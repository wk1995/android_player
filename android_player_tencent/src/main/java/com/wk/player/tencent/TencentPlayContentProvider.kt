package com.wk.player.tencent

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.tencent.live2.V2TXLiveDef.V2TXLiveLogConfig
import com.tencent.live2.V2TXLivePremier
import com.tencent.rtmp.TXLiveBase
import com.tencent.rtmp.TXLiveBaseListener


class TencentPlayContentProvider : ContentProvider() {

    companion object {
        private const val TAG = "TencentPlayContentProvider"
    }

    override fun onCreate(): Boolean {
        Log.d(TAG, " onCreate ")
        val licenceURL = "xxxx" // 获取到的 licence url
        val licenceKey = "xxxx" // 获取到的 licence key

       /* val liveLogConfig = V2TXLiveLogConfig()
        liveLogConfig.enableConsole = true
        V2TXLivePremier.setLogConfig(liveLogConfig)
        V2TXLivePremier.setLicence(
            context,
            licenceURL,
            licenceKey
        )*/
        TXLiveBase.getInstance().setLicence(context, licenceURL, licenceKey)
        TXLiveBase.setListener(object : TXLiveBaseListener() {
            override fun onLicenceLoaded(result: Int, reason: String) {
                Log.i(TAG, "onLicenceLoaded: result:$result, reason:$reason")
            }
        })

        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }
}