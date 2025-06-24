#set( $NAME_LOW = $NAME.substring(0,1).toLowerCase() + $NAME.substring(1) )
#set( $featureIndex = $PACKAGE_NAME.indexOf(".feature") )
#if( $featureIndex > 0 )
    #set( $ROOT_PACKAGE_NAME = $PACKAGE_NAME.substring(0, $featureIndex) )
#else
    #set( $ROOT_PACKAGE_NAME = "" )
#end
package ${PACKAGE_NAME}

import io.mockk.MockKAnnotations
import kotlinx.coroutines.test.runTest
import ${ROOT_PACKAGE_NAME}.testing.utils.MainDispatcherRule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherRule::class)
class ${NAME}ViewModelTests {

    // TODO: mock the repositories needed for the view model
    //@MockK
    //private lateinit var yourRepository: YourRepository

    private lateinit var viewModel: ${NAME}ViewModel

    private fun createViewModel() = ${NAME}ViewModel(
        // TODO: pass the needed mocked repositories or anything
    )

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        // TODO: define mock returns for the repositories for all tests
        // coEvery { yourRepository.isConnected() } returns true
    }

    @Test
    // TODO: set the test name
    fun `GIVEN initial conditions WHEN event happens THEN expected result`() =
        runTest {
        // GIVEN
        // TODO: define mock returns for the repositories specific for this test

        // WHEN
        viewModel = createViewModel()

        // THEN
        // TODO: verify methods are called for this test
        // coVerify { yourRepository.isConnected() }
        // TODO: assert UiState has the correct values
        // assertTrue(viewModel.${NAME_LOW}UiState.value.list.isNotEmpty())
    }
}