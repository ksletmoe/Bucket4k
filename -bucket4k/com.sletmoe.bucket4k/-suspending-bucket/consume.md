//[Bucket4k](../../../index.md)/[com.sletmoe.bucket4k](../index.md)/[SuspendingBucket](index.md)/[consume](consume.md)

# consume

[jvm]\
suspend fun [consume](consume.md)(tokensToConsume: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html))

Consumes the specified number of [tokensToConsume](consume.md), suspending as long as necessary to accumulate the requisite number of tokens. This algorithm is a direct copy of Bucket4j BlockingBucket's [consume](https://github.com/bucket4j/bucket4j/blob/master/asciidoc/src/main/docs/asciidoc/basic/api-reference.adoc#consume), except it suspends the calling coroutine's execution while waiting for available tokens, rather than blocking the thread. See the Bucket4j [consume](https://github.com/bucket4j/bucket4j/blob/master/asciidoc/src/main/docs/asciidoc/basic/api-reference.adoc#consume) documentation for more details.

#### Parameters

jvm

| | |
|---|---|
| tokensToConsume | The number of tokens to try and consume from the bucket. Must be a positive value. |
