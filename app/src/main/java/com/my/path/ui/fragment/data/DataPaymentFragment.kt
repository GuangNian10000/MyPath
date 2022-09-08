package com.my.path.ui.fragment.data

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.config.SysConfig
import com.my.path.app.ext.clickWithDebounce
import com.my.path.app.ext.getBackgroundExt
import com.my.path.app.util.CacheUtil
import com.my.path.app.util.ImageUtil.loadGaussianImage
import com.my.path.app.util.ImageUtil.loadRoundImage
import com.my.path.app.weight.customview.MyMarkerView
import com.my.path.data.model.bean.*
import com.my.path.databinding.FragmentHomeDataPaymentBinding
import com.my.path.ui.dialog.SwitchBookDialog
import com.my.path.viewmodel.request.data.RequestDataViewModel
import com.my.path.viewmodel.state.data.DataSituationViewModel
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.lxj.xpopup.XPopup
import me.guangnian.mvvm.ext.parseState


/**
 * @author GuangNian
 * @description:
 * @date : 2022/2/17 11:19 上午
 */
class DataPaymentFragment : BaseFragment<DataSituationViewModel, FragmentHomeDataPaymentBinding>(),
    OnChartValueSelectedListener {

    private val requestDataViewModel: RequestDataViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {

        addLoadingObserve(requestDataViewModel)

        arguments?.run {
            getParcelable<BaseData>("baseData")?.let {
                mViewModel.baseData.set(it)
                requestDataViewModel.dataPayment(it.bid)
                requestDataViewModel.dataIncome(it.bid)
                setBookNull(it.cover,it.name)
            }
        }

        mDatabind.viewmodel = mViewModel

        mDatabind.click = ProxyClick()

        mDatabind.recyclerView.linear().setup {
            addType<Income>(R.layout.item_data_payment)
        }.models

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

        requestDataViewModel.dataIncomeResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                if(it.data.size==0){
                    mDatabind.zanwushuj1.visibility = View.VISIBLE
                }else{
                    mDatabind.zanwushuj1.visibility = View.GONE
                }
                mDatabind.recyclerView.models = it.data
            })
        }

        requestDataViewModel.dataPaymentResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                if(it.data.size==0){
                    mDatabind.chart.visibility = View.GONE
                    mDatabind.zanwushuj.visibility = View.VISIBLE
                }else{
                    mDatabind.chart.visibility = View.VISIBLE
                    mDatabind.zanwushuj.visibility = View.GONE
                }
                loadingData(it.data)
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

                            mViewModel.baseData.set(book)
                            requestDataViewModel.dataPayment(book.bid)
                            requestDataViewModel.dataIncome(book.bid)
                            setBookNull(book.cover,book.name)
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

    //加载柱状图
    fun loadingData(data:ArrayList<DataPayment>){
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
        xAxis.gridColor = context?.getColor(R.color.asdiaw)!!
        xAxis.axisLineColor =context?.getColor(R.color.asdiaw)!!
        xAxis.enableGridDashedLine(10f, 10f, 0f)
        xAxis.textColor = context?.getColor(R.color.c1c1c1)!!

        //左Y轴
        val leftAxis: YAxis = mDatabind.chart.axisLeft
        //  leftAxis.setLabelCount(5, false)
        leftAxis.setDrawGridLines(true)
        leftAxis.setDrawLabels(false)
        leftAxis.setDrawAxisLine(false)
        leftAxis.setStartAtZero(false)
        leftAxis.setDrawZeroLine(false)
        // horizontal grid lines
        //leftAxis.enableGridDashedLine(10f, 10f, 0f)
        leftAxis.gridColor = context?.getColor(R.color.asdiaw)!!
        leftAxis.axisLineColor =context?.getColor(R.color.asdiaw)!!
        //leftAxis.spaceBottom = 16f

        //右Y轴
        val rightAxis: YAxis = mDatabind.chart.axisRight
        // rightAxis.setLabelCount(5, false)
        rightAxis.setDrawGridLines(true)
        rightAxis.setDrawLabels(false)
        rightAxis.setDrawAxisLine(false)
        rightAxis.setStartAtZero(false)
        rightAxis.setDrawZeroLine(false)
        //rightAxis.enableGridDashedLine(10f, 10f, 0f)
        rightAxis.gridColor = context?.getColor(R.color.asdiaw)!!
        rightAxis.axisLineColor =context?.getColor(R.color.asdiaw)!!
        //rightAxis.spaceBottom = 16f


        //隐藏图例
        mDatabind.chart.legend.isEnabled = false;

        //限制放大
//            mDatabind.chart.viewPortHandler.setMaximumScaleX(2.0f); //限制X轴放大限制
//            mDatabind.chart.viewPortHandler.setMaximumScaleY(2.0f);// 限制Y轴放大限制
//


        mDatabind.chart.getDescription().setEnabled(false)

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mDatabind.chart.setMaxVisibleValueCount(60)

        // scaling can now only be done on x- and y-axis separately

        // scaling can now only be done on x- and y-axis separately

        //  mDatabind.chart.animateY(2500)
        // mDatabind.chart.setScaleMinima(-1.2f, -1.0f);              //x轴默认放大1.2倍 要不然x轴数据展示不全
//设置柱子(组)居中对齐x轴上的点 如果改正true X轴不能正常循环赋值 value值会出现-1
        xAxis.setAxisMinimum(-0.5f)
        xAxis.setAxisMaximum(data.size-1f + 0.5f)
        xAxis.setCenterAxisLabels(false)
        //  xAxis.setAxisMaximum(data.size.toFloat());            //最大值
        xAxis.setGranularity(1f);                               //设置最小间隔
        mDatabind.chart.legend.isEnabled = false
        mDatabind.chart.setFitBars(true)
        mDatabind.chart.setExtraBottomOffset(15f)
        // set data
        mDatabind.chart.data = generateDataLine(data)
        mDatabind.chart.invalidate()
    }

    private fun generateDataLine(data : ArrayList<DataPayment>): BarData {
        val values1 = ArrayList<BarEntry>()
        val xList :ArrayList<String> = ArrayList()
        for (i in 0 until data.size) {
            values1.add(
                BarEntry(
                    i.toFloat(),
                    ((data[i].money)).toFloat()
                )
            )
            xList.add(data[i].date)
        }

        //点击弹窗
        val mv = MyMarkerView(activity,xList, R.layout.custom_marker_view)
        mv.chartView = mDatabind.chart // For bounds control
        mDatabind.chart.marker = mv // Set the marker to the chart

        //设置x轴上的标签
        mDatabind.chart.xAxis.valueFormatter =LabelFormatter(xList)


        val d1 = BarDataSet(values1,"")

        d1.highLightColor = Color.rgb(255,84,76)
        d1.setDrawValues(true)
        d1.color =context?.getColor(R.color.base_color_q)!!
        d1.valueTextColor = context?.getColor(R.color.c1c1c1)!!
        d1.valueTextSize = 10f
        d1.valueFormatter = LabelBarTextFormatter(values1)

        val sets = ArrayList<IBarDataSet>()
        sets.add(d1)
        return BarData(sets)
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