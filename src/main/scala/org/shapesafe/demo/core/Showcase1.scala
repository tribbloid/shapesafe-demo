package org.shapesafe.demo.core

import org.shapesafe.core.arity.ops.ArityOps
import org.shapesafe.core.shape.Shape

object Showcase1 extends App {

  val s1 = Shape(3).named("i")
  val s2 = s1 >< s1
  val s3 = s2 >< s2
  val s4 = s3 >< s3

  print(s4)

  val e4 = s4.eval
  print(e4)

  val flat = s4.flatten(ArityOps.:*)
  val eFlat = flat.eval
  print(eFlat)

  flat
    .dot(
      Shape(6561)
    )
    .eval
}
