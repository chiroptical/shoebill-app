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

val arrowVersion = "1.1.4-rc.3"

dependencies {
    add("kspCommonMainMetadata", "io.arrow-kt:arrow-optics-ksp-plugin:$arrowVersion")
}

fun makeKspDir(name: String) = file("build/generated/ksp/metadata/$name/kotlin")

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
                kotlin.srcDir(makeKspDir("commonMain"))
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