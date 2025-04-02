pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven(url="https://jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url="https://jitpack.io")
    }
}



<<<<<<< HEAD
rootProject.name = "My Application Genius"
=======
rootProject.name = "My Application"
>>>>>>> a7950c80503642a42f360b8df071c530ae832800
include(":app")
