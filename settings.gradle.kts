pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)

    repositories {
        google()
        mavenCentral()


    }
}

rootProject.name = "RemindmeV2"
include(":app")



// content {
//  includeGroupByRegex("com\\.android.*")
//includeGroupByRegex("com\\.google.*")
//includeGroupByRegex("androidx.*")
// }
