package com.igor_shaula.api_polling.ui_layer.list_ui.all_for_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.igor_shaula.api_polling.R
import com.igor_shaula.api_polling.data_layer.VehicleRecord

class VehicleListAdapter(private val onClickFunction: (VehicleRecord) -> Unit) :
    RecyclerView.Adapter<VehicleItemViewHolder>() {

    private var items: List<VehicleRecord> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vehicle, parent, false)
        return VehicleItemViewHolder(itemView, onClickFunction)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VehicleItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(items: List<VehicleRecord>) {
        this.items = items
        notifyDataSetChanged() // todo remove this warning by providing the position somehow
    }
}
