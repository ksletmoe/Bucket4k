//[Bucket4k](../../index.md)/[com.sletmoe.bucket4k](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [SuspendingBucket](-suspending-bucket/index.md) | [jvm]<br>class [SuspendingBucket](-suspending-bucket/index.md)<br>[SuspendingBucket](-suspending-bucket/index.md) is an opaque wrapper around [Bucket4j's](https://github.com/bucket4j/bucket4j)io.github.bucket4j.local.LockFreeBucket, and implements an interface semantically equivalent to [BlockingBucket](https://github.com/bucket4j/bucket4j/blob/master/asciidoc/src/main/docs/asciidoc/basic/api-reference.adoc#iogithubbucket4jblockingbucket). Whereas Bucket4j's blocking behavior is just that, [SuspendingBucket](-suspending-bucket/index.md) instead delays, making it safe to use in a coroutine context. |
| [SuspendingBucketConfiguration](-suspending-bucket-configuration/index.md) | [jvm]<br>data class [SuspendingBucketConfiguration](-suspending-bucket-configuration/index.md)(var timeMeter: TimeMeter = TimeMeter.SYSTEM_MILLISECONDS, var mathType: MathType = MathType.INTEGER_64_BITS, mutableLimits: [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;Bandwidth&gt; = mutableListOf()) |
| [SuspendingBucketTest](-suspending-bucket-test/index.md) | [jvm]<br>class [SuspendingBucketTest](-suspending-bucket-test/index.md) |
