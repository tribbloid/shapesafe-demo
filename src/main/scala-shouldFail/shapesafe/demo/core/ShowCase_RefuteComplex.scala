package shapesafe.demo.core

import shapesafe.core.Ops
import shapesafe.core.shape.{Names, Shape}

object ShowCase_RefuteComplex {

  object Composition {
    import ShowCase_Complex.Composition._

    val s1 = Shape(1024, 768)

    val s2 = c3p1(s1, kernel, 0, 1, 2).reason

    s2.named("i", "i").einSum.-->*("i").reason
  }
}
