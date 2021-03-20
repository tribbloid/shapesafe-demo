package org.shapesafe.demo.core

import org.shapesafe.core.arity.ops.ArityOps
import org.shapesafe.core.shape.{Names, Shape, ShapeAPI}

object S3_ShapeArithmetics extends App {

  val ij = Names("i", "j")

  def conv2D(
      signal: ShapeAPI,
      kernel: ShapeAPI,
      padding: ShapeAPI,
      stride: ShapeAPI
  ) = {

//    TypeVizCT[padding._Shape].show

    val _padding = (padding >< padding).withNames(ij)
    val _stride = (stride >< stride).withNames(ij)

    val result = signal
      .withNames(ij)
      .flattenWith(ArityOps.:-, kernel.withNames(ij))
      .flattenWith(ArityOps.:+, _padding)
      .flattenWith(ArityOps.:/, _stride)

    result
  }

  val s1 = Shape(1024, 1024)
  val kernel = Shape(3, 3)

  val s2 = conv2D(s1, kernel, 1, 2)
  println(s2)

//  TypeVizCT.infer(s2).show

  val s2E = s2.eval
  println(s2E)
}
