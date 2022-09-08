package com.my.path.viewmodel.request

import com.my.path.data.repository.request.HttpRequestCoroutine

import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.ext.requestNoCheck

class RequestMainViewModel : BaseViewModel() {
//    var verAndroidResult = MutableLiveData<VerAndroid>()
//
//    fun verAndroid() {
//        requestNoCheck({ HttpRequestCoroutine.verAndroid()},{
//            verAndroidResult.value = it
//        },{},true)
//    }
//
//    fun logDevice() {
//        requestNoCheck({ HttpRequestCoroutine.logDevice(LogPushData().sortingData())},{
//        },{},false)
//    }
//
//    fun logGather(content:String) {
//        requestNoCheck({ HttpRequestCoroutine.logGather(LogPushData().sortingData()+content)},{
//        },{},false)
//    }
//
//
//    fun logChapter(bid:String,cid:String,page:Int,size:String,last:String,time:String) {
//        Log.d("Log_service_areReadingStatistics", "$page---------");
//        if(null==bid||""==bid){
//            return
//        }
//        //读取本地log
//        val readLog = SharedPreUtil.readListObj(SysConfig.readLog)
//        readLog.add(ReadLog(bid,cid,page.toString(),size,last,time))
//        SharedPreUtil.saveListObj(SysConfig.readLog,readLog)
//        if(readLog.size>=20){
//            Log.d("Log_service_areReadingStatistics", Gson().toJson(readLog))
//            SharedPreUtil.saveListObj(SysConfig.readLog,ArrayList())
//            requestNoCheck({ HttpRequestCoroutine.logChapter(readLog.toJson())},{
//            },{},false)
//        }
//    }

    fun firebasePerformance(){
        requestNoCheck({HttpRequestCoroutine.firebasePerformance()},{

        },{})

    }
}