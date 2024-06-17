package com.altunoymak.esarj.presentation.ui.search

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.altunoymak.esarj.data.model.searchstation.Suggestion
import com.altunoymak.esarj.databinding.SearchRecyclerItemBinding

class SearchAdapter(private val onItemClicked : (Suggestion) -> Unit) : ListAdapter<Suggestion, SearchAdapter.SuggestionViewHolder>(SearchDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        val binding = SearchRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SuggestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SuggestionViewHolder(private val binding: SearchRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(suggestion: Suggestion) {
            suggestion.highlightedText?.let {
                val formattedText = Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT)
                binding.titleTextView.text = formattedText
            }
            binding.root.setOnClickListener {
                onItemClicked(suggestion)
            }
        }
    }

    class SearchDiffCallBack : DiffUtil.ItemCallback<Suggestion>() {
        override fun areItemsTheSame(oldItem: Suggestion, newItem: Suggestion): Boolean {
            return oldItem.chargingStation?.address == newItem.chargingStation?.address
        }

        override fun areContentsTheSame(oldItem: Suggestion, newItem: Suggestion): Boolean {
            return oldItem == newItem
        }
    }
}