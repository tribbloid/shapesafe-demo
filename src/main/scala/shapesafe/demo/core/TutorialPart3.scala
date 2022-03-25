package shapesafe.demo.core

import shapesafe.core.Ops
import shapesafe.core.shape.Index.LtoR
import shapesafe.core.shape.{Indices, LeafShape, Shape}

object TutorialPart3 {

  /*
  In this tutorial we will write a verifier for the tensor typing example in:

  https://ai.facebook.com/blog/paving-the-way-for-software-20-with-kotlin/

  A 9-layer feedforward neural network
   */
  object TensorTyping {

    import shapesafe.core.shape.ProveShape.AsLeafShape._

    trait Tensor {

      val shape: Shape
      type _Shape >: shape.type <: Shape

      def _shape: _Shape = shape

      def into(layer: Module): layer.ApplyT[_Shape] = {
        val result: layer.ApplyT[_Shape] = layer(this)

        result
      }

      def into_![O <: LeafShape](layer: Module)(
          implicit
//          to: layer.ApplyShape[_Shape]#ReasonTo[O] // FIXME: a compiler error in Scala 2.13.8 caused this alternative to break
          evalTo: layer.ApplyShape[_Shape]#_ShapeType |-@- O
      ): Input[Shape.^[O]] = {
        val unevaluated = into(layer)
        val proven: O = evalTo(unevaluated._shape.shapeType)

        Input(proven.^)
      }
    }

    case class Input[S <: Shape.Leaf_](override val shape: S) extends Tensor {

      override type _Shape = S
    }

    trait LazyTensor extends Tensor {
      override type _Shape = shape.type
    }

    trait Module {

      type ApplyT[I <: Shape] <: LazyTensor
      def apply(input: Tensor): ApplyT[input._Shape]

      type ApplyShape[I <: Shape] = ApplyT[I]#_Shape
    }

    case class Conv2D[OC <: Shape.Leaf.Vector_](
        nChannel: OC
    ) extends Module {

      case class ApplyT[I <: Shape](input: I) extends LazyTensor {

        val shape = {

          val ij = input
            .rearrangeBy(Indices >< LtoR(0) >< LtoR(1))
          ij >< nChannel
        }
      }

      override def apply(input: Tensor): ApplyT[input._Shape] =
        ApplyT(input._shape)
    }

    case class Pooling2D() extends Module {

      case class ApplyT[I <: Shape](input: I) extends LazyTensor {

        val shape = {

          val right = Shape(2, 2, 1)
          Ops.:/.applyByDim(input, right)
        }
      }

      override def apply(input: Tensor): ApplyT[input._Shape] =
        ApplyT(input._shape)
    }

    case class Flatten() extends Module {

      case class ApplyT[I <: Shape](input: I) extends LazyTensor {

        val shape = {

          Ops.:*.reduceByName(input.:<<=*("i", "i", "i"))
        }
      }

      override def apply(input: Tensor): ApplyT[input._Shape] =
        ApplyT(input._shape)
    }

    case class FullyConnected[IS <: Shape, OS <: Shape](
        in: IS,
        out: OS
    ) extends Module {

      case class ApplyT[I <: Shape](input: I) extends LazyTensor {

        val shape = {

          val empty = Ops.==!.applyByDim(input, in).select0

          val result = empty >< out
          result
        }
      }

      override def apply(input: Tensor): ApplyT[input._Shape] =
        ApplyT(input._shape)
    }

    {
      val data = Input(Shape(28, 28, 1))
      data.shape.reason

      val conv1 = Conv2D(Shape(6))
      val maxPool1 = Pooling2D()

      val conv2 = Conv2D(Shape(16))
      val maxPool2 = Pooling2D()

      val flatten = Flatten()

      val fc1 = FullyConnected(Shape(784), Shape(120))
      val fc2 = FullyConnected(Shape(120), Shape(84))
      val fc3 = FullyConnected(Shape(84), Shape(10))

      val output = data into_!
        conv1 into_!
        maxPool1 into_!
        conv2 into_!
        maxPool2 into_!
        flatten into_!
        fc1 into_!
        fc2 into_!
        fc3

      output.shape.peek
    }
  }
}
