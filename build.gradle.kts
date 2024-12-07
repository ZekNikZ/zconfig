plugins {
    id("java")
}

group = "io.zkz.zconfig"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly("org.jetbrains:annotations:26.0.1")
}

tasks.test {
    useJUnitPlatform()
}