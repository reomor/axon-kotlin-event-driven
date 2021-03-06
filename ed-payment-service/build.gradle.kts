import org.apache.tools.ant.filters.ReplaceTokens
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val axonSpringBootStarter: String by project
val h2Version: String by project

plugins {

  kotlin("jvm")
  kotlin("plugin.spring")
  kotlin("plugin.noarg")

  id("org.springframework.boot")
  id("io.spring.dependency-management")
}

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

noArg {
  annotation("org.axonframework.spring.stereotype.Aggregate")
  annotation("javax.persistence.Entity")
}

dependencies {

  implementation(projects.edCore)
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.axonframework:axon-spring-boot-starter:$axonSpringBootStarter")
  implementation("com.h2database:h2:$h2Version")

  compileOnly("org.projectlombok:lombok")
  annotationProcessor("org.projectlombok:lombok")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
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

tasks.withType<ProcessResources> {
  copySpec {
    from("src/main/resources")
    include(
      "**/application*.yml",
      "**/application*.yaml",
      "**/application*.properties",
    )
    project.properties.forEach { prop ->
      if (prop.value != null) {
        filter(ReplaceTokens::class, "tokens" to mapOf(prop.key to prop.value))
        filter(ReplaceTokens::class, "tokens" to mapOf("project." + prop.key to prop.value))
      }
    }
  }
}
