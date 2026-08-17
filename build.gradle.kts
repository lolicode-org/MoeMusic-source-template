import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.4.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.4.10"
    id("com.gradleup.shadow") version "9.6.1"
    id("idea")
}

version = providers.gradleProperty("version").get()
group = providers.gradleProperty("maven_group").get()

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        name = "Lolicode Releases"
        url = uri("https://maven.lolicode.org/releases")
        content {
            includeGroupByRegex("org\\.lolicode.*")
        }
    }
    maven {
        name = "Lolicode Snapshots"
        url = uri("https://maven.lolicode.org/snapshots")
        content {
            includeGroupByRegex("org\\.lolicode.*")
        }
    }
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/lolicode-org/MoeMusic")
        credentials {
            username = providers.gradleProperty("gpr.user")
                .orElse(providers.environmentVariable("GITHUB_ACTOR"))
                .orElse("")
                .get()
            password = providers.gradleProperty("gpr.key")
                .orElse(providers.environmentVariable("GITHUB_PACKAGES_TOKEN"))
                .orElse(providers.environmentVariable("PACKAGES_READ_TOKEN"))
                .orElse(providers.environmentVariable("GITHUB_TOKEN"))
                .orElse("")
                .get()
        }
        content {
            includeGroupByRegex("org\\.lolicode.*")
        }
    }
}

dependencies {
    // MoeMusic API transitively provides the guaranteed runtime baseline:
    // Kotlin stdlib, kotlinx-coroutines, kotlinx-serialization (core + json), and SLF4J API.
    // Do not shade these into standalone plugin jars.
    compileOnly("org.lolicode.moemusic:api:${providers.gradleProperty("plugin_api_version").get()}")

    testImplementation(kotlin("test"))
    testImplementation("org.lolicode.moemusic:api:${providers.gradleProperty("plugin_api_version").get()}")
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
        // Lock compilation baseline to Kotlin 2.2 to match the lowest guaranteed runtime
        // across all supported host platforms (e.g. Minecraft Forge 1.20.1 KotlinForForge 4.12.0).
        apiVersion = KotlinVersion.KOTLIN_2_2
        languageVersion = KotlinVersion.KOTLIN_2_2
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

tasks.jar {
    inputs.property("projectName", project.name)

    from("LICENSE") {
        rename { "${it}_${project.name}" }
    }
}

tasks.shadowJar {
    archiveClassifier.set("full")

    /*
     * Add third-party implementation dependencies above when your real plugin needs them.
     * The shadow jar is the artifact users should put in config/moemusic/plugins/.
     *
     * Host-provided dependencies such as MoeMusic API, Kotlin runtime, slf4j, and serialization
     * stay outside this jar because the MoeMusic standalone plugin classloader delegates those
     * packages to the parent runtime first.
     */
    dependencies {
        exclude(dependency("org.jetbrains.kotlin:.*:.*"))
        exclude(dependency("org.jetbrains:annotations:.*"))
        exclude(dependency("org.jetbrains.kotlinx:.*:.*"))
        exclude(dependency("org.slf4j:.*:.*"))
        exclude(dependency("org.lolicode.moemusic:.*:.*"))
    }
}
