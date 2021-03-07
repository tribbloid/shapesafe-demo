package org.shapesafe.demo.breeze

import org.shapesafe.core.arity.ops.ArityOps
import org.shapesafe.core.shape.Shape

object A extends App {

  val s1 = Shape(3).named("i")
  val s2 = s1 >< s1
  val s3 = s2 >< s2
  val s4 = s3 >< s3

  print(s4)
  print(s4.eval)

  val flat = s4.flatten(ArityOps.:*)
  print(flat.eval)

  flat
    .dot(
      Shape(6561)
    )
    .eval
}
