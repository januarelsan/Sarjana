package com.buahbatu.januar

import com.buahbatu.januar.kmeans.Algo
import com.buahbatu.januar.kmeans.TransformedLampModel
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testAlgo() {
        val dataset = listOf(
            LampModel(isLampOn = false, time = "12/31/2018 06:00:00.0"),
            LampModel(isLampOn = true, time = "12/31/2018 18:00:00.0"),
            LampModel(isLampOn = false, time = "12/31/2018 06:00:00.0"),
            LampModel(isLampOn = true, time = "12/31/2018 19:00:00.0")
        )

        val cluster = Algo(dataset).run()
        println(cluster)
        assertEquals(cluster[0], TransformedLampModel(1.0, 66600.0))
        assertEquals(cluster[1], TransformedLampModel(0.0, 21600.0))
    }

    @Test
    fun testEuclid() {
        val a = TransformedLampModel(0.0, 0.0)
        val b = TransformedLampModel(3.0, 4.0)
        val c = TransformedLampModel.distance(a, b)
        assert(c == 5.0)
    }
}
