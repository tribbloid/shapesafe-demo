package org.shapesafe.demo.core.c1

import org.shapesafe.core.shape.Shape

object S2_Named extends App {

  val s1 = Shape(100, 200, 3).named("i", "j", "k")
  // val s1 = Shape(100, 200, 3).named("i", "j")

  val x1 = s1.Sub("j")
  val x2 = s1.Sub(1)
  val y = Shape(200)

  println((x1 dot y).eval)
  println((x2 dot y).eval)
}
