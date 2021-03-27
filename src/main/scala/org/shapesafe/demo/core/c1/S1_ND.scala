package org.shapesafe.demo.core.c1

import org.shapesafe.core.arity.ops.ArityOps
import org.shapesafe.core.shape.Shape

object S1_ND extends App {

//  val s1 = Shape(3).named("i")
  val s1 = Shape(2).named("i")
  val s2 = s1 >< s1
  val s3 = s2 >< s2
  val s4 = s3 >< s3

  println(s4)

  val s4E = s4.eval
  println(s4E)

  val flat = s4.flatten(ArityOps.:*)
  val flatE = flat.eval
  println(flatE)

  val w = flat dot Shape(256) // shortcut for ("i") einSum ("i") ->* ("i")
  val wE = w.eval
  println(wE)
}
