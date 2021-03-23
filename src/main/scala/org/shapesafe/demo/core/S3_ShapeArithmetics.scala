package org.shapesafe.demo.core

import org.shapesafe.core.arity.ArityAPI
import org.shapesafe.core.arity.ops.ArityOps
import org.shapesafe.core.shape.{Names, Shape}

object S3_ShapeArithmetics extends App {

  val ij = Names("i", "j")

  def conv2D[
      S1 <: Shape,
      S2 <: Shape
  ](
      signal: S1,
      kernel: S2,
      padding: ArityAPI,
      stride: ArityAPI
  ) = {

    val _padding = (Shape >|< padding >|< padding).withNames(ij)
    val _stride = (Shape >|< stride >|< stride).withNames(ij)

    signal
      .withNames(ij)
      .flattenWith(ArityOps.:-, kernel.withNames(ij))
      .flattenWith(ArityOps.:+, _padding)
      .flattenWith(ArityOps.:/, _stride)
  }

  val s1 = Shape(1024, 1024)
  val kernel = Shape(3, 3)

//  val s2 = conv2D(s1, kernel, Arity.fromSInt(1), Arity.fromSInt(2))
  val s2 = conv2D(s1, kernel, 1, 2)
  println(s2)

  val s2E = s2.eval
  println(s2E)
}
