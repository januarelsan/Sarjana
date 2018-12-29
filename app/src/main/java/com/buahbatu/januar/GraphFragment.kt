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
import java.util.*


class GraphFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.graph_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val series = LineGraphSeries<DataPoint>(
            arrayOf(
                DataPoint(Calendar.getInstance().apply {
                    add(Calendar.DATE, 1)
                }.time, 0.0),
                DataPoint(Calendar.getInstance().apply {
                    add(Calendar.DATE, 2)
                }.time, 1.0),
                DataPoint(Calendar.getInstance().apply {
                    add(Calendar.DATE, 3)
                }.time, 0.0),
                DataPoint(Calendar.getInstance().apply {
                    add(Calendar.DATE, 4)
                }.time, 1.0),
                DataPoint(Calendar.getInstance().apply {
                    add(Calendar.DATE, 5)
                }.time, 0.0),
                DataPoint(Calendar.getInstance().apply {
                    add(Calendar.DATE, 6)
                }.time, 1.0),
                DataPoint(Calendar.getInstance().apply {
                    add(Calendar.DATE, 7)
                }.time, 0.0),
                DataPoint(Calendar.getInstance().apply {
                    add(Calendar.DATE, 8)
                }.time, 1.0)
            )
        )
        graph.run {
            addSeries(series)
            viewport.isScalable = true

            // set date label formatter
            gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(this@GraphFragment.context)
            gridLabelRenderer.numHorizontalLabels = 3 // only 4 because of the space

//            // set manual x bounds to have nice steps
//            viewport.setMinX(d1.getTime());
//            viewport.setMaxX(d3.getTime());
//            viewport.setXAxisBoundsManual(true);

            // as we use dates as labels, the human rounding to nice readable numbers
            // is not necessary
            gridLabelRenderer.setHumanRounding(false)
        }


    }
}