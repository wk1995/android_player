package com.wk.player

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.wk.android_player.R
import com.wk.player.LiveViewModel.Companion.LIVE_DIALOG_BUNDLE_CONTENT
import com.wk.player.LiveViewModel.Companion.LIVE_DIALOG_BUNDLE_LEFT
import com.wk.player.LiveViewModel.Companion.LIVE_DIALOG_BUNDLE_RIGHT
import com.wk.player.LiveViewModel.Companion.LIVE_DIALOG_BUNDLE_TAG
import com.wk.player.LiveViewModel.Companion.LIVE_DIALOG_BUNDLE_TITLE
import com.wk.player.LiveViewModel.Companion.LIVE_DIALOG_MAP_CLICK_LISTENER
import com.wk.player.LiveViewModel.Companion.SEND_MSG_OPERATE
import com.wk.player.log.LogUtil
import com.wk.player.play.GlobalPlayerComponent
import com.wk.player.play.LiveCoverComponent
import com.wk.player.play.LivePlayingMaskComponent
import com.wk.player.play.PlayStateMaskTipsComponent
import com.wk.player.play.UserIdComponent
import com.wk.player.play.VideoViewContainer


class LiveActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LiveActivity"
        const val ROUTE_LIVE = "/live/detail"
        const val ROUTE_ARGUMENT_LIVE_ID = "id"
        const val ROUTE_ARGUMENT_TAB = "type"
        const val ROUTE_ARGUMENT_TAB_DETAIL = "detail"
        const val ROUTE_ARGUMENT_TAB_CHAT = "chat"
        const val ROUTE_ARGUMENT_TAB_RECOMMEND = "recomend"
    }

    private val mLiveViewModel by lazy {
        ViewModelProvider(this).get(LiveViewModel::class.java)
    }


    /**
     * 播放选择速率的弹框
     * */
    private var mCourseSpeedDialog: CourseSpeedDialog? = null

    private lateinit var flLivePlayer: VideoViewContainer

    /**
     * [flLivePlayer]父布局
     * */
    private lateinit var flLivePlayerParent: ViewGroup
    private lateinit var originParam: ViewGroup.LayoutParams
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.live_activity)
        mLiveViewModel.operate.observe(this) {
            operate(it)
        }
        mLiveViewModel.operateMap.observe(this) {
            operateMap(it)
        }
        flLivePlayer = findViewById(R.id.flLivePlayer)
        val controller = GestureVideoController.obtain()
        controller.removeAllControlComponent()
        controller.initGlobalPlay()
        controller.mControlWrapper = ControlWrapper(mLiveViewModel, controller)
        flLivePlayer.mController = controller
        flLivePlayer.addComponentToThis(LiveCoverComponent(this))
        flLivePlayer.addComponentToThis(LivePlayingMaskComponent(this))
        flLivePlayer.addComponentToThis(UserIdComponent(this))
        flLivePlayer.addComponentToThis(PlayStateMaskTipsComponent(this))
        flLivePlayer.addComponentToThis(GlobalPlayerComponent(this), true)

        mLiveViewModel.controller = controller
        flLivePlayerParent = flLivePlayer.parent as ViewGroup
        originParam = flLivePlayer.layoutParams
        initLive(controller)
    }


    private fun initLive(controller: GestureVideoController) {
        var liveId = intent.getStringExtra(ROUTE_ARGUMENT_LIVE_ID).orEmpty()
        //没有传直播id，说明是进入已有直播间，或者无效直播间
        if (TextUtils.isEmpty(liveId)) {
            //返回直播页面
            liveId = controller.liveId
            controller.useLastPlayState()
            LogUtil.d(TAG, "liveId is null 没传id，使用上一个直播间数据")
        } else {
            if (liveId == controller.liveId) {
                //当前直播间
                LogUtil.d(TAG, "相同直播id，使用上一个直播间数据")
                controller.useLastPlayState()
            } else {
                //表明是新直播
                LogUtil.d(TAG, "新直播")
                mLiveViewModel.initLive()
            }
        }
        //若进入的直播间跟上一个直播间相同，最好不做额外请求，但如果不请求，就不会出发，
        // 直播tab 的页面展示了，现在tab内容跟请求绑定在一起了，需要优化
        mLiveViewModel.requestLiveDetail(
            liveId
        )
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        LogUtil.d(TAG, "onNewIntent")
        setIntent(intent)
        val controller = GestureVideoController.obtain()
        flLivePlayer.mController = controller
        initLive(controller)
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            closePage(false)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun closePage(needDestroyPlayer: Boolean) {
        if (!needDestroyPlayer) {
            if (mLiveViewModel.isInValidPlay()) {
                LogUtil.d(TAG, "closePage: 无效直播状态 直接退出直播间")
                flLivePlayer.destroy(true)
                finish()
                return
            }
            if (mLiveViewModel.isFullScreen) {
                mLiveViewModel.toggleFullScreenState()
                return
            }
            LogUtil.d(TAG, "closePage: 不想真正退出直播间")
            /*  if (!PermissionUtils.checkPermission(this)) {
                  LogUtil.d(TAG, "closePage: 没有全局悬浮窗权限")
                  val map = HashMap<String, Any>()
                  map[LIVE_DIALOG_BUNDLE_CONTENT] = "后台播放需要开启权限"
                  map[LIVE_DIALOG_BUNDLE_LEFT] = "不需要，直接退出"
                  map[LIVE_DIALOG_BUNDLE_RIGHT] = "我去申请"
                  map[LIVE_DIALOG_MAP_CLICK_LISTENER] =
                      object : LiveDialogFragment.ILiveDialogClickListener {
                          override fun rightClick() {
                              PermissionUtils.requestPermission(this@LiveActivity, object :
                                  OnPermissionResult {
                                  override fun permissionResult(isOpen: Boolean) {
                                      LogUtil.d(TAG, "permissionResult:  isOpen $isOpen")
                                      flLivePlayer.destroy(!isOpen)
                                      finish()
                                  }
                              })
                          }

                          override fun leftClick() {
                              flLivePlayer.destroy(true)
                              finish()
                          }
                      }
                  showLiveDialog(map)
              } else {
                  LogUtil.d(TAG, "closePage: 有全局悬浮窗权限")
                  flLivePlayer.destroy(false)
                  finish()
              }*/
        } else {
            LogUtil.d(TAG, "closePage: 想退出直播间")
            flLivePlayer.destroy(true)
            finish()
        }
    }


    private fun showFullScreen(isFullScreen: Boolean) {
        val rootParent: ViewGroup = flLivePlayerParent.parent as ViewGroup
        if (!isFullScreen) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            flLivePlayerParent.removeView(flLivePlayer)
            rootParent.removeView(flLivePlayer)
            flLivePlayerParent.addView(flLivePlayer, 0, originParam)
        } else {
            flLivePlayerParent.removeView(flLivePlayer)
            rootParent.removeView(flLivePlayer)
            val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
            rootParent.addView(flLivePlayer, params)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    private fun showShare(bundle: Bundle) {

    }

    private fun showBitmapShare(bundle: Bundle) {

    }

    private fun showSpeechChoices() {
        if (mCourseSpeedDialog == null) {
            mCourseSpeedDialog = CourseSpeedDialog()
        }
        mCourseSpeedDialog?.setSpeedPosition(flLivePlayer.getRate())
        mCourseSpeedDialog?.mListener = { speed ->
            flLivePlayer.setRate(speed)
        }
        mCourseSpeedDialog?.show(supportFragmentManager, "playBlackVideoSpeed")
    }

    private fun showJoinGroupFail() {

    }

    private fun operateMap(map: Map<String, Any>) {
        when (map[SEND_MSG_OPERATE]) {
            LiveViewModel.OPERATE_SHOW_LIVE_DIALOG -> {
                showLiveDialog(map)
            }
        }
    }


    private fun operate(bundle: Bundle) {
        when (bundle.getString(SEND_MSG_OPERATE) ?: "") {
            LiveViewModel.OPERATE_BACK -> {
                closePage(false)
            }

            LiveViewModel.OPERATE_CLOSE_LIVE -> {
                closePage(true)
            }

            LiveViewModel.OPERATE_SHOW_SPEECH_CHOICES -> {
                showSpeechChoices()
            }

            LiveViewModel.OPERATE_SHOW_JOIN_GROUP_FAIL -> {
                showJoinGroupFail()
            }

            LiveViewModel.OPERATE_FULL_SCREEN -> {
                showFullScreen(mLiveViewModel.isFullScreen)
            }

            LiveViewModel.OPERATE_PAUSE_LIVE -> {
                flLivePlayer.pausePlay(true)
            }

            LiveViewModel.OPERATE_SHARE_LIVE -> {
                showShare(bundle)
            }

            LiveViewModel.OPERATE_SHARE_BITMAP_LIVE -> {
                showBitmapShare(bundle)
            }

            LiveViewModel.OPERATE_SHOW_LIVE_DIALOG -> {
                showLiveDialog(bundle)
            }
        }
    }


    private fun showLiveDialog(bundle: Bundle) {
        val title = bundle.getString(LIVE_DIALOG_BUNDLE_TITLE) ?: ""
        val content = bundle.getString(LIVE_DIALOG_BUNDLE_CONTENT) ?: ""
        val leftText = bundle.getString(LIVE_DIALOG_BUNDLE_LEFT) ?: ""
        val rightText = bundle.getString(LIVE_DIALOG_BUNDLE_RIGHT) ?: ""
        val tag = bundle.getString(LIVE_DIALOG_BUNDLE_TAG) ?: ""
        val dialog = LiveDialogFragment.LiveDialogBuilder().setTitle(title).setContent(content)
            .setRightText(rightText).setLeftText(leftText).build()
        dialog.show(supportFragmentManager, tag)
    }

    private fun showLiveDialog(map: Map<String, Any>) {
        val title = map[LIVE_DIALOG_BUNDLE_TITLE]?.toString() ?: ""
        val content = map[LIVE_DIALOG_BUNDLE_CONTENT]?.toString() ?: ""
        val leftText = map[LIVE_DIALOG_BUNDLE_LEFT]?.toString() ?: ""
        val rightText = map[LIVE_DIALOG_BUNDLE_RIGHT]?.toString() ?: ""
        val tag = map[LIVE_DIALOG_BUNDLE_TAG]?.toString() ?: ""
        val click =
            map[LIVE_DIALOG_MAP_CLICK_LISTENER] as? LiveDialogFragment.ILiveDialogClickListener
        val dialog = LiveDialogFragment.LiveDialogBuilder().setTitle(title).setContent(content)
            .setRightText(rightText).setLeftText(leftText).setListener(click).build()
        dialog.show(supportFragmentManager, tag)
    }

}