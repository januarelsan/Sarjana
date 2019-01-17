package com.buahbatu.januar

class LampModel() {
    var isLampOn: Boolean = false
    var time: String = ""

    constructor(isLampOn: Boolean, time: String) : this() {
        this.isLampOn = isLampOn
        this.time = time
    }

    private fun oneOrZero() = if (isLampOn) 1 else 0

    fun toPayload(): String {
        return "${oneOrZero()}|$time"
    }

    companion object {
        fun fromPayload(payload: String): LampModel {
            val splitted = payload.split("|")
            val isLampOn = splitted[0] == "1"
            val time = splitted[1]
            return LampModel(isLampOn, time)
        }
    }
}

const val TimeFormat = "MM/dd/yy hh:mm:ss.s"

object Data {
    val itemList = mutableListOf<LampModel>()
    const val kCount = 2
    var myKCount = 0
}
