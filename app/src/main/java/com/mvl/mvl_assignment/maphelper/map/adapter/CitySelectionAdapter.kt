package com.mvl.mvl_assignment.maphelper.map.adapter

import com.webcroptech.aladdin.view.map.adapter.LocalitySelectionAdapter.*
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mvl.mvl_assignment.R

import com.mvl.mvl_assignment.maphelper.map.model.CityModel
import java.util.*

class CitySelectionAdapter(
    var mContext: Context, listOfCity: List<CityModel>,
    mItemClickListener: CityItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private var cityListFiltered: List<CityModel>
    private val cityList: List<CityModel>
    private val mItemClickListener: CityItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_CITY) {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_city_address, parent, false)
            //v.findViewById(android.R.id.text1).setBackgroundColor(Color.BLUE);
            CityViewHolder(v)
        } else {
            val v: View =
                LayoutInflater.from(parent.context).inflate(R.layout.empty_view, parent, false)
            NoResultViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        if (viewType == VIEW_TYPE_CITY) {
            val h = holder as CityViewHolder
            h.tvCityName.setText(cityListFiltered[position].name)
            h.itemView.setOnClickListener { mItemClickListener.onItemClicked(cityListFiltered[position]) }
        } else {
            val h = holder as NoResultViewHolder
            h.tvNoResult.text = "No result found"
            h.itemView.setOnClickListener {
                //AppUtils.showToast(mContext, "No result found")
                //mItemClickListener.onItemClicked(cityListFiltered.get(position));
            }
        }
    }

    override fun getItemCount(): Int {
        return if (cityListFiltered.size == 0) {
            1
        } else cityListFiltered.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (cityListFiltered.size == 0) {
            VIEW_TYPE_NO_RESULT
        } else {
            VIEW_TYPE_CITY
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                cityListFiltered = if (charString.isEmpty()) {
                    cityList
                } else {
                    val filteredList: MutableList<CityModel> = ArrayList<CityModel>()
                    for (row in cityList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = cityListFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                cityListFiltered = filterResults.values as ArrayList<CityModel>
                notifyDataSetChanged()
            }
        }
    }

    interface CityItemClickListener {
        fun onItemClicked(selectedCity: CityModel?)
    }

    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvCityName: TextView

        init {
            tvCityName = itemView.findViewById(R.id.tv_city_name)
        }
    }

    inner class NoResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNoResult: TextView

        init {
            tvNoResult = itemView.findViewById(R.id.empty_Data)
        }
    }

    companion object {
        private const val VIEW_TYPE_CITY = 0
        private const val VIEW_TYPE_NO_RESULT = 1
    }

    init {
        cityList = listOfCity
        cityListFiltered = listOfCity
        this.mItemClickListener = mItemClickListener
    }
}