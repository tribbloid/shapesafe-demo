package shapesafe.demo.core

import shapesafe.core.arity.Arity
import shapesafe.core.shape.{Shape, Names, Indices, Index}
import shapesafe.core.Ops

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
    val v345 = Shape(3, 4, 5)

    object Tensor {

      v3.reason

      v34.reason

      (Shape & Arity(3) & Arity(4)).reason

      (Shape & 3 & 4).reason
    }

    object NamedTensor {

      (Shape &
        (Arity(3) :<<- "latitude") &
        (Arity(4) :<<- "longitude")).reason

      val names = Names & "latitude" & "longitude"
      (v34 :<<= names).reason

      // short syntax for names construction
      val namedV34 = (v34 :<<= Names("latitude", "longitude")).reason

      // ... and assignment
      (v34 :<<=* ("latitude", "longitude")).reason

      // misaliged dimension will be refuted
      (v345 :<<=* ("latitude", "longitude")).reason

      // name(s) can be removed by assigning an empty string
      namedV34.:<<=*("", "").reason
    }

    object Rearrange {

      // by indices
      v34
        .rearrangeBy(
          Indices >< Index.Left(1) >< Index.Left(0)
        )
        .reason

      // ... cannot go out of index bound tho
      v34
        .rearrangeBy(
          Indices >< Index.Left(2) >< Index.Left(1)
        )
        .reason

      // by names

      (v34 :<<=* ("x", "y"))
        .rearrangeBy(Names & "y" & "x")
        .reason

      (v34 :<<=* ("x", "y"))
        .rearrange("y", "x")
        .reason

      // ... similarly, cannot use non-existing name(s)
      (v34 :<<=* ("x", "y"))
        .rearrange("y", "z")
        .reason

      // transpose is a shortcut for selecting dimensions backwards from a matrix
      v34.transpose.reason

      // ... it doesn't work on higher dimension tensors
      v345.transpose.reason
    }

    object OuterProduct {

      (v3 >< v4).reason

      val x1 = (v3.:<<=*("x") >< v4.:<<=*("y")).reason
      (x1 >< x1).reason
    }

    object ApplyByDimensions {

      // any binary operator on Arity can be broadcasted on 2 tensors of equal number of dimensions
      // enables Kronecker's product
      Ops.:*.applyByDim(v34, v34.transpose).reason

      // ... and direct sum
      Ops.:+.applyByDim(v34.transpose, v34).reason

      // application on 2 tensors of different number of dimensions will be refuted
      Ops.:+.applyByDim(v34.transpose, v345).reason
    }

    object ReduceByName {}

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
