package com.my.path.app.ext


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.angcyo.tablayout.DslTabLayout
import com.my.path.R
import com.my.path.app.util.SettingUtil
import com.my.path.ui.fragment.home.DataFragment
import com.my.path.ui.fragment.home.MeFragment
import com.my.path.ui.fragment.home.MessageFragment
import com.my.path.ui.fragment.home.WritingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupPosition
import com.lxj.xpopup.util.XPopupUtils
import me.guangnian.mvvm.base.appContext
import com.angcyo.tablayout.delegate.ViewPager1Delegate
import com.chad.library.adapter.base.BaseQuickAdapter
import com.my.path.app.config.SysConfig
import com.my.path.app.eventViewModel
import com.drake.brv.utils.addModels
import com.drake.brv.utils.models
import com.kingja.loadsir.core.LoadService
import com.my.path.app.network.stateCallback.ListDataUiState
import com.my.path.app.weight.loadCallBack.*
import com.my.path.app.weight.recyclerview.DefineLoadMoreView
import com.my.path.app.weight.recyclerview.SwRecyclerView
import com.my.path.data.model.bean.PopupCallback
import com.my.path.data.model.bean.inUserHome
import com.my.path.ui.dialog.*
import com.my.path.weight.loadCallBack.LoadingCallback
import com.drake.brv.PageRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kingja.loadsir.core.LoadSir
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import me.guangnian.mvvm.base.fragment.BaseVmFragment
import me.guangnian.mvvm.network.AppException
import me.guangnian.mvvm.state.ResultState


/**
 * @author???: GuangNian
 * @date???: 2020/2/20
 * @description???:????????????????????????????????????
 */
fun Toolbar.initTitle(title:String?,onBack: (toolbar: Toolbar) -> Unit){
    val titleView =  findViewById<TextView>(R.id.tvTitle)
    titleView?.text = title
    val buttonBack = findViewById<ImageView>(R.id.imageBack)
    buttonBack?.clickWithDebounce {  onBack.invoke(this) }
}

/**
 * ?????????
 * */
fun Fragment.releaseComment(hint:String=getString(R.string.shuoshuonidawesdcdr), back: (String, String) -> Unit){
    XPopup
        .Builder(requireContext())
        .asCustom(ReleaseCommentSmallDialog(requireActivity(),hint){content,path->
            back.invoke(content, path)
        }).show()
}

/**
 * ????????????
 * */
fun Fragment.replyComment(hint:String=getString(R.string.pinglnxooe), back: (String, String) -> Unit){
    XPopup
        .Builder(requireContext())
        .asCustom(ReplyPictureSmallDialog(requireActivity(),getString(R.string.huifuadasecy)+hint){content,path->
            back.invoke(content, path)
        }).show()
}

/**
 * ??????????????????
 * */
fun Fragment.showConfirmPopup(
    title: String,
    textDefault:String="",
    back: (Boolean) -> Unit = {}
) {
    activity?.let {
        XPopup.Builder(it)
            // .moveUpToKeyboard(true)
            .asCustom(ConfirmPopup(it,title,textDefault,back))
            .show()

    }
}

fun setReplySplice(
    textView: TextView,
    user: String,
    userId: Int,
    content: String
) {
    var user = user
    try {
        val builder = SpannableStringBuilder()
        user = "$user: "
        val sp1 = SpannableString(user)
        sp1.setSpan(Clickable { v: View? ->
            eventViewModel.inAppFragment.value = inUserHome(userId)
        }, 0, user.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val fcs1 = ForegroundColorSpan(getColor(R.color.book_comments_text_back))
        sp1.setSpan(fcs1, 0, user.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) //?????????????????????
        builder.append(sp1) //??????
        val sp2 = SpannableString(content)
        sp2.setSpan(Clickable(View.OnClickListener { v: View? -> }),
            0,
            content.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val fcs2 =
            ForegroundColorSpan(getColor(R.color.book_comments_huifu))
        sp2.setSpan(fcs2, 0, content.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) //?????????????????????
        builder.append(sp2)
        textView.text = builder
        textView.movementMethod = LinkMovementMethod.getInstance() //???????????????????????????
        textView.highlightColor =getColor(R.color.transparent)
    } catch (e: Exception) {}
}

fun View.setReplySplice(
    textView: TextView,
    user: String,
    userId: Int,
    content: String,
    path:String = ""
) {
    var user = user
    try {
        val builder = SpannableStringBuilder()
        user = "$user: "
        val sp1 = SpannableString(user)
        sp1.setSpan(Clickable { v: View? ->
            eventViewModel.inAppFragment.value = inUserHome(userId)
//                val intent = Intent(activity, PersonalHomeActivity::class.java)
//                intent.putExtra("userId", userId)
//                activity.startActivity(intent)
        }, 0, user.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val fcs1 = ForegroundColorSpan(
            ContextCompat.getColor(
                appContext,
                R.color.book_comments_text_back
            )
        )

        sp1.setSpan(fcs1, 0, user.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) //?????????????????????
        builder.append(sp1) //??????


        val sp2 = SpannableString(content)
        sp2.setSpan(
            Clickable { v: View? -> },
            0,
            content.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val fcs2 =
            ForegroundColorSpan(ContextCompat.getColor(appContext, R.color.book_comments_huifu))
        sp2.setSpan(fcs2, 0, content.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) //?????????????????????
        builder.append(sp2)


        //????????????
        if(path!=""){
            val textImg  = appContext.getString(R.string.chakandatudawesd)
            val sp3 = SpannableString(textImg)
            sp3.setSpan(Clickable { v: View? ->
                showImg(path)
            }, 0, textImg.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            val fcs3 = ForegroundColorSpan(ContextCompat.getColor(appContext, R.color.book_comments_text_back))

            sp3.setSpan(fcs3, 0, textImg.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) //?????????????????????
            builder.append(sp3) //??????
        }


        textView.setText(builder)
        textView.setMovementMethod(LinkMovementMethod.getInstance()) //???????????????????????????
        textView.setHighlightColor(
            ContextCompat.getColor(
                appContext,
                R.color.transparent
            )
        ) //???????????????????????????????????????



    } catch (e: Exception) {
    }
}

internal class Clickable(private val mListener: View.OnClickListener) :
    ClickableSpan(), View.OnClickListener {
    override fun onClick(v: View) {
        mListener.onClick(v)
    }

    override fun updateDrawState(ds: TextPaint) {
        ds.color = ds.linkColor
        ds.isUnderlineText = false //???????????????????????????
    }
}

fun DslTabLayout.initTask(fragmentList:List<Fragment>,
                          viewPager: ViewPager,
                          childFragmentManager: FragmentManager){
    configTabLayoutConfig {
        onSelectIndexChange =
            { fromIndex, selectIndexList, reselect, fromUser ->
                _viewPagerDelegate?.onSetCurrentItem(
                    fromIndex,
                    selectIndexList.last()
                )
            }
    }

    ViewPager1Delegate.install(viewPager, this)
    viewPager.adapter =
        object : FragmentStatePagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return fragmentList[position]
            }

            override fun getCount(): Int {
                return fragmentList.size
            }
        }
    viewPager.offscreenPageLimit = fragmentList.size
}


@ColorInt
fun getColor(c:Int):Int= ContextCompat.getColor(appContext,c)

fun getBackgroundExt(@DrawableRes c:Int): Drawable? = ContextCompat.getDrawable(appContext,c)

fun Activity.showService(view:View,checkBox: CheckBox){
    XPopup.Builder(this)
        .atView(checkBox)
        .popupPosition(PopupPosition.Top)
        .hasShadowBg(false)
        .isClickThrough(true)
        .offsetY(XPopupUtils.dp2px(this, -26f))
        .offsetX(XPopupUtils.dp2px(this, -10f))
        .asCustom(
            LoginAttachDialog(this)
                .setBubbleBgColor(this.getColor(R.color.login_service_sc))
                .setArrowWidth(XPopupUtils.dp2px(this, 8f))
                .setArrowHeight(XPopupUtils.dp2px(this, 5f))
                //.setArrowOffset(-10)
        )
        .show()
    val animation = AnimationUtils.loadAnimation(this, R.anim.transla_checkbox)
    view.startAnimation(animation)
}

/**
 * ??????BottomNavigation???????????? ?????????????????????Toast ---- ?????????????????????????????????bug
 * @receiver BottomNavigationViewEx
 * @param ids IntArray
 */
fun BottomNavigationView.interceptLongClick(vararg ids:Int) {
    val bottomNavigationMenuView: ViewGroup = (this.getChildAt(0) as ViewGroup)
    for (index in ids.indices){
        bottomNavigationMenuView.getChildAt(index).findViewById<View>(ids[index]).setOnLongClickListener {
            true
        }
    }
}

fun BottomNavigationView.init(navigationItemSelectedAction: (Int) -> Unit): BottomNavigationView {
    itemIconTintList = null//SettingUtil.getColorStateList(SettingUtil.getColor(appContext))
    itemTextColor = SettingUtil.getColorStateList(appContext)
    setOnNavigationItemSelectedListener {
        navigationItemSelectedAction.invoke(it.itemId)
        true
    }
    return this
}

fun ViewPager2.initMain(fragment: Fragment): ViewPager2 {
    //???????????????
    this.isUserInputEnabled = false
    this.offscreenPageLimit = 5
    //???????????????
    adapter = object : FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return WritingFragment()
                }
                1 -> {
                    return MessageFragment()
                }
                2 -> {
                    return DataFragment()
                }
                3 -> {
                    return MeFragment()
                }
                else -> {
                    return WritingFragment()
                }
            }
        }
        override fun getItemCount() = 4
    }
    return this
}

fun Toolbar.initTitleWeb(title:String?, onBack: (toolbar: Toolbar) -> Unit){
    val titleView =  findViewById<TextView>(R.id.tvTitle)
    titleView?.text = title
    val buttonBack =  findViewById<ImageView>(R.id.imageBack)
    buttonBack?.clickWithDebounce {    onBack.invoke(this) }
}

fun Fragment.initTitleWeb(title:String?, onBack: (fragment: Fragment) -> Unit){
    val titleView =  this.activity?.findViewById<TextView>(R.id.tvTitle)
    titleView?.text = title
    val buttonBack =  this.activity?.findViewById<ImageView>(R.id.imageBack)
    buttonBack?.clickWithDebounce {    onBack.invoke(this) }
}

//???????????????Recyclerview
fun SwipeRecyclerView.init(
    layoutManger: RecyclerView.LayoutManager,
    isScroll: Boolean = true
): SwipeRecyclerView {
    layoutManager = layoutManger
    setHasFixedSize(true)
    isNestedScrollingEnabled = isScroll
    return this
}

//??????SwipeRecyclerView
fun SwRecyclerView.init(
    layoutManger: RecyclerView.LayoutManager,
    bindAdapter: RecyclerView.Adapter<*>,
    isScroll: Boolean = true
): SwRecyclerView {
    layoutManager = layoutManger
    setHasFixedSize(true)
    adapter = bindAdapter
    isNestedScrollingEnabled = isScroll
    return this
}

/**
 * ?????????????????????
 * */
fun Fragment.showInputPopup(
    title: String,
    textDefault:String="",
    num: Int = 5,
    popupCallback: PopupCallback
) {
    activity?.let {
        XPopup.Builder(it)
            .moveUpToKeyboard(true)
            .asCustom(AddLabelPopup(it,title,textDefault,num,popupCallback))
            .show()
    }

}


//???????????????Recyclerview
fun SwRecyclerView.init(
    layoutManger: RecyclerView.LayoutManager,
    isScroll: Boolean = true
): SwRecyclerView {
    layoutManager = layoutManger
    setHasFixedSize(true)
    isNestedScrollingEnabled = isScroll
    return this
}



//????????? SwipeRefreshLayout
fun SwipeRefreshLayout.init(onRefreshListener: () -> Unit) {
    this.run {
        setOnRefreshListener {
            onRefreshListener.invoke()
        }
        //??????????????????
        setColorSchemeColors(SettingUtil.getColor(appContext))
    }
}


fun SwRecyclerView.initFooter(loadmoreListener: SwRecyclerView.LoadMoreListener): DefineLoadMoreView {
    val footerView = DefineLoadMoreView(appContext)
    //?????????????????????
    footerView.setLoadViewColor(SettingUtil.getOneColorStateList(appContext))
    //????????????????????????
    footerView.setmLoadMoreListener(SwRecyclerView.LoadMoreListener {
        footerView.onLoading()
        loadmoreListener.onLoadMore()
    })
    this.run {
        //????????????????????????
        setLoadMoreListener(loadmoreListener)
    }
    return footerView
}



fun RecyclerView.initFloatBtn(floatbtn: FloatingActionButton) {
    //??????recyclerview?????????????????????????????????????????????????????????????????????
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        @SuppressLint("RestrictedApi")
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!canScrollVertically(-1)) {
                floatbtn.visibility = View.INVISIBLE
            }
        }
    })
    floatbtn.backgroundTintList = SettingUtil.getOneColorStateList(appContext)
    floatbtn.setOnClickListener {
        val layoutManager = layoutManager as LinearLayoutManager
        //????????????recyclerview ?????????????????????????????????????????????40????????????????????????????????????????????????????????????????????????
        if (layoutManager.findLastVisibleItemPosition() >= 40) {
            scrollToPosition(0)//?????????????????????????????????(??????)
        } else {
            smoothScrollToPosition(0)//??????????????????????????????(?????????)
        }
    }
}
fun AppCompatActivity.showMessage(
    message: String,
    title: String = "????????????",
    positiveButtonText: String = "??????",
    positiveAction: () -> Unit = {},
    negativeButtonText: String = "",
    negativeAction: () -> Unit = {}
) {
    MaterialDialog(this)
        .cancelable(true)
        .lifecycleOwner(this)
        .show {
            title(text = title)
            message(text = message)
            positiveButton(text = positiveButtonText) {
                positiveAction.invoke()
            }
            if (negativeButtonText.isNotEmpty()) {
                negativeButton(text = negativeButtonText) {
                    negativeAction.invoke()
                }
            }
            getActionButton(WhichButton.POSITIVE).updateTextColor(SettingUtil.getColor(this@showMessage))
            getActionButton(WhichButton.NEGATIVE).updateTextColor(getColor(R.color.tanchuansdgawe))
        }
}

/**
 * ??????????????????
 */
fun <T> loadListData2(
    data: ListDataUiState<T>,
    baseQuickAdapter: BaseQuickAdapter<T, *>,
    loadService: LoadService<*>,
    recyclerView: SwRecyclerView,
    swipeRefreshLayout: SwipeRefreshLayout,
    type:String = ""
) {
    swipeRefreshLayout.isRefreshing = false
    recyclerView.loadMoreFinish(data.isEmpty, data.hasMore)
    if (data.isSuccess) {
        //??????
        when {
            //???????????????????????? ?????????????????????
            data.isFirstEmpty -> {
                loadService.showEmpty(type)
            }
            //????????????
            data.isRefresh -> {
                baseQuickAdapter.setList(data.listData)
                loadService.showSuccess()
            }
            //???????????????
            else -> {
                baseQuickAdapter.addData(data.listData)
                loadService.showSuccess()
            }
        }
    } else {
        //??????
        if (data.isRefresh) {
            //??????????????????????????????????????????????????????????????????
            loadService.showError(data.errMessage)
        } else {
            recyclerView.loadMoreError(0, data.errMessage)
        }
    }
}

/**
 * ??????????????????
 */
fun <T> loadListData(
    data: ListDataUiState<T>,
    loadService: LoadService<*>,
    recyclerView: SwRecyclerView,
    swipeRefreshLayout: SwipeRefreshLayout,
    type:String = ""
) {
    swipeRefreshLayout.isRefreshing = false
    recyclerView.loadMoreFinish(data.isEmpty, data.hasMore)
    if (data.isSuccess) {
        //??????
        when {
            //???????????????????????? ?????????????????????
            data.isFirstEmpty -> {
                loadService.showEmpty(type)
            }
            //????????????
            data.isRefresh -> {
                recyclerView.models = data.listData
                loadService.showSuccess()
            }
            //???????????????
            else -> {
                recyclerView.addModels(data.listData)
                loadService.showSuccess()
            }
        }
    } else {
        //??????
        if (data.isRefresh) {
            //??????????????????????????????????????????????????????????????????
            loadService.showError(data.errMessage)
        } else {
            recyclerView.loadMoreError(0, data.errMessage)
        }
    }
}

/**
 * ??????????????????
 */
fun <T> loadListData(
    data: ListDataUiState<T>,
    recyclerView: RecyclerView,
    page: PageRefreshLayout
) {
    if(data.isRefresh){
        page.showLoading()
    }
    if (data.isSuccess) {
        //??????
        when {
            //???????????????????????? ?????????????????????
            data.isFirstEmpty -> {
                page.showEmpty()
            }
            //????????????
            data.isRefresh -> {
                page.showContent()
                recyclerView.models = data.listData
            }
            //???????????????
            else -> {
                recyclerView.addModels(data.listData)
            }
        }
    } else {
        //??????
    }
}

/**
 * ???????????????
 */
fun LoadService<*>.showLoading() {
    this.showCallback(LoadingCallback::class.java)
}

fun Context.showMobilePopup(view:View,onCallback :((Boolean) -> Unit)){
    XPopup.Builder(appContext)
        .offsetX(90)
        .hasShadowBg(false)
        .atView(view)
        .moveUpToKeyboard(false)
        .asCustom(MobilePopup(this,onCallback))
        .show()
}

fun Context.changeDialog(title:String,default:String="",onCallback :((String) -> Unit)){
    XPopup.Builder(appContext)
        .moveUpToKeyboard(false)
        .asCustom(ChangeTextPopup(this,title,default,onCallback))
        .show()
}

fun Fragment.showUserService(onCallback :((Boolean) -> Unit)){
    XPopup.Builder(appContext)
        .moveUpToKeyboard(false)
        .dismissOnTouchOutside(false)
        .isRequestFocus(false)
        .autoFocusEditText(true)
        .asCustom(UserServicePopup(this,onCallback))
        .show()
}

fun Activity.changePenNameDialog(onCallback :((String) -> Unit)){
    XPopup.Builder(appContext)
        .moveUpToKeyboard(false)
        .dismissOnTouchOutside(false)
//        .isRequestFocus(false)
//        .autoFocusEditText(true)
        .asCustom(ChangePenTextPopup(this,onCallback))
        .show()
}

fun loadServiceInit(view: View, callback: () -> Unit): LoadService<Any> {
    val loadsir = LoadSir.getDefault().register(view) {
        //??????????????????????????????
        callback.invoke()
    }
    loadsir.showSuccess()
    SettingUtil.setLoadingColor(SettingUtil.getColor(appContext), loadsir)
    return loadsir
}

/**
 * ???????????????
 */
fun LoadService<*>.showEmpty(type:String="") {
    when (type){
        SysConfig.NUll_HOME_DATA->  this.showCallback(EmptyDataCallback::class.java)
        SysConfig.NUll_CHAPTER_LIST-> this.showCallback(EmptyChapterCallback::class.java)
        SysConfig.NUll_WRItING_LIST-> this.showCallback(EmptyWritingCallback::class.java)
        SysConfig.NUll_RIli_LIST-> this.showCallback(EmptyRiliallback::class.java)
        SysConfig.NUll_MESSAGE_LIST-> this.showCallback(EmptyMessageback::class.java)
        else-> this.showCallback(EmptyCallback::class.java)
    }
}



/**
 * ??????????????????
 * @param message ?????????????????????????????????
 */
fun LoadService<*>.showError(message: String = "") {
    this.setErrorText(message)
    this.showCallback(ErrorCallback::class.java)
}

fun LoadService<*>.showPenName(message: String = "") {
    this.setErrorPanNameText(message)
    this.showCallback(ErrorPenNameCallback::class.java)
}

fun LoadService<*>.setErrorText(message: String) {
    if (message.isNotEmpty()) {
        this.setCallBack(ErrorCallback::class.java) { _, view ->
            view.findViewById<TextView>(R.id.error_text).text = message
        }
    }
}

fun LoadService<*>.setErrorPanNameText(message: String) {
    if (message.isNotEmpty()) {
        this.setCallBack(ErrorPenNameCallback::class.java) { _, view ->
            view.findViewById<TextView>(R.id.error_text).text = message
        }
    }
}

fun <T> BaseVmFragment<*>.parseState(
    resultState: ResultState<T>,
    swipeRefreshLayout: SwipeRefreshLayout,
    loadService: LoadService<*>,
    onSuccess: (T) -> Unit,
    onError: ((AppException) -> Unit)? = null,
    loadType: String  = "",
    onLoading: ((message:String) -> Unit)? = null
) {
    when (resultState) {
        is ResultState.Loading -> {
            if(onLoading==null){
                showLoading(resultState.loadingMessage)
            }else{
                onLoading.invoke(resultState.loadingMessage)
            }
        }
        is ResultState.Success -> {
            swipeRefreshLayout.isRefreshing = false
            dismissLoading()
            if(null!=resultState.data){
                loadService.showSuccess()
                onSuccess(resultState.data)
            }else{
                loadService.showEmpty(loadType)
            }
        }

        is ResultState.Error -> {
            swipeRefreshLayout.isRefreshing = false
            dismissLoading()
            if(loadType==SysConfig.NUll_WRItING_LIST){
                loadService.showPenName(resultState.error.errorMsg)
            }else{
                loadService.showError(resultState.error.errorMsg)
            }
            onError?.run { this(resultState.error) }
        }
    }
}

fun <T> BaseVmFragment<*>.parseState(
    resultState: ResultState<T>,
    loadService: LoadService<*>,
    onSuccess: (T) -> Unit,
    onError: ((AppException) -> Unit)? = null,
    loadType: String  = "",
    onLoading: ((message:String) -> Unit)? = null
) {
    when (resultState) {
        is ResultState.Loading -> {
            if(onLoading==null){
                showLoading(resultState.loadingMessage)
            }else{
                onLoading.invoke(resultState.loadingMessage)
            }
        }
        is ResultState.Success -> {
            dismissLoading()
            if(null!=resultState.data){
                loadService.showSuccess()
                onSuccess(resultState.data)
            }else{
                loadService.showEmpty(loadType)
            }
        }

        is ResultState.Error -> {
            dismissLoading()
            if(loadType==SysConfig.NUll_WRItING_LIST){
                loadService.showPenName(resultState.error.errorMsg)
            }else{
                loadService.showError(resultState.error.errorMsg)
            }
            onError?.run { this(resultState.error) }
        }
    }
}