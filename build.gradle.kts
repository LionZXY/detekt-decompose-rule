plugins {
    alias(libs.plugins.kotlin.jvm)
    id("maven-publish")
}

group = "uk.kulikov.detekt.decompose"
version = "1.0-SNAPSHOT"

dependencies {
    compileOnly(libs.decompose)
    compileOnly(libs.detekt.api)

    testImplementation(libs.detekt.test)
    testImplementation(libs.kotest)
    testImplementation(libs.jupiter)
}

kotlin {
    jvmToolchain(8)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    systemProperty("junit.jupiter.testinstance.lifecycle.default", "per_class")
    systemProperty("compile-snippet-tests", project.hasProperty("compile-test-snippets"))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
