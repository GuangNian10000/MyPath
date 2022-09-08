package com.my.path.app.util.ait

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.fragment.app.Fragment
import com.my.path.R
import com.my.path.app.App
import java.util.*
import java.util.regex.Pattern

/**
 * Create by peng on 2019/7/5
 */
object AitpeopleUtil {
    private const val TAG = "AitpeopleUtil"

    //private static Pattern PATTERN = Pattern.compile("(?<=\\{\\[)(.+?)(?=\\]\\})");
    private val PATTERN =
        Pattern.compile("<a [^>]*?href=(?<quot>[\"']?)(?<value>[^>]+?)\\k<quot>(>| [^>]*?>)(?<text>.+?)</a>")
    private val pattern = Pattern.compile("<a.*?href=\"(.*?)\".*?>(.*?)</a>")

    private val PATTERN_TOP =
        Pattern.compile("<em [^>]*?(>| [^>]*?>)(?<text>.+?)</em>")
    private val pattern_TOP = Pattern.compile("<em.*?data-id=\"(.*?)\".*?>(.*?)</em>")

    private val PATTERN_IMG =
        Pattern.compile("<img [^>]*?(>| [^>]*?>)(?<text>.+?)</img>")

    private val pattern_IMG = Pattern.compile("<img.*?data-id=\"(.*?)\".*?>(.*?)</img>")

    private val PATTERN_EM_D =
        Pattern.compile("<em>(?<text>.+?)</em>")

    private val PATTERN_EM =
        Pattern.compile("<em>(.*?)</em>")

    /**
     * 将符合格式{[@wawa,99]} 替换为文字@wawa,并将名字和id存到集合对象中
     * @param content
     * @param activity
     * @return spannableString
     */
    fun getSpStr(activity: Fragment, content: String): SpannableString {
        val aitList: MutableList<AtBean> = ArrayList()
        try {

        }catch (e:Exception){}
        val matcher = PATTERN.matcher(content)
        var text = ""

        //----------------------------------@----------------------------------------

        while (matcher.find()) {
            val target = matcher.group()
            val Ma = pattern.matcher(matcher.group())
            while (Ma.find()) {
                val uId = Objects.requireNonNull(Ma.group(1)).replace("/profile/", "")
                val name = "@ " + Ma.group(2)
                // 把文字块内容保存到集合中，索引位置暂时不保存
                val bean = AtBean(abnormalTextToInt(uId, 0).toString().toInt(), name, 0, 0)
                aitList.add(bean)
                assert(name != null)
                text = if (text == "") {
                    content.replace(target, name)
                } else {
                    text.replace(target, name)
                }
                val locStart = content.indexOf(name)
                val locEnd = content.lastIndexOf(name)
            }
        }

        //---------------屏蔽词---------
        val matcher4 = PATTERN_EM_D.matcher(content)
        while (matcher4.find()) {
            val target = matcher4.group()
            val Ma = PATTERN_EM.matcher(matcher4.group())
            while (Ma.find()) {
                val name = Ma.group(1)//Objects.requireNonNull(Ma.group(1))
                // 把文字块内容保存到集合中，索引位置暂时不保存
                val bean = AtBean(abnormalTextToInt("", 0).toString().toInt(), name, 0, 0)
                aitList.add(bean)
                assert(name != null)
                text = if (text == "") {
                    content.replace(target, name)
                } else {
                    text.replace(target, name)
                }
                val locStart = content.indexOf(name)
                val locEnd = content.lastIndexOf(name)
            }
        }

        //----------------------------------话题----------------------------------------
        //    if(text==""){
        val matcher2 = PATTERN_TOP.matcher(content)
        while (matcher2.find()) {
                val target = matcher2.group()
                val Ma = pattern_TOP.matcher(matcher2.group())
                while (Ma.find()) {
                    val uId = Objects.requireNonNull(Ma.group(1))
                    val name =Ma.group(2)
                    // 把文字块内容保存到集合中，索引位置暂时不保存
                    val bean = AtBean(abnormalTextToInt(uId, 0).toString().toInt(), name, 0, 0)
                    aitList.add(bean)
                    assert(name != null)
                    text = if (text == "") {
                        content.replace(target, name)
                    } else {
                        text.replace(target, name)
                    }
                    val locStart = content.indexOf(name)
                    val locEnd = content.lastIndexOf(name)
                }
            }
        //  }

        //----------------------------------图----------------------------------------
        val matcher3 = PATTERN_IMG.matcher(content)
        while (matcher3.find()) {
            val target = matcher3.group()
            val Ma = pattern_IMG.matcher(matcher3.group())
            while (Ma.find()) {
                val imgSrc = Objects.requireNonNull(Ma.group(1))
                val name = Ma.group(2)//
                // 把文字块内容保存到集合中，索引位置暂时不保存
                val bean = AtBean(imgSrc, name, 0, 0)
                aitList.add(bean)
                assert(name != null)
                text = if (text == "") {
                    content.replace(target, name)
                } else {
                    text.replace(target, name)
                }
                val locStart = content.indexOf(name)
                val locEnd = content.lastIndexOf(name)
            }
        }


        val lastStr = if (text == "") content else text
        // 这里是把所有文字块全部替换为需要显示的文字，然后在找文字块索引
        getAtBeanList(lastStr, aitList)
        return getClickSpannableString(activity,lastStr, aitList)
    }

    //将异常的字符串转化为整数
    fun abnormalTextToInt(text: String?, defaultValue: Int): Int {
        return try {
            Pattern.compile("[^0-9]").matcher(text).replaceAll("").trim { it <= ' ' }.toInt()
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 将符合格式{[@wawa,99]} 替换为文字@wawa,并将名字和id存到集合对象中
     * @param content
     * @param activity
     * @return spannableString
     */
    fun getSpStr(fragment: Fragment,content: String, aitEditText: AitEditText,  onBack: () -> Unit): SpannableString {
        val aitList: MutableList<AtBean> = ArrayList()
        val matcher = PATTERN.matcher(content)
        var text = ""
        var start = 0
        while (matcher.find()) {
            val target = matcher.group()
            val Ma = pattern.matcher(matcher.group())
            while (Ma.find()) {
                val uId = Objects.requireNonNull(Ma.group(1)).replace("/profile/", "")
                val name = "@ " + Ma.group(2)
                // 把文字块内容保存到集合中，索引位置暂时不保存
                val bean = AtBean(uId.toInt(), name, 0, 0)
                aitList.add(bean)
                assert(name != null)
                text = if (text == "") {
                    content.replace(target, name)
                } else {
                    text.replace(target, name)
                }
                val locStart = text.indexOf(name, start)
                start = locStart + name.length
                val formatRangBean = FormatRangBean(locStart, locStart + name.length)
                formatRangBean.uploadFormatText = target
                aitEditText.mRangeManager.add(formatRangBean)
            }
        }
        //if(text==""){
            val matcher2 = PATTERN_TOP.matcher(content)
            while (matcher2.find()) {
                val target = matcher2.group()
                val Ma = pattern_TOP.matcher(matcher2.group())
                while (Ma.find()) {
                    val uId = Objects.requireNonNull(Ma.group(1))
                    val name =Ma.group(2)
                    // 把文字块内容保存到集合中，索引位置暂时不保存
                    val bean = AtBean(abnormalTextToInt(uId, 0).toString().toInt(), name, 0, 0)
                    aitList.add(bean)
                    assert(name != null)
                    text = if (text == "") {
                        content.replace(target, name)
                    } else {
                        text.replace(target, name)
                    }
                    val locStart = text.indexOf(name, start)
                    start = locStart + name.length
                    val formatRangBean = FormatRangBean(locStart, locStart + name.length)
                    formatRangBean.uploadFormatText = target
                    aitEditText.mRangeManager.add(formatRangBean)
                }
            }
      //  }
        //----------------------------------图----------------------------------------
        val matcher3 = PATTERN_IMG.matcher(content)
        while (matcher3.find()) {
            val target = matcher3.group()
            val Ma = pattern_IMG.matcher(matcher3.group())
            while (Ma.find()) {
                val imgSrc = Objects.requireNonNull(Ma.group(1))
                val name =  Objects.requireNonNull(Ma.group(2))//Ma.group(2)
                // 把文字块内容保存到集合中，索引位置暂时不保存
                val bean = AtBean(imgSrc, name, 0, 0)
                aitList.add(bean)
                assert(name != null)
                text = if (text == "") {
                    content.replace(target, name)
                } else {
                    text.replace(target, name)
                }
                val locStart = text.indexOf(name, start)
                start = locStart + name.length
                val formatRangBean = FormatRangBean(locStart, locStart + name.length)
                formatRangBean.uploadFormatText = target
                aitEditText.mRangeManager.add(formatRangBean)
            }
        }


        //---------------屏蔽词---------
        val matcher4 = PATTERN_EM_D.matcher(content)
        while (matcher4.find()) {
            val target = matcher4.group()
            val Ma = PATTERN_EM.matcher(matcher4.group())
            while (Ma.find()) {
                val name = Ma.group(1)//Objects.requireNonNull(Ma.group(1))
                // 把文字块内容保存到集合中，索引位置暂时不保存
                val bean = AtBean("", name, 0, 0)
                aitList.add(bean)
                assert(name != null)
                text = if (text == "") {
                    content.replace(target, name)
                } else {
                    text.replace(target, name)
                }
                val locStart = text.indexOf(name, start)
                start = locStart + name.length
                val formatRangBean = FormatRangBean(locStart, locStart + name.length)
                formatRangBean.uploadFormatText = target
                aitEditText.mRangeManager.add(formatRangBean)
            }
        }

        val lastStr = if (text == "") content else text
        // 这里是把所有文字块全部替换为需要显示的文字，然后在找文字块索引
        getAtBeanList(lastStr, aitList)
        return getClickSpannableString(fragment,lastStr, aitList)
    }

    /**
     * 查找文字块索引
     * @param str
     * @param aitList
     */
    fun getAtBeanList(str: String, aitList: List<AtBean>) {
        var end = 0
        for (atBean in aitList) {
            val s1 = str.indexOf(atBean.name, end) // 从上次发现文字块索引位置开始往后寻找
            if (s1 == -1) {
                //从头开始查找
                val reS1 = str.indexOf(atBean.name)
                if (reS1 != -1) {
                    atBean.startPos = reS1
                    atBean.endPos = reS1+atBean.name.length
                }
                continue
            }
            end = s1 + atBean.name.length
            atBean.startPos = s1
            atBean.endPos = end
        }
    }

    /**
     * 给文字块集合设置点击事件
     * @param str
     * @param atBeanList
     * @param activity
     * @return
     */
    fun getClickSpannableString(
        fragment: Fragment,
        str: String?,
        atBeanList: List<AtBean>
    ): SpannableString {
        val spannableStr = SpannableString(str)
        for (atBean in atBeanList) {
            val clickableSpan = Clickable({ v: View? ->
                   //onBack.invoke()

//                    if (atBean.name.startsWith("@")){
//                        fragment.inUserHomeFragmentType(atBean.id)
//                    }
//                    else if(null!=atBean.imgSrc)  fragment.inPictureViewerFragment(atBean.imgSrc)
//                    else if(atBean.name.startsWith("#")){
//                        fragment.inDynamicChildPageFragment(atBean.id,atBean.name)
//                    }
                },
                R.color.base_color
//                if (atBean.name.startsWith("@")) R.color.book_comments_text_back
//                else if(null!=atBean.imgSrc)   R.color.book_comments_text_back
//                else if(atBean.name.startsWith("#")) R.color.book_comments_text_back
//                else R.color.registered_text
            )
            spannableStr.setSpan(
                clickableSpan,
                atBean.startPos,
                atBean.endPos,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return spannableStr
    }



    class Clickable(private val mListener: View.OnClickListener, _color: Int) : ClickableSpan(),
        View.OnClickListener {
        var color: Int

        // 设置显示样式
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = Objects.requireNonNull(App.instance).getColor(color) // 设置颜色
            ds.isUnderlineText = false // 设置下划线
        }

        override fun onClick(v: View) {
            mListener.onClick(v)
        }

        init {
            color = if (_color == 0) Color.BLUE else _color
        }
    }
}