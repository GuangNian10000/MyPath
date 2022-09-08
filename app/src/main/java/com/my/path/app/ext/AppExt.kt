package com.my.path.app.ext

import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.my.path.R
import com.my.path.app.network.ApiService
import com.my.path.app.util.SettingUtil
import com.my.path.data.interfaces.PopupShieldingCallback
import com.my.path.ui.dialog.ShieldingDialog
import com.hjq.toast.ToastUtils
import com.lxj.xpopup.XPopup
import com.mallotec.reb.localeplugin.utils.LocaleHelper
import me.guangnian.mvvm.base.appContext
import me.guangnian.mvvm.ext.util.screenHeight
import me.guangnian.mvvm.ext.util.screenWidth
import me.guangnian.mvvm.util.es.EasyPermission
import me.guangnian.mvvm.util.es.GrantResult
import me.guangnian.mvvm.util.es.Permission
import me.guangnian.mvvm.util.es.PermissionRequestListener
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * @author GuangNian
 * @description:
 * @date : 2022/7/28 7:11 下午
 */

fun strToIntNULL(str:String?):String{
    if(null==str){
        return "0"
    }
    if(""==str){
        return "0"
    }
    return str
}

@Synchronized
fun getVersionName(context: Activity?): String? {
    try {
        context?.let {
            val packageManager: PackageManager = context.getPackageManager()
            val packageInfo: PackageInfo = packageManager.getPackageInfo(
                context.packageName, 0
            )
            return packageInfo.versionName
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

fun Fragment.showMessage(
    message: String,
    title: String = "温馨提示",
    positiveButtonText: String = "确定",
    positiveAction: () -> Unit = {},
    negativeButtonText: String = "",
    negativeAction: () -> Unit = {}
) {
    activity?.let {
        MaterialDialog(it)
            .cancelable(true)
            .lifecycleOwner(viewLifecycleOwner)
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
                getActionButton(WhichButton.POSITIVE).updateTextColor(SettingUtil.getColor(it))
                getActionButton(WhichButton.NEGATIVE).updateTextColor(SettingUtil.getColor(it))
            }
    }
}


fun ifLanguage(locale: Locale):Boolean{
    val countryApp = locale.country
    val languageApp = locale.language
    //是否是中国
    return if(languageApp.contains("cn")||languageApp.contains("CN")||
        languageApp.contains("zh")||languageApp.contains("Zh")){
        //是否为香港台湾
        !(countryApp.contains("TW")||countryApp.contains("tw")
                ||countryApp.contains("HK")||countryApp.contains("hk")
                ||countryApp.contains("HK")||countryApp.contains("hk"))
    }else{
        false
    }
}

/**
 * 获取当前语言
 * true 简体
 * false 繁体
 * */
fun Context.getLanguage():Boolean{
    //系统语言
    val currentSystemLocale =  LocaleHelper.getInstance().getCurrentSystemLocale()
    //app语言
    val currentAppLocale = LocaleHelper.getInstance(). getCurrentAppLocale(this)

    /**
     * 判断是否为跟随系统
     * 没有设置过则跟随系统，否则跟随app内部设置
     * */
    return if(SettingUtil.isSystemLanguages(this)) {
        //跟随系统
        ifLanguage(currentSystemLocale)
    }else{
        //跟随app
        ifLanguage(currentAppLocale)
    }
}

/**
 * 设置简繁语言
 * */
fun Context.setLanguage(isSimplified:Boolean){
    if(isSimplified){
        RetrofitUrlManager.getInstance().setGlobalDomain(ApiService.SERVER_URL_CN)
        LocaleHelper.getInstance()
            .language(Locale.CHINESE)
            .apply(this)
    }else{
        RetrofitUrlManager.getInstance().setGlobalDomain(ApiService.SERVER_URL_TW)
        LocaleHelper.getInstance()
            .language(Locale.TRADITIONAL_CHINESE)
            .apply(this)
    }
}

fun View.setVisibilityExt(b:Boolean){
    visibility = if(b) View.VISIBLE else View.GONE
}

fun getStrBody(str:String) =
    RequestBody.create("text/plain".toMediaTypeOrNull(), str)

fun getImageBody(file: File) =
    MultipartBody.Part.createFormData("img", file.name, RequestBody.create("multipart/*".toMediaTypeOrNull(), file))


fun getHtmlColor(str:String,color:String):String = "<font color='$color'>$str<font/>"

/**
 * 显示软键盘（输入法）
 *
 * @param activity
 * @param editText
 */
fun Context.showInputMethod(editText: EditText) {
    val inputMethodManager =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)

}

fun Context.hideSoftKeyboard(editText: EditText) {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
}


//提示框
fun makeToast(str:String?){
    str?.let {
        if(""!=it){
            ToastUtils.setView(R.layout.toast_custom_view)
            ToastUtils.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL)
            //  ToastUtils.setGravity(Gravity.CENTER_HORIZONTAL)
            ToastUtils.show(str)
        }
    }

}

/**
 * 存储
 * 权限请求
 * */
fun Activity.requestPermissionSTORAGE(){
    EasyPermission.with(this)
        .addPermissions(Permission.READ_EXTERNAL_STORAGE)
        .addPermissions(Permission.WRITE_EXTERNAL_STORAGE)
        .request(object : PermissionRequestListener() {
            override fun onGrant(result: Map<String, GrantResult>) {
                //权限申请返回
                if(result[Permission.READ_EXTERNAL_STORAGE]?.value!=0||result[Permission.WRITE_EXTERNAL_STORAGE]?.value!=0){
                    //finish()
                }
            }

            override fun onCancel(stopPermission: String) {
                //在addRequestPermissionRationaleHandler的处理函数里面调用了NextAction.next(NextActionType.STOP,就会中断申请过程，直接回调到这里来
            }
        })
}

/**
 * 隐藏软键盘
 */
fun hideSoftKeyboard(activity: Activity?) {
    activity?.let { act ->
        val view = act.currentFocus
        view?.let {
            val inputMethodManager =
                act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}



//将px值转换为dip或dp值，保证尺寸大小不变
fun Context.pxToDp(pxValue: Float): Int {
    val scale: Float = resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

//将dip或dp值转换为px值，保证尺寸大小不变
fun Context.dpToPx(dipValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (dipValue * scale + 0.5f).toInt()
}

//将px值转换为sp值，保证文字大小不变
fun Context.pxToSp(pxValue: Float): Int {
    val fontScale = resources.displayMetrics.scaledDensity
    return (pxValue / fontScale + 0.5f).toInt()
}

//将sp值转换为px值，保证文字大小不变
fun Context.spToPx(spValue: Float): Int {
    val fontScale = resources.displayMetrics.scaledDensity
    return (spValue * fontScale + 0.5f).toInt()
}

val <T> T.dp: Float by lazy {
    Resources.getSystem()?.displayMetrics?.density ?: 0f
}

val <T> T.dpi: Int by lazy {
    Resources.getSystem()?.displayMetrics?.density?.toInt() ?: 0
}


fun designWidth(w:Float) = (appContext.screenWidth*w)/360

fun designHeight(h:Float) = (appContext.screenHeight*h)/760

//去空格和换行
fun getStringNoBlank(str: String): String {
    return if ("" != str) {
        val p: Pattern = Pattern.compile("\\s*|\t|\r|\n")
        val m: Matcher = p.matcher(str)
        m.replaceAll("")
    } else {
        str
    }
}

/**
 * 获取年
 * */
fun getYear(): String {
    return if (android.os.Build.VERSION.SDK_INT >= 24){
        SimpleDateFormat("yyyy").format(Date())
    }else{
        val tms = Calendar.getInstance()
        tms.get(Calendar.YEAR).toString() + "-" + tms.get(Calendar.MONTH).toString() + "-" + tms.get(Calendar.DAY_OF_MONTH).toString() + " " + tms.get(Calendar.HOUR_OF_DAY).toString() + ":" + tms.get(Calendar.MINUTE).toString() +":" + tms.get(Calendar.SECOND).toString() +"." + tms.get(Calendar.MILLISECOND).toString()
    }
}

/**
 * 获取月
 * */
fun getMonth(): String {
    return if (android.os.Build.VERSION.SDK_INT >= 24){
        SimpleDateFormat("M").format(Date())
    }else{
        val tms = Calendar.getInstance()
        tms.get(Calendar.YEAR).toString() + "-" + tms.get(Calendar.MONTH).toString() + "-" + tms.get(Calendar.DAY_OF_MONTH).toString() + " " + tms.get(Calendar.HOUR_OF_DAY).toString() + ":" + tms.get(Calendar.MINUTE).toString() +":" + tms.get(Calendar.SECOND).toString() +"." + tms.get(Calendar.MILLISECOND).toString()
    }
}

/**
 * 获取日
 * */
fun getDay(): String {
    return if (android.os.Build.VERSION.SDK_INT >= 24){
        SimpleDateFormat("dd").format(Date())
    }else{
        var tms = Calendar.getInstance()
        tms.get(Calendar.YEAR).toString() + "-" + tms.get(Calendar.MONTH).toString() + "-" + tms.get(Calendar.DAY_OF_MONTH).toString() + " " + tms.get(Calendar.HOUR_OF_DAY).toString() + ":" + tms.get(Calendar.MINUTE).toString() +":" + tms.get(Calendar.SECOND).toString() +"." + tms.get(Calendar.MILLISECOND).toString()
    }
}

fun getMonthLastDay(year:Int ,month:Int ):Int{
    val a = Calendar.getInstance()
    a[Calendar.YEAR] = year
    a[Calendar.MONTH] = month - 1
    a[Calendar.DATE] = 1 //把日期设置为当月第一天

    a.roll(Calendar.DATE, -1) //日期回滚一天，也就是最后一天

    return a[Calendar.DATE]
}

fun Fragment.showShieldingDialog(uid:Int,position:Int,popupShieldingCallback: PopupShieldingCallback){
    XPopup.Builder(context)
        .asCustom(ShieldingDialog(this,uid ,position,popupShieldingCallback))
        .show()
}

