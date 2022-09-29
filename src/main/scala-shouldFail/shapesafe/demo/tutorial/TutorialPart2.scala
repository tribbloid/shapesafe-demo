package shapesafe.demo.tutorial

import shapesafe.core.arity.Arity
import shapesafe.core.shape.{Index, Indices, Names, Shape}
import shapesafe.core.Ops

object TutorialPart2 {

  object ShapeOperations {

    // Shape is the slightly larger atomic building block
    // each representing a multidimensional tensor type
    val v3 = Shape(4)
    val v4 = Shape(3)
    val v34 = Shape(3, 4)
    val v345 = Shape(3, 4, 5)

    object Tensor {

      // the elementary shape type is expressed as the Cartesian product of 1 or multiple arities
      v3.reason
      v34.reason
      v345.reason

      // the default constructor is actually a shortcut, alternatively:
      (Shape & Arity(3) & Arity(4)).reason
      (Shape & 3 & 4).reason
    }

    object NamedTensor {

      // First proposed in http://nlp.seas.harvard.edu/NamedTensor
      // users can optionally assign a name to each dimension
      // either when constructing a new Shape
      (Shape &
        (Arity(3) :<<- "latitude") &
        (Arity(4) :<<- "longitude")).reason

      // or by batch assigning Names to shape of a nameless tensor
      val names = Names & "latitude" & "longitude"
      (v34 :<<= names).reason

      // short syntax for names construction
      val namedV34 = (v34 :<<= Names("lat", "lng")).reason

      // ... and assignment
      (v34 :<<=* ("lat", "lng")).reason

      // misaliged dimension will be refuted
      (v345 :<<=* ("lat", "lng")).reason

      // name(s) can be removed by assigning an empty string
      namedV34.:<<=*("", "").reason
      namedV34.:<<=*("lat", "").reason
    }

    object Rearrange {

      // by indices
      v34
        .rearrangeBy(
          Indices >< Index.LtoR(1) >< Index.LtoR(0)
        )
        .reason

      // ... index cannot go out of bound tho
      v34
        .rearrangeBy(
          Indices >< Index.LtoR(2) >< Index.LtoR(1)
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

      // "><" is the basic operator that concatenate all dimensions together
      (v3 >< v4).reason

      // ... while preserving their names
      val x1 = v34.:<<=*("x", "y") >< v345.:<<=*("i", "j", "k")
      x1.reason

      // this can go arbitrarily long
      (x1 >< x1).reason
    }

    object ApplyByDimension {

      // any binary operator for Arity can be broadcasted on 2 tensors of equal number of dimensions
      // enables Kronecker's product
      Ops.:*.applyByDim(v34, v34.transpose).reason

      // ... and direct sum
      Ops.:+.applyByDim(v34.transpose, v34).reason

      // application on 2 tensors of different number of dimensions will be refuted
      Ops.:+.applyByDim(v34.transpose, v345).reason

      // if you want to relax this condition, use:
      Ops.:+.applyByDimDropLeft(v34.transpose, v345).reason
    }

    object ReduceByName {

      // any binary operator for Arity can also be used to fold dimensions with the same name into 1
      Ops.:*.reduceByName(Shape(3, 4, 5, 6) :<<=* ("x", "x", "y", "y")).reason

      // applicable to multiple operands, the following 2 expressions are identical:
      Ops.:*.reduceByName(
        Shape(3, 4) :<<=* ("x", "y"),
        Shape(5, 6) :<<=* ("x", "y")
      ).reason
      Ops.:*.reduceByName(
        (Shape(3, 4) :<<=* ("x", "y")) >< (Shape(5, 6) :<<=* ("x", "y"))
      ).reason

      // EinSum is a shortcut of [distinct name check] ∘ [reduce by name with equality check]
      Shape(3, 4)
        .:<<=*("i", "j")
        .einSum(
          Shape(4, 5).:<<=*("j", "k")
        )
        .-->*("i", "k")
        .reason

      // ... of which both dot and matmul are a shortcuts
      Shape(3).dot(Shape(3)).reason
      Shape(3).dot(Shape(4)).reason
      Shape(3, 4).matMul(Shape(4, 5)).reason
      Shape(3, 4).matMul(Shape(5, 4)).reason
    }
  }
}
