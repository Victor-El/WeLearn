package me.codeenzyme.welearn.view.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.codeenzyme.welearn.databinding.TopicItemLayoutBinding
import me.codeenzyme.welearn.model.Course
import me.codeenzyme.welearn.model.Course.*

class TopicsRecyclerViewAdapter(
    private val context: Context,
    private val onTopicsClickListener: OnTopicsClickListener
): RecyclerView.Adapter<TopicsRecyclerViewAdapter.TopicViewHolder>() {

    private val topics = mutableListOf<Topic>()

    private lateinit var _binding: TopicItemLayoutBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        _binding = TopicItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return TopicViewHolder(_binding.root)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val binding = TopicItemLayoutBinding.bind(holder.itemView)

        binding.topicView.text = topics[position].title
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.root.tooltipText = topics[position].title
        }
        binding.root.setOnClickListener {
            onTopicsClickListener.onClick(topics, position)
        }
        binding.downloadBtn.setOnClickListener {
            onTopicsClickListener.onClickDownload(topics, position)
        }
    }

    override fun getItemCount(): Int {
        return topics.size
    }

    fun update(topicList: List<Topic>) {
        val diffUtilCallback = TopicsDiffUtilCallback(topics, topicList)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
        topics.clear()
        topics.addAll(topicList)
        diffResult.dispatchUpdatesTo(this)
    }


    inner class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnTopicsClickListener {
        fun onClick(topics: List<Topic>, position: Int)
        fun onClickDownload(topics: List<Topic>, position: Int)
    }

    class TopicsDiffUtilCallback(val oldList: List<Topic>, val newList: List<Topic>) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].videoUrl == newList[newItemPosition].videoUrl
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }
}