package com.sletmoe.bucket4k

import io.github.bucket4j.BucketConfiguration
import io.github.bucket4j.ConfigurationBuilder
import io.github.bucket4j.MathType
import io.github.bucket4j.TokensInheritanceStrategy
import kotlin.time.Duration

/**
 * [SuspendingBucket] is an opaque wrapper around [Bucket4j's](https://github.com/bucket4j/bucket4j)
 * [io.github.bucket4j.local.LockFreeBucket], and implements an interface semantically equivalent to
 * [BlockingBucket](https://bucket4j.com/8.2.0/toc.html#blocking-bucket).
 * Whereas Bucket4j's blocking behavior is just that, [SuspendingBucket] instead delays, making it safe to use in a
 * coroutine context.
 */
class SuspendingBucket private constructor(private val impl: SuspendingBucketImpl) {
    /**
     * Delegates directly to the underlying LockFreeBucket's toString implementation.
     */
    override fun toString(): String = impl.toString()

    /**
     * Returns the number of currently available tokens. Delegates directly to the underlying LockFreeBucket's
     * [getAvailableTokens](https://bucket4j.com/8.2.0/toc.html#getavailabletokens) implementation.
     */
    val availableTokens: Long = impl.availableTokens

    /**
     * Returns the Bucket4j BucketConfiguration of the underlying LockFreeBucket. Delegates directly to the underlying
     * LockFreeBucket's getConfiguration implementation.
     */
    val configuration: BucketConfiguration = impl.configuration

    /**
     * Resets all tokens to max capacity. Delegates directly to the underlying LockFreeBucket's
     * [reset](https://bucket4j.com/8.2.0/toc.html#reset) implementation.
     */
    fun reset() = impl.reset()

    /**
     * Tries to consume the specified number of [tokensToConsume]. Delegates directly to the underlying LockFreeBucket's
     * [tryConsume](https://bucket4j.com/8.2.0/toc.html#tryconsume) implementation.
     *
     * @param [tokensToConsume] The number of tokens to try and consume from the bucket. Must be a positive value.
     * @return true if the tokens were consumed, false otherwise.
     */
    fun tryConsume(tokensToConsume: Long): Boolean = impl.tryConsume(tokensToConsume)

    /**
     * Tries to consume the specified number of [tokensToConsume], suspending up to [maxWaitTime] if necessary in order
     * to accumulate enough tokens to consume the specified amount.
     *
     * The (slightly simplified) logic is as follows:
     *   - if [tokensToConsume] tokens are available, consume them and return true immediately
     *   - else if enough tokens will be made available to satisfy [tokensToConsume] within [maxWaitTime],
     *     the function will delay until the requisite number of tokens are available, consume them, and return true.
     *   - else, will immediately return false.
     *
     *   It should also be noted that this function is fair: If invoked twice without enough tokens for the first
     *   request to succeed, the first request will be serviced before subsequent requests.
     *
     * This algorithm is a direct copy of Bucket4j
     * BlockingBucket's [tryConsume](https://bucket4j.com/8.2.0/toc.html#tryconsume-2), except it suspends the calling
     * coroutine's execution while waiting for available tokens, rather than blocking the thread. See the Bucket4j
     * [tryConsume](https://bucket4j.com/8.2.0/toc.html#tryconsume-2) documentation for more details.
     *
     * @param [tokensToConsume] The number of tokens to try and consume from the bucket. Must be a positive value.
     * @param [maxWaitTime] The maximum amount of time to wait for the bucket to refill enough tokens such that the
     * requested [tokensToConsume] can be consumed.
     * @return true if the tokens were consumed, false otherwise.
     */
    suspend fun tryConsume(tokensToConsume: Long, maxWaitTime: Duration): Boolean = impl.tryConsumeSuspending(
        tokensToConsume,
        maxWaitTime,
    )

    /**
     * Consumes the specified number of [tokensToConsume], suspending as long as necessary to accumulate the requisite
     * number of tokens. This algorithm is a direct copy of Bucket4j
     * BlockingBucket's [consume](https://bucket4j.com/8.2.0/toc.html#consume), except it suspends the calling
     * coroutine's execution while waiting for available tokens, rather than blocking the thread. See the Bucket4j
     * [consume](https://bucket4j.com/8.2.0/toc.html#consume) documentation for more details.
     *
     * @param [tokensToConsume] The number of tokens to try and consume from the bucket. Must be a positive value.
     */
    suspend fun consume(tokensToConsume: Long) = impl.consumeSuspending(tokensToConsume)

    /**
     * Adds the specified number of [tokensToAdd] to the bucket, not exceeding the capacity of the bucket. Delegates
     * directly to the underlying LockFreeBucket's [addTokens](https://bucket4j.com/8.2.0/toc.html#addtokens)
     * implementation.
     *
     * @param [tokensToAdd] The number of tokens to add to the bucket.
     */
    fun addTokens(tokensToAdd: Long) = impl.addTokens(tokensToAdd)

    /**
     * Adds the specified number of [tokensToAdd] to the bucket, even if it would result in the bucket containing more
     * than its specified capacity. Delegates directly to the underlying LockFreeBucket's
     * [forceAddTokens](https://bucket4j.com/8.2.0/toc.html#forceaddtokens) implementation.
     *
     * @param [tokensToAdd] The number of tokens to add to the bucket.
     */
    fun forceAddTokens(tokensToAdd: Long) = impl.forceAddTokens(tokensToAdd)

    /**
     * Replaces the bucket's configuration. This delegates directly to the underlying LockFreeBucket's
     * [replaceConfiguration](https://bucket4j.com/8.2.0/toc.html#replaceconfiguration)
     * implementation.
     *
     * The inheritance strategy is complex enough that I'm not going to summarize the algorithm here, see Bucket4j's
     * [documentation](https://bucket4j.com/8.2.0/toc.html#replaceconfiguration) for more information.
     *
     * @param [newConfiguration] The new BucketConfiguration to use.
     * @param [tokensInheritanceStrategy] Configuration for the strategy to use for token inheritance.
     */
    fun replaceConfiguration(
        newConfiguration: BucketConfiguration,
        tokensInheritanceStrategy: TokensInheritanceStrategy,
    ) = impl.replaceConfiguration(
        newConfiguration,
        tokensInheritanceStrategy,
    )

    companion object {
        /**
         * A [type-safe builder](https://kotlinlang.org/docs/type-safe-builders.html) for constructing SuspendingBucket
         * objects.
         *
         * @param [configure] A [function literal with receiver](https://kotlinlang.org/docs/lambdas.html#function-literals-with-receiver),
         * used for specifying SuspendingBucket configuration.
         * @return A SuspendingBucket, constructed with the specified configuration.
         */
        fun build(configure: SuspendingBucketConfiguration.() -> Unit = {}): SuspendingBucket {
            val config = SuspendingBucketConfiguration()
            config.configure()

            val configurationBuilder = ConfigurationBuilder()
            config.limits.forEach { configurationBuilder.addLimit(it) }

            return SuspendingBucket(
                SuspendingBucketImpl(configurationBuilder.build(), MathType.INTEGER_64_BITS, config.timeMeter),
            )
        }
    }
}
