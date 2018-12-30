package com.buahbatu.januar.kmeans

import com.buahbatu.januar.LampModel
import com.buahbatu.januar.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

class TransformedLampModel(val y: Double = 0.0, val x: Double = 0.0) {

    fun copy() = TransformedLampModel(y, x)

    override fun equals(other: Any?): Boolean {
        return other is TransformedLampModel && other.x == x && other.y == y
    }

    companion object {
        fun distance(a: TransformedLampModel, b: TransformedLampModel) = Math.sqrt (
            Math.pow((a.x - b.x), 2.0) + Math.pow((a.y - b.y), 2.0)
        )

        fun transformed(original: LampModel): TransformedLampModel {
            val format = SimpleDateFormat(TimeFormat, Locale.getDefault())
            val date = format.parse(original.time)

            val cal = Calendar.getInstance().apply {
                time = date
            }

            val hours = cal.get(Calendar.HOUR_OF_DAY) * 3600.0 // to double
            val minutes = cal.get(Calendar.MINUTE) * 60.0 // to double
            val seconds = cal.get(Calendar.SECOND) * 1.0 // to double

            return TransformedLampModel(
                y = if (original.isLampOn) 1.0 else 0.0,
                x = hours + minutes + seconds
            )
        }
    }
}

fun List<TransformedLampModel>.isClosestFrom(a: TransformedLampModel) = map {
    TransformedLampModel.distance(a, it)
}.let {
    it.indexOf(it.min())
}

class Algo(dataset: List<LampModel>) {

    private val transformedDataSet = dataset.map { TransformedLampModel.transformed(it) }

    fun run(clusters: List<TransformedLampModel> = (1..2).map { transformedDataSet[0].copy() }
            , maxIteration: Int = -1) : List<TransformedLampModel> {

        val clusterMap = clusters.mapIndexed { _, _ ->
            mutableListOf<TransformedLampModel>()
        }

        transformedDataSet.forEach {
            val clusterIndex = clusters.isClosestFrom(it)
            clusterMap[clusterIndex].add(it)
        }

        val newCluster = clusterMap.mapIndexed { index, cluster ->
            val newX = if (cluster.size > 0) (cluster.map { it.x }.sum() / cluster.size) else clusters[index].x
            val newY = if (cluster.size > 0) (cluster.map { it.y }.sum() / cluster.size) else clusters[index].y
            TransformedLampModel(newY, newX)
        }

        val isClusterPointMoved = newCluster.withIndex().any {
            TransformedLampModel.distance(it.value, clusters[it.index]) > 0
        }

        return if (!isClusterPointMoved || maxIteration == 0) {
            newCluster
        } else {
            run(newCluster, maxIteration - 1)
        }
    }
}