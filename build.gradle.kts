plugins {
    java
    kotlin("jvm") version "1.4.10"
}

group = "no.royag"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    testImplementation("junit:junit:4.12")
}
