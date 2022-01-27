package shapesafe.demo.core

import shapesafe.core.Ops
import shapesafe.core.shape.{Names, ShapeType, Shape}

object ShowCase_Error {

  object Tensor {

    import ShowCase.Tensor._

    flatten.requireEqual(Shape(256)).reason
  }

  object NamedTensor {

    import ShowCase.NamedTensor._

    (x1 requireEqual Shape(100)).reason
    (x2 requireEqual Shape(100)).reason

    (m1 requireEqual Shape(100, 3)).reason
    m2.matMul(m1).reason
  }

  object EinSum {

    import ShowCase.EinSum._

    {
      val m = s1
        .named("i", "j", "channel")
        .einSum(
          s2.named("j", "k", "channel")
        ) -->* ("i", "l", "channel")

      m.reason
    }

    {
      val m = s1
        .named("i", "j", "k")
        .einSum(
          s2.named("j", "k", "channel")
        ) -->* ("i", "k", "channel")

      m.reason
    }
  }

  object KroneckerProduct {

    import ShowCase.KroneckerProduct._

    val einsum = {
      Ops.EinSum(
        kProd :<<=* ("t", "x"),
        downsample :<<=* ("x", "y")
      ) -->* ("t", "y")
    }

    einsum.reason
  }

  object ShapeArithmetics {
    import ShowCase.ShapeArithmetics._

    val e2 = conv2D(s1, kernel, 1, 0)

    val e2E = e2.reason

    s2.requireEqual(Shape(511, 511)).reason
  }
}

object ShowCase_ComplexError {

  object Composition {
    import ShowCase_Complex.Composition._

    val s1 = Shape(1024, 768)

    val s2 = c3p1(s1, kernel, 0, 1, 2).reason

    s2.named("i", "i").einSum.-->*("i").reason
  }
}
