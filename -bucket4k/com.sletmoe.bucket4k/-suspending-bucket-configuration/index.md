//[Bucket4k](../../../index.md)/[com.sletmoe.bucket4k](../index.md)/[SuspendingBucketConfiguration](index.md)

# SuspendingBucketConfiguration

[jvm]\
data class [SuspendingBucketConfiguration](index.md)(var timeMeter: TimeMeter = TimeMeter.SYSTEM_MILLISECONDS, var mathType: MathType = MathType.INTEGER_64_BITS, mutableLimits: [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;Bandwidth&gt; = mutableListOf())

## Constructors

| | |
|---|---|
| [SuspendingBucketConfiguration](-suspending-bucket-configuration.md) | [jvm]<br>constructor(timeMeter: TimeMeter = TimeMeter.SYSTEM_MILLISECONDS, mathType: MathType = MathType.INTEGER_64_BITS, mutableLimits: [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;Bandwidth&gt; = mutableListOf()) |

## Functions

| Name | Summary |
|---|---|
| [addLimit](add-limit.md) | [jvm]<br>fun [addLimit](add-limit.md)(bandwidth: Bandwidth) |

## Properties

| Name | Summary |
|---|---|
| [limits](limits.md) | [jvm]<br>val [limits](limits.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Bandwidth&gt; |
| [mathType](math-type.md) | [jvm]<br>var [mathType](math-type.md): MathType |
| [timeMeter](time-meter.md) | [jvm]<br>var [timeMeter](time-meter.md): TimeMeter |
