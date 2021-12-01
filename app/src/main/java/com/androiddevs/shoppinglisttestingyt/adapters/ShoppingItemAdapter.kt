package com.androiddevs.shoppinglisttestingyt.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.bumptech.glide.RequestManager
import kotlinx.android.synthetic.main.item_shopping.view.*
import kotlinx.android.synthetic.main.item_shopping.view.tvShoppingItemPrice
import javax.inject.Inject

class ShoppingItemAdapter @Inject constructor(
    val glide:RequestManager
):RecyclerView.Adapter<ShoppingItemAdapter.ShoppingViewHolder>() {
    private val differCallBack=object :DiffUtil.ItemCallback<ShoppingItem>(){
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
         return   oldItem.id==newItem.id
        }
        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem) =
            oldItem.hashCode()==newItem.hashCode()

    }
    private val differ=AsyncListDiffer(this,differCallBack)
   var shoppingItems: List<ShoppingItem>
    get()=differ.currentList
    set(value) = differ.submitList(value)

    class ShoppingViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
      val view=LayoutInflater.from(parent.context).inflate(R.layout.item_shopping,parent,false)
        return ShoppingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
     val shoppingItem=shoppingItems.get(position)
        holder.itemView.apply {
            glide.load(shoppingItem.imageUrl).into(ivShoppingImage)
            tvName.text=shoppingItem.name
            val amountText="${shoppingItem.amount}x"
            tvShoppingItemAmount.text= amountText
            val priceText="${shoppingItem.price}€"
            tvShoppingItemPrice.text=priceText
        }
    }

    override fun getItemCount(): Int {
     return  shoppingItems.size
    }
}