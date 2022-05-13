val vs: Versions = versions()

dependencies {

    implementation(project(":core"))
    testImplementation(testFixtures(project(":core")))
    testImplementation("io.tryp:splain_${vs.scalaV}:1.0.1")
    //Caution: NOT the same version as compiler plugin. Should always depend on a published release
}
