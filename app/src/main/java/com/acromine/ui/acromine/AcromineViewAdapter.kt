import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acromine.databinding.ItemLongFormBinding
import com.acromine.data.model.LfModel

class AcromineViewAdapter(
    private val longForms: List<LfModel?>?
) : RecyclerView.Adapter<AcromineViewAdapter.AcromineViewHolder>() {
    // ViewHolder to hold references to the views
    inner class AcromineViewHolder(private val binding: ItemLongFormBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Bind the data to the views
        fun bind(longForm: LfModel?) {
            binding.apply {
                tvLong.text = "Long Form: ${longForm?.lf ?: "N/A"}"  // Null-safe with default
                tvFreq.text = "Frequency: ${longForm?.freq ?: "N/A"}"
                tvSince.text = "Since: ${longForm?.since ?: "N/A"}"
            }
        }
    }

    // Inflate the layout for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcromineViewHolder {
        val binding = ItemLongFormBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AcromineViewHolder(binding)
    }

    // Bind data for the item at the given position
    override fun onBindViewHolder(holder: AcromineViewHolder, position: Int) {
        holder.bind(longForms?.get(position))
    }

    // Return the total number of items
    override fun getItemCount(): Int = longForms?.size ?: 0
}