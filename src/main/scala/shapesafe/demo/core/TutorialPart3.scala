package shapesafe.demo.core

import shapesafe.core.Ops
import shapesafe.core.shape.Index.LtoR
import shapesafe.core.shape.{Indices, LeafShape, Shape}

object TutorialPart3 {

  // In this tutorial we will write a verifier for the Tensor typing example in:
  // https://ai.facebook.com/blog/paving-the-way-for-software-20-with-kotlin/
  // A 9-layer feedforward neural network
  object TensorTyping {

    import shapesafe.core.shape.ProveShape.AsLeafShape._

    trait Tensor {

      val out: Shape
      type Out >: out.type <: Shape

      def _out: Out = out

      def into[O <: LeafShape](layer: Layer): layer.ApplyT[Out] = {
        val neo: layer.ApplyT[Out] = layer.apply(_out)

        neo
      }

      def into_![O <: LeafShape](layer: Layer)(
          implicit
          //          to: layer.ShapeOut[S]#ReasonTo[O] // TODO: why this break?
          evalTo: layer.ApplyShape[Out]#_ShapeType |-@- O
      ): EagerTensor[Shape.^[O]] = {
        val neo: layer.ApplyShape[Out] = layer.apply(_out)._out
        val proven: O = evalTo(neo.shapeType)

        EagerTensor(proven.^)
      }
    }

    case class EagerTensor[S <: Shape](override val out: S) extends Tensor {

      override type Out = S
    }

    trait Layer {

      trait LazyTensor extends Tensor {
        override type Out = out.type
      }

      type ApplyT[I <: Shape] <: LazyTensor
      def apply[I <: Shape](input: I): ApplyT[I]

      type ApplyShape[I <: Shape] = ApplyT[I]#Out
    }

    case class Conv[OC <: LeafShape.Vector_](
        oChannel: OC
    ) extends Layer {

      case class ApplyT[I <: Shape](input: I) extends LazyTensor {

        val out = {

          val ij = input
            .rearrangeBy(Indices >< LtoR(0) >< LtoR(1))
          ij >< oChannel
        }
      }

      override def apply[I <: Shape](input: I): ApplyT[I] =
        ApplyT[I](input)
    }

    case class Pooling() extends Layer {

      case class ApplyT[I <: Shape](input: I) extends LazyTensor {

        val out = {

          val right = Shape(2, 2, 1)
          Ops.:/.applyByDim(input, right)
        }
      }

      override def apply[I <: Shape](input: I): ApplyT[I] =
        ApplyT[I](input)
    }

    case class Flatten() extends Layer {

      case class ApplyT[I <: Shape](input: I) extends LazyTensor {

        val out = {

          Ops.:*.reduceByName(input.:<<=*("i", "i", "i"))
        }
      }

      override def apply[I <: Shape](input: I): ApplyT[I] =
        ApplyT[I](input)
    }

    case class

    {
      val data = EagerTensor(Shape(28, 28, 1))
      data.out.reason

      val conv1 = Conv(Shape(6))
      val maxPool1 = Pooling()

      val conv2 = Conv(Shape(16))
      val maxPool2 = Pooling()

      val flatten = Flatten()

      val ou0 = conv1.apply(data.out)
      ou0._out.peek

      val ou1 = data into conv1

      ou1._out.peek

      val output = data into_!
        conv1 into_!
        maxPool1 into_!
        conv2 into_!
        maxPool2 into_!
        flatten

    }
  }
}
