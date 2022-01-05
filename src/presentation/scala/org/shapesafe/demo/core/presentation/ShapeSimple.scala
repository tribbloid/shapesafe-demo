package org.shapesafe.demo.core.presentation

import org.shapesafe.core.shape.Shape

object ShapeSimple extends App {

  val v1 = Shape(4)
  val v2 = Shape(3)

  // v1.dot(v1).reason
  // v1.dot(v2).reason
  // v2.cross(v2).reason
  // v1.cross(v1).reason

  val m1 = Shape(3, 4)
  val m2 = Shape(4, 5)

  // m1.matMul(m2).reason
  // m2.matMul(m1).eval
}
