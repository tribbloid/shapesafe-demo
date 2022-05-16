val vs: Versions = versions()

dependencies {

    implementation(project(":core"))
    testImplementation(testFixtures(project(":core")))
    //Caution: NOT the same version as compiler plugin. Should always depend on a published release
}
