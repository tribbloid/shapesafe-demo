package shapesafe.demo.core

import shapesafe.core.Ops
import shapesafe.core.arity.Arity
import shapesafe.core.arity.ops.ArityOpsLike
import shapesafe.core.shape.Index.LtoR
import shapesafe.core.shape.ShapeReasoning.PeekShape
import shapesafe.core.shape.{Indices, LeafShape, Shape}

object TutorialPart3 {

  // In this tutorial we will write a verifier for the Tensor typing example in:
  // https://ai.facebook.com/blog/paving-the-way-for-software-20-with-kotlin/
  // A 9-layer feedforward neural network
  object TensorTyping {

    case class Tensor[S <: Shape](shape: S) {

      import shapesafe.core.shape.ProveShape.|-

      def into[LL <: Layer, O <: LeafShape](layer: LL)(
          implicit
          prove: layer.ShapeOut[S]#_ShapeType |- O,
          reporter: PeekShape.Case[layer.ShapeOut[S]#_ShapeType]
      ): Tensor[Shape.^[O]] = {
        val proven = prove(layer.shapeOut[S](this).shapeType)

        Tensor(Shape.^(proven))
      }
    }

    trait NewShape {

      val _shape: Shape

      final type Out = _shape.type
      def out: Out = _shape
    }

    trait Layer {

      type ShapeOp[I <: Shape] <: NewShape
      def shapeOp[I <: Shape](input: I): ShapeOp[I]

      final type ShapeOut[I <: Shape] = ShapeOp[I]#Out
      final def shapeOut[I <: Shape](tensor: Tensor[I]): ShapeOut[I] = shapeOp(
        tensor.shape
      ).out
    }

    case class Conv[OC <: LeafShape.Vector_](
        oChannel: OC
    ) extends Layer {

      case class ShapeOp[I <: Shape](input: I) extends NewShape {

        val _shape = {

          val ij = input
            .rearrangeBy(Indices >< LtoR(0) >< LtoR(1))
          ij >< oChannel
        }
      }

      override def shapeOp[I <: Shape](input: I): ShapeOp[I] =
        ShapeOp[I](input)
    }

    case class Pooling() extends Layer {

      case class ShapeOp[I <: Shape](input: I) extends NewShape {

        val _shape = {

          val right = Shape(2, 2, 1)
          Ops.:/.applyByDim(input, right)
        }
      }

      override def shapeOp[I <: Shape](input: I): ShapeOp[I] =
        ShapeOp[I](input)
    }

    case class Flatten() extends Layer {

      case class ShapeOp[I <: Shape](input: I) extends NewShape {

        val _shape = {

          Ops.:*.reduceByName(input.:<<=*("i", "i", "i"))
        }
      }

      override def shapeOp[I <: Shape](input: I): ShapeOp[I] =
        ShapeOp[I](input)
    }

    {
      val data = Tensor(Shape(28, 28, 1))
      data.shape.reason

      val conv1 = Conv(Shape(6))
      val maxPool1 = Pooling()

      val conv2 = Conv(Shape(16))
      val maxPool2 = Pooling()

      val flatten = Flatten()

      val output = data into
        conv1 into
        maxPool1 into
        conv2 into
        maxPool2 into
        flatten

      flatten.shapeOp(Shape(3, 4, 5)).out.reason
    }
  }
}
