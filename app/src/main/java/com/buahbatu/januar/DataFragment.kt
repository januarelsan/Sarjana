package com.buahbatu.januar

import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.data_fragment.*
import kotlinx.android.synthetic.main.lamp_item.view.*


class DataFragment : Fragment() {

    private val database by lazy { FirebaseDatabase.getInstance().getReference("lamp_data") }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.data_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = Adapter()
        rvData.adapter = adapter
        ptrLayout.setOnRefreshListener {
            ptrLayout.isRefreshing = false
            loadNewData(adapter)
        }
    }

    fun loadNewData(adapter: Adapter) {
        getFirebaseData(adapter)
    }

    private fun getFirebaseData(adapter: Adapter) {
        // get data from firebase
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(MainActivity::class.java.simpleName, "runAlgo", databaseError.toException())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val lampData = dataSnapshot.children.mapNotNull { it.getValue(LampModel::class.java) }
                lampData.forEach { data ->
                    Data.itemList
                        .any { it.time == data.time }
                        .let { exist ->
                            if (!exist) Data.itemList.add(data)
                        }
                }

                adapter.notifyDataSetChanged()
            }
        })
    }

    class Adapter : RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(container: ViewGroup, vhType: Int): ViewHolder {
            val view = LayoutInflater.from(container.context).inflate(R.layout.lamp_item, container,false)
            return ViewHolder(view)
        }

        override fun getItemCount() = Data.itemList.size

        override fun onBindViewHolder(vh: ViewHolder, pos: Int) {
            vh.bind(Data.itemList[pos])
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: LampModel){
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