plugins {
    kotlin("multiplatform")
    id("kotlinx-serialization")
    id("maven-publish")
}

kotlin {
    jvm()
    js {
        browser {
            testTask {
                enabled = false
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core")

                api(project(":common-runtime"))
            }
        }
    }
}
//
//dependencies {
//    commonMainImplementation("org.jetbrains.kotlin:kotlin-stdlib-common")
//    commonMainImplementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common")
//    commonMainApi(project(":common-runtime"))
//
//    commonTestImplementation("org.jetbrains.kotlin:kotlin-test-common")
//    commonTestImplementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
//
//
//    "jvmMainImplementation"("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
//    "jvmMainImplementation"("com.fasterxml.jackson.module:jackson-module-kotlin")
//    "jvmMainImplementation"("org.jetbrains.kotlinx:kotlinx-serialization-runtime")
//    "jvmTestImplementation"("org.jetbrains.kotlin:kotlin-test-junit5")
//    "jvmTestImplementation"("org.junit.jupiter:junit-jupiter-engine")
//
//    "jsMainImplementation"("org.jetbrains.kotlin:kotlin-stdlib-js")
//    "jsMainImplementation"("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js")
//
//    "jsTestImplementation"("org.jetbrains.kotlin:kotlin-test-js")
//}
