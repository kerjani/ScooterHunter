package com.kernacs.scooterhunter.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kernacs.scooterhunter.MainCoroutineRule
import com.kernacs.scooterhunter.data.entity.ScooterEntity
import com.kernacs.scooterhunter.fakeEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ScooterDaoTest {

    lateinit var database: ScootersDatabase

    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ScootersDatabase::class.java
        ).allowMainThreadQueries().build()

        systemUnderTest = database.scootersDao()
    }

    @After
    fun cleanUp() {
        database.close()
    }

    private lateinit var systemUnderTest: ScootersDao

    @Test
    fun insertScooter_verifyScooterDbIsNotEmpty() = mainCoroutineRule.runBlockingTest {
        systemUnderTest.insertAll(listOf(fakeEntity))

        val scooters = systemUnderTest.getScooters()

        assertThat(scooters, `not`(nullValue()))
        assertThat(scooters.size, `is`(1))
        val scooter = scooters.first()
        assertThat(scooter, `not`(nullValue()))
        assertThat(scooter.fleetBirdId, `is`(fakeEntity.fleetBirdId))
        assertThat(scooter.battery, `is`(fakeEntity.battery))
        assertThat(scooter.latitude, `is`(fakeEntity.latitude))
        assertThat(scooter.longitude, `is`(fakeEntity.longitude))
        assertThat(scooter.model, `is`(fakeEntity.model))
        assertThat(scooter.resolution, `is`(fakeEntity.resolution))
        assertThat(scooter.state, `is`(fakeEntity.state))
        assertThat(scooter.zoneId, `is`(fakeEntity.zoneId))
    }

    @Test
    fun insertScooterWithSameId_ReplaceOnConflict_returnNewlyInsertedScooter() =
        mainCoroutineRule.runBlockingTest {
            val newScooterEntity = fakeEntity.copy(model = "other")
            // Insert first scooter
            systemUnderTest.insertAll(listOf(fakeEntity))

            // Insert new scooter with same id
            systemUnderTest.insertAll(listOf(newScooterEntity))

            val scooter = systemUnderTest.getScooterBId(fakeEntity.id)

            assertThat<ScooterEntity>(scooter, `not`(nullValue()))
            assertThat(scooter, `is`(newScooterEntity))
            assertThat(scooter?.model, `is`(newScooterEntity.model))
        }

    @Test
    fun deleteScooter_returnNullValue() = mainCoroutineRule.runBlockingTest {
        systemUnderTest.insertAll(listOf(fakeEntity))
        systemUnderTest.deleteAll()

        val scooter = systemUnderTest.getScooterBId(fakeEntity.id)

        assertThat(scooter, `is`(nullValue()))
    }

}
