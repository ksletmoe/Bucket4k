package com.sletmoe.bucket4k

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Refill
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.timing.eventually
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.longs.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import java.lang.IllegalArgumentException
import kotlin.system.measureTimeMillis
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

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

            test("should delay for the required token filling time") {
                var consumed = false
                val elapsedMillis = measureTimeMillis {
                    // ensure we complete in a reasonable amount of time
                    eventually(3.5.seconds) {
                        consumed = bucket.tryConsume(tokensToConsume = 8L, maxWaitTime = 4.seconds)
                    }
                }

                // should take just over 3 seconds (first 5 covered by starting capacity, 3 seconds to accumulate 3 more
                // tokens
                elapsedMillis shouldBeGreaterThanOrEqual 3.seconds.inWholeMilliseconds
                consumed shouldBe true
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

            test("should delay for the required token filling time") {
                val elapsedMillis = measureTimeMillis {
                    eventually(4.5.seconds) {
                        bucket.consume(tokensToConsume = 9L)
                    }
                }

                elapsedMillis shouldBeGreaterThanOrEqual 4.seconds.inWholeMilliseconds
            }
        }
    }
}
