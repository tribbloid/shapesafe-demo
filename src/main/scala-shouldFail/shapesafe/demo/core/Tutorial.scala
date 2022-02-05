package shapesafe.demo.core

import shapesafe.core.arity.Arity
import shapesafe.core.shape.{Shape, Names, Indices, Index}
import shapesafe.core.Ops

object Tutorial {

  /*
  Enter the world of shapesafe.
  This is a casually written tutorial intended for doers.

  For more general information, please refer to our github page:

  https://github.com/tribbloid/shapesafe

  If you do not understand any part of this, please submit a pull request to this project
  ... with comments on the part, this will help us to explain it better
   */

  // let's do it by the numbers:
  // Arity is the smallest atomic building block
  // each representing a 1D vector type
  // the most common Arity type is backed by an integer number
  val v0 = Arity(0)
  val v1 = Arity(1)
  val v2 = Arity(2)
  val v3 = Arity(3)
  val v4 = Arity(4)

  // "Unchecked" is a special Arity type that enables gradual typing
  val vU = Arity.Unchecked

  object CompileTimeReasoning {

    // a counterintuitive distinction of shapesafe is the lazy verification paradigm
    // arities or shapes are, by default, typed by their (unvalidated) expressions,
    // calling "peek" on each can displayed the expression in compile-time
    // (remember to enable INFO log level)
    val expr = (v1 :+ v2) :* v3
    expr.peek

    // it is perfectly viable to define illegal expressions
    val illegal = (v1 :/ v0)
    val illegal2 = (v1 ==! v0)

    // ... and peek them as-is
    illegal.peek
    illegal2.peek

    // calling "eval" on it triggers its actual verification, simplifying it into its minimal form
    val result = expr.eval
    result.peek

    illegal.eval

    // one advantage is that it is possible to define a "wildcard" verifier for functions or classes
    // that applies to all kinds of inputs
    def isEven(arity: Arity) = {
      val result = (arity :/ v2 :* v2) ==! arity

      // remember that you can't use "peek" or "eval" here, the exact input type is still unknown
      result.eval

      result
    }
    isEven(Arity(4)).eval
    isEven(Arity(3)).eval

    // finally, calling "reason" fires "peek" and "eval" simultaneously
    val result2 = expr.reason
    result2.peek
  }

  object ArityOperations {

    object Arithmetics {

      // basic integer arithmetics can be applied to Arities of integer type
      // namely, +, -, * and /
      (v1 :+ v2).reason

      val vx = v1 :+ v2 :- v3
      vx.reason

      // divide-by-zero operation will cause the verification to fail
      (v1 :/ v0).reason
      (v1 :/ vx).reason

      // "Unchecked" Arity can cause any expression containing it to also degrade to "Unchecked"
      (v1 :+ vU).reason
    }

    object Equalities {

      // equality operations are similar to arithmetic operations, but the outcome is only true or false
      // if the outcome is false, the verification will fail
      (v1 ==! v2).reason
      (v1 :+ v2 ==! v3).reason
      (v1 ==! v2 :+ v3).reason

      // the evaluated value of each is always the TIGHTEST TYPE CONSTRAINT of the FIRST operand
      // thus, the "Unchecked" Arity type may be discarded for being too loose
      (v1 ==! vU).reason
      (vU ==! v1).reason

      // this also applies to inequalities
      (v1 =/=! v2 :+ v3).reason

      (v1 <=! v2 :+ v3).reason
      (v1 >=! v2 :+ v3).reason
    }
  }

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
          Indices >< Index.Left(1) >< Index.Left(0)
        )
        .reason

      // ... index cannot go out of bound tho
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
