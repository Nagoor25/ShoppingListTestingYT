package com.androiddevs.shoppinglisttestingyt.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.other.Status
import com.androiddevs.shoppinglisttestingyt.viewmodel.ShoppingViewModel
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_shopping_item.*
import kotlinx.android.synthetic.main.fragment_shopping.*
import javax.inject.Inject

class AddShoppingItemFragment @Inject constructor(
    val glide:RequestManager
):Fragment(R.layout.fragment_add_shopping_item) {
    lateinit var viewModel: ShoppingViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
     viewModel= ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        subscribeToObservers()
        btnAddShoppingItem.setOnClickListener {
            viewModel.insertShoopingItem(
                etShoppingItemName.text.toString(),
                etShoppingItemAmount.text.toString(),
                etShoppingItemPrice.text.toString()
            )
        }
        ivShoppingImage.setOnClickListener {
            findNavController().navigate(AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment())
        }
        val callback= object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.setCurrentImgUrl("")
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }
    private fun subscribeToObservers(){
    viewModel.currentImgUrl.observe(viewLifecycleOwner, Observer {
        glide.load(it).into(ivShoppingImage)
    })
        viewModel.insertShoppingItemStatus.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { result->
            when(result.status){
               Status.SUCCESS ->
               {
               Snackbar.make(requireActivity().rootLayout,"Shopping Item Added",
                   Snackbar.LENGTH_LONG).show()
               }
                Status.ERROR ->
                {
                    Snackbar.make(requireActivity().rootLayout,result.message?:"Un error Occurred",
                        Snackbar.LENGTH_LONG).show()
                }
                Status.LOADING->
                {
                 /* no Op*/
                }
            }
            }
        })
    }
}