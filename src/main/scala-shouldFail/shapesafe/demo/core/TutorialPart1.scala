package shapesafe.demo.core

import shapesafe.core.arity.Arity
import shapesafe.core.shape.{Shape, Names, Indices, Index}
import shapesafe.core.Ops

object TutorialPart1 {

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
}
