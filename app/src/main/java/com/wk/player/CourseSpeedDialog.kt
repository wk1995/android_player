package com.wk.player

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wk.android_player.R


// * 课程倍速dialog
 class CourseSpeedDialog : BaseXDialogFragment() {

    private val mSpeedList = floatArrayOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f)
    var mListener: ((speed: Float) -> Unit)? = null
    private var mAdapter: CourseSpeedAdapter? = null
    private var mOldPosition = -1
    private var mCurrentPosition = 2

    private lateinit var recycler_list: RecyclerView
    private lateinit var tv_cancel: TextView

    override fun getLayoutResourceId(): Int = R.layout.dialog_course_speed
    override fun setGravity(): Int = Gravity.BOTTOM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        context?.let {
            recycler_list = view.findViewById(R.id.recycler_list)
            tv_cancel = view.findViewById(R.id.tv_cancel)
            if (null == mAdapter) mAdapter = CourseSpeedAdapter(it)
            recycler_list.adapter = mAdapter
            tv_cancel.setOnClickListener {
                dismissAllowingStateLoss()
            }
        }
    }

    fun setSpeedPosition(speed: Float) {
        mCurrentPosition = mSpeedList.indexOfFirst { speed == it }
    }

    inner class CourseSpeedAdapter(
        context: Context,
        private val inflater: LayoutInflater = LayoutInflater.from(context)
    ) : RecyclerView.Adapter<CourseSpeedAdapter.CourseSpeedVH>() {

        inner class CourseSpeedVH(view: View) : RecyclerView.ViewHolder(view) {
            var tv_speed: TextView
            init {
                tv_speed = view.findViewById(R.id.tv_speed)
            }
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, itemType: Int): CourseSpeedVH =
            CourseSpeedVH(inflater.inflate(R.layout.item_course_speed, viewGroup, false))

        override fun onBindViewHolder(vh: CourseSpeedVH, @SuppressLint("RecyclerView") position: Int) {
            vh.let {
                mSpeedList.elementAtOrNull(position)?.let { speed ->
                    it.tv_speed.run {
                        setTextColor(resources.getColor(if (position == mCurrentPosition) R.color.color_1DA1F2 else R.color.color_1c1f28))
                        text = speed.toString()
                        setOnClickListener {
                            if (position == mCurrentPosition) {
                                dismissAllowingStateLoss()
                                return@setOnClickListener
                            }
                            mOldPosition = mCurrentPosition
                            mCurrentPosition = position
                            notifyItemChanged(mOldPosition)
                            notifyItemChanged(mCurrentPosition)
                            mListener?.invoke(speed)
                            dismissAllowingStateLoss()
                        }
                    }
                }
            }
        }

        override fun getItemCount(): Int = mSpeedList.size

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}
