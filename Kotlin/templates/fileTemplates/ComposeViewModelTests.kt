#set( $NAME_LOW = $NAME.substring(0,1).toLowerCase() + $NAME.substring(1) )
#set( $packageParts = ${StringUtils.split($PACKAGE_NAME, ".")} )
#set( $index = 0)
#set( $end = ${packageParts.size()} - 5 )
#set( $ROOT_PACKAGE_NAME = "" )
#foreach($index in [0..$end])
    #if( $index == 0 )
        #set( $ROOT_PACKAGE_NAME = "${packageParts.get($index)}" )
    #else
        #set( $ROOT_PACKAGE_NAME = "$ROOT_PACKAGE_NAME.${packageParts.get($index)}" )
    #end
#end
package ${PACKAGE_NAME}

import ${ROOT_PACKAGE_NAME}.testing.utils.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ${NAME}ViewModelTests {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    // TODO: mock the repositories needed for the view model
    //@MockK
    //private lateinit var yourRepository: YourRepository

    private lateinit var viewModel: ${NAME}ViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        // TODO: define mock returns for the repositories for all tests
        // coEvery { yourRepository.isConnected() } returns true
    }

    @Test
    // TODO: set the test description ()
    fun yourTestDescription() = runTest {
        // GIVEN
        // TODO: define mock returns for the repositories specific for this test

        // WHEN
        viewModel = ${NAME}ViewModel(
            // TODO: pass the needed mocked repositories or anything
        )

        // THEN
        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.${NAME_LOW}UiState.collect() }
        // TODO: verify methods are called for this test
        // coVerify { yourRepository.isConnected() }
        // TODO: assert UiState has the correct values
        // assertTrue(viewModel.${NAME_LOW}UiState.value.list.isNotEmpty())
        collectJob.cancel()
    }
}