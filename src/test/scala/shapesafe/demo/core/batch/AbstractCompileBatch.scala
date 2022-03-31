package shapesafe.demo.core.batch

import shapesafe.BaseSpec
import splain.runtime.TryCompile

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

    var durationMS: Long = -1

    lazy val durationMSCap: Long = 20 * 1000

    it(s"benchmarking ...") {
      engine.reporter.reset()

      val t1 = System.currentTimeMillis()
      val compile = engine.apply(code)
      val t2 = System.currentTimeMillis()

      durationMS = t2 - t1

      val issues = compile.issues
      val out = issues.map(v => v.copy(sourceName = names).display).mkString("\n")

      out.shouldBe(groundTruth)
    }

    it(s"ETA ${durationMSCap}ms") {
      println("")

      assert(durationMS <= durationMSCap, ": Benchmark is too slow")
    }
  }

}
