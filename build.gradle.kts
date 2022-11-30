val junit_jupiter_version: String by project
val assertj_version: String by project
val lombok_version: String by project
val jetbrains_annotations_version: String by project
val mockito_version: String by project


plugins {
    id("java")
}

group = "geektime.tdd.args"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations:$jetbrains_annotations_version")
    compileOnly("org.projectlombok:lombok:$lombok_version")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit_jupiter_version")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junit_jupiter_version")
    testImplementation("org.assertj:assertj-core:$assertj_version") {
        exclude("net.bytebuddy:byte-buddy")
    }

    testImplementation("org.mockito:mockito-junit-jupiter:$mockito_version")


    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit_jupiter_version")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
