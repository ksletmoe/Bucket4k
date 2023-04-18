//[Bucket4k](../../../index.md)/[com.sletmoe.bucket4k](../index.md)/[SuspendingBucket](index.md)/[forceAddTokens](force-add-tokens.md)

# forceAddTokens

[jvm]\
fun [forceAddTokens](force-add-tokens.md)(tokensToAdd: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html))

Adds the specified number of [tokensToAdd](force-add-tokens.md) to the bucket, even if it would result in the bucket containing more than its specified capacity. Delegates directly to the underlying LockFreeBucket's [forceAddTokens](https://github.com/bucket4j/bucket4j/blob/master/asciidoc/src/main/docs/asciidoc/basic/api-reference.adoc#forceaddtokens) implementation.

#### Parameters

jvm

| | |
|---|---|
| tokensToAdd | The number of tokens to add to the bucket. |
