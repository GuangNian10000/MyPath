package com.my.path.ui.fragment.message

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.config.SysConfig
import com.my.path.app.ext.*
import com.my.path.app.util.CacheUtil
import com.my.path.data.interfaces.PopupShieldingCallback
import com.my.path.data.model.bean.Post
import com.my.path.data.model.bean.Reply
import com.my.path.databinding.*
import com.my.path.ui.dialog.BookTagDialog
import com.my.path.ui.dialog.CommentSortingDialog
import com.my.path.viewmodel.request.RequestMeViewModel
import com.my.path.viewmodel.request.message.RequestCommentActionViewModel
import com.my.path.viewmodel.request.message.RequestMessageViewModel
import com.my.path.viewmodel.state.message.MessageListViewModel
import com.drake.brv.utils.*
import com.kingja.loadsir.core.LoadService
import com.lxj.xpopup.XPopup
import me.guangnian.mvvm.base.appContext
import me.guangnian.mvvm.ext.parseState

/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　:
 */
class CommentListFragment : BaseFragment<MessageListViewModel, FragmentCommentListBinding>() {
    private lateinit var loadsir: LoadService<Any>

    private val requestMessageViewModel: RequestMessageViewModel by viewModels()

    private val requestCommentActionViewModel: RequestCommentActionViewModel by viewModels()

    private val meRequestViewModel: RequestMeViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel

        addLoadingObserve(requestMessageViewModel)
        addLoadingObserve(requestCommentActionViewModel)
        addLoadingObserve(meRequestViewModel)

        //状态页配置
        loadsir = loadServiceInit(mDatabind.page) {
            //点击重试时触发的操作
            loadsir.showLoading()
            meRequestViewModel.meGetbook()
        }


        mDatabind.rv.linear().divider(R.drawable.bg_fengexian).setup {
            addType<Post>(R.layout.item_comment_text_list)

            R.id.imageGd.onClick {
                getModelOrNull<Post>()?.let {
                    showShieldingDialog(it.author._id,modelPosition,object : PopupShieldingCallback {
                        override fun shielding(uid: Int, pos: Int) {
                            mDatabind.page.refresh()
                        }
                    })
                }

            }

            R.id.chakanged.onClick {
                getModelOrNull<Post>()?.let {
                    inCommentDetailsFragment(mViewModel.currentBid.get(),it._id)
                }

            }

            //点赞
            R.id.imageView15.onClick {
                getModelOrNull<Post>()?.let {
                    if( it.isliked==1){
                        it.isliked=0
                        it.likecount--
                    }else{
                        it.isliked=1
                        it.likecount++
                    }
                    notifyItemChanged(modelPosition)
                    requestCommentActionViewModel.commentLiked(it._id,it.isliked)
                }
            }

            //回复
            R.id.imageView16.onClick {
                getModelOrNull<Post>()?.let {
                    replyComment(it.author.nick){ content, path->
                        it.replycount++
                        CacheUtil.getUserData()?.let { user->
                            it.reply.add(Reply(content,user.nick,user.uid,path))
                        }
                        notifyItemChanged(modelPosition)
                        requestCommentActionViewModel.commentReplyPost(it._id,content,path)
                    }
                }
            }

            //置顶
            R.id.textView16.onClick {
                getModelOrNull<Post>()?.let {post->
                    val desc = if(post.istop==0) getString(R.string.quedidsnjsahdjsae) else getString(
                                            R.string.quxiaodjashdjase)
                    this@CommentListFragment.showConfirmPopup(getString(R.string.zhidingdasdsaae),desc) {
                        requestCommentActionViewModel.commentTop(post._id)
                    }
                }


            }

            //加精
            R.id.textView17.onClick {
                getModelOrNull<Post>()?.let {post->
                    val desc = if(post.isgood==0) getString(R.string.quedingdjsajdsk) else getString(
                                            R.string.quxiaojiajdiasndusae)
                    this@CommentListFragment.showConfirmPopup(getString(R.string.jiajingdiasje),desc) {
                        requestCommentActionViewModel.commentGood(modelPosition,if( post.isgood==1) 0 else 1,post._id)
                    }
                }
            }

            //删除
            R.id.tvShanchu.onClick {
                getModelOrNull<Post>()?.let {post->
                    this@CommentListFragment.showConfirmPopup(getString(R.string.shanchudasesd),getString(R.string.quedingdyaoshanhuas)){
                        requestCommentActionViewModel.commentDel(post._id,modelPosition)
                    }
                }
            }
        }

        // 下拉刷新
        mDatabind.page.onRefresh {
            requestMessageViewModel.commentList(mViewModel.currentBid.get(),mViewModel.getCommentType(),index)
        }

        mDatabind.page.onLoadMore {
            requestMessageViewModel.commentList(mViewModel.currentBid.get(),mViewModel.getCommentType(),index)
        }

        meRequestViewModel.meGetbook()

        mDatabind.viewMessageBookTop.showBook.clickWithDebounce {
            XPopup.Builder(appContext)
                .atView(mDatabind.viewMessageBookTop.title)
                .moveUpToKeyboard(false)
                .asCustom(BookTagDialog(requireActivity(),mViewModel.currentBid.get()){
                    mViewModel.currentBid.set(it._id)
                    mViewModel.currentTitle.set(it.title)
                    mDatabind.page.refresh()})
                .show()
        }

        mDatabind.textView12.clickWithDebounce {
            XPopup.Builder(requireContext())
                .atView(mDatabind.textView12)
                .offsetX(-50)
                .asCustom(CommentSortingDialog(requireActivity(),mViewModel.getCommentType()){
                        mViewModel.type = it
                        mViewModel.textType.set(mViewModel.typeTextMap[it])
                        mDatabind.page.refresh()}
                        .setArrowWidth(25)
                        .setArrowHeight(25)
                        .setBubbleRadius(12)
                        .setBubbleBgColor(getColor(R.color.white)))
                .show()
        }
    }

    override fun createObserver() {

//        requestCommentActionViewModel.replyPostResult.observe(viewLifecycleOwner) { resultState ->
//            parseState(resultState, {
//
//            })
//        }

        requestCommentActionViewModel.commentDelResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, {
                mDatabind.rv.mutable.removeAt(it.position)
                mDatabind.rv.bindingAdapter.notifyItemRemoved(it.position)
            })
        }

        requestCommentActionViewModel.commentGoodResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, {
                mDatabind.page.refresh()
            })
        }

        requestCommentActionViewModel.commentTopResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, {
                mDatabind.page.refresh()
            })
        }
        meRequestViewModel.meGetbookResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState,loadsir, {
                if(it.books.size>0){
                    val data = it.books[0]
                    mViewModel.currentBid.set(data._id)
                    mViewModel.currentTitle.set(data.title)
                    mDatabind.page.refresh()
                    mDatabind.viewMessageBookTop.title.visibility = View.VISIBLE
                    mDatabind.textView12.visibility = View.VISIBLE
                }else{
                    mDatabind.textView12.visibility = View.GONE
                    mDatabind.viewMessageBookTop.title.visibility = View.GONE
                    loadsir.showEmpty(SysConfig.NUll_MESSAGE_LIST)
                }
            },{},SysConfig.NUll_MESSAGE_LIST)
        }

        requestMessageViewModel.commentListDataResult.observe(viewLifecycleOwner, Observer {
            mViewModel.Jnum = it.jnum
        })

        requestMessageViewModel.commentListResult.observe(viewLifecycleOwner, Observer {
            mDatabind.page.addData(it.listData,mDatabind.rv.bindingAdapter,{it.isEmpty},{it.hasMore})
        })
    }

}