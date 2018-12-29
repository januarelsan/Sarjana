package com.buahbatu.januar

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.data_fragment.*
import kotlinx.android.synthetic.main.lamp_item.view.*

class DataFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.data_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvData.adapter = Adapter()
    }

    class Item (val isLampOn: Boolean, val time: String)

    class Adapter : RecyclerView.Adapter<ViewHolder>() {
        val itemList = mutableListOf<Item>(
            Item(false, "Kapan-Kapan"),
            Item(false, "Kapan-Kapan"),
            Item(true, "Kapan-Kapan"),
            Item(false, "Kapan-Kapan"),
            Item(true, "Kapan-Kapan"),
            Item(true, "Kapan-Kapan")
        )

        override fun onCreateViewHolder(container: ViewGroup, vhType: Int): ViewHolder {
            val view = LayoutInflater.from(container.context).inflate(R.layout.lamp_item, container,false)
            return ViewHolder(view)
        }

        override fun getItemCount() = itemList.count()

        override fun onBindViewHolder(vh: ViewHolder, pos: Int) {
            vh.bind(itemList[pos])
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Item){
            itemView.ivLamp.imageTintList = if (item.isLampOn) {
                ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.colorPrimary))
            } else {
                null
            }

            itemView.tvDate.text = item.time
            itemView.tvDate.setTextColor(if (item.isLampOn) {
                ContextCompat.getColor(itemView.context, R.color.colorPrimary)
            } else {
                ContextCompat.getColor(itemView.context, android.R.color.black)
            })
        }
    }
}