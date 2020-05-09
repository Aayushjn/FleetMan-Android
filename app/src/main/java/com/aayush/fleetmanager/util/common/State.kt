package com.aayush.fleetmanager.util.common

/**
 * Used to represent states of progress for any operation (API calls, DB fetches, etc.)
 */
sealed class State {
    /**
     * Operation completed successfully. Used to carry data
     */
    data class Success<T>(val data: T): State()

    /**
     * Operation currently underway
     */
    object Loading: State()

    /**
     * Operation failed with [reason]
     */
    data class Failure(val reason: String = ""): State() {
        constructor(t: Throwable): this(t.message ?: "")
    }
}