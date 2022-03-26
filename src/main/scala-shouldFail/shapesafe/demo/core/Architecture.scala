package shapesafe.demo.core

import shapesafe.core.arity.Arity
import shapesafe.core.shape.{Shape, Names, Indices, Index}
import shapesafe.core.Ops

object Architecture {

  object Example1 {

    val a = Shape(1, 2)
    val b = Shape(3, 4)
    (a >< b).reason
    val s1 = (a >< b).:<<=*("i", "j")
    s1.reason
  }
}
