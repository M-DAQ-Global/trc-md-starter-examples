plugins {
    application
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
}

version = file("${project.rootDir}/version.txt").readText().trim()

dependencies {
    implementation("com.m-daq:trc-market-data-client-sdk:${version}")
    implementation("io.netty:netty-all:4.1.93.Final")
    implementation("org.agrona:agrona:1.18.1")
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.slf4j:slf4j-simple:1.7.32")
    testImplementation("org.slf4j:slf4j-api:1.7.32")
}

application {
    mainClass.set("trc.md.starter.App")
}