package shapesafe.demo.core.c1

import shapesafe.core.arity.ops.ArityOps
import shapesafe.core.shape.Shape

object S1_ND extends App {

  val s1 = Shape(3).named("i")
  s1.reason

  val s2 = s1 >< s1
  val s3 = s2 >< s2
  val s4 = s3 >< s3

  val s4E = s4.reason
  val flatten = s4E.flatten(ArityOps.:*).reason

  flatten.requireEqual(Shape(256)).reason
}
