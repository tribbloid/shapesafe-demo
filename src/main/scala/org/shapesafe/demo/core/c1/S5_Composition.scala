package org.shapesafe.demo.core.c1

import org.shapesafe.core.arity.ops.ArityOps
import org.shapesafe.core.shape.{Names, Shape, ShapeAPI}

// TODO: compilation for such complex proof is seriously slow
//  can scala compiler uses GPU?
object S5_Composition extends App {

  import S4_ShapeArithmetics.conv2D

  lazy val ij = Names("i", "j")

  def c3p1(
      in: ShapeAPI,
      kernel: ShapeAPI,
      padding: ShapeAPI,
      stride: ShapeAPI,
      pooling: ShapeAPI
  ) = {

    def conv(in: ShapeAPI) = {
      conv2D(in, kernel, padding, stride)
    }

    val c1 = conv(in)
    val c2 = conv(c1)
    val c3 = conv(c2)

    val _pooling = (pooling >< pooling).withNames(ij)
    val p1 = c3.withNames(ij).flattenWith(ArityOps.:/, _pooling)

    p1
  }

  val s1 = Shape(1024, 1024)
  val kernel = Shape(3, 3)

  val s2 = c3p1(s1, kernel, 0, 1, 2)
  // s2.peek

  val s2E = s2.eval
  // s2E.peek

  s2E.elementWise(Shape(507, 507)).eval
}
