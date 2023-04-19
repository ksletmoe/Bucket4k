package com.sletmoe.bucket4k

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Refill
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.timing.eventually
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@OptIn(ExperimentalStdlibApi::class, ExperimentalCoroutinesApi::class)
class SuspendingBucketTest : FunSpec() {
    private lateinit var bucket: SuspendingBucket

    init {
        beforeEach {
            // 5 tokens up front (with a max capacity of 5), add one token per 1 second interval
            bucket = SuspendingBucket.build {
                addLimit(Bandwidth.classic(5, Refill.intervally(1, 1.seconds.toJavaDuration())))
            }
        }

        context("tryConsume with maxWaitTime") {
            test("should throw if tokensToConsume is equal to 0") {
                shouldThrow<IllegalArgumentException> {
                    bucket.tryConsume(tokensToConsume = 0L, maxWaitTime = 3.seconds)
                }
            }

            test("should throw if tokensToConsume is less than 0") {
                shouldThrow<IllegalArgumentException> {
                    bucket.tryConsume(tokensToConsume = -1L, maxWaitTime = 2.seconds)
                }
            }

            test("should throw if maxWaitTime is a 0 length duration") {
                shouldThrow<IllegalArgumentException> {
                    bucket.tryConsume(tokensToConsume = 1L, maxWaitTime = 0.seconds)
                }
            }

            test("should throw if maxWaitTime is a negative duration") {
                shouldThrow<IllegalArgumentException> {
                    bucket.tryConsume(tokensToConsume = 1L, (-3).seconds)
                }
            }

            test("impossible requests should return false immediately") {
                eventually(100.milliseconds) {
                    // we can't accumulate the required tokens in maxWaitTime
                    bucket.tryConsume(tokensToConsume = 6L, maxWaitTime = 10.milliseconds) shouldBe false
                }
            }

            test("should delay for the required token filling time").config(coroutineTestScope = true) {
                var done = false

                val deferredConsumed = async {
                    val consumed = bucket.tryConsume(tokensToConsume = 8L, maxWaitTime = 4.seconds)
                    done = true
                    consumed
                }

                // should need to wait 3 seconds to accumulate enough tokens, given 5 are covered by the starting
                // capacity, and we need 3 seconds to accumulate the 3 remaining
                done shouldBe false
                testCoroutineScheduler.advanceTimeBy(2.5.seconds.inWholeMilliseconds)
                done shouldBe false
                testCoroutineScheduler.advanceTimeBy(0.5.seconds.inWholeMilliseconds)

                eventually(1.seconds) {
                    done shouldBe true
                }

                deferredConsumed.await() shouldBe true
            }
        }

        context("consume") {
            test("should throw if tokensToConsume is 0") {
                shouldThrow<IllegalArgumentException> {
                    bucket.consume(tokensToConsume = 0L)
                }
            }

            test("should throw if tokensToConsume is less than 0") {
                shouldThrow<IllegalArgumentException> {
                    bucket.consume(tokensToConsume = -1L)
                }
            }

            test("should delay for the required token filling time").config(coroutineTestScope = true) {
                var done = false

                launch {
                    bucket.consume(tokensToConsume = 9L)
                    done = true
                }

                // should take 4 seconds to accumulate the required number of tokens
                done shouldBe false
                testCoroutineScheduler.advanceTimeBy(3.seconds.inWholeMilliseconds)
                done shouldBe false

                testCoroutineScheduler.advanceTimeBy(1.seconds.inWholeMilliseconds)
                eventually(1.seconds) {
                    done shouldBe true
                }
            }
        }
    }
}
