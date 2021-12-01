package com.androiddevs.shoppinglisttestingyt.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.adapters.ShoppingItemAdapter
import com.androiddevs.shoppinglisttestingyt.viewmodel.ShoppingViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_shopping.*
import javax.inject.Inject


class ShoppingFragment @Inject constructor(
    val shoppingItemAdapter: ShoppingItemAdapter
) :Fragment(R.layout.fragment_shopping) {
    lateinit var viewmodel: ShoppingViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel= ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        subscribeObservers()
        setRecyclerView()
       /*viewModel=ViewModelProvider(this).get(MainViewModel::class.java)*/
        /*viewmodel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(ShoppingViewModel::class.java)*/
     fabAddShoppingItem.setOnClickListener {
         findNavController().navigate(ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingItemFragment())
     }
    }

   private val itemTouchHelper=object : ItemTouchHelper.SimpleCallback(
        0,LEFT or RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position=viewHolder.layoutPosition
            val shoppingItem=shoppingItemAdapter.shoppingItems[position]
            viewmodel.deleteShoppingItem(shoppingItem)
            Snackbar.make(requireView(),"Shopping Item Deleted ",Snackbar.LENGTH_LONG).apply {
                setAction("Undo"){
                    viewmodel.insertShoppingItemIntoDB(shoppingItem)
                }
                    .show()
            }
        }
      }
    private fun subscribeObservers(){
     viewmodel.shoppingItems.observe(viewLifecycleOwner, Observer {
         shoppingItemAdapter.shoppingItems=it
     })
     viewmodel.totalPrice.observe(viewLifecycleOwner, Observer {
      val price=it?:0f
      val priceText="Total Price:$price€"
         tvShoppingItemPrice.text=priceText
     })
    }
    private fun setRecyclerView(){
        rvShoppingItems.apply {
            adapter=shoppingItemAdapter
            val llm=LinearLayoutManager(requireContext())
            ItemTouchHelper(itemTouchHelper).attachToRecyclerView(this)
        }
    }

}