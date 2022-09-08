package com.my.path.ui.fragment.message

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.ext.*
import com.my.path.data.interfaces.PopupShieldingCallback
import com.my.path.data.model.bean.*
import com.my.path.databinding.*
import com.my.path.viewmodel.request.RequestMeViewModel
import com.my.path.viewmodel.request.message.RequestCommentActionViewModel
import com.my.path.viewmodel.request.message.RequestMessageViewModel
import com.my.path.viewmodel.state.message.CommentDetailsViewModel
import com.drake.brv.utils.*
import me.guangnian.mvvm.ext.parseState

/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　:
 */
class CommentDetailsFragment : BaseFragment<CommentDetailsViewModel, FragmentCommentDetailsBinding>() {

    private val requestMessageViewModel: RequestMessageViewModel by viewModels()

    private val requestCommentActionViewModel: RequestCommentActionViewModel by viewModels()

    private val meRequestViewModel: RequestMeViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel

        addLoadingObserve(requestMessageViewModel)
        addLoadingObserve(requestCommentActionViewModel)
        addLoadingObserve(meRequestViewModel)


        arguments?.run {
            mViewModel.id = getInt("id",0)
            mViewModel.bid = getInt("bid",0)
        }

        mViewModel.currentTitle.set(getString(R.string.pinglunxiangdawesd))

        mDatabind.rv.linear().setup {
            addType<Post>(R.layout.item_reply_list)
            addType<PostHead>(R.layout.item_comment_text_head)

            R.id.imageGd.onClick {
                getModelOrNull<Post>()?.let {
                    showShieldingDialog(it.author._id,modelPosition,object :
                        PopupShieldingCallback {
                        override fun shielding(uid: Int, pos: Int) {
                            mDatabind.page.refresh()
                        }
                    })
                }

            }

            //回复点赞
            R.id.imageView15_re.onClick {
                getModelOrNull<Post>()?.let {
                    if( it.isliked==1){
                        it.isliked=0
                        it.likecount--
                    }else{
                        it.isliked=1
                        it.likecount++
                    }
                    notifyItemChanged(modelPosition)
                    requestCommentActionViewModel.commentReplyLiked(it._id,it.isliked)
                }
            }

            //点赞
            R.id.imageView15.onClick {
                getModelOrNull<PostHead>()?.let {
                    if( it.isliked==1){
                        it.isliked=0
                        it.likecount--
                    }else{
                        it.isliked=1
                        it.likecount++
                    }
                    notifyItemChanged(0)
                    requestCommentActionViewModel.commentLiked(it._id,it.isliked)
                }
            }

            //回复
            R.id.imageView16.onClick {
                getModelOrNull<PostHead>()?.let {
                    replyComment(it.author.nick){ content, path->
                        requestCommentActionViewModel.commentReplyPost(it._id,content,path)
                    }
                }
            }
        }

        // 下拉刷新
        mDatabind.page.onRefresh {
            requestMessageViewModel.commentReply(mViewModel.id,index)
        }

        mDatabind.page.onLoadMore {
            requestMessageViewModel.commentReply(mViewModel.id,index)
        }

        //评论详情
        requestMessageViewModel.commentDetails(mViewModel.bid,mViewModel.id)
    }

    override fun createObserver() {

        requestCommentActionViewModel.replyPostResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, {
                mDatabind.page.refresh()
            })
        }

        requestMessageViewModel.commentDetailsResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, {
                mDatabind.rv.bindingAdapter.addHeader(it, animation = true)
                mDatabind.page.refresh()

                mDatabind.views.clickWithDebounce {
                    replyComment(it.author.nick){ content, path->
                        requestCommentActionViewModel.commentReplyPost(mViewModel.id,content,path)
                    }
                }
            })
        }

        requestMessageViewModel.commentReplyResult.observe(viewLifecycleOwner, Observer {
            mDatabind.page.addData(it.listData,mDatabind.rv.bindingAdapter,{it.isEmpty},{it.hasMore})
        })
    }

}