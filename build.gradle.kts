plugins {
    java
    `build-dashboard`
    checkstyle
    pmd
    id("com.github.spotbugs") version "4.5.0"
    jacoco
    id("nebula.release") version "15.1.0"
}

extra.set("junitVersion", "5.6.2")

val junitVersion: String by extra

repositories {
    jcenter()
}

dependencies {

    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(
        arrayOf(
            "-parameters",
            "-Xdoclint:none",
            "-Xlint",
            "-Xlint:all",
            "-Xlint:-serial",
            "-Xlint:-rawtypes"
        )
    )
}

checkstyle {
    toolVersion = "8.36"
}

tasks.withType<com.github.spotbugs.snom.SpotBugsTask>().configureEach {
    showProgress.set(true)
    effort.set(com.github.spotbugs.snom.Effort.MAX)
    reports.create("html") {
        isEnabled = true
    }
}

afterEvaluate {
    tasks.findByName("spotbugsTest")
}

tasks.withType<JacocoReport> {
    reports.html.destination = file("${buildDir}/reports/jacoco")
}

tasks.withType<Test> {
    finalizedBy(tasks.getByName("jacocoTestReport"))
}

//tasks.jacocoTestReport {
//    reports {
//        xml.isEnabled = false
//        csv.isEnabled = false
//        html.destination = file("${buildDir}/jacocoHtml")
//    }
//}
