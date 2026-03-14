import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier
import java.net.URL

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("org.jlleitschuh.gradle.ktlint")

    id("signing")
    id("maven-publish")
    id("io.github.gradle-nexus.publish-plugin")
}

group = "com.sletmoe.bucket4k"
version = Ci.version

repositories {
    mavenCentral()
}

dependencies {
    implementation(KotlinX.coroutines.core)
    api("com.bucket4j:bucket4j_jdk17-core:_")

    testImplementation(kotlin("test"))
    testImplementation(Testing.kotest.runner.junit5)
    testImplementation(Testing.kotest.assertions.core)
    testImplementation(KotlinX.coroutines.test)
}

tasks.test {
    useJUnitPlatform()
}

java {
    withJavadocJar()
    withSourcesJar()

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    compilerOptions {
        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_3)
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dokka {
    moduleName.set(project.name)
    moduleVersion.set(project.version.toString())

    dokkaPublications.html {
        outputDirectory.set(layout.buildDirectory.dir("dokka/html"))
        failOnWarning.set(false)
        suppressObviousFunctions.set(true)
        suppressInheritedMembers.set(false)
        offlineMode.set(false)
    }

    dokkaSourceSets.main {
        documentedVisibilities.set(setOf(VisibilityModifier.Public))
        reportUndocumented.set(false)
        skipEmptyPackages.set(true)
        skipDeprecated.set(false)
        suppressGeneratedFiles.set(true)
        jdkVersion.set(17)
        languageVersion.set("17")
        apiVersion.set("17")
        enableKotlinStdLibDocumentationLink.set(true)
        enableJdkDocumentationLink.set(true)
        sourceRoots.from(file("src"))

        sourceLink {
            localDirectory.set(projectDir.resolve("src"))
            remoteUrl.set(URL("https://github.com/ksletmoe/Bucket4k/tree/mainline/src").toURI())
            remoteLineSuffix.set("#L")
        }

        perPackageOption {
            suppress.set(false)
            skipDeprecated.set(false)
            reportUndocumented.set(false)
            documentedVisibilities.set(
                setOf(
                    VisibilityModifier.Public,
                ),
            )
        }
    }
}

tasks.register("dokkaHtml") {
    group = "documentation"
    description = "Backward-compatible alias for Dokka HTML generation."
    dependsOn(tasks.named("dokkaGeneratePublicationHtml"))
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
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
}
