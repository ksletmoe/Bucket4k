import org.jetbrains.dokka.DokkaConfiguration.Visibility
import org.jetbrains.dokka.Platform
import org.jetbrains.dokka.gradle.DokkaTask
import java.net.URL

plugins {
    kotlin("jvm") version "1.8.10"
    id("org.jetbrains.dokka") version "1.8.10"
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1"

    id("signing")
    id("maven-publish")
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
}

group = "com.sletmoe.bucket4k"
version = Ci.version

repositories {
    mavenCentral()
}

dependencies {
    implementation(KotlinX.coroutines.core)
    api("com.bucket4j:bucket4j-core:_")

    testImplementation(kotlin("test"))
    testImplementation(Testing.kotest.runner.junit5)
    testImplementation(Testing.kotest.assertions.core)
}

tasks.test {
    useJUnitPlatform()
}

java {
    withJavadocJar()
    withSourcesJar()

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

kotlin {
    jvmToolchain(11)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
    }
}

tasks.withType<DokkaTask>().configureEach {
    moduleName.set(project.name)
    moduleVersion.set(project.version.toString())
    outputDirectory.set(buildDir.resolve("dokka/$name"))
    failOnWarning.set(false)
    suppressObviousFunctions.set(true)
    suppressInheritedMembers.set(false)
    offlineMode.set(false)

    dokkaSourceSets {
        configureEach {
            documentedVisibilities.set(setOf(Visibility.PUBLIC))
            reportUndocumented.set(false)
            skipEmptyPackages.set(true)
            skipDeprecated.set(false)
            suppressGeneratedFiles.set(true)
            jdkVersion.set(11)
            languageVersion.set("1.8")
            apiVersion.set("1.8")
            noStdlibLink.set(false)
            noJdkLink.set(false)
            platform.set(Platform.DEFAULT)
            sourceRoots.from(file("src"))

            sourceLink {
                localDirectory.set(projectDir.resolve("src"))
                remoteUrl.set(URL("https://github.com/ksletmoe/Bucket4k/tree/mainline/src"))
                remoteLineSuffix.set("#L")
            }

            perPackageOption {
                suppress.set(false)
                skipDeprecated.set(false)
                reportUndocumented.set(false)
                documentedVisibilities.set(
                    setOf(
                        Visibility.PUBLIC,
                    ),
                )
            }
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name.set("bucket4k")
                description.set("A suspending Kotlin wrapper around Bucket4j.")
                url.set("https://www.github.com/ksletmoe/Bucket4k")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("ksletmoe")
                        name.set("Kyle Sletmoe")
                        email.set("kyle.sletmoe@gmail.com")
                    }
                }

                scm {
                    url.set("https://github.com/ksletmoe/Bucket4k")
                    connection.set("scm:git://github.com/ksletmoe/Bucket4k.git")
                    developerConnection.set("scm:git://github.com/ksletmoe/Bucket4k")
                }
            }
        }
    }
}

val signingKey: String? by project
val signingPassword: String? by project

signing {
    useGpgCmd()

    if (signingKey != null && signingPassword != null) {
        @Suppress("UnstableApiUsage")
        useInMemoryPgpKeys(signingKey, signingPassword)
    }

    sign(publishing.publications["mavenJava"])
//    val signingKey = findProperty("signingKey") as String?
//    val signingPassword = findProperty("signingPassword") as String?
//    useInMemoryPgpKeys(signingKey, signingPassword)
//    @Suppress("UnstableApiUsage")
//    sign(publishing.publications["mavenJava"])
}
