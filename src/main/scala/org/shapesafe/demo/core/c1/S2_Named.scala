package org.shapesafe.demo.core.c1

import org.shapesafe.core.shape.Shape

object S2_Named extends App {

  val s1 = Shape(100, 200, 3).named("i", "j", "k")

  val x1 = s1.Sub("j")
  val x2 = s1.Sub(1)

  (x1 elementWise Shape(200)).reason
  (x2 elementWise Shape(200)).reason

  val x3 = s1.Sub("k")
  (x3 elementWise Shape(3)).reason

  val m1 = s1.transpose("i", "j")
  val m2 = s1.transpose("j", "k")

  m1.matMul(m2).reason
}
