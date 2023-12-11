pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io/")
    }
}

rootProject.name = "waremoon"
include(":app")
include(":unityLibrary")
project(":unityLibrary").projectDir=File("unityLibrary")