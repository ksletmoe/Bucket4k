rootProject.name = "bucket4k"

plugins {
    id("de.fayard.refreshVersions") version "0.60.5"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

refreshVersions {
    enableBuildSrcLibs()
}

refreshVersions {
    enableBuildSrcLibs()
    rejectVersionIf {
        candidate.stabilityLevel != de.fayard.refreshVersions.core.StabilityLevel.Stable
    }
}
