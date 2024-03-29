scalaVersion := "2.13.10"

val groupId = "ai.acyclic.shapesafe"
val projectName = "shapesafe-demo"
val projectVersion = "0.1.4"

val macroParadiseVersion = "2.1.1"

/// projects
lazy val root = project
  .in(file("."))
  .settings(
    resolvers += Resolver.mavenLocal
  )
  .settings(commonSettings)

/// settings

lazy val commonSettings = {

  var settings = Def.settings(compileSettings)

  Option(System.getProperty("buildProfile"))
    .map(_.toLowerCase())
    .getOrElse("") match {

    case "" =>
      // for demo environment only, should be disabled in CI
      settings ++= Def.settings(
        Compile / unmanagedSourceDirectories ++= {

          Seq(
            sourceDirectory.value / "main" / "scala-shouldFail",
            sourceDirectory.value / "main" / "scala-complex"
          )
        }
        // enable splain plugin
        // ,
        // libraryDependencies += {
        //   val v = "1.0.0"
        //   println(s"using splain $v")
        //   compilerPlugin(
        //     "io.tryp" %% "splain" % v cross CrossVersion.patch
        //   )
        // },
        // scalacOptions ++= {
        //   Seq(
        //     "-Vimplicits",
        //     "-Vimplicits-verbose-tree"
        //   )
        // }
      )

    case "ci" =>
      settings ++= Def.settings(
        Compile / unmanagedSourceDirectories += {

          sourceDirectory.value / "main" / "scala-complex"
        }
      )
  }

  settings
}

// TODO: I see no point in maintaining an independent sbt profile, should discard it after gradle 7.3
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
  Compile / console / scalacOptions -= "-Ywarn-unused-import",
  Test / console / scalacOptions -= "-Ywarn-unused-import",
  libraryDependencies ++= Seq(
    scalaOrganization.value % "scala-compiler" % scalaVersion.value,
    "ai.acyclic.shapesafe" %% "shapesafe-core" % projectVersion
  ),
  libraryDependencies ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      // if scala 2.13+ is used, macro annotations are merged into scala-reflect
      // https://github.com/scala/scala/pull/6606
      case Some((2, v)) if v >= 13 =>
        Seq()
      case _ =>
        Seq(
          compilerPlugin(
            "org.scalamacros" % "paradise" % macroParadiseVersion cross CrossVersion.patch
          )
        )
    }
  }
)
