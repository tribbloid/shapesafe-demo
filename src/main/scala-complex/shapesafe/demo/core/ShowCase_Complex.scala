package shapesafe.demo.core

import shapesafe.core.Ops
import shapesafe.core.shape.{Names, Shape}

// TODO: compilation for such complex proof is seriously slow
//  can scala compiler uses GPU?
object ShowCase_Complex {

  object Composition {

    import ShowCase.ShapeArithmetics.conv2D

    def c3p1(
        in: Shape,
        kernel: Shape,
        padding: Shape,
        stride: Shape,
        pooling: Shape
    ) = {

      val ij = Names("i", "j")

      def conv(in: Shape) = {
        conv2D(in, kernel, padding, stride)
      }

      val c1 = conv(in)
      val c2 = conv(c1)
      val c3 = conv(c2)

      val _pooling = pooling >< pooling
      val p1 = Ops.:/.applyByDim(c3.namedBy(ij), _pooling)

      p1
    }

    val s1 = Shape(1024, 1024)
    val kernel = Shape(3, 3)

    val c2 = c3p1(s1, kernel, 0, 1, 2)
    val s2 = c2.reason

    s2.named("i", "i").einSum.-->*("i").reason
  }
}
