import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mvl.mvl_assignment.R
import com.mvl.mvl_assignment.data.model.response.GetAddress
import com.mvl.mvl_assignment.databinding.AdapterListRowBinding
import com.mvl.mvl_assignment.helper.showLog


class RecyclerViewAdapter(private var items: LiveData<ArrayList<GetAddress>>,val listener:OnItemClickListener): RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewViewHolder>() {
    var recyclerViewItems = ArrayList<GetAddress>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewViewHolder {
        val binding = AdapterListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

                listener.onItemClick(it.a.latitude,it.a.longitude,it.b.latitude,it.b.longitude,it.a.name,it.b.name)
            }

        }

        holder.itemView.setOnClickListener {

        }
    }
    interface OnItemClickListener {
        fun onItemClick(
            id: Double,
            longitude: Double,
            longitude1: Double,
            latitude: Double,
            locA:String,
            locB:String

        )
    }

    override fun getItemCount(): Int {
        return items.value?.size!!
    }
    fun setData(newRecyclerViewItems: ArrayList<GetAddress>){
        val diffCallback = DiffCallback(recyclerViewItems, newRecyclerViewItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        recyclerViewItems.clear()
        recyclerViewItems.addAll(newRecyclerViewItems)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class RecyclerViewViewHolder(private val binding: AdapterListRowBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(recyclerViewItem: GetAddress) = with(binding) {
            item = recyclerViewItem
        }

    }

    inner class DiffCallback(
        private var oldList: ArrayList<GetAddress>,
        private var newList: ArrayList<GetAddress>
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

