pluginManagement {
    repositories {
        mavenLocal()
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        gradlePluginPortal()
    }
    plugins {
        id("com.android.application") version "9.2.1"
        id("com.android.library") version "9.2.1"
        id("org.jetbrains.kotlin.android") version "2.3.21"
        id("dev.rikka.tools.refine") version "4.4.0"
        id("dev.rikka.tools.autoresconfig") version "1.2.2"
        id("dev.rikka.tools.materialthemebuilder") version "1.5.1"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") }
    }
}

rootProject.name = "MaaMeow"
include(":app")
include(":hidden-api")
include(":annotation-api")
include(":ksp-processor")

// Shizuku embedded modules
val shizukuRoot = "shizuku"

// Shizuku API modules
include(":shizuku-aidl")
project(":shizuku-aidl").projectDir = file("$shizukuRoot/aidl")

include(":shizuku-shared")
project(":shizuku-shared").projectDir = file("$shizukuRoot/shared")

include(":shizuku-api")
project(":shizuku-api").projectDir = file("$shizukuRoot/api")

include(":shizuku-provider")
project(":shizuku-provider").projectDir = file("$shizukuRoot/provider")

include(":shizuku-server-shared")
project(":shizuku-server-shared").projectDir = file("$shizukuRoot/server-shared")

include(":shizuku-rish")
project(":shizuku-rish").projectDir = file("$shizukuRoot/rish")

// Shizuku core modules
include(":shizuku-common")
project(":shizuku-common").projectDir = file("$shizukuRoot/common")

include(":shizuku-starter")
project(":shizuku-starter").projectDir = file("$shizukuRoot/starter")

include(":shizuku-server")
project(":shizuku-server").projectDir = file("$shizukuRoot/server")

include(":shizuku-shell")
project(":shizuku-shell").projectDir = file("$shizukuRoot/shell")
 
