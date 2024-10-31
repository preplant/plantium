project.group = "plt.sprout.plantium"
project.version = "1.0.0"

val depends = mapOf(
    "exposed" to "0.56.0",
    "invui" to "1.41",
    "commandapi" to "9.6.0",
    "paper" to "1.20.4-R0.1-SNAPSHOT"
)

plugins {
    kotlin("jvm") version "2.0.0"

    id("maven-publish")
    id("java-library")
    id("com.gradleup.shadow") version "8.3.5"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }

    withSourcesJar()
    withJavadocJar()
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
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    api("org.jetbrains.exposed", "exposed-core", depends["exposed"])
    api("org.jetbrains.exposed", "exposed-dao", depends["exposed"])
    api("org.jetbrains.exposed", "exposed-jdbc", depends["exposed"])

    api("xyz.xenondevs.invui", "invui", depends["invui"])
    api("xyz.xenondevs.invui", "invui-kotlin", depends["invui"])

    api("dev.jorel", "commandapi-bukkit-shade", depends["commandapi"])

    compileOnly("io.papermc.paper", "paper-api", depends["paper"])
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