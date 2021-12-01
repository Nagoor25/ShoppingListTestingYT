package com.androiddevs.shoppinglisttestingyt.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.responses.ImageResponse
import com.androiddevs.shoppinglisttestingyt.other.Constants
import com.androiddevs.shoppinglisttestingyt.other.Event
import com.androiddevs.shoppinglisttestingyt.other.Resource
import com.androiddevs.shoppinglisttestingyt.repositories.ShoppingRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class ShoppingViewModel @ViewModelInject constructor(
private val repository :ShoppingRepository
): ViewModel() {
    val shoppingItems = repository.observeAllShoppingItems()
    val totalPrice = repository.observeTotalPrice()
    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images: LiveData<Event<Resource<ImageResponse>>> = _images

    private val _currentImgUrl =MutableLiveData<String>()
    val currentImgUrl:LiveData<String> = _currentImgUrl

   private val _insertShoppingItemStatus=MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus:LiveData<Event<Resource<ShoppingItem>>> = _insertShoppingItemStatus

    fun setCurrentImgUrl(url:String){
        _currentImgUrl.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem)=viewModelScope.launch{
         repository.deleteShoppingItem(shoppingItem)
    }
    fun insertShoppingItemIntoDB(shoppingItem: ShoppingItem)=viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun insertShoopingItem(name:String,amountString: String,priceString: String){
     if(name.isEmpty()||amountString.isEmpty()||priceString.isEmpty()){
         _insertShoppingItemStatus.postValue(Event(Resource.error("This fields must not be empty ",null)))
         return
     }
        if(name.length>Constants.MAX_NAME_LENGTH){
            _insertShoppingItemStatus.postValue(Event(Resource.error("name of item must not exceed ${Constants.MAX_NAME_LENGTH} characters",null)))
            return
        }
        if(priceString.length>Constants.MAX_PRICE_LENGTH){
            _insertShoppingItemStatus.postValue(Event(Resource.error("price of item must not exceed ${Constants.MAX_PRICE_LENGTH} characters",null)))
            return
        }
        val amount=try {
           amountString.toInt()
        }catch (e:Exception){
      _insertShoppingItemStatus.postValue(Event(Resource.error("Enter a valid amount ",null)))
            return
        }
        val shoppingItem=ShoppingItem(name,amount,priceString.toFloat(),_currentImgUrl.value?:"")
        insertShoppingItemIntoDB(shoppingItem)
        setCurrentImgUrl("")
        _insertShoppingItemStatus.postValue(Event(Resource.success(shoppingItem)))

    }
    fun searchForImage(imageQuery:String){
        if(imageQuery.isEmpty()) {
            return
        }
        _images.value= Event(Resource.loading(null))
         viewModelScope.launch {
          val response= repository.searchForImage(imageQuery)
             _images.value=Event(response)
         }
    }
}

