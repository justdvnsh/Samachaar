package divyansh.tech.kotnewreader.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import divyansh.tech.kotnewreader.R
import kotlinx.android.synthetic.main.key_phrases_preview.view.*

class KeyPhrasesAdapter: RecyclerView.Adapter<KeyPhrasesAdapter.KeyPhrasesViewHolder>() {

    inner class KeyPhrasesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val diffCallback = object: DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): KeyPhrasesAdapter.KeyPhrasesViewHolder {
        return KeyPhrasesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.key_phrases_preview, parent, false)
        )
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: KeyPhrasesAdapter.KeyPhrasesViewHolder, position: Int) {
        holder.itemView.keyPhraseText.text = differ.currentList[position]
    }
}