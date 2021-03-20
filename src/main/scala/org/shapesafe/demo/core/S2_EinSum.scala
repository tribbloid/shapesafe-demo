package org.shapesafe.demo.core

import org.shapesafe.core.shape.Shape

object S2_EinSum extends App {

  val s1 = Shape(999999, 8, 3)

  val s2 = Shape(8, 361, 3)

  val m = s1
    .named("i", "j", "channel")
    .einSum(
      s2.named("j", "k", "channel")
    ) -->* ("i", "k", "channel")
  println(m)

  val mE = m.eval
  println(mE)
}
