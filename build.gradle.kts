plugins {
    id("java")
    id("maven-publish")
    kotlin("jvm")
}

group = "io.simple"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(22)
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = project.group.toString()
            artifactId = "simple.http.server"
            version = project.version.toString()

            pom {
                name.set("SimpleHttpServer-Kotlin")
                description.set("A simple server library written purely in Kotlin")
                url.set("https://github.com/faridGuliyew/SimpleHttpServer-Kotlin")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("https://github.com/faridGuliyew")
                        name.set("Farid Guliyev")
                        email.set("ferid.quliyev.sec@gmail.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/faridGuliyew/SimpleHttpServer-Kotlin.git")
                    developerConnection.set("scm:git:ssh://github.com:faridGuliyew/SimpleHttpServer-Kotlin.git")
                    url.set("https://github.com/faridGuliyew/SimpleHttpServer-Kotlin")
                }
            }
        }
    }


    repositories {
        maven {
            name = "SimpleHttpServer-Kotlin"
            url = uri("https://maven.pkg.github.com/faridGuliyew/SimpleHttpServer-Kotlin")

            credentials {
                username = (findProperty("repoUser") as String? ?: "")
                password = (findProperty("repoPassword") as String? ?: "")
            }
        }
    }
}