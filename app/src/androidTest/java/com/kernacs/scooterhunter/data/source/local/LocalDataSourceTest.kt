package com.kernacs.scooterhunter.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.kernacs.scooterhunter.MainCoroutineRule
import com.kernacs.scooterhunter.fakeEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class ScooterLocalDataSourceTest {

    private lateinit var database: ScootersDatabase

    private lateinit var systemUnderTest: ScootersLocalDataSource

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ScootersDatabase::class.java
        ).allowMainThreadQueries().build()

        systemUnderTest = ScootersLocalDataSource(database.scootersDao())
    }

    @After
    fun cleanUp() {
        database.close()
    }

    @Test
    fun saveScooter_getScooter_returnSameScooter() = mainCoroutineRule.runBlockingTest {
        systemUnderTest.saveScooters(listOf(fakeEntity))

        val returnedScooters = systemUnderTest.getScooters()

        assertThat(returnedScooters, `not`(nullValue()))
        assertThat(returnedScooters.size, not(0))
        val returnedScooter = returnedScooters.first()
        assertThat(returnedScooter, `is`(fakeEntity))
    }

    @Test
    fun deleteScooter_getScooters_returnsEmpty() = mainCoroutineRule.runBlockingTest {
        systemUnderTest.saveScooters(listOf(fakeEntity))
        systemUnderTest.deleteScooters()

        val result = systemUnderTest.getScooters()
        assertThat(result, `not`(nullValue()))
        assertThat(result.size, `is`(0))
    }

}
