package com.mvl.mvl_assignment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mvl.mvl_assignment.R
import com.mvl.mvl_assignment.databinding.CachedRowBinding
import com.mvl.mvl_assignment.helper.showLog
import com.mvl.mvl_assignment.model.CashedList


class CachedAdapter(private var items: LiveData<ArrayList<CashedList>>, val listener:OnItemClickListener): RecyclerView.Adapter<CachedAdapter.RecyclerViewViewHolder>() {
    var recyclerViewItems = ArrayList<CashedList>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewViewHolder {
        val binding = CachedRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewViewHolder(binding)
    }
    override fun onBindViewHolder(holder: RecyclerViewViewHolder, position: Int) {
        items.value?.get(position)?.let {
            holder.bind(it)
            println(it)



        }




        holder.itemView.findViewById<CardView>(R.id.card_view).setOnClickListener {
            showLog("clicked","clicked")
            items.value?.get(position)?.let {



                listener.onItemClick(it.a.latitude,it.a.longitude,it.a.name)
            }

        }

        holder.itemView.setOnClickListener {

        }
    }
    interface OnItemClickListener {
        fun onItemClick(
            id: Double,
            longitude: Double,
            locA:String,

            )
    }

    override fun getItemCount(): Int {
        return items.value?.size!!
    }
    fun setData(newRecyclerViewItems: ArrayList<CashedList>){
        val diffCallback = DiffCallback(recyclerViewItems, newRecyclerViewItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        recyclerViewItems.clear()
        recyclerViewItems.addAll(newRecyclerViewItems)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class RecyclerViewViewHolder(private val binding: CachedRowBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(recyclerViewItem: CashedList) = with(binding) {
            cachedLocation = recyclerViewItem
        }

    }

    inner class DiffCallback(
        private var oldList: ArrayList<CashedList>,
        private var newList: ArrayList<CashedList>
    ): DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

    }
}

