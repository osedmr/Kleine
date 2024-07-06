package com.example.kelineyt.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kelineyt.data.Product
import com.example.kelineyt.databinding.ProductRvItemBinding
import com.example.kelineyt.helper.getProductPrice

class BestProductAdapter : RecyclerView.Adapter<BestProductAdapter.BestProductViewHolder>() {

    inner class BestProductViewHolder(val binding: ProductRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imgProduct)

                val priceAfterOffer = product.offerPercentage.getProductPrice(product.price)
                tvNewPrice.text = "$ ${String.format("%.2f", priceAfterOffer)}"
                tvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

                if (product.offerPercentage == null) {
                    tvNewPrice.visibility = View.INVISIBLE
                }
                tvPrice.text = "$ ${product.price}"
                tvName.text = product.name
            }
        }
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)

    private var originalList: List<Product> = listOf()

    fun updateItems(newItems: List<Product>) {
        originalList = newItems
        differ.submitList(newItems)
    }

    fun filter(query: String) {
        val filteredList = if (query.isEmpty()) {
            originalList
        } else {
            originalList.filter { it.name.contains(query, ignoreCase = true) }
        }
        differ.submitList(filteredList)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BestProductViewHolder {
        return BestProductViewHolder(
            ProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BestProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick: ((Product) -> Unit)? = null
}
