plugins {
    kotlin("multiplatform") version "1.7.22"
    application
    idea
    id("com.google.devtools.ksp") version "1.7.22-1.0.8"
}

group = "app.shoebill"
version = "0.1"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

val arrowVersion = "1.1.4-rc.2"

dependencies {
    add("kspCommonMainMetadata", "io.arrow-kt:arrow-optics-ksp-plugin:$arrowVersion")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(IR) {
        // binaries.executable()
        browser()
        nodejs()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.arrow-kt:arrow-core:$arrowVersion")
                implementation("io.arrow-kt:arrow-optics:$arrowVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-netty:2.0.2")
                implementation("io.ktor:ktor-server-html-builder-jvm:2.0.2")
                implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
            }
        }
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react:18.2.0-pre.346")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:18.2.0-pre.346")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion:11.9.3-pre.346")
            }
        }
        val jsTest by getting
    }
}

application {
    mainClass.set("app.shoebill.application.ServerKt")
}

// Using `nodejs()` in `js(IR)` block above
// tasks.named<Copy>("jvmProcessResources") {
//     val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
//     from(jsBrowserDistribution)
// }

tasks.named<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.named<Jar>("jvmJar"))
}

// Took this from https://kotlinlang.org/docs/ksp-quickstart.html#make-ide-aware-of-generated-code
//idea {
//    module {
//        // Not using += due to https://github.com/gradle/gradle/issues/8749
//        sourceDirs = sourceDirs + file("build/generated/ksp/main/kotlin") // or tasks["kspKotlin"].destination
//        testSourceDirs = testSourceDirs + file("build/generated/ksp/test/kotlin")
//        generatedSourceDirs = generatedSourceDirs + file("build/generated/ksp/main/kotlin") + file("build/generated/ksp/test/kotlin")
//    }
//}