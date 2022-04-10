package shapesafe.demo.core.batch

import shapesafe.BaseSpec
import splain.runtime.{Issue, TryCompile}

import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source

trait AbstractCompileBatch {

  lazy val engine = TryCompile.UseNSC("")

  def dir: String

  trait SourceSpec extends BaseSpec {

    def groundTruth: String

    lazy val names: String = this.getClass.getName
      .stripSuffix("$")
      .stripSuffix("Spec")
      .split('.')
      .mkString(File.separator)

    lazy val path: Path = Paths.get(dir, s"$names.scala")

    lazy val code: String = {

      val src = Source.fromFile(path.toUri)
      val code = src.getLines().mkString("\n")
      code
    }

    final lazy val (issues: Seq[Issue], durationMS: Long) = {
      engine.reporter.reset()

      val t1 = System.currentTimeMillis()
      val compile = engine.apply(code)
      val t2 = System.currentTimeMillis()

      compile.issues -> (t2 - t1)
    }

    lazy val durationMSCap: Long = 20 * 1000

    describe(names) {
      it(s"Compiling ...") {
        val out = issues.map(v => v.copy(sourceName = names).display).mkString("\n")

        out.shouldBe(groundTruth)
      }

      val CapMsg = s"ETA ${durationMSCap}ms"
      it(CapMsg) {

        val msg = s"takes ${durationMS}ms - $CapMsg"

        println(s"$names - $msg")

        Predef.assert(
          durationMS <= durationMSCap,
          s"compilation is too slow: $msg"
        )
      }
    }

  }

}
