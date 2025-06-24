#set($NAME_LOW = $NAME.substring(0,1).toLowerCase() + $NAME.substring(1))
#set( $featureIndex = $PACKAGE_NAME.indexOf(".feature") )
#if( $featureIndex > 0 )
    #set( $ROOT_PACKAGE_NAME = $PACKAGE_NAME.substring(0, $featureIndex) )
#else
    #set( $ROOT_PACKAGE_NAME = "" )
#end
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}
#end

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ${ROOT_PACKAGE_NAME}.app.common.designsystem.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ${NAME}Screen(
    viewModel: ${NAME}ViewModelInterface = hiltViewModel<${NAME}ViewModel>(),
) {
    val uiState by viewModel.${NAME_LOW}UiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.navigation) {
        when (val navigation = uiState.navigation) {
            // TODO: include here all you navigations
            //is ${NAME}Navigation.NavigateToDetail -> {
            //    onNavigateToDetail(navigation.itemId)
            //}
            else -> {
                // Does nothing
            }
        }
        viewModel.handle(${NAME}Event.DidNavigate)
    }

    // TODO: complete
}

@Preview
@Composable
private fun ${NAME}ScreenPreview() {
    AppTheme {
        ${NAME}Screen (
            viewModel = composePreviewViewModel
        )
    }
}

private val composePreviewViewModel by lazy {
    object : ${NAME}ViewModelInterface {
        // Outputs
        override val ${NAME_LOW}UiState = MutableStateFlow(${NAME}UiState())
        override val loading = MutableStateFlow(false)
        override val error = MutableStateFlow(null)

        // Inputs
        override fun closeError() {}
        override fun handle(event: ${NAME}Event) {}
    }
}