plugins {
    `kotlin-dsl`
}

repositories {

    mavenCentral()
    maven {
        url = uri("https://artifactory.sg.m-daq.net/artifactory/repo")
    }

}

version = file("${project.rootDir}/version.txt").readText().trim()

dependencies {
    implementation ("com.mdaq.trc:trc-market-data-client-sdk:${version}")
}


val distPath = "../dist"
val appPath = "$distPath/app"

val generateDistribution = tasks.register("generateDistribution", Copy::class) {
    delete(distPath)
    val customLocation = file("$appPath/libs")
    configurations.runtimeClasspath.get().forEach{
        if(it.name.startsWith("trc-market-data-client-sdk")){
            from(it)
            into(customLocation)
        }
    }

    copy {
        from("../app"){
            include("**/*.*")
            exclude("**/build.gradle.*")
            exclude("src/main/resources/client.truststore")
        }
        into(appPath)
    }

    val tokens = mapOf("version" to version)
    inputs.properties(tokens)
    copy {
        from("../app/build.gradle.kts.template")
        into(appPath)
        rename("build.gradle.kts.template", "build.gradle.kts")
        filter<org.apache.tools.ant.filters.ReplaceTokens>("tokens" to tokens)
    }

    copy {
        from("../Readme.md")
        into(appPath)
    }

}

val sdkVersion = version
val buildPath = "../build"
tasks.register<Zip>("packageDistribution") {
    delete(buildPath)
    archiveFileName.set("trc-md-starter-${sdkVersion}.zip")
    destinationDirectory.set(File(buildPath))

    from(distPath)
    dependsOn(generateDistribution)
}

