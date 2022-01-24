package shapesafe.demo.core

import shapesafe.core.arity.Arity
import shapesafe.core.arity.ops.ArityOps
import shapesafe.core.shape.{Names, Shape, ShapeAPI}

object ArityOperators {

  {
    val v1 = Arity(1)
    val v2 = Arity(2)
    val v3 = Arity(1)

    (v1 ==! v2).reason
    (v1 ==! v3).reason
  }

  {
    val v1 = Arity(1)
    val v2 = Arity(2)
    val v3 = Arity(3)

    (v1 ==! v2 :+ v3).reason
    (v1 :+ v2 ==! v3).reason
  }
}
