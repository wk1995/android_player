package com.wk.player

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.wk.android_player.R

class LiveDialogFragment : DialogFragment(R.layout.live_dialog), View.OnClickListener {

    interface ILiveDialogClickListener {
        fun leftClick() {

        }

        fun rightClick() {

        }
    }


    class LiveDialogBuilder {
        private var mTitle: String = ""

        private var mContent: String = ""

        private var mOkText: String = ""

        private var mCancelText: String = ""

        private var listener: ILiveDialogClickListener? = null

        fun setTitle(title: String): LiveDialogBuilder {
            mTitle = title
            return this
        }

        fun setContent(content: String): LiveDialogBuilder {
            mContent = content
            return this
        }

        fun setRightText(rightText: String): LiveDialogBuilder {
            mOkText = rightText
            return this
        }

        fun setLeftText(leftText: String): LiveDialogBuilder {
            mCancelText = leftText
            return this
        }

        fun setListener(listener: ILiveDialogClickListener?): LiveDialogBuilder {
            this.listener = listener
            return this
        }


        fun build(): LiveDialogFragment {
            val mLiveDialogFragment = LiveDialogFragment()
            mLiveDialogFragment.cancelText = mCancelText
            mLiveDialogFragment.title = mTitle
            mLiveDialogFragment.content = mContent
            mLiveDialogFragment.okText = mOkText
            mLiveDialogFragment.mLiveDialogClickListener = listener
            return mLiveDialogFragment
        }
    }

    private lateinit var tvLiveDialogTitle: TextView
    private lateinit var tvLiveDialogContent: TextView
    private lateinit var btnLiveDialogOk: Button
    private lateinit var btnLiveDialogCancel: Button

    private var title: String = ""

    private var content: String = ""

    private var okText: String = ""

    private var cancelText: String = ""


    private var mLiveDialogClickListener: ILiveDialogClickListener? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.apply {
            tvLiveDialogTitle = findViewById(R.id.tvLiveDialogTitle)
            tvLiveDialogContent = findViewById(R.id.tvLiveDialogContent)
            btnLiveDialogOk = findViewById(R.id.btnLiveDialogOk)
            btnLiveDialogCancel = findViewById(R.id.btnLiveDialogCancel)
            btnLiveDialogOk.setOnClickListener(this@LiveDialogFragment)
            btnLiveDialogCancel.setOnClickListener(this@LiveDialogFragment)
            viewVisibility(tvLiveDialogTitle, title)
            viewVisibility(tvLiveDialogContent, content)
            viewVisibility(btnLiveDialogOk, okText)
            viewVisibility(btnLiveDialogCancel, cancelText)
        }
    }

    private inline fun viewVisibility(
        view: TextView,
        text: String,
        show: (String) -> Boolean = { TextUtils.isEmpty(it) }
    ) {
        view.visibility = if (show(text)) {
            View.GONE
        } else {
            view.text = text
            View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnLiveDialogOk -> {
                mLiveDialogClickListener?.rightClick()
                dismiss()
            }
            R.id.btnLiveDialogCancel -> {
                mLiveDialogClickListener?.leftClick()
                dismiss()
            }
        }
    }
}