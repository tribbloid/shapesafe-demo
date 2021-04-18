package org.shapesafe.demo.core.c1

import org.shapesafe.core.shape.Shape

object S2_Named extends App {

  val s1 = Shape(100, 200, 3).named("i", "j", "k")
  // val s1 = Shape(100, 200, 3).named("i", "j")

  val x1 = s1.Sub("j")
  val x2 = s1.Sub(1)
  val x3 = s1.Sub("k")
  val y = Shape(200)

  // (x1 dot y).peek
  // (x2 dot y).peek
  // (x3 dot y).peek
}
