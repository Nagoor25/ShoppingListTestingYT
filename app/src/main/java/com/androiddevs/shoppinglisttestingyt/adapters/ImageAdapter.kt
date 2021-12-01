package com.androiddevs.shoppinglisttestingyt.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.shoppinglisttestingyt.R
import com.bumptech.glide.RequestManager
import kotlinx.android.synthetic.main.item_image.view.*
import javax.inject.Inject

class ImageAdapter @Inject constructor(
    private val glide:RequestManager
):RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private val differCallBack=object :DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem==newItem

        override fun areContentsTheSame(oldItem: String, newItem: String)
                =oldItem==newItem

    }
    val differList=AsyncListDiffer(this,differCallBack)

    var images:List<String>
        get()=differList.currentList
        set(value) = differList.submitList(value)

   private var onItemClickListener:((String)->Unit)?=null

    fun setOnItemClickListener(listener:(String)->Unit){
        onItemClickListener=listener
    }

    class ImageViewHolder(view : View) :RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.item_image,parent,false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val url=images[position]
        holder.itemView.apply {
            glide.load(url).into(ivShoppingImage)
            setOnClickListener {
                onItemClickListener?.let {  click->
                    click(url)
                }
            }
        }
    }

    override fun getItemCount(): Int {
       return images.size
    }
}

