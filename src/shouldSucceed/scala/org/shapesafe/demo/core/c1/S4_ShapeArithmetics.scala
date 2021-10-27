package org.shapesafe.demo.core.c1

import org.shapesafe.core.arity.ops.ArityOps
import org.shapesafe.core.shape.{Names, Shape, ShapeAPI}

object S4_ShapeArithmetics extends App {

  def conv2D(
      in: ShapeAPI,
      kernel: ShapeAPI,
      padding: ShapeAPI,
      stride: ShapeAPI
  ) = {

    val ij = Names("i", "j")

    val _padding = (padding >< padding).namedWith(ij)
    val _stride = (stride >< stride).namedWith(ij)

    val result = in
      .namedWith(ij)
      .flattenWith(ArityOps.:-, kernel.namedWith(ij))
      .flattenWith(ArityOps.:+, _padding)
      .flattenWith(ArityOps.:/, _stride)

    result
  }

  val s1 = Shape(1024, 768)
  val kernel = Shape(3, 3)

  val s2 = conv2D(s1, kernel, 1, 2)

  val s2E = s2.reason

  s2E.requireEqual(Shape(511, 383)).reason
}
