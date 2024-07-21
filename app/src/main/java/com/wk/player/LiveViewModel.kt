package com.wk.player

import android.os.Bundle
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wk.player.api.IPageController
import com.wk.player.log.LogUtil
import rx.subscriptions.CompositeSubscription
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class LiveViewModel : ViewModel(), IPageController{

    companion object {
        const val OPERATE_BACK = "back"
        const val OPERATE_CLOSE_LIVE = "closeLive"
        const val OPERATE_FULL_SCREEN = "showFullScreen"
        const val OPERATE_SHOW_SPEECH_CHOICES = "showSpeechChoices"
        const val OPERATE_SHOW_JOIN_GROUP_FAIL = "showJoinGroupFail"
        const val OPERATE_PAUSE_LIVE = "pauseLive"
        const val OPERATE_SHARE_LIVE = "shareLive"
        const val OPERATE_SHARE_BITMAP_LIVE = "shareBitmapLive"
        const val OPERATE_SHOW_LIVE_DIALOG = "showLiveDialog"
        const val OPERATE_ENABLE_INPUT_PANEL = "enableInputPanel"
        const val OPERATE_DISABLE_INPUT_PANEL = "disableInputPanel"
        const val OPERATE_DISABLE_INPUT = "disableInput"
        const val OPERATE_INIT_CHATROOM = "initChatRoom"
        const val OPERATE_GET_COUPONLISTPAGE = "couponListPage"
        const val OPERATE_INIT_DETAIL = "initDetail"
        const val OPERATE_ENABLE_COUPON = "enableCoupon"

        /**
         * 是否预约
         * */
        const val SEND_MSG_RESERVATION = "reservation"
        const val SEND_MSG_LIVE_STATUS = "liveStatus"
        const val SEND_MSG_BUNDLE_KEY_START_TIME = "startTime"
        const val SEND_MSG_SHARE_URL = "shareUrl"
        const val SEND_MSG_BUNDLE_KEY_TITLE = "title"
        const val SEND_MSG_IMAGE_SHARE = "imgShare"
        const val SEND_MSG_KEY_SUMMARY = "summary"
        const val SEND_MSG_PLAY_BACK_LINK = "playbackLink"
        const val SEND_MSG_SUPPORT_PLAYBACK = "supportPlayback"
        const val SEND_MSG_LIVE_LINK = "liveLink"
        const val SEND_MSG_IMG_DETAIL = "imgDetail"
        const val SEND_MSG_OPERATE = "operate"
        const val SEND_MSG_TITLE = "title"
        const val SEND_MSG_HAS_SHARE_BITMAP = "hasShareBitmap"

        private const val TAG = "LiveModel"


        const val LIVE_DIALOG_BUNDLE_TITLE = "liveDialogTitle"
        const val LIVE_DIALOG_BUNDLE_CONTENT = "liveDialogContent"
        const val LIVE_DIALOG_BUNDLE_LEFT = "liveDialogLeft"
        const val LIVE_DIALOG_BUNDLE_TAG = "liveDialogTAG"
        const val LIVE_DIALOG_BUNDLE_RIGHT = "liveDialogRight"
        const val LIVE_DIALOG_MAP_CLICK_LISTENER = "liveDialogClick"

        const val REQUEST_BODY_LIVE_ID = "liveId"
        const val REQUEST_BODY_OPERATE_EVENT = "event"
    }

    var controller: GestureVideoController? = null

    private val mDisposables = CompositeSubscription()

    val operate: MutableLiveData<Bundle> by lazy {
        MutableLiveData()
    }

    val operateMap: MutableLiveData<Map<String, Any>> by lazy {
        MutableLiveData()
    }
    val independentSaleLiveData: MutableLiveData<Boolean?> by lazy {
        MutableLiveData(null)
    }



    /**
     * 当前是否是全屏
     * */
    var isFullScreen = false

    var chatRoomId = ""
    var liveId = ""

    var assistantUid = ""

    var couponListPage: String? = ""

    override fun back() {
        sendMainValue(OPERATE_BACK)
    }

    override fun closeLive() {
        sendMainValue(OPERATE_CLOSE_LIVE)
    }

    /**
     * 无效直播状态
     * */
    fun isInValidPlay() = controller?.mPlayComponentDataBean?.playState == PlayState.PLAY_STATE_INIT

    override fun shareLive() {
        /*val bundle = Bundle()
        bundle.putString(SEND_MSG_OPERATE, OPERATE_SHARE_LIVE)
        bundle.putString(
            SEND_MSG_BUNDLE_KEY_START_TIME,
            getTimeFromServerString(liveDetail?.startTime, const = {
                SimpleDateFormat("MM月dd日 HH:mm", Locale.CHINA).format(it)
            })
        )
        bundle.putString(SEND_MSG_IMAGE_SHARE, liveDetail?.imgShare)
        bundle.putString(SEND_MSG_KEY_SUMMARY, liveDetail?.summary ?: "")
        bundle.putString(SEND_MSG_BUNDLE_KEY_TITLE, liveDetail?.title ?: "")
        bundle.putString(SEND_MSG_SHARE_URL, liveDetail?.shareUrl)
        sendMainValue(bundle)*/
    }

    fun <T> getTimeFromServerString(
        time: String?,
        defaultDate: Date = Date(0),
        const: (Date) -> T
    ): T {
        val reformat = SimpleDateFormat("MMM d, yyyy hh:mm:ss a", Locale.ENGLISH)
        return try {
            val targetDate = reformat.parse(time?:"0") ?: defaultDate
            return const(targetDate)
        } catch (e: ParseException) {
            const(defaultDate)
        }
    }

    override fun toggleFullScreenState() {
        isFullScreen = !isFullScreen
        sendMainValue(OPERATE_FULL_SCREEN)
    }

    override fun reservation() {

    }


    override fun showJoinGroupFail() {
        sendMainValue(OPERATE_SHOW_JOIN_GROUP_FAIL)
    }

    override fun showSpeechChoices() {
        sendMainValue(OPERATE_SHOW_SPEECH_CHOICES)
    }

    fun sendMainValue(msg: Bundle) {
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            operate.value = msg
        } else {
            operate.postValue(msg)
        }
    }

    private fun sendMainValue(msg: String) {
        LogUtil.d(TAG, "sendMainValue $msg")
        val bundle = Bundle()
        bundle.putString(SEND_MSG_OPERATE, msg)
        sendMainValue(bundle)
    }

    /**
     * 初始化直播间状态
     * */
    fun initLive() {
        controller?.initData()
    }


    /**
     * 请求直播详情
     * */
    fun requestLiveDetail(liveId: String) {

    }




    override fun onCleared() {
        super.onCleared()
        mDisposables.clear()
    }



}