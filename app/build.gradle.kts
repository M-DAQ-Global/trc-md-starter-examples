/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/7.6/userguide/building_java_projects.html
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    implementation(files("../libs/trc-market-data-client-sdk-1.2.0-SNAPSHOT.jar"))
    implementation("io.netty:netty-all:4.1.93.Final")
    implementation("org.agrona:agrona:1.18.1")
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("com.opencsv:opencsv:5.1")
    testImplementation("org.slf4j:slf4j-api:1.7.32")

}

application {
    // Define the main class for the application.
    mainClass.set("trc.md.starter.App")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
