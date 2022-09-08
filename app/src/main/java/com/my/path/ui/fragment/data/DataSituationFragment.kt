package com.my.path.ui.fragment.data

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.viewModels
import com.angcyo.tablayout.isHorizontal
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.config.SysConfig
import com.my.path.app.ext.clickWithDebounce
import com.my.path.app.ext.dpi
import com.my.path.app.ext.getBackgroundExt
import com.my.path.app.util.CacheUtil
import com.my.path.app.util.ImageUtil.loadGaussianImage
import com.my.path.app.util.ImageUtil.loadRoundImage
import com.my.path.app.weight.customview.MyMarkerView
import com.my.path.data.model.bean.*
import com.my.path.databinding.FragmentHomeDataSituationBinding
import com.my.path.ui.dialog.SwitchBookDialog
import com.my.path.viewmodel.request.data.RequestDataViewModel
import com.my.path.viewmodel.state.data.DataSituationViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.lxj.xpopup.XPopup
import me.guangnian.mvvm.ext.parseState


/**
 * @author GuangNian
 * @description:
 * @date : 2022/2/17 11:19 上午
 */
class DataSituationFragment : BaseFragment<DataSituationViewModel, FragmentHomeDataSituationBinding>(),
    OnChartValueSelectedListener {
    private val requestDataViewModel: RequestDataViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {

        addLoadingObserve(requestDataViewModel)

        arguments?.run {
            getParcelable<BaseData>("baseData")?.let {
                mViewModel.baseData.set(it)

                requestDataViewModel.dataSurvey(it.bid)
                setBookNull(it.cover,it.name)
            }
        }

        mDatabind.viewmodel = mViewModel

        mDatabind.click = ProxyClick()

        mDatabind.dslTabLayout.apply {
            configTabLayoutConfig {
                if (orientation.isHorizontal()) {
                    tabIndicator.indicatorWidth = 20 * dpi
                } else {
                    tabIndicator.indicatorHeight = 20 * dpi
                }

                //选中index的回调
                onSelectIndexChange = { fromIndex, selectIndexList, reselect,fromUser ->
                    val toIndex = selectIndexList.first()

                    loadingData()
                }
            }
        }

        mDatabind.viewDataSituation.constraintLayout6.clickWithDebounce {
            ProxyClick().swBook()
        }

        mDatabind.viewDataSituation.frameLayout5.clickWithDebounce {
            ProxyClick().swBook()
        }

        mDatabind.viewDataSituation.imagekk.clickWithDebounce {
            ProxyClick().swBook()
        }
    }

    override fun createObserver() {

        requestDataViewModel.dataSurveyResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                mViewModel.dataSurvey = it

                loadingData()
            })
        }


        requestDataViewModel.dataBaseResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                mViewModel.baseData.get()?.let {baseData->
                    it.data.forEach {
                        if(it.bid ==baseData.bid){
                            it.select = true
                        }
                    }
                    XPopup.Builder(context)
                        .asCustom(SwitchBookDialog(requireActivity(),it.data){ book->
                            setBookNull(book.cover,book.name)
                            mViewModel.baseData.set(book)
                            requestDataViewModel.dataSurvey(book.bid)
                        }).show()
                }

            })
        }
    }

    inner class ProxyClick {
        fun swBook(){
            requestDataViewModel.dataBase()
        }


    }

    //加载曲线图
    fun loadingData(){
//        if(null!=mDatabind.chart.data){
//            mDatabind.chart.clearValues()
//        }
        mViewModel.dataSurvey?.let {
            val index = mDatabind.dslTabLayout.currentItemIndex
            var data : List<dataSur> = ArrayList()

            when(index){
                0-> data =it.hits
                1-> data =it.bookshelf
                2-> data =it.ticket
            }
//            when(index){
//                0-> data =it.ticket
//                1-> data =it.ticket
//                2-> data =it.ticket
//            }

            //加载曲线图
            mDatabind.chart.setOnChartValueSelectedListener(this)
            mDatabind.chart.setTouchEnabled(true)
            mDatabind.chart.isDragEnabled = true
            mDatabind.chart.setScaleEnabled(true)
            mDatabind.chart.setPinchZoom(true)
            mDatabind.chart.description.isEnabled = false
            mDatabind.chart.setDrawGridBackground(false)

            val xAxis: XAxis = mDatabind.chart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(true)
            xAxis.setDrawAxisLine(false)
            //设置x启使值
            xAxis.axisMinimum = -0.5f
            xAxis.axisMaximum = data.size.toFloat()-0.8f
            xAxis.gridColor = context?.getColor(R.color.asdiaw)!!
            xAxis.axisLineColor =context?.getColor(R.color.asdiaw)!!
            xAxis.enableGridDashedLine(10f, 10f, 0f)
            xAxis.textColor = context?.getColor(R.color.text_noasd)!!
            xAxis.spaceMin = 10.2f
            xAxis.spaceMax = 10.2f
            xAxis.textSize = 6f;//设置文字大小


            //左Y轴
            val leftAxis: YAxis = mDatabind.chart.axisLeft
            leftAxis.setLabelCount(5, false)
            leftAxis.setDrawGridLines(true)
            leftAxis.setDrawLabels(false)
            leftAxis.setDrawAxisLine(false)
            leftAxis.axisMinimum = 2f // this replaces setStartAtZero(true)
            leftAxis.setStartAtZero(false)
            leftAxis.setDrawZeroLine(false)
            // horizontal grid lines
            //leftAxis.enableGridDashedLine(10f, 10f, 0f)
            leftAxis.gridColor = context?.getColor(R.color.asdiaw)!!
            leftAxis.axisLineColor =context?.getColor(R.color.asdiaw)!!
            //leftAxis.spaceBottom = 16f

            //右Y轴
            val rightAxis: YAxis = mDatabind.chart.axisRight
            rightAxis.setLabelCount(5, false)
            rightAxis.setDrawGridLines(true)
            rightAxis.setDrawLabels(false)
            rightAxis.setDrawAxisLine(false)
            rightAxis.axisMinimum = 2f  // this replaces setStartAtZero(true)
            rightAxis.setStartAtZero(false)
            rightAxis.setDrawZeroLine(false)
            //rightAxis.enableGridDashedLine(10f, 10f, 0f)
            rightAxis.gridColor = context?.getColor(R.color.asdiaw)!!
            rightAxis.axisLineColor =context?.getColor(R.color.asdiaw)!!
            //rightAxis.spaceBottom = 16f


            //隐藏图例
            mDatabind.chart.legend.isEnabled = false;

            //限制放大
            mDatabind.chart.viewPortHandler.setMaximumScaleX(2.0f); //限制X轴放大限制
            mDatabind.chart.viewPortHandler.setMaximumScaleY(2.0f);// 限制Y轴放大限制

            // set data
            mDatabind.chart.data = generateDataLine(data)
            mDatabind.chart.setExtraBottomOffset(15f)
            mDatabind.chart.animateX(750)
        }

    }

    private fun generateDataLine(data : List<dataSur>): LineData {
        val values1 = ArrayList<Entry>()
        val xList :ArrayList<String> = ArrayList()
        for (i in 0..data.size-1) {
            values1.add(
                Entry(
                    i.toFloat(),
                    ((data[i].num)).toFloat()
                )
            )

            xList.add(data[i].getDateG())
        }

        //点击弹窗
        val mv = MyMarkerView(activity,xList, R.layout.custom_marker_view)
        mv.chartView = mDatabind.chart // For bounds control
        mDatabind.chart.marker = mv // Set the marker to the chart


        //设置x轴上的标签
        mDatabind.chart.xAxis.valueFormatter = LabelFormatter(xList)


        val d1 = LineDataSet(values1,"")
        d1.lineWidth = 2f
        d1.circleRadius = 4f
        d1.highLightColor = Color.rgb(255,84,76)
        d1.setDrawValues(true)
        d1.color =context?.getColor(R.color.base_color)!!
        d1.setCircleColor(context?.getColor(R.color.base_color)!!)

        d1.valueTextColor = context?.getColor(R.color.base_color)!!
        d1.valueTextSize = 10f
        d1.valueFormatter = LabelPointTextFormatter(values1)

        val sets = ArrayList<ILineDataSet>()
        sets.add(d1)
        return LineData(sets)
    }


    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }

    override fun onNothingSelected() {
    }

    fun setBookNull(cover:String,bookName:String){
        val tvUserName = mDatabind.viewDataSituation.viewBookCover.tvUserName
        val bookNameCover = mDatabind.viewDataSituation.viewBookCover.bookNameCover
        val imageBook =  mDatabind.viewDataSituation.viewBookCover.bookCover
        bookNameCover.setTextSizeSp(8f)
        tvUserName.setTextSizeSp(6f)
        if(""!=cover){
            tvUserName.post {
                val bool = tvUserName.canFillFull( "")
                if(bool){
                    tvUserName.setText("")
                }
            }
            bookNameCover.post {
                val bool = bookNameCover.canFillFull( "")
                if(bool){
                    bookNameCover.setText("")
                }
            }
            requireContext().loadRoundImage(cover, imageBook, SysConfig.BookRounded)
            requireContext().loadGaussianImage(cover,  mDatabind.viewDataSituation.imagebg)
        }else{
            tvUserName.post {
                val text = CacheUtil.getUserData()?.author+" 著"
                val bool = tvUserName.canFillFull(text)
                if(bool){
                    tvUserName.setText(text)
                }
            }
            bookNameCover.post {
                val bool = bookNameCover.canFillFull(bookName)
                if(bool){
                    bookNameCover.setText(bookName)
                }
            }
            requireContext().loadRoundImage(R.drawable.ic_morenstudawesd, imageBook,
                SysConfig.BookRounded
            )
            mDatabind.viewDataSituation.imagebg.setImageDrawable(getBackgroundExt(R.drawable.bg_moren_book_s))
        }

    }

}