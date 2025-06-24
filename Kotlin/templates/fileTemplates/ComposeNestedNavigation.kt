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
import androidx.navigation.compose.navigation
import ${FEATURE_PACKAGE_NAME}.ui.${NAME_LOW}.navigation.${NAME_LOW}Graph
import ${FEATURE_PACKAGE_NAME}.ui.${NAME_LOW}.navigation.${NAME_SNAKE_UPPER}_ROUTE

const val ${NAME_SNAKE_UPPER}_NESTED_NAVIGATION_ROUTE = "${NAME_SNAKE_LOWER}_nested_navigation_route"

fun NavController.navigateTo${NAME}() {
    this.navigate(${NAME_SNAKE_UPPER}_NESTED_NAVIGATION_ROUTE)
}

fun NavGraphBuilder.${NAME_LOW}FeatureGraph(
    navController: NavController,
) {
    navigation(
        startDestination = ${NAME_SNAKE_UPPER}_ROUTE,
        route = ${NAME_SNAKE_UPPER}_NESTED_NAVIGATION_ROUTE
    ) {
        ${NAME_LOW}Graph(navController)
    }
}