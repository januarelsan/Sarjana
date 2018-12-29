package com.buahbatu.januar

import com.jjoe64.graphview.DefaultLabelFormatter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class LabelFormatter : DefaultLabelFormatter() {
    /**
     * the date format that will convert
     * the unix timestamp to string
     */
    private val mDateFormat: DateFormat = SimpleDateFormat("hh:ss.s", Locale.getDefault())

    /**
     * calendar to avoid creating new date objects
     */
    private val mCalendar: Calendar = Calendar.getInstance()

    /**
     * formats the x-values as date string.
     *
     * @param value raw value
     * @param isValueX true if it's a x value, otherwise false
     * @return value converted to string
     */
    override fun formatLabel(value: Double, isValueX: Boolean): String {
        if (isValueX) {
            // format as date
            mCalendar.timeInMillis = value.toLong()
            return mDateFormat.format(mCalendar.timeInMillis)
        } else {
            return super.formatLabel(value, isValueX)
        }
    }
}