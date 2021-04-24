package org.shapesafe.demo.core.c1.failed

import shapeless.test.illTyped

object S1_ND extends App {

  illTyped(
    """
import org.shapesafe.core.arity.ops.ArityOps
import org.shapesafe.core.shape.Shape

val s1 = Shape(3).named("i")
val s2 = s1 >< s1
val s3 = s2 >< s2
val s4 = s3 >< s3

println(s4)

val s4E = s4.eval
println(s4E)

val flat = s4.flatten(ArityOps.:*)
val flatE = flat.eval
println(flatE)

flat.elementWise(Shape(256)).eval
""",
    ".*"
  )
}