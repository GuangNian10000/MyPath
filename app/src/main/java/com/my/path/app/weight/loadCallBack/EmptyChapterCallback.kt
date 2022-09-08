package com.my.path.app.weight.loadCallBack


import android.content.Context
import android.view.View
import com.my.path.R
import com.my.path.app.eventViewModel
import com.kingja.loadsir.callback.Callback


class EmptyChapterCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.view_chapter_list_null
    }

    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        eventViewModel.inAddChapter.value = true
        return true
    }
}