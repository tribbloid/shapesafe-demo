package org.shapesafe.demo.core.c1

import org.shapesafe.core.arity.ops.ArityOps
import org.shapesafe.core.shape.Shape

object S1_ND extends App {

  // val s1 = Shape(3).named("i")
  val s1 = Shape(2).named("i")
//  s1.peek

  val s2 = s1 >< s1
  val s3 = s2 >< s2
  val s4 = s3 >< s3
//  s4.peek

  val s4E = s4.eval
//  s4E.peek

  val flat = s4E.flatten(ArityOps.:*).eval.peek

  flat.elementWise(Shape(256)).eval
}
