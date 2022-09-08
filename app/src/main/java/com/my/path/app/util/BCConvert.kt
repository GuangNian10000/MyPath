package com.my.path.app.util

/**
@author GuangNian
@description:   半全角转换
@date : 2021/9/3 11:18 上午
 */
object BCConvert {
    /**
     * ASCII表中可见字符从!开始，偏移位值为33(Decimal)
     */
    private const val DBC_CHAR_START = 33 // 半角!
        .toChar()

    /**
     * ASCII表中可见字符到~结束，偏移位值为126(Decimal)
     */
    private const val DBC_CHAR_END = 126 // 半角~
        .toChar()

    /**
     * 全角对应于ASCII表的可见字符从！开始，偏移值为65281
     */
    private const val SBC_CHAR_START = 65281 // 全角！
        .toChar()

    /**
     * 全角对应于ASCII表的可见字符到～结束，偏移值为65374
     */
    private const val SBC_CHAR_END = 65374 // 全角～
        .toChar()

    /**
     * ASCII表中除空格外的可见字符与对应的全角字符的相对偏移
     */
    private const val CONVERT_STEP = 65248 // 全角半角转换间隔

    /**
     * 全角空格的值，它没有遵从与ASCII的相对偏移，必须单独处理
     */
    private const val SBC_SPACE = 12288 // 全角空格 12288
        .toChar()

    /**
     * 半角空格的值，在ASCII中为32(Decimal)
     */
    private const val DBC_SPACE = ' ' // 半角空格

    /**
     * <PRE>
     * 半角字符->全角字符转换
     * 只处理空格，!到˜之间的字符，忽略其他
    </PRE> *
     */
    fun bj2qj(src: String?): String? {
        if (src == null) {
            return src
        }
        val buf = StringBuilder(src.length)
        val ca = src.toCharArray()
        for (i in ca.indices) {
            when {
                ca[i] == DBC_SPACE -> { // 如果是半角空格，直接用全角空格替代
                    buf.append(SBC_SPACE)
                }
                ca[i] in DBC_CHAR_START..DBC_CHAR_END -> { // 字符是!到~之间的可见字符
                    buf.append((ca[i] + CONVERT_STEP))
                }
                else -> { // 不对空格以及ascii表中其他可见字符之外的字符做任何处理
                    buf.append(ca[i])
                }
            }
        }
        return buf.toString()
    }

    /**
     * 半角转换全角
     *
     * @param src
     * @return
     */
    fun bj2qj(src: Char): Int {
        var r = src.code
        if (src == DBC_SPACE) { // 如果是半角空格，直接用全角空格替代
//            src = SBC_SPACE
        } else if (src in DBC_CHAR_START..DBC_CHAR_END) { // 字符是!到~之间的可见字符
            r = src.code + CONVERT_STEP
        }
        return r
    }


    /**
     * <PRE>
     * 全角字符->半角字符转换
     * 只处理全角的空格，全角！到全角～之间的字符，忽略其他
    </PRE> *
     */
    fun qj2bj(src: String?): String? {
        if (src == null) {
            return src
        }
        val buf = StringBuilder(src.length)
        val ca = src.toCharArray()
        for (i in src.indices) {
            when {
                ca[i] in SBC_CHAR_START..SBC_CHAR_END -> { // 如果位于全角！到全角～区间内
                    buf.append((ca[i] - CONVERT_STEP))
                }
                ca[i] == SBC_SPACE -> { // 如果是全角空格
                    buf.append(DBC_SPACE)
                }
                else -> { // 不处理全角空格，全角！到全角～区间外的字符
                    buf.append(ca[i])
                }
            }
        }
        return buf.toString()
    }

    /**
     * 全角转换半角
     *
     * @param src
     * @return
     */
    fun qj2bj(src: Char): Int {
        var r = src.code
        if (src in SBC_CHAR_START..SBC_CHAR_END) { // 如果位于全角！到全角～区间内
            r = src.code - CONVERT_STEP
        } else if (src == SBC_SPACE) { // 如果是全角空格
            r = DBC_SPACE.code
        }
        return r
    }
}