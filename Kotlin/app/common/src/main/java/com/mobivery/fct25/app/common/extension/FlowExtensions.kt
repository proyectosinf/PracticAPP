package com.mobivery.fct25.app.common.extension

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

fun <T1, T2> Flow<Result<T1>>.combineResults(other: Flow<Result<T2>>): Flow<Result<Pair<T1, T2>>> {
    return combine(other) { a, b ->
        when {
            a.isFailure -> Result.failure(a.exceptionOrNull()!!)
            b.isFailure -> Result.failure(b.exceptionOrNull()!!)
            else -> Result.success(a.getOrThrow() to b.getOrThrow())
        }
    }
}

fun <T1, T2, T3> Flow<Result<T1>>.combineResults(
    flow1: Flow<Result<T2>>,
    flow2: Flow<Result<T3>>,
): Flow<Result<Triple<T1, T2, T3>>> {
    return combineResults(flow1)
        .combineResults(flow2)
        .map { result ->
            if (result.isSuccess) {
                val value = result.getOrThrow()
                Result.success(Triple(value.first.first, value.first.second, value.second))
            } else {
                Result.failure(result.exceptionOrNull()!!)
            }
        }
}