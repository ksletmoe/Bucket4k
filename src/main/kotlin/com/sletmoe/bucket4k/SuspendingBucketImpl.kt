package com.sletmoe.bucket4k

import io.github.bucket4j.BucketConfiguration
import io.github.bucket4j.BucketExceptions
import io.github.bucket4j.LimitChecker.checkMaxWaitTime
import io.github.bucket4j.LimitChecker.checkTokensToConsume
import io.github.bucket4j.MathType
import io.github.bucket4j.TimeMeter
import io.github.bucket4j.local.LockFreeBucket
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds

internal class SuspendingBucketImpl(
    bucketConfiguration: BucketConfiguration,
    mathType: MathType,
    timeMeter: TimeMeter,
) : LockFreeBucket(bucketConfiguration, mathType, timeMeter) {
    suspend fun tryConsumeSuspending(tokensToConsume: Long, maxWaitTime: Duration): Boolean = coroutineScope {
        checkTokensToConsume(tokensToConsume)
        val maxWaitTimeNanos = maxWaitTime.inWholeNanoseconds
        checkMaxWaitTime(maxWaitTimeNanos)

        val nanosToDelay: Long = reserveAndCalculateTimeToSleepImpl(
            tokensToConsume,
            maxWaitTimeNanos,
        )
        if (nanosToDelay == INFINITY_DURATION) {
            listener.onRejected(tokensToConsume)
            return@coroutineScope false
        }

        listener.onConsumed(tokensToConsume)
        if (nanosToDelay > 0L) {
            delay(nanosToDelay.nanoseconds)
            listener.onParked(nanosToDelay)
        }

        true
    }

    suspend fun consumeSuspending(tokensToConsume: Long) {
        checkTokensToConsume(tokensToConsume)

        val nanosToDelay: Long = reserveAndCalculateTimeToSleepImpl(
            tokensToConsume,
            INFINITY_DURATION,
        )
        if (nanosToDelay == INFINITY_DURATION) {
            throw BucketExceptions.reservationOverflow()
        }

        listener.onConsumed(tokensToConsume)
        if (nanosToDelay > 0L) {
            delay(nanosToDelay.nanoseconds)
        }
    }
}
