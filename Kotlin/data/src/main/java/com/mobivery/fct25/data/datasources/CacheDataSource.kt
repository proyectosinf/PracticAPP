package com.mobivery.fct25.data.datasources

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CacheDataSource @Inject constructor() {

    /**
     *  It is strongly advised not to pass around complex data objects when navigating,
     *  but instead pass the minimum necessary information, such as a unique identifier
     *  or other form of ID, as arguments when performing navigation actions.
     *
     *  Complex objects should be stored as data in a single source of truth,
     *  such as the data layer. Once you land on your destination after navigating,
     *  you can then load the required information from the single source of truth
     *  by using the passed ID.
     *  To retrieve the arguments in your ViewModel that's responsible for accessing the data layer,
     *  you can use the ViewModel’s SavedStateHandle.
     *
     *  https://developer.android.com/jetpack/compose/navigation#retrieving-complex-data
     */


}