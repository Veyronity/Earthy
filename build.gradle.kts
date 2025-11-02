plugins {
    id("java")
    id("org.ajoberstar.grgit") version "5.0.0-rc.3"
}

group = "au.lupine"
version = "2.0.0"

subprojects {
    group = rootProject.group
    version = rootProject.version

    apply(plugin = "java")

    tasks.jar {
        archiveFileName.set("earthy-${project.name}-${project.version}.jar")

        from("${rootDir}/LICENSE.txt") {
            into("")
        }
    }

    base {
        archivesName.set(rootProject.name)
    }

    repositories {
        mavenCentral()
    }
}

tasks.jar {
    enabled = false
}
