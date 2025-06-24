#set($NAME_LOW = $NAME.substring(0,1).toLowerCase() + $NAME.substring(1))
#set( $featureIndex = $PACKAGE_NAME.indexOf(".feature") )
#if( $featureIndex > 0 )
    #set( $ROOT_PACKAGE_NAME = $PACKAGE_NAME.substring(0, $featureIndex) )
#else
    #set( $ROOT_PACKAGE_NAME = "" )
#end
package ${PACKAGE_NAME}

import ${ROOT_PACKAGE_NAME}.app.common.base.BaseViewModel
import ${ROOT_PACKAGE_NAME}.app.common.base.BaseViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface ${NAME}ViewModelInterface : BaseViewModelInterface {
    // Outputs
    val ${NAME_LOW}UiState: StateFlow<${NAME}UiState>

    // Inputs
    fun handle(event: ${NAME}Event)
}

@HiltViewModel
class ${NAME}ViewModel @Inject constructor(
) : BaseViewModel(), ${NAME}ViewModelInterface {

    private val _${NAME_LOW}UiState = MutableStateFlow(${NAME}UiState())
    override val ${NAME_LOW}UiState = _${NAME_LOW}UiState.asStateFlow()

    override fun handle(event: ${NAME}Event) {
        when (event) {
            ${NAME}Event.DidNavigate -> didNavigate()
        }
    }

    private fun didNavigate() {
        if (_${NAME_LOW}UiState.value.navigation != null) {
            _${NAME_LOW}UiState.update {
                it.copy(
                    navigation = null,
                )
            }
        }
    }
}

data class ${NAME}UiState(
    val yourData: String? = null,
    val navigation: ${NAME}Navigation? = null,
)

sealed interface ${NAME}Navigation {}

sealed interface ${NAME}Event {
    data object DidNavigate : ${NAME}Event
}