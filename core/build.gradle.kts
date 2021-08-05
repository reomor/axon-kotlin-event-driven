import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

val axonSpringBootStarter: String by project

plugins {

  kotlin("jvm")
  kotlin("plugin.spring")
  kotlin("plugin.noarg")

  id("org.springframework.boot")
  id("io.spring.dependency-management")
}

group = "com.github.reomor"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

noArg {
  annotation("org.axonframework.spring.stereotype.Aggregate")
  annotation("javax.persistence.Entity")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.axonframework:axon-spring-boot-starter:$axonSpringBootStarter")

  compileOnly("org.projectlombok:lombok")
  annotationProcessor("org.projectlombok:lombok")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
}

// just a library without main class
tasks.withType<BootJar> {
  enabled = false
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "11"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
