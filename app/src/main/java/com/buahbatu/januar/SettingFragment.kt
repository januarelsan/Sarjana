package com.buahbatu.januar

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.buahbatu.januar.kmeans.Algo
import com.buahbatu.januar.kmeans.TransformedLampModel
import com.jjoe64.graphview.series.DataPoint
import kotlinx.android.synthetic.main.fragment_setting.*
import com.jjoe64.graphview.series.LineGraphSeries



class SettingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnRunAlgo.setOnClickListener {
            runAlgorithm()
        }

        btnSetK.setOnClickListener {
            inputK.text.toString().toIntOrNull()?.let { k ->
                if (k > 0 && k <= Data.itemList.size) {
                    Data.myKCount = k
                    Snackbar.make(btnRunAlgo, "Pengaturan diterima", Snackbar.LENGTH_SHORT).show()
                }
                else Snackbar.make(btnRunAlgo, "Nilai K kurang dari 1 atau lebih besar dari banyak data", Snackbar.LENGTH_SHORT).show()
            } ?: Snackbar.make(btnRunAlgo, "Nilai K bukan angka", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun runAlgorithm() {
        if (Data.itemList.size < Data.kCount) {
            Snackbar.make(btnRunAlgo, "Data masih kurang dari ${Data.kCount}", Snackbar.LENGTH_SHORT).show()
            return
        }

        val count = Data.itemList.size
        val sseList = mutableListOf<Double>()
        val clusters = mutableListOf<List<TransformedLampModel>>()
        val clusterMaps = mutableListOf<List<List<TransformedLampModel>>>()
        for (i in 2..count) {
            val algo = Algo(Data.itemList, i).run()
            sseList.add(algo.sse)
            clusters.add(algo.cluster)
            clusterMaps.add(algo.clusterMap)
        }

        val text = clusters.zip(clusterMaps).mapIndexed { index, pair ->
            val inside = pair.first.zip(pair.second).joinToString(separator = "\n") {
                "Centroid: ${it.first}\nMember: ${it.second}\n"
            }
            "=========K ${index + 2}: SSE ${sseList[index]}==========\n$inside"
        }.joinToString(separator = "\n")
        tvResult.text = text

        graphSetting.run {
            viewport.isScrollable = true
            viewport.isScalable = true
            removeAllSeries()
            val series = LineGraphSeries<DataPoint>(
                sseList.mapIndexed { index, d ->
                    DataPoint(index + Data.kCount.toDouble(), d)
                }.toTypedArray()
            )

            addSeries(series)
        }
    }
}
