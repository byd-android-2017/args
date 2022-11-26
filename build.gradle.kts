val junit_jupiter_version: String by project
val assertj_version: String by project

plugins {
    id("java")
}

group = "geektime.tdd.args"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:20.1.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit_jupiter_version")
    testImplementation("org.assertj:assertj-core:$assertj_version")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit_jupiter_version")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
