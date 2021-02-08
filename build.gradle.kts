val vs: Versions = versions()

dependencies {

    implementation(project(":verify:breeze"))
    testApi(testFixtures(project(":macro")))
}
