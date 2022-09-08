package com.my.path.ui.fragment.writing

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.eventViewModel
import com.my.path.app.ext.clickWithDebounce
import com.my.path.data.model.bean.Novel
import com.my.path.data.model.bean.Yc
import com.my.path.data.model.bean.YcData
import com.my.path.databinding.FragmentAddWritingBinding
import com.my.path.ui.dialog.BookTypeSelectionDialog
import com.my.path.viewmodel.request.home.RequestWritingViewModel
import com.my.path.viewmodel.state.writing.WritingChangeViewModel
import com.lxj.xpopup.XPopup
import me.guangnian.mvvm.base.appContext
import me.guangnian.mvvm.ext.nav
import me.guangnian.mvvm.ext.parseState

/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　: h5页面
 */
class WritingAddFragment : BaseFragment<WritingChangeViewModel, FragmentAddWritingBinding>() {

    private val requestWritingViewModel: RequestWritingViewModel by viewModels()

    companion object{
        val CLASSTYPE_LXFL = "CLASSTYPELXFL"
        val CLASSTYPE_XXSJ = "CLASSTYPEXXSJ"
        val CLASSTYPE_SDFG = "CLASSTYPESDFG"
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel

        mDatabind.proxyClick = ProxyClick()

        mViewModel.title.set(getString(R.string.chuangjianzuoasdae))

        mViewModel.novel.set(Novel())

        addLoadingObserve(requestWritingViewModel)

        mDatabind.viewWritingBottomAdd.conLx.clickWithDebounce {
            ProxyClick().showLxDialog()
        }

        mDatabind.viewWritingBottomAdd.conXx.clickWithDebounce {
            ProxyClick().showXxDialog()
        }

        mDatabind.viewWritingBottomAdd.conSd.clickWithDebounce {
            ProxyClick().showSdDialog()
        }

        mDatabind.viewWritingTop.frameLayout2.visibility = View.GONE
    }

    inner class ProxyClick {
        //创建
        fun createBook(){
            mViewModel.novel.get()?.apply {
                requestWritingViewModel.authorCreate(obName,obIntro,yc,lx,xx,sj,sd,fg)
            }
        }

        //类型
        fun showLxDialog(){
            requestWritingViewModel.authorYclx()
        }

        fun showXxDialog(){
            requestWritingViewModel.authorXxsj()
        }

        fun showSdDialog(){
            requestWritingViewModel.authorSdfg()
        }
    }

    override fun createObserver() {

        //创建作品
        requestWritingViewModel.authorCreateResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                    data->
                nav().navigateUp()
                eventViewModel.writingEvent.value = true
            }, {})
        }

        requestWritingViewModel.authorYclxResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, {
                showTypeDialog(CLASSTYPE_LXFL,it)
            })
        }

        requestWritingViewModel.authorXxsjResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, {
                showTypeDialog(CLASSTYPE_XXSJ,it)
            })
        }

        requestWritingViewModel.authorSdfgResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, {
                showTypeDialog(CLASSTYPE_SDFG,it)
            })
        }
    }

    //弹窗
    private fun showTypeDialog(type:String,list:ArrayList<Yc>){
        mViewModel.novel.get()?.apply {
            when(type){
                WritingChangesFragment.CLASSTYPE_LXFL ->{
                    list.forEach {
                        if(it.id==yc){
                            it.selected=true

                            it.data.forEach { ycData->
                                if(ycData.id==lx){
                                    ycData.selected=true
                                }
                            }
                        }
                    }
                }
                WritingChangesFragment.CLASSTYPE_XXSJ ->{
                    list.forEach {
                        if(it.id==xx){
                            it.selected=true

                            it.data.forEach { ycData->
                                if(ycData.id==sj){
                                    ycData.selected=true
                                }
                            }
                        }
                    }
                }
                WritingChangesFragment.CLASSTYPE_SDFG ->{
                    list.forEach {
                        if(it.id==sd){
                            it.selected=true

                            it.data.forEach { ycData->
                                if(ycData.id==fg){
                                    ycData.selected=true
                                }
                            }
                        }
                    }
                }
            }

           if(list.filter {
                it.selected
            }.isEmpty()&&list.size>0){
               list[0].selected=true
            }
            XPopup.Builder(appContext)
                .moveUpToKeyboard(false)
                .asCustom(BookTypeSelectionDialog(requireActivity(),type,list){
                        yc, ycData ->
                    setTypeData(type,yc,ycData)
                })
                .show()
        }
    }

    //设置数据
    private fun setTypeData(type:String,ycs:Yc,ycData: YcData){
        mViewModel.novel.get()?.apply {
            when(type){
                CLASSTYPE_LXFL->{
                    yc = ycs.id
                    lx = ycData.id
                    obYclx = ( ycs.name+"-"+ycData.name)
                }
                CLASSTYPE_XXSJ->{
                    xx = ycs.id
                    sj = ycData.id
                    obXXsj = (ycs.name+"-"+ycData.name)
                }
                CLASSTYPE_SDFG->{
                    sd = ycs.id
                    fg = ycData.id
                    obSdfg = (ycs.name+"-"+ycData.name)
                }
            }
        }
    }
}