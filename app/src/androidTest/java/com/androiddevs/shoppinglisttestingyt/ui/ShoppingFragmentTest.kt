package com.androiddevs.shoppinglisttestingyt.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.adapters.ShoppingItemAdapter
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.androiddevs.shoppinglisttestingyt.launchFragmentInHiltContainer
import com.androiddevs.shoppinglisttestingyt.viewmodel.ShoppingViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ShoppingFragmentTest{
    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @get:Rule
    val instantTaskExecutorRule=InstantTaskExecutorRule()
    @Inject
    lateinit var testFragmentFactory:TestShoppingFragmentFactory
    @Before
    fun setUp(){
        hiltRule.inject()
    }
    @Test
    fun clickAddShoppingItemButton_navigateToAddShoppingItemFragment(){
    val navController=mock(NavController::class.java)
        launchFragmentInHiltContainer<ShoppingFragment> {
            Navigation.setViewNavController(requireView(),navController)
        }
        onView(withId(R.id.fabAddShoppingItem)).perform(click())
        verify(navController).navigate(
            ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingItemFragment()
        )
    }
    @Test
    fun swipeDeleteShoppingItemInDb(){
        val shoppingItem=ShoppingItem("TEST",1,5f,"TEST",1)
        var testViewModel:ShoppingViewModel?=null
        launchFragmentInHiltContainer<ShoppingFragment>(fragmentFactory =testFragmentFactory ) {
      testViewModel=viewModel
            viewmodel?.insertShoppingItemIntoDB(shoppingItem)
        }
        onView(withId(R.id.rvShoppingItems)).perform(
          RecyclerViewActions.actionOnItemAtPosition<ShoppingItemAdapter.ShoppingViewHolder>(
              0,
              swipeLeft()
          )
        )
      assertThat(testViewModel?.shoppingItems?.getOrAwaitValue()).isEmpty()
    }
}