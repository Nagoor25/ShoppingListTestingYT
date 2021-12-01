package com.androiddevs.shoppinglisttestingyt.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.responses.ImageResponse
import com.androiddevs.shoppinglisttestingyt.other.Resource

class FakeShoppingRepositoryAndroidTest:ShoppingRepository {
    private val shoppingItemsList= mutableListOf<ShoppingItem>()
    private val observableShoppingItems=MutableLiveData<List<ShoppingItem>>(shoppingItemsList)
    private val observableTotalPrice=MutableLiveData<Float>()

    private var shouldReturnNetworkError= false
    fun setShouldReturnNetworkError(value:Boolean){
        shouldReturnNetworkError =value
    }
    fun refreshShoppingItemList(){
        observableShoppingItems.postValue(shoppingItemsList)
        observableTotalPrice.postValue(getTotalPrice())
    }

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItemsList.add(shoppingItem)
        refreshShoppingItemList()
    }

    private fun getTotalPrice(): Float {
        return shoppingItemsList.sumByDouble {it.price.toDouble()}.toFloat()
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
         shoppingItemsList.remove(shoppingItem)
        refreshShoppingItemList()
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
      return observableShoppingItems
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return observableTotalPrice
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return if(shouldReturnNetworkError){
            Resource.error("Error",null)
        }
        else{
            Resource.success(ImageResponse(listOf(),0,0))
        }
    }

}