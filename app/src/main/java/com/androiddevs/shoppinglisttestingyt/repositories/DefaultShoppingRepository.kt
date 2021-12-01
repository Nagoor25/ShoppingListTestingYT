package com.androiddevs.shoppinglisttestingyt.repositories

import androidx.lifecycle.LiveData
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingDao
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.PixabayApi
import com.androiddevs.shoppinglisttestingyt.data.remote.responses.ImageResponse
import com.androiddevs.shoppinglisttestingyt.other.Resource
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
  private val shoppingDao: ShoppingDao,
  private val pixabayApi: PixabayApi
) :ShoppingRepository{
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
     shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
     shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems():LiveData<List<ShoppingItem>> {
    return shoppingDao.observeAllShoppingItems()
    }

    override  fun observeTotalPrice(): LiveData<Float> {
     return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>{
       return try {
           val response=pixabayApi.searchForImageItem(imageQuery)
           if(response.isSuccessful){
              response.body()?.let {imageResponse->
                  return  Resource.success(imageResponse)
              }?:Resource.error("An Unknown Error Occured",null)

           }else{
               Resource.error("An Unknown Error Occured",null)
            }
        }catch (e :Exception){
           Resource.error(e.message.toString(),null)
        }

    }
}