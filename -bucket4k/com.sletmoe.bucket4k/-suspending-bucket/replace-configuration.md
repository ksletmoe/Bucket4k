//[Bucket4k](../../../index.md)/[com.sletmoe.bucket4k](../index.md)/[SuspendingBucket](index.md)/[replaceConfiguration](replace-configuration.md)

# replaceConfiguration

[jvm]\
fun [replaceConfiguration](replace-configuration.md)(newConfiguration: BucketConfiguration, tokensInheritanceStrategy: TokensInheritanceStrategy)

Replaces the bucket's configuration. This delegates directly to the underlying LockFreeBucket's [replaceConfiguration](https://github.com/bucket4j/bucket4j/blob/master/asciidoc/src/main/docs/asciidoc/basic/api-reference.adoc#replaceconfiguration) implementation.

The inheritance strategy is complex enough that I'm not going to summarize the algorithm here, see Bucket4j's [documentation](https://github.com/bucket4j/bucket4j/blob/master/asciidoc/src/main/docs/asciidoc/basic/api-reference.adoc#replaceconfiguration) for more information.

#### Parameters

jvm

| | |
|---|---|
| newConfiguration | The new BucketConfiguration to use. |
| tokensInheritanceStrategy | Configuration for the strategy to use for token inheritance. |
