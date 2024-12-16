val javaLanguageVersion = 17

project.group = "plt.sprout.plantium"
project.version = "2.5.0"

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadow)

    id("maven-publish")
    id("java-library")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaLanguageVersion))
    }

    withSourcesJar()
    withJavadocJar()
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(javaLanguageVersion))
    }
}

repositories {
    mavenCentral()

    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.xenondevs.xyz/releases")
}

publishing {
    publications {
        create<MavenPublication>("jitpack") {
            artifact(tasks.shadowJar)

            artifact(tasks.named<Jar>("sourcesJar"))
            artifact(tasks.named<Jar>("javadocJar"))

            pom {
                withXml {
                    asNode().appendNode("repositories").apply {
                        appendNode("repository").apply {
                            appendNode("id", "codemc")
                            appendNode("url", "https://repo.codemc.io/repository/maven-public/")
                        }
                        appendNode("repository").apply {
                            appendNode("id", "papermc")
                            appendNode("url", "https://repo.papermc.io/repository/maven-public/")
                        }
                        appendNode("repository").apply {
                            appendNode("id", "sonatype")
                            appendNode("url", "https://oss.sonatype.org/content/groups/public/")
                        }
                        appendNode("repository").apply {
                            appendNode("id", "xenondevs")
                            appendNode("url", "https://repo.xenondevs.xyz/releases")
                        }
                    }
                }
            }
        }
    }
}

dependencies {
    implementation(libs.stdlib)
    implementation(libs.reflect)
    api(libs.exposedCore)
    api(libs.exposedDao)
    api(libs.exposedJdbc)
    api(libs.invui)
    api(libs.invuiKotlin)
    api(libs.commandapi)
    compileOnly(libs.paper)
}

sourceSets {
    main {
        kotlin.srcDir("src/main/kotlin")
    }
}

defaultTasks("build")

tasks {
    processResources {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    jar {
        enabled = false
    }

    shadowJar {
        archiveClassifier.set("")
    }

    build {
        dependsOn(shadowJar)
    }
}
