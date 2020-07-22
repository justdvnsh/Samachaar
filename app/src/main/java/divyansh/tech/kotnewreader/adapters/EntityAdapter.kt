package divyansh.tech.kotnewreader.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.network.models.MLModels.Entities
import divyansh.tech.kotnewreader.network.models.MLModels.EntitiesModels
import kotlinx.android.synthetic.main.entity_layout_preview.view.*
import kotlinx.android.synthetic.main.key_phrases_preview.view.*

class EntityAdapter: RecyclerView.Adapter<EntityAdapter.EntityViewHolder>() {

    inner class EntityViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val diffCallback = object: DiffUtil.ItemCallback<Entities>() {
        override fun areItemsTheSame(oldItem: Entities, newItem: Entities): Boolean {
            return oldItem.text == newItem.text
        }

        override fun areContentsTheSame(oldItem: Entities, newItem: Entities): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EntityAdapter.EntityViewHolder {
        return EntityViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.entity_layout_preview, parent, false)
        )
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: EntityAdapter.EntityViewHolder, position: Int) {
        val ent = differ.currentList[position]
        holder.itemView.apply {
            entityName.text = ent.text
            entityType.text = ent.type ?: "No Type"
        }
    }
}