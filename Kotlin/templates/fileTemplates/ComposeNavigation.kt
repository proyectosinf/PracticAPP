#set( $regex = "([a-z])([A-Z]+)" )
#set( $replacement = "$1_$2" )
#set( $NAME_SNAKE_UPPER = $NAME.replaceAll($regex, $replacement).toUpperCase() )
#set( $NAME_SNAKE_LOWER = $NAME.replaceAll($regex, $replacement).toLowerCase() )
#set( $NAME_LOW = $NAME.substring(0,1).toLowerCase() + $NAME.substring(1) )
#set( $featureIndex = $PACKAGE_NAME.indexOf(".feature") )
#if( $featureIndex > 0 )
    #set( $ROOT_PACKAGE_NAME = $PACKAGE_NAME.substring(0, $featureIndex) )
#else
    #set( $ROOT_PACKAGE_NAME = "" )
#end
#set( $navigationIndex = $PACKAGE_NAME.indexOf(".navigation") )
#if( $navigationIndex > 0 )
    #set( $FEATURE_PACKAGE_NAME = $PACKAGE_NAME.substring(0, $navigationIndex) )
#else
    #set( $FEATURE_PACKAGE_NAME = "" )
#end
package ${PACKAGE_NAME}

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ${FEATURE_PACKAGE_NAME}.${NAME}Screen

internal const val ${NAME_SNAKE_UPPER}_ROUTE = "${NAME_SNAKE_LOWER}_route"

fun NavController.navigateTo${NAME}() {
    this.navigate(${NAME_SNAKE_UPPER}_ROUTE)
}

fun NavGraphBuilder.${NAME_LOW}Graph(
    navController: NavController,
) {
    composable(${NAME_SNAKE_UPPER}_ROUTE) {
        ${NAME}Screen()
    }
}