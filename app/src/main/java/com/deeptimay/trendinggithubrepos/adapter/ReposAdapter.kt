package com.deeptimay.trendinggithubrepos.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.deeptimay.trendinggithubrepos.data.model.Repo
import com.deeptimay.trendinggithubrepos.databinding.ItemTrendingRepoBinding


class ReposAdapter : PagingDataAdapter<Repo, ReposAdapter.ViewHolder>(REPO_COMPARATOR) {

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTrendingRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { repo ->
            with(holder) {
                itemView.tag = repo
                if (repo != null) {
                    bind(createOnClickListener(binding, repo, position), repo)
                }
            }
        }
    }

    private fun createOnClickListener(binding: ItemTrendingRepoBinding, repo: Repo, position: Int): View.OnClickListener {
        return View.OnClickListener {
            snapshot()[position]?.isSelectedItem?.let { snapshot()[position]?.isSelectedItem = !it }
            notifyItemChanged(position)
        }
    }

    class ViewHolder(val binding: ItemTrendingRepoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, repo: Repo) {

            binding.apply {

                Glide.with(itemView)
                    .load(repo.owner?.avatarUrl)
                    .centerCrop()
                    .error(android.R.drawable.stat_notify_error)
                    .into(ivUserAvatar)

                val str = SpannableString((repo.owner?.login ?: "") + " / " + repo.name)
                str.setSpan(
                    StyleSpan(Typeface.NORMAL),
                    repo.owner?.login?.length ?: 0,
                    str.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                tvUsername.text = str
                tvRepoDesc.text = repo.description
                tvRepoLang.text = repo.language
                tvRepoStars.text = repo.stargazersCount.toString()
                tvRepoFork.text = repo.forksCount.toString()

                ViewCompat.setTransitionName(this.ivUserAvatar, "avatar_${repo.id}")

                if (repo.isSelectedItem)
                    binding.containerTrendingRepo.setBackgroundColor(Color.parseColor("#DC746C"))
                else
                    binding.containerTrendingRepo.setBackgroundColor(Color.parseColor("#ffffff"))

                root.setOnClickListener(listener)
            }
        }
    }
}