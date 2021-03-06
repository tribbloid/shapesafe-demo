import scala.sys.process._

/// variables

scalaVersion := "2.13.5"

val groupId = "org.shapesafe"
val projectName = "shapesafe-demo"
val projectVersion = "0.1.0-SNAPSHOT"

val macroParadiseVersion = "2.1.1"
//val shapelessVersion = "2.3.3"
//val scalaCheckVersion = "1.15.2"

/// projects
lazy val root = project
  .in(file("."))
  .settings(commonSettings)
  .settings(
    resolvers += Resolver.mavenLocal,
    skip in publish := true,
    sources in Compile := Seq.empty,
    sources in Test := Seq.empty
  )

/// settings

lazy val commonSettings = Def.settings(
  compileSettings
)

lazy val compileSettings = Def.settings(
  scalaOrganization := "org.scala-lang",
  scalacOptions ++= Seq(
    "-encoding",
    "UTF-8",
    "-unchecked",
    "-deprecation",
    "-feature",
//    "-language:existentials",
    "-language:higherKinds",
//    "-language:implicitConversions",
//    "-language:experimental.macros",
    "-Xfatal-warnings",
    //    "-Xlint:-unused,_",
    //    "-Yliteral-types",
    "-Ywarn-numeric-widen"
    //    "-Ywarn-value-discard"
  ),
  scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, v)) if v >= 13 =>
        Nil
      case _ =>
        Seq(
          "-Yno-adapted-args",
          "-Ywarn-unused-import",
          "-Xplugin-require:macroparadise"
        )
    }
  },
  scalacOptions in (Compile, console) -= "-Ywarn-unused-import",
  scalacOptions in (Test, console) -= "-Ywarn-unused-import",
  libraryDependencies ++= Seq(
    scalaOrganization.value % "scala-compiler" % scalaVersion.value,
    "org.shapesafe" % "shapesafe-djl" % projectVersion,
    "org.shapesafe" % "shapesafe-breeze" % projectVersion
    // "org.scalacheck" %% "scalacheck" % scalaCheckVersion % Test
  ),
  libraryDependencies ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      // if scala 2.13+ is used, macro annotations are merged into scala-reflect
      // https://github.com/scala/scala/pull/6606
      case Some((2, v)) if v >= 13 =>
        Seq()
      case v @ _ =>
        println(v)
        Seq(
          compilerPlugin(
            "org.scalamacros" % "paradise" % macroParadiseVersion cross CrossVersion.patch
          )
        )
    }
  }
)

// lazy val miscSettings = Def.settings(
//   initialCommands += s"""
//     import $rootPkg.ops._
//     import $rootPkg.twoface._
//   """
// )
