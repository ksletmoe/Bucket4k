package com.sletmoe.bucket4k

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.TimeMeter

/**
 * Defines the configuration for creating a [SuspendingBucket].
 */
data class SuspendingBucketConfiguration(
    /**
     * Set's the TimeMeter to be used by the underlying Bucket4j objects. By default, you may choose either
     * TimeMeter.SYSTEM_MILLISECONDS or TimeMeter.SYSTEM_NANOSECONDS. Caution: see Bucket4j's
     * [documentation about using nanoseconds](https://bucket4j.com/8.2.0/toc.html#customizing-time-measurement-choosing-nanotime-time-resolution)
     * before choosing that option.
     */
    var timeMeter: TimeMeter = TimeMeter.SYSTEM_MILLISECONDS,
    private var mutableLimits: MutableList<Bandwidth> = mutableListOf(),
) {
    internal val limits: List<Bandwidth>
        get() = mutableLimits.toList()

    /**
     * Adds a [bandwidth] limit to the bucket. See the Bucket4j documentation on [Bandwidths](https://bucket4j.com/8.2.0/toc.html#bandwidth)
     * for more information.
     */
    fun addLimit(bandwidth: Bandwidth) {
        mutableLimits.add(bandwidth)
    }
}
