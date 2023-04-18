//[Bucket4k](../../../index.md)/[com.sletmoe.bucket4k](../index.md)/[SuspendingBucket](index.md)/[tryConsume](try-consume.md)

# tryConsume

[jvm]\
fun [tryConsume](try-consume.md)(tokensToConsume: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

Tries to consume the specified number of [tokensToConsume](try-consume.md). Delegates directly to the underlying LockFreeBucket's [tryConsume](https://github.com/bucket4j/bucket4j/blob/master/asciidoc/src/main/docs/asciidoc/basic/api-reference.adoc#tryconsume) implementation.

#### Return

true if the tokens were consumed, false otherwise.

#### Parameters

jvm

| | |
|---|---|
| tokensToConsume | The number of tokens to try and consume from the bucket. Must be a positive value. |

[jvm]\
suspend fun [tryConsume](try-consume.md)(tokensToConsume: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), maxWaitTime: [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

Tries to consume the specified number of [tokensToConsume](try-consume.md), suspending up to [maxWaitTime](try-consume.md) if necessary in order to accumulate enough tokens to consume the specified amount.

The (slightly simplified) logic is as follows:

- 
   if [tokensToConsume](try-consume.md) tokens are available, consume them and return true immediately
- 
   else if enough tokens will be made available to satisfy [tokensToConsume](try-consume.md) within [maxWaitTime](try-consume.md),     the function will delay until the requisite number of tokens are available, consume them, and return true.
- 
   else, will immediately return false.

It should also be noted that this function is fair: If invoked twice without enough tokens for the first request to succeed, the first request will be serviced before subsequent requests.

This algorithm is a direct copy of Bucket4j BlockingBucket's [tryConsume](https://github.com/bucket4j/bucket4j/blob/master/asciidoc/src/main/docs/asciidoc/basic/api-reference.adoc#tryconsume-1), except it suspends the calling coroutine's execution while waiting for available tokens, rather than blocking the thread. See the Bucket4j [tryConsume](https://github.com/bucket4j/bucket4j/blob/master/asciidoc/src/main/docs/asciidoc/basic/api-reference.adoc#tryconsume-1) documentation for more details.

#### Return

true if the tokens were consumed, false otherwise.

#### Parameters

jvm

| | |
|---|---|
| tokensToConsume | The number of tokens to try and consume from the bucket. Must be a positive value. |
| maxWaitTime | The maximum amount of time to wait for the bucket to refill enough tokens such that the requested [tokensToConsume](try-consume.md) can be consumed. |
