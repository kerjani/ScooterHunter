package com.kernacs.scooterhunter.data.repository

import com.kernacs.scooterhunter.MainCoroutineRule
import com.kernacs.scooterhunter.data.entity.ScooterEntity
import com.kernacs.scooterhunter.data.source.local.ScootersLocalDataSource
import com.kernacs.scooterhunter.data.source.remote.ScootersRemoteDataSource
import com.kernacs.scooterhunter.fakeEntity
import com.kernacs.scooterhunter.fakeScooterDto
import com.kernacs.scooterhunter.invalidDataException
import com.kernacs.scooterhunter.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class ScooterRepositoryTest {

    @Mock
    private lateinit var remoteDataSource: ScootersRemoteDataSource

    @Mock
    private lateinit var localDataSource: ScootersLocalDataSource

    private lateinit var systemUnderTest: ScooterRepository

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        systemUnderTest = ScooterRepository(remoteDataSource, localDataSource)
    }

    @Test
    fun `assert that downloaded dto parsed successfully to entity in the remote source`() =
        mainCoroutineRule.runBlockingTest {
            `when`(remoteDataSource.getScooters()).thenReturn(
                Result.Success(listOf(fakeScooterDto))
            )

            val response = systemUnderTest.getScooters()

            verify(remoteDataSource, times(1)).getScooters()
            verify(localDataSource, times(1)).saveScooters(listOf(fakeEntity))
            verifyNoMoreInteractions(localDataSource)

            when (response) {
                is Result.Success -> {
                    val result = response.data
                    assertThat(result, notNullValue())
                    assertThat(result?.size, not(0))
                    val scooter = result?.first()
                    assertThat<ScooterEntity>(scooter, notNullValue())
                    assertThat(scooter!!.id, `is`(fakeScooterDto.id))
                    assertThat(scooter.battery, `is`(fakeScooterDto.battery))
                    assertThat(scooter.fleetBirdId, `is`(fakeScooterDto.fleetBirdId))
                    assertThat(scooter.latitude, `is`(fakeScooterDto.latitude))
                    assertThat(scooter.longitude, `is`(fakeScooterDto.longitude))
                    assertThat(scooter.model, `is`(fakeScooterDto.model))
                    assertThat(scooter.resolution?.value, `is`(fakeScooterDto.resolution))
                    assertThat(scooter.state.value, `is`(fakeScooterDto.state))
                    assertThat(scooter.zoneId, `is`(fakeScooterDto.zoneId))
                }
            }
        }

    @Test
    fun `assert that getScooter fetches successfully from the local source`() =
        mainCoroutineRule.runBlockingTest {
            `when`(localDataSource.getScooter(fakeEntity.id)).thenReturn(
                fakeEntity
            )

            val response = systemUnderTest.getScooter(fakeEntity.id)

            verify(localDataSource, times(1)).getScooter(fakeEntity.id)
            verifyNoMoreInteractions(remoteDataSource)

            when (response) {
                is Result.Success -> {
                    val scooter = response.data
                    assertThat<ScooterEntity>(scooter, notNullValue())
                    assertThat(scooter, equalTo(fakeEntity))
                }
            }
        }

    @Test
    fun `assert that fetch from the remote source returns with an Error`() =
        mainCoroutineRule.runBlockingTest {
            `when`(remoteDataSource.getScooters()).thenReturn(
                Result.Error(invalidDataException)
            )

            val response = systemUnderTest.getScooters()

            verify(remoteDataSource, times(1)).getScooters()
            verifyNoMoreInteractions(localDataSource)

            when (response) {
                is Result.Error -> {
                    val result = response.exception
                    assertThat(result, notNullValue())
                    assertThat(result, `is`(invalidDataException))
                }
            }
        }

    @Test
    fun `assert that getScooter fetches empty data from the remote source`() =
        mainCoroutineRule.runBlockingTest {
            `when`(remoteDataSource.getScooters()).thenReturn(
                Result.Success(emptyList())
            )

            val response = systemUnderTest.getScooters()

            verify(remoteDataSource, times(1)).getScooters()
            verifyNoInteractions(localDataSource)

            when (response) {
                is Result.Error -> {
                    val result = response.exception
                    assertThat(result, notNullValue())
                    assertThat(result, notNullValue())
                }
            }
        }
}
