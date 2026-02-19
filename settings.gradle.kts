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
        maven { url = uri("https://jitpack.io") } // Often needed for OSMDroid or other libs
    }
}

<<<<<<< HEAD
rootProject.name = "MiteGo"
=======
rootProject.name = "CaçaMites"
>>>>>>> 0b5bd63 (Branding: Transformació a CaçaMites i infraestructura Backend/CI/CD)
include(":app")
