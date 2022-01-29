package shapesafe.demo.core

import shapesafe.core.arity.Arity
import shapesafe.core.shape.{Shape, Names}
import shapesafe.core.shape.Indices

object Tutorial {

  val v1 = Arity(1)
  val v2 = Arity(2)
  val v3 = Arity(3)
  val v4 = Arity(4)
  val vU = Arity.Unchecked
  val vI = Arity.Unprovable

  object PeekAndEval {

    val expr = (v1 :+ v2) :* v3
    val illegal = (v1 :+ vI)

    expr.peek

    val result = expr.eval
    result.peek

    illegal.eval

    val result2 = expr.reason
    result2.peek
  }

  object ArityOperations {

    object Arithmetics {

      (v1 :+ v2).reason

      val v0 = v1 :+ v2 :- v3
      v0.reason

      (v1 :/ v0).reason

      (v1 :+ vU).reason
    }

    object Equalities {

      (v1 ==! v2).reason
      (v1 :+ v2 ==! v3).reason

      (v1 ==! v2 :+ v3).reason
      (v1 =/=! v2 :+ v3).reason

      (v1 ==! vU).reason

      (vU ==! v2).reason
    }
  }

  object ShapeOperations {

    val v3 = Shape(4)
    val v4 = Shape(3)
    val v34 = Shape(3, 4)

    object Tensor {

      v3.reason

      v34.reason

      (Shape & Arity(3) & Arity(4)).reason

      (Shape & 3 & 4).reason
    }

    object NamedTensor {

      (v34 :<<=* ("latitude", "longitude")).reason
      (v34 :<<= Names("latitude", "longitude")).reason
      (v34 :<<=
        (Names & "latitude" & "longitude")).reason

      (Shape &
        (Arity(3) :<<- "latitude") &
        (Arity(4) :<<- "longitude")).reason
    }

    object Selection {

      // by indices
      v34.select(Indices >< Index.Left(0) >< Index.Left(1)).reason
    }

    object OuterProduct {}

    v1.dot(v1).reason
    v1.dot(v2).reason
    v2.cross(v2).reason
    v1.cross(v1).reason

    val m1 = Shape(3, 4)
    val m2 = Shape(4, 5)

    m2.matMul(m1).reason
    m1.matMul(m2).reason
    // m2.matMul(m1).eval
  }

}
