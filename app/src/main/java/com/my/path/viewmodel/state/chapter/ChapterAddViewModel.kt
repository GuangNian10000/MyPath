package com.my.path.viewmodel.state.chapter


import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.ObservableField
import com.my.path.app.ext.getStringNoBlank
import com.my.path.app.util.SettingUtil.setWriteTemporaryCache
import com.my.path.data.model.bean.ChapterContent
import com.my.path.data.model.bean.ChapterData
import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.callback.databind.StringObservableField

class ChapterAddViewModel : BaseViewModel() {

    var title = StringObservableField("新建章节")

    var bid:Int=0

    var chapterContent:ObservableField<ChapterContent>  = ObservableField()

    var chapterData:ChapterData = ChapterData()

    //监听输入，每影响3个字符保存一次
    val textWatcher = object : SimpleTextWatcher(){
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)

            //空格换行不算字符
            val length = getStringNoBlank(s.toString()).length
            //mViewModel.saveWordSize.set(length.toString()+"字")
            chapterContent.get()?.apply {
                setWriteTemporaryCache(this)
            }
        }
    }

    abstract class SimpleTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {}
    }
}