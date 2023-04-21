# Bucket4k
![build](https://github.com/ksletmoe/Bucket4k/actions/workflows/build.yml/badge.svg)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://github.com/ksletmoe/Bucket4k/blob/mainline/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/com.sletmoe.bucket4k/bucket4k.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.sletmoe.bucket4k%22%20AND%20a:%22bucket4k%22)
[<img src="https://img.shields.io/nexus/s/https/s01.oss.sonatype.org/com.sletmoe.bucket4k/bucket4k.svg?label=latest%20snapshot&style=plastic"/>](https://s01.oss.sonatype.org/content/repositories/snapshots/com/sletmoe/bucket4k/bucket4k/)


A Kotlin wrapper around [Bucket4j](https://github.com/bucket4j/bucket4j) which suspends and plays nicely with coroutines.

See the [changelog](https://github.com/ksletmoe/Bucket4k/blob/mainline/changelog.md).

## Features
* Not another token bucket implementation: Uses Bucket4k's tried and true token bucket implementation.
* No locks: Wraps Bucket4j's LockFreeBucket.
* Suspendable functions: None of the included functionality blocks the calling thread, instead suspending coroutine 
  execution while waiting for available tokens. No locks means no blocked threads waiting for said locks.

## Using Bucket4k
Add Bucket4k as a project dependency:

`build.gradle.kts:`
```kotlin
dependencies {
    implementation("com.sletmoe.bucket4k:bucket4k:<version>")
}
```

Create a SuspendingBucket, configuring token limits as desired:

```kotlin
val tokenBucket = SuspendingBucket.build {
    addLimit(Bandwidth.simple(5, 1.seconds.toJavaDuration()))
    addLimit(Bandwidth.simple(30, 1.minutes.toJavaDuration()))
}
```

We can then use the token bucket. For example, to limit transactions per second on an API operation within a Ktor web application:
```kotlin
fun Route.widgetRouting() {
    route("/widget") {
        get {
            if (tokenBucket.tryConsume(1)) {
                call.respond(listWidgets())
            } else {
                call.respondText("Too many requests! Slow your roll.", status = HttpStatusCode.TooManyRequests)
            }
        }
    }
}
```

Or we could use it to rate-limit asynchronous calls to some API:

```kotlin
val webPagesToGet = listOf("https://my.web.page/foo", ...)

val results = webPagesToGet.map { webPageUri ->
    async(Dispatchers.IO) { 
        tokenBucket.consume(1)
        httpClient.get(webPageUri)
    }
}.map { it.await() }
```

Read the [API documentation](https://bucket4k.sletmoe.com/bucket4k/com.sletmoe.bucket4k/index.html) for more information.
