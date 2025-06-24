#set( $NAME_LOW = $NAME.substring(0,1).toLowerCase() + $NAME.substring(1) )
#set( $featureIndex = $PACKAGE_NAME.indexOf(".feature") )
#if( $featureIndex > 0 )
    #set( $ROOT_PACKAGE_NAME = $PACKAGE_NAME.substring(0, $featureIndex) )
#else
    #set( $ROOT_PACKAGE_NAME = "" )
#end
package ${PACKAGE_NAME}

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import ${ROOT_PACKAGE_NAME}.app.common.designsystem.theme.AppTheme
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ${NAME}ScreenTests {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @MockK
    private lateinit var viewModel: ${NAME}ViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        every { viewModel.loading } returns MutableStateFlow(false)
        every { viewModel.error } returns MutableStateFlow(null)
    }

    @Test
    // TODO: change test description
    fun yourTestDescription() {
        // GIVEN
        every { viewModel.${NAME_LOW}UiState } returns MutableStateFlow(
            ${NAME}UiState(
                // TODO: added initial values
            )
        )

        // WHEN
        composeTestRule.setContent {
            AppTheme {
                ${NAME}Screen(
                    viewModel = viewModel,
                )
            }
        }

        // THEN
        // TODO: assert that some texts are there
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.common_accept_text))
            .assertExists()
    }
}