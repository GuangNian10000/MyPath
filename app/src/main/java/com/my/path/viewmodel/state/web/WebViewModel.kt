package com.my.path.viewmodel.state.web

import me.guangnian.mvvm.base.viewmodel.BaseViewModel


/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　:webView的ViewModel
 */
class WebViewModel : BaseViewModel() {
    //标题
    var showTitle: String = ""
    //网络访问路径
    var url: String = ""
}