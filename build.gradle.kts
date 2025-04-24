plugins {
    kotlin("jvm") version "2.2.0-Beta1"

    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("io.papermc.paperweight.userdev") version "2.0.0-SNAPSHOT"
}

group = "dev.decduck3"
version = "1.0-SNAPSHOT"

val embed: Configuration by configurations.creating
configurations.compileOnly.get().extendsFrom(embed)

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven("https://repo.onarandombox.com/content/groups/public/")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    embed("com.esotericsoftware:kryo:5.6.2")

    // Other Dependencies
    paperweight.paperDevBundle("1.21.5-R0.1-SNAPSHOT")
}

tasks {
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.21.5")
    }
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

tasks.shadowJar {
    from(
        embed.resolve().map {
            if (it.isDirectory) it else zipTree(it).matching { exclude { it.name.endsWith("module-info.class") } }
        }) {}
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
