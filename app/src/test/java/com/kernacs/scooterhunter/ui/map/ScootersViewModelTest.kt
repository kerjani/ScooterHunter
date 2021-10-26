package com.kernacs.scooterhunter.ui.map

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kernacs.scooterhunter.MainCoroutineRule
import com.kernacs.scooterhunter.data.repository.ScooterRepository
import com.kernacs.scooterhunter.fakeEntity
import com.kernacs.scooterhunter.getOrAwaitValue
import com.kernacs.scooterhunter.invalidDataException
import com.kernacs.scooterhunter.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class ScootersViewModelTest {

    private var repository: ScooterRepository = mock(ScooterRepository::class.java)


    private lateinit var systemUnderTest: ScootersViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        systemUnderTest =
            ScootersViewModel(repository)
    }

    @Test
    fun `assert that getScooters receives data from the repository successfully`() =
        mainCoroutineRule.runBlockingTest {
            `when`(repository.getScooters()).thenReturn(
                Result.Success(listOf(fakeEntity))
            )

            systemUnderTest.load()
            verify(repository, times(1)).getScooters()

            assertThat(systemUnderTest.isLoading.getOrAwaitValue(), `is`(false))
            assertThat(systemUnderTest.error.getOrAwaitValue(), nullValue())
        }

    @Test
    fun `assert that getScooters receives empty data from the repository`() =
        mainCoroutineRule.runBlockingTest {
            `when`(repository.getScooters()).thenReturn(Result.Success(emptyList()))

            systemUnderTest.load()
            verify(repository, times(1)).getScooters()

            assertThat(systemUnderTest.isLoading.getOrAwaitValue(), `is`(false))
            assertThat(systemUnderTest.error.getOrAwaitValue(), nullValue())
        }

    @Test
    fun `assert that getScooters receives an error from the repository`() =
        mainCoroutineRule.runBlockingTest {
            `when`(repository.getScooters()).thenReturn(Result.Success(null))
            `when`(repository.getScooters()).thenReturn(
                Result.Error(invalidDataException)
            )

            systemUnderTest.load()
            verify(repository, times(1)).getScooters()

            assertThat(systemUnderTest.isLoading.getOrAwaitValue(), `is`(false))
            assertThat(systemUnderTest.error.getOrAwaitValue(), `is`(invalidDataException))
        }

    @Test
    fun `assert that refreshData receives scooter data from the repository successfully`() =
        mainCoroutineRule.runBlockingTest {
            `when`(repository.getScooters()).thenReturn(Result.Success(listOf(fakeEntity)))

            val spyViewModel = spy(systemUnderTest)
            spyViewModel.refreshData()
            verify(spyViewModel).load()
            verify(repository, times(1)).getScooters()

            assertThat(spyViewModel.isLoading.getOrAwaitValue(), `is`(false))
            assertThat(spyViewModel.error.getOrAwaitValue(), nullValue())
        }

    @Test
    fun `assert that refreshData receives null data from the repository`() =
        mainCoroutineRule.runBlockingTest {
            `when`(repository.getScooters()).thenReturn(Result.Success(null))

            val spyViewModel = spy(systemUnderTest)
            spyViewModel.refreshData()
            verify(spyViewModel).load()
            verify(repository, times(1)).getScooters()

            assertThat(spyViewModel.error.getOrAwaitValue(), CoreMatchers.notNullValue())
            assertThat(spyViewModel.isLoading.getOrAwaitValue(), `is`(false))
        }

    @Test
    fun `assert that refreshData receives an error from the repository`() =
        mainCoroutineRule.runBlockingTest {
            `when`(repository.getScooters()).thenReturn(
                Result.Error(invalidDataException)
            )

            val spyViewModel = spy(systemUnderTest)
            spyViewModel.refreshData()
            verify(spyViewModel).load()
            verify(repository, times(1)).getScooters()

            assertThat(spyViewModel.isLoading.getOrAwaitValue(), `is`(false))
            assertThat(spyViewModel.error.getOrAwaitValue(), `is`(invalidDataException))
        }
}
