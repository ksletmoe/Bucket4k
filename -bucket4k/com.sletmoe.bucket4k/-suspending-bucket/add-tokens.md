//[Bucket4k](../../../index.md)/[com.sletmoe.bucket4k](../index.md)/[SuspendingBucket](index.md)/[addTokens](add-tokens.md)

# addTokens

[jvm]\
fun [addTokens](add-tokens.md)(tokensToAdd: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html))

Adds the specified number of [tokensToAdd](add-tokens.md) to the bucket, not exceeding the capacity of the bucket. Delegates directly to the underlying LockFreeBucket's [consume](https://github.com/bucket4j/bucket4j/blob/master/asciidoc/src/main/docs/asciidoc/basic/api-reference.adoc#addtokens) implementation.

#### Parameters

jvm

| | |
|---|---|
| tokensToAdd | The number of tokens to add to the bucket. |
