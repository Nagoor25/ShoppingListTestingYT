package com.androiddevs.shoppinglisttestingyt.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.androiddevs.shoppinglisttestingyt.MainCoroutineRule
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValueTest
import com.androiddevs.shoppinglisttestingyt.other.Constants
import com.androiddevs.shoppinglisttestingyt.other.Status
import com.androiddevs.shoppinglisttestingyt.repositories.FakeShoppingRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
@ExperimentalCoroutinesApi
class ShoppingViewModelTest{

    @get:Rule
    var instantTaskExecutiveRule=InstantTaskExecutorRule()
    @get:Rule
    var mainCoroutineRule=MainCoroutineRule()
    private lateinit var viewModel: ShoppingViewModel
    @Before
    fun setup(){
        viewModel= ShoppingViewModel(FakeShoppingRepository())
    }
  @Test
   fun `insert shopping item with empty field,retuns error`(){
     viewModel.insertShoopingItem("name","","3.0")
      val value= viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

      assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
   }
    @Test
    fun `insert shopping item with too long name,retuns error`(){
        val name_str= buildString {
            for (i in 1..Constants.MAX_NAME_LENGTH+1)
            append(1)
        }
        viewModel.insertShoopingItem(name_str,"5","3.0")
        val value= viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }
    @Test
    fun `insert shopping item with too high amount,retuns error`(){

        viewModel.insertShoopingItem("name","9959999999993252121","3.0")
        val value= viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }
    @Test
    fun `insert  shopping item with valid fields,retuns true`(){
        viewModel.insertShoopingItem("name","5","3.0")
        val value= viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

}