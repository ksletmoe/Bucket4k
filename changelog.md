# Changelog

#### 2.1.0
* Updated dependency and plugin versions:
  * Kotlin `2.2.21`
  * kotlinx.coroutines `1.10.2`
  * Kotest `6.1.3`
  * Bucket4j `8.16.1`
  * Dokka Gradle plugin `2.1.0`
  * ktlint Gradle plugin `12.3.0`
  * de.fayard.buildSrcLibs `0.60.6`
* Added `kotlinx-coroutines-test` to test dependencies for coroutine test API compatibility.
* Regenerated `versions.properties` using `./gradlew refreshVersions`.

#### 2.0.0
* Updated to build for JDK17 ahead of the OpenJDK 11 EOL date.
* Updated dependencies

#### 1.1.0
Updated Bucket4J to version 8.9.0 and added support for the new [Bandwidth builder API](https://bucket4j.com/8.9.0/toc.html#bandwidth).

#### 1.0.0
Initial release.
