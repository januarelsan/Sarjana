package com.buahbatu.januar

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.DataPoint
import kotlinx.android.synthetic.main.graph_fragment.*
import java.text.SimpleDateFormat
import java.util.*


class GraphFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.graph_fragment, container, false)
    }

    private fun createGraph() {
        val format = SimpleDateFormat(TimeFormat, Locale.getDefault())
        val data = Data.itemList.map {
            val date = format.parse(it.time)
            val value = if (it.isLampOn) 1.0 else 0.0
            DataPoint(date, value)
        }.sortedBy { it.x }
        val series = LineGraphSeries<DataPoint>(data.toTypedArray())
        graph.run {
            removeAllSeries()
            addSeries(series)
            viewport.isScalable = true
            viewport.isScrollable = true

            // set date label formatter
            gridLabelRenderer.labelFormatter = LabelFormatter()

            // as we use dates as labels, the human rounding to nice readable numbers
            // is not necessary
            gridLabelRenderer.setHumanRounding(false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createGraph()

        ptrLayout.setOnRefreshListener {
            ptrLayout.isRefreshing = false
            createGraph()
        }
    }
}