package org.shapesafe.demo.core.c2

import org.shapesafe.core.arity.Arity

object AritySimple extends App {

  {
    val v1 = Arity(1)
    val v2 = Arity(2)

    (v1 ==! v1).reason
    // (v1 ==! v2).reason
  }

  {
    val v1 = Arity(1)
    val v2 = Arity(2)
    val v3 = Arity(3)

    (v1 ==! v2 :+ v3).reason
    (v1 :+ v2 ==! v3).reason
  }
}
