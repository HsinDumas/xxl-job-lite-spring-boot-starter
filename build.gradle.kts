plugins {
    `java-library`
    id("com.vanniktech.maven.publish") version "0.37.0"
}

group = "com.github.hsindumas"

val resolvedVersion = providers.gradleProperty("releaseVersion")
    .orElse(providers.environmentVariable("RELEASE_VERSION"))
    .orElse("3.4.2-SNAPSHOT")
version = resolvedVersion.get()

val nettyVersion = "4.2.15.Final"
val gsonVersion = "2.14.0"
val groovyVersion = "5.0.6"
val springVersion = "6.2.8"
val springBootVersion = "3.4.8"
val slf4jApiVersion = "2.0.18"
val jakartaAnnotationApiVersion = "3.0.0"
val xxlToolVersion = "2.5.0"

repositories {
    mavenLocal()
    maven(url = "https://maven.aliyun.com/repository/public")
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(17)
}

fun Project.enforceTagDrivenRelease() {
    val isCi = providers.environmentVariable("CI").orNull == "true"
    val refType = providers.environmentVariable("GITHUB_REF_TYPE").orNull
    val refName = providers.environmentVariable("GITHUB_REF_NAME").orNull
    val releaseVersion = providers.gradleProperty("releaseVersion")
        .orElse(providers.environmentVariable("RELEASE_VERSION"))
        .orNull

    if (!isCi || refType != "tag" || refName.isNullOrBlank()) {
        throw GradleException(
            "Release publishing is restricted to GitHub Actions tag builds (push tag vX.Y.Z)."
        )
    }

    if (releaseVersion.isNullOrBlank()) {
        throw GradleException(
            "releaseVersion must be provided from tag workflow and match the pushed tag version."
        )
    }

    val expectedVersion = refName.removePrefix("v")
    if (releaseVersion != expectedVersion) {
        throw GradleException(
            "releaseVersion ($releaseVersion) does not match tag version ($expectedVersion)."
        )
    }
}

gradle.taskGraph.whenReady {
    val releasePublishRequested = allTasks.any {
        it.name == "publishReleaseToMavenCentral" || it.name == "publishAndReleaseToMavenCentral"
    }
    if (releasePublishRequested) {
        project.enforceTagDrivenRelease()
    }
}

dependencies {
    implementation("io.netty:netty-codec-http:$nettyVersion")
    implementation("com.google.code.gson:gson:$gsonVersion")
    implementation("com.xuxueli:xxl-tool:$xxlToolVersion")
    implementation("org.apache.groovy:groovy:$groovyVersion")

    compileOnly("org.springframework:spring-context:$springVersion")
    compileOnly("org.springframework:spring-web:$springVersion")

    compileOnly("org.springframework.boot:spring-boot-starter:$springBootVersion")
    compileOnly("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")
    compileOnly("org.springframework.boot:spring-boot-configuration-processor:$springBootVersion")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:$springBootVersion")

    implementation("org.slf4j:slf4j-api:$slf4jApiVersion")

    compileOnly("jakarta.annotation:jakarta.annotation-api:$jakartaAnnotationApiVersion")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.register("printVersion") {
    group = "help"
    description = "Prints the current project version"
    doLast {
        println(project.version)
    }
}

tasks.register("publishReleaseToMavenCentral") {
    group = "publishing"
    description = "Publishes and releases this module to Sonatype Central"
    dependsOn("publishAndReleaseToMavenCentral")
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates(
        "com.github.hsindumas",
        "xxl-job-lite-spring-boot-starter",
        project.version.toString(),
    )

    pom {
        name = "xxl-job-lite-spring-boot-starter"
        description = "适用于 xxl-job 的 spring-boot-starter (阉割 embedServer)"
        url = "https://github.com/HsinDumas/xxl-job-lite-spring-boot-starter"
        licenses {
            license {
                name = "GNU GENERAL PUBLIC LICENSE, Version 3"
                url = "https://github.com/HsinDumas/xxl-job-lite-spring-boot-starter/blob/master/LICENSE"
            }
        }
        developers {
            developer {
                id = "HsinDumas"
                name = "HsinDumas"
                email = "HsinDumas@gmail.com"
            }
        }
        scm {
            url = "https://github.com/HsinDumas/xxl-job-lite-spring-boot-starter"
            connection = "scm:git:git://github.com/HsinDumas/xxl-job-lite-spring-boot-starter.git"
            developerConnection = "scm:git:ssh://git@github.com/HsinDumas/xxl-job-lite-spring-boot-starter.git"
        }
    }
}

tasks.matching { it.name == "generateMetadataFileForMavenPublication" }.configureEach {
    dependsOn("plainJavadocJar")
}
