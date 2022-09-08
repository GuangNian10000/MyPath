package com.my.path.ui.fragment.chapter

import android.os.Bundle
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.eventViewModel
import com.my.path.app.ext.*
import com.my.path.data.model.bean.Novel
import com.my.path.databinding.FragmentChapterBinding
import com.my.path.viewmodel.state.chapter.ChapterListViewModel

/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　:
 */
class ChapterListFragment : BaseFragment<ChapterListViewModel, FragmentChapterBinding>() {

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.viewmodel = mViewModel

        mDatabind.proxyClick = ProxyClick()

        arguments?.run {
            getParcelable<Novel>("novel")?.let {
                mViewModel.title.set(it.name)
                mViewModel.novel =it
                mDatabind.include2.title.text = it.name
            }
        }
//
//        eventViewModel.inAddChapter.observeInFragment(this){
//            inChapterAddFragment(mViewModel.novel.bid)
//        }

        eventViewModel.allChapterNum.observeInFragment(this){
            mViewModel.allChapterNum.set(it)
        }

        eventViewModel.draftChapterNum.observeInFragment(this){
            mViewModel.draftChapterNum.set(it)
        }

        val fragmentEndList = listOf(ChapterAllListFragment(mViewModel.novel,0),ChapterAllListFragment( mViewModel.novel,1))

        mDatabind.viewChapterListTable.dslTabLayout.initTask(fragmentEndList,mDatabind.viewpager,childFragmentManager)
    }

    inner class ProxyClick {

        //添加章节
        fun addChapter(){
            eventViewModel.inAddChapter.value = true
           // inChapterAddFragment(mViewModel.novel.bid)
        }

        fun reverseOrder(){
            val a = eventViewModel.isReverseOrder.value
            if(null!=a){
                eventViewModel.isReverseOrder.value = !a
                mDatabind.imageView9.setImageDrawable(if(a)getBackgroundExt(R.drawable.ic_shunxuasesad) else getBackgroundExt(R.drawable.ic_shunxudase))
            }else{
                eventViewModel.isReverseOrder.value = true
            }
        }
    }

    override fun createObserver() {
    }
}