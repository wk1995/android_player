package com.wk.player

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewDebug.ExportedProperty
import android.view.ViewDebug.IntToString
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment

/**
 * 弹窗基类
 */
abstract class BaseXDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(STYLE_NO_TITLE, setTheme())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutResourceId(), container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.run {
            attributes = attributes?.apply {
                gravity = setGravity()
                width = getWidth()
                height = getHeight()
            }
        }
    }

    @LayoutRes
    abstract fun getLayoutResourceId(): Int
    open fun setGravity(): Int = Gravity.NO_GRAVITY

    @ExportedProperty(
        category = "layout",
        mapping = [IntToString(
            from = ViewGroup.LayoutParams.MATCH_PARENT,
            to = "MATCH_PARENT"
        ), IntToString(from = ViewGroup.LayoutParams.WRAP_CONTENT, to = "WRAP_CONTENT")]
    )
    open fun getWidth(): Int = ViewGroup.LayoutParams.MATCH_PARENT

    @ExportedProperty(
        category = "layout",
        mapping = [IntToString(
            from = ViewGroup.LayoutParams.MATCH_PARENT,
            to = "MATCH_PARENT"
        ), IntToString(from = ViewGroup.LayoutParams.WRAP_CONTENT, to = "WRAP_CONTENT")]
    )
    open fun getHeight(): Int = ViewGroup.LayoutParams.WRAP_CONTENT


}