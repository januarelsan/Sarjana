package com.buahbatu.januar

class LampModel(val isLampOn: Boolean, val time: String)

const val TimeFormat = "MM/dd/yy hh:mm:ss.s"

object Data {
    val itemList = mutableListOf<LampModel>()
}
