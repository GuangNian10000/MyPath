package com.my.path.ui.adapter.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import com.my.path.R
import com.my.path.data.model.bean.AreaCode
import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination
import java.util.HashMap

/**
 * @date 2019/10/17 16:00
 * 城市选择
 */
class CityAdapter(context: Context, list: List<AreaCode>, ItemOnClick: CityItemOnClick) :
    BaseAdapter() {
    private val list: List<AreaCode>
    private var alphaIndexer: MutableMap<String, Int>
    private var sections: Array<String?>
    private val context: Context
    private val ItemOnClick: CityItemOnClick

    init {
        this.ItemOnClick = ItemOnClick
        //赋值初始化
        this.context = context
        this.list = list
        alphaIndexer = HashMap()
        sections = arrayOfNulls(list.size)

        //把相邻的相同的首字母放到一起,同时首个字母显示
        var startIndex = 0
        if (list.size > 2) {
            startIndex = 2
        }
        for (i in startIndex until list.size) {
            val currentStr = getPinYinFirst(list[i].name)
            val previewStr = if (i - 1 >= 0) getPinYinFirst(list[i - 1].name) else " "
            if (previewStr != currentStr) {
                //前一个首字母与当前首字母不同时加入HashMap中同时显示该字母
                val name = getPinYinFirst(list[i].name)
                alphaIndexer[name] = i
                sections[i] = name
            }
        }
    }


    val cityMap: Map<String, Int>
        get() = alphaIndexer

    fun initData() {
        alphaIndexer = HashMap()
        sections = arrayOfNulls(list.size)

        //把相邻的相同的首字母放到一起,同时首个字母显示
        var startIndex = 0
        if (list.size > 2) {
            startIndex = 2
        }
        for (i in startIndex until list.size) {
            val currentStr = getPinYinFirst(list[i].name)
            val previewStr = if (i - 1 >= 0) getPinYinFirst(list[i - 1].name) else " "
            if (previewStr != currentStr) {
                //前一个首字母与当前首字母不同时加入HashMap中同时显示该字母
                val name = getPinYinFirst(list[i].name)
                alphaIndexer[name] = i
                sections[i] = name
            }
        }
    }


    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView: View? = convertView
        val holder: ViewHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_area_code, null)
            holder = ViewHolder()
            holder.alpha = convertView
                .findViewById<TextView>(R.id.item_city_alpha)
            holder.name = convertView
                .findViewById<TextView>(R.id.item_city_name)
            holder.rl_item = convertView
                .findViewById<RelativeLayout>(R.id.rl_item)
            holder.tvCode = convertView.findViewById<TextView>(R.id.tv_code)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        holder.name?.setText(list[position].name)
        holder.tvCode?.setText("+" + list[position].number)
        val currentStr: String
        if (position == 0) {
            currentStr = "当前"
            //  holder.alpha.setVisibility(View.VISIBLE);
        } else {
            currentStr = getPinYinFirst(list[position].name)
            val previewStr = if (position - 1 >= 0) getPinYinFirst(
                list[position - 1]
                    .name
            ) else " "
            if (previewStr != currentStr) {
                //     holder.alpha.setVisibility(View.VISIBLE);
            } else {
                //  holder.alpha.setVisibility(View.GONE);
            }
        }
        holder.alpha?.setText(currentStr)
        holder.rl_item?.setOnClickListener(View.OnClickListener { view: View? ->
            ItemOnClick.onClick(
                list[position]
            )
        })
        return convertView!!
    }

    private inner class ViewHolder {
        var rl_item: RelativeLayout? = null
        var alpha: TextView? = null
        var name: TextView? = null
        var tvCode: TextView? = null
    }

    companion object {
        fun getPinYinFirst(str: String?): String {
            if (null == str || "" == str) {
                return ""
            }
            val pinyin = getPinYin(str)
            return pinyin.substring(0, 1).toUpperCase()
        }

        /**
         * 汉子转拼音
         * @param src
         * @return
         */
        fun getPinYin(src: String?): String {
            var t1: CharArray? = null
            if (null == src || src == "") {
                return ""
            }
            t1 = src.toCharArray()
            // System.out.println(t1.length);
            var t2 = arrayOfNulls<String>(t1.size)
            // System.out.println(t2.length);
            // 设置汉字拼音输出的格式
            val t3 = HanyuPinyinOutputFormat()
            t3.setCaseType(HanyuPinyinCaseType.LOWERCASE)
            t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE)
            t3.setVCharType(HanyuPinyinVCharType.WITH_V)
            var t4 = ""
            val t0 = t1.size
            try {
                for (i in 0 until t0) {
                    // 判断能否为汉字字符
                    // System.out.println(t1[i]);
                    if (Character.toString(t1[i]).matches(Regex("[\\u4E00-\\u9FA5]+"))) {
                        t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3) // 将汉字的几种全拼都存到t2数组中
                        t4 += t2[0].toString() + " " // 取出该汉字全拼的第一种读音并连接到字符串t4后
                    } else {
                        // 如果不是汉字字符，间接取出字符并连接到字符串t4后
                        t4 += Character.toString(t1[i])
                    }
                }
            } catch (e: BadHanyuPinyinOutputFormatCombination) {
                e.printStackTrace()
            }
            return t4
        }
    }

}