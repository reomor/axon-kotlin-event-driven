plugins {

  kotlin("jvm") apply false
  kotlin("plugin.spring") apply false
  kotlin("plugin.noarg") apply false

  id("org.springframework.boot") apply false
  id("io.spring.dependency-management") apply false
}

group = "com.github.reomor"
version = "0.0.1-SNAPSHOT"
//java.sourceCompatibility = JavaVersion.VERSION_11

subprojects {

  group = rootProject.group
  version = rootProject.version

  repositories {
    mavenCentral()
  }
}
