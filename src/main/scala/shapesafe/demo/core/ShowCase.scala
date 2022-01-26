package shapesafe.demo.core

import shapesafe.core.Ops
import shapesafe.core.shape.{Shape, ShapeAPI}

object ShowCase {

  object Tensor {

    val s1 = Shape(3).named("i")
    s1.reason

    val s2 = s1 >< s1
    val s3 = s2 >< s2
    val s4 = s3 >< s3

    val s4E = s4.reason
    val flatten = s4E.reduceByName(Ops.:*).reason

    flatten.requireEqual(Shape(6561)).reason
  }

  object NamedTensor {

    val channels = Shape(100, 200, 3).named("i", "j", "k")

    val x1 = channels.select1("j")
    val x2 = channels.select1(1)

    (x1 requireEqual Shape(200)).reason
    (x2 requireEqual Shape(200)).reason

    val m1 = channels.select("i", "j")
    val m2 = channels.select("j", "k")

    (m1 requireEqual Shape(100, 200)).reason
    m1.matMul(m2).reason
  }

  object EinSum {

    val s1 = Shape(999999, 8, 3)

    val s2 = Shape(8, 361, 3)

    val m = s1
      .named("i", "j", "channel")
      .einSum(
        s2.named("j", "k", "channel")
      ) -->* ("i", "k", "channel")

    m.reason
  }

  object ShapeArithmetics {

    def conv2D(
        in: ShapeAPI,
        kernel: ShapeAPI,
        padding: ShapeAPI,
        stride: ShapeAPI
    ) = {

      val _padding = padding >< padding
      val _stride = stride >< stride

      val result = in
        .applyPerDim(Ops.:-, kernel)
        .applyPerDim(Ops.:+, _padding)
        .applyPerDim(Ops.:/, _stride)

      result
    }

    val s1 = Shape(1024, 768)
    val kernel = Shape(3, 3)

    val s2 = conv2D(s1, kernel, 1, 2)

    val s2E = s2.reason

    s2E.requireEqual(Shape(511, 383)).reason
  }

}
