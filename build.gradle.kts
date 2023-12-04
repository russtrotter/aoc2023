plugins {
    id("java")
}

group = "net.flatball"
version = "1.0-SNAPSHOT"
java {
    version = JavaVersion.VERSION_21;
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}