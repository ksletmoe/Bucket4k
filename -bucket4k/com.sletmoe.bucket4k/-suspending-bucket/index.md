//[Bucket4k](../../../index.md)/[com.sletmoe.bucket4k](../index.md)/[SuspendingBucket](index.md)

# SuspendingBucket

[jvm]\
class [SuspendingBucket](index.md)

[SuspendingBucket](index.md) is an opaque wrapper around [Bucket4j's](https://github.com/bucket4j/bucket4j)io.github.bucket4j.local.LockFreeBucket, and implements an interface semantically equivalent to [BlockingBucket](https://github.com/bucket4j/bucket4j/blob/master/asciidoc/src/main/docs/asciidoc/basic/api-reference.adoc#iogithubbucket4jblockingbucket). Whereas Bucket4j's blocking behavior is just that, [SuspendingBucket](index.md) instead delays, making it safe to use in a coroutine context.

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [addTokens](add-tokens.md) | [jvm]<br>fun [addTokens](add-tokens.md)(tokensToAdd: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html))<br>Adds the specified number of [tokensToAdd](add-tokens.md) to the bucket, not exceeding the capacity of the bucket. Delegates directly to the underlying LockFreeBucket's [consume](https://github.com/bucket4j/bucket4j/blob/master/asciidoc/src/main/docs/asciidoc/basic/api-reference.adoc#addtokens) implementation. |
| [consume](consume.md) | [jvm]<br>suspend fun [consume](consume.md)(tokensToConsume: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html))<br>Consumes the specified number of [tokensToConsume](consume.md), suspending as long as necessary to accumulate the requisite number of tokens. This algorithm is a direct copy of Bucket4j BlockingBucket's [consume](https://github.com/bucket4j/bucket4j/blob/master/asciidoc/src/main/docs/asciidoc/basic/api-reference.adoc#consume), except it suspends the calling coroutine's execution while waiting for available tokens, rather than blocking the thread. See the Bucket4j [consume](https://github.com/bucket4j/bucket4j/blob/master/asciidoc/src/main/docs/asciidoc/basic/api-reference.adoc#consume) documentation for more details. |
| [forceAddTokens](force-add-tokens.md) | [jvm]<br>fun [forceAddTokens](force-add-tokens.md)(tokensToAdd: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html))<br>Adds the specified number of [tokensToAdd](force-add-tokens.md) to the bucket, even if it would result in the bucket containing more than its specified capacity. Delegates directly to the underlying LockFreeBucket's [forceAddTokens](https://github.com/bucket4j/bucket4j/blob/master/asciidoc/src/main/docs/asciidoc/basic/api-reference.adoc#forceaddtokens) implementation. |
| [replaceConfiguration](replace-configuration.md) | [jvm]<br>fun [replaceConfiguration](replace-configuration.md)(newConfiguration: BucketConfiguration, tokensInheritanceStrategy: TokensInheritanceStrategy)<br>Replaces the bucket's configuration. This delegates directly to the underlying LockFreeBucket's [replaceConfiguration](https://github.com/bucket4j/bucket4j/blob/master/asciidoc/src/main/docs/asciidoc/basic/api-reference.adoc#replaceconfiguration) implementation. |
| [reset](reset.md) | [jvm]<br>fun [reset](reset.md)()<br>Resets all tokens to max capacity. Delegates directly to the underlying LockFreeBucket's [reset](https://github.com/bucket4j/bucket4j/blob/master/asciidoc/src/main/docs/asciidoc/basic/api-reference.adoc#reset) implementation. |
| [toString](to-string.md) | [jvm]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Delegates directly to the underlying LockFreeBucket's toString implementation. |
| [tryConsume](try-consume.md) | [jvm]<br>fun [tryConsume](try-consume.md)(tokensToConsume: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Tries to consume the specified number of [tokensToConsume](try-consume.md). Delegates directly to the underlying LockFreeBucket's [tryConsume](https://github.com/bucket4j/bucket4j/blob/master/asciidoc/src/main/docs/asciidoc/basic/api-reference.adoc#tryconsume) implementation.<br>[jvm]<br>suspend fun [tryConsume](try-consume.md)(tokensToConsume: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), maxWaitTime: [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Tries to consume the specified number of [tokensToConsume](try-consume.md), suspending up to [maxWaitTime](try-consume.md) if necessary in order to accumulate enough tokens to consume the specified amount. |

## Properties

| Name | Summary |
|---|---|
| [availableTokens](available-tokens.md) | [jvm]<br>val [availableTokens](available-tokens.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>Returns the number of currently available tokens. Delegates directly to the underlying LockFreeBucket's [getAvailableTokens](https://github.com/bucket4j/bucket4j/blob/master/asciidoc/src/main/docs/asciidoc/basic/api-reference.adoc#getavailabletokens) implementation. |
| [configuration](configuration.md) | [jvm]<br>val [configuration](configuration.md): BucketConfiguration<br>Returns the Bucket4j BucketConfiguration of the underlying LockFreeBucket. Delegates directly to the underlying LockFreeBucket's getConfiguration implementation. |
