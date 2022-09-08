package com.my.path.ui.fragment.chapter

/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　: h5页面
 */
//class ChapterDraftListFragment(val novel: Novel) : BaseFragment<ChapterListViewModel, FragmentChapterAllBinding>() {
//
//    private val requestChapterDataViewModel: RequestChapterDataViewModel by viewModels()
//
//    override fun initView(savedInstanceState: Bundle?) {
//
//        mDatabind.viewmodel = mViewModel
//
//        mDatabind.rv.linear().setup {
//            mDatabind.rv.init(LinearLayoutManager(requireContext()))
//            addType<ChapterData>(R.layout.item_chapter)
//            R.id.itemFrame.onClick {
//
//            }
//        }.models
//
//        // 下拉刷新
//        mDatabind.page.onRefresh {
//            requestChapterDataViewModel.authorChapterDraftlist(novel.bid)
//        }.autoRefresh()
//    }
//
//    inner class ProxyClick {
//
//    }
//
//    override fun createObserver() {
//        requestChapterDataViewModel.authorChapterlistResult.observe(viewLifecycleOwner, Observer {
//            mDatabind.rv.models = it
//            if(it.size==0){
//                mDatabind.page.showEmpty()
//            }
//            mDatabind.page.showContent()
//        })
//    }
//}