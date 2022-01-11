val vs: Versions = versions()

dependencies {

    implementation(project(":core"))
    testImplementation(testFixtures(project(":macro")))
}
