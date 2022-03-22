package shapesafe.demo.core

import shapesafe.core.shape.Index.LtoR
import shapesafe.core.shape.ShapeReasoning.PeekShape
import shapesafe.core.shape.{Indices, LeafShape, Shape}

object TutorialPart3_Original {

  // In this tutorial we will write a verifier for the Tensor typing example in:
  // https://ai.facebook.com/blog/paving-the-way-for-software-20-with-kotlin/
  // A 9-layer feedforward neural network
  object TensorTyping {

    case class Tensor[S <: Shape](shape: S) {

      def into1[LL <: Layer](layer: LL): layer.TensorOut[S] =
        layer.transform[S](this)

      import shapesafe.core.shape.ProveShape.|-

      def into[LL <: Layer, O <: LeafShape](layer: LL)(implicit
          prove: layer.ShapeOut[S]#_ShapeType |- O,
          reporter: PeekShape.Case[O]
      ): Tensor[Shape.^[O]] = {
        val proven = prove(layer.shapeOut[S](this).shapeType)

        Tensor(Shape.^(proven))
      }

    }

    trait ShapeOpLike {

      val _shape: Shape

      final type Out = _shape.type
      def out: Out = _shape
    }

    trait Layer {

      type ShapeOp[I <: Shape] <: ShapeOpLike
      def shapeOp[I <: Shape](input: I): ShapeOp[I]

      final type ShapeOut[I <: Shape] = ShapeOp[I]#Out
      def shapeOut[I <: Shape](tensor: Tensor[I]): ShapeOut[I] = shapeOp(
        tensor.shape
      ).out

      final type TensorOut[I <: Shape] = Tensor[ShapeOp[I]#Out]

      def transform[I <: Shape](tensor: Tensor[I]): TensorOut[I] =
        Tensor(shapeOp(tensor.shape).out)
    }

    case class Conv[OC <: LeafShape.Vector_](
        oChannel: OC
    ) extends Layer {

      case class ShapeOp[I <: Shape](input: I) extends ShapeOpLike {

        val _shape = {

          val ij = input
            .rearrangeBy(Indices >< LtoR(0) >< LtoR(1))
          ij >< oChannel
        }
      }

      override def shapeOp[I <: Shape](input: I): ShapeOp[I] =
        ShapeOp[I](input)
    }

    {
      val data = Tensor(Shape(28, 28, 1))
      data.shape.reason

      val conv1 = Conv(Shape(6))

      val ss = conv1.shapeOp(data.shape)
      ss.out.reason

      val tt = conv1.transform(data)
      tt.shape.reason

      val output = data into
        conv1
      output.shape.reason
    }
  }
}
