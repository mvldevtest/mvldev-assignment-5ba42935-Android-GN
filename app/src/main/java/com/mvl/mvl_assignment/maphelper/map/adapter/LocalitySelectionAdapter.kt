package com.webcroptech.aladdin.view.map.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mvl.mvl_assignment.R

import com.mvl.mvl_assignment.maphelper.map.model.LocalityModel
import java.util.*

class LocalitySelectionAdapter(
    var mContext: Context, listOfLocality: List<LocalityModel>,
    mItemClickListener: LocalityItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private var localityListFiltered: List<LocalityModel>
    private val localityList: List<LocalityModel>
    private val mItemClickListener: LocalityItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_LOCALITY) {
            val v: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_locality_address, parent, false)
            //v.findViewById(android.R.id.text1).setBackgroundColor(Color.BLUE);
            LocalityViewHolder(v)
        } else {
            val v: View =
                LayoutInflater.from(parent.context).inflate(R.layout.empty_view, parent, false)
            NoResultViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        if (viewType == VIEW_TYPE_LOCALITY) {
            val h = holder as LocalityViewHolder
            h.tvLocalityName.setText(localityListFiltered[position].name)
            h.itemView.setOnClickListener { mItemClickListener.onItemClicked(localityListFiltered[position]) }
        } else {
            val h = holder as NoResultViewHolder
            h.tvNoResult.text = "No result found"
            h.itemView.setOnClickListener {
                //AppUtils.showToast(mContext, "No result found")
                //mItemClickListener.onItemClicked(localityListFiltered.get(position));
            }
        }
    }

    override fun getItemCount(): Int {
        return if (localityListFiltered.size == 0) {
            1
        } else localityListFiltered.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (localityListFiltered.size == 0) {
            VIEW_TYPE_NO_RESULT
        } else {
            VIEW_TYPE_LOCALITY
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                localityListFiltered = if (charString.isEmpty()) {
                    localityList
                } else {
                    val filteredList: MutableList<LocalityModel> = ArrayList<LocalityModel>()
                    for (row in localityList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = localityListFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                localityListFiltered = filterResults.values as ArrayList<LocalityModel>
                notifyDataSetChanged()
            }
        }
    }

    interface LocalityItemClickListener {
        fun onItemClicked(selectedLocality: LocalityModel?)
    }

    inner class LocalityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvLocalityName: TextView

        init {
            tvLocalityName = itemView.findViewById(R.id.tv_locality_name)
        }
    }

    inner class NoResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNoResult: TextView

        init {
            tvNoResult = itemView.findViewById(R.id.empty_Data)
        }
    }

    companion object {
        private const val VIEW_TYPE_LOCALITY = 0
        private const val VIEW_TYPE_NO_RESULT = 1
    }

    init {
        localityList = listOfLocality
        localityListFiltered = listOfLocality
        this.mItemClickListener = mItemClickListener
    }
}