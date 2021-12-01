package com.androiddevs.shoppinglisttestingyt.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.androiddevs.shoppinglisttestingyt.launchFragmentInHiltContainer
import com.androiddevs.shoppinglisttestingyt.ui.ShoppingFragment
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import javax.inject.Inject
import javax.inject.Named

//@ExperimentalCoroutinesApi
//@RunWith(AndroidJUnit4::class)
@SmallTest
@HiltAndroidTest
class ShoppingDaoTest {
    @get:Rule
    var instantTaskExecutorRule=InstantTaskExecutorRule()
    @get:Rule
    var hiltRule=HiltAndroidRule(this)
    @Inject
    @Named("test_db")
    lateinit var database: ShoppingDatabase
    private lateinit var dao: ShoppingDao

    @Before
    fun setup() {
        //--before we using hilt test module
        /*database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingDatabase::class.java
        ).allowMainThreadQueries().build()*/
        hiltRule.inject()
        dao = database.getShoppingDao()

    }
    @After
    fun tearDown(){
        database.close()
    }
    @Test
    fun inserShopingItem() = runBlockingTest {
        val shoppingItem=ShoppingItem("shoes",100,1f,"demo",1)
        dao.insertShoppingItem(shoppingItem)

        val allShoppingItems=dao.observeAllShoppingItems().getOrAwaitValue()
        assertThat(allShoppingItems).contains(shoppingItem)
    }

@Test
    fun deleteShopingItem()= runBlockingTest {
        val shoppingItem=ShoppingItem("shoes",100,1f,"demo",1)
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)
        val allShoppingItem=dao.observeAllShoppingItems().getOrAwaitValue()
        assertThat(allShoppingItem).doesNotContain(shoppingItem)
    }

    @Test
    fun observeTotalPriceSum()= runBlockingTest {
        val shoppingItem=ShoppingItem("shoes",2,10f,"demo",1)
        val shoppingItem2=ShoppingItem("shoes",4,5.5f,"demo",2)
        val shoppingItem3=ShoppingItem("shoes",0,100f,"demo",3)
        dao.insertShoppingItem(shoppingItem)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)
        val totalPriceSum=dao.observeTotalPrice().getOrAwaitValue()
        assertThat(totalPriceSum).isEqualTo(2*10f+4*5.5f)

    }/* @Test
    fun testLanchFragmentHiltContainer(){
        launchFragmentInHiltContainer<ShoppingFragment> {

        }

        }*/
    /*test case ex*/

    }
