package divyansh.tech.kotnewreader.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.models.MLModels.RelatedNews
import kotlinx.android.synthetic.main.related_news_item_preview.view.*

class RelatedNewsAdapter: RecyclerView.Adapter<RelatedNewsAdapter.RelatedNewsViewHolder>() {
    inner class RelatedNewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val diffCallback = object: DiffUtil.ItemCallback<RelatedNews>() {
        override fun areItemsTheSame(oldItem: RelatedNews, newItem: RelatedNews): Boolean {
            return oldItem.link == newItem.link
        }

        override fun areContentsTheSame(oldItem: RelatedNews, newItem: RelatedNews): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedNewsViewHolder {
        return RelatedNewsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.related_news_item_preview, parent, false)
        )
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: RelatedNewsViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            relatedTextViewTitle.text = article.title
            relatedTextViewPublished.text = article.published
            relatedTextViewSource.text = article.source.title
            setOnClickListener {
                onItemClickListener?.let {
                    it(article)
                }
            }
        }
    }

    private var onItemClickListener: ((RelatedNews) -> Unit)? = null

    fun setOnItemClickListener(listener: (RelatedNews) -> Unit) {
        onItemClickListener = listener
    }
}