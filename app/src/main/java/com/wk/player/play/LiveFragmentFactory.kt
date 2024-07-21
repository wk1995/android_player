package com.wk.player.play

import android.os.Bundle
import androidx.fragment.app.Fragment

interface LiveFragmentFactory {
    fun create(position: Int, bundle: Bundle): Fragment
}