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

      val shape: Shape
      type _Shape >: shape.type <: Shape

      def _shape: _Shape = shape

      def into[O <: LeafShape](layer: Layer): layer.ApplyT[_Shape] = {
        val neo: layer.ApplyT[_Shape] = layer.apply[this.type](this)

        neo
      }

      def into_![O <: LeafShape](layer: Layer)(
          implicit
          //          to: layer.ShapeOut[S]#ReasonTo[O] // TODO: why this break?
          evalTo: layer.ApplyShape[_Shape]#_ShapeType |-@- O
      ): EagerTensor[Shape.^[O]] = {
        val neo: layer.ApplyShape[_Shape] = layer.apply[this.type](this)._shape
        val proven: O = evalTo(neo.shapeType)

        EagerTensor(proven.^)
      }
    }

    case class EagerTensor[S <: Shape](override val shape: S) extends Tensor {

      override type _Shape = S
    }

    trait LazyTensor extends Tensor {
      override type _Shape = shape.type
    }

    trait Layer {

      type ApplyT[I <: Shape] <: LazyTensor
      def apply[T <: Tensor](input: T): ApplyT[T#_Shape]

      type ApplyShape[I <: Shape] = ApplyT[I]#_Shape
    }

    case class Conv[OC <: LeafShape.Vector_](
        oChannel: OC
    ) extends Layer {

      case class ApplyT[I <: Shape](input: I) extends LazyTensor {

        val shape = {

          val ij = input
            .rearrangeBy(Indices >< LtoR(0) >< LtoR(1))
          ij >< oChannel
        }
      }

      override def apply[T <: Tensor](input: T): ApplyT[T#_Shape] =
        ApplyT(input._shape)
    }

    case class Pooling() extends Layer {

      case class ApplyT[I <: Shape](input: I) extends LazyTensor {

        val shape = {

          val right = Shape(2, 2, 1)
          Ops.:/.applyByDim(input, right)
        }
      }

      override def apply[T <: Tensor](input: T): ApplyT[T#_Shape] =
        ApplyT(input._shape)
    }

    case class Flatten() extends Layer {

      case class ApplyT[I <: Shape](input: I) extends LazyTensor {

        val shape = {

          Ops.:*.reduceByName(input.:<<=*("i", "i", "i"))
        }
      }

      override def apply[T <: Tensor](input: T): ApplyT[T#_Shape] =
        ApplyT(input._shape)
    }

    case class FullyConnected[IS <: Shape, OS <: Shape](
        in: IS,
        out: OS
    ) extends Layer {

      case class ApplyT[I <: Shape](input: I) extends LazyTensor {

        val shape = {

          val empty = Ops.==!.applyByDim(input, in)
            .rearrangeBy(Indices.Eye)

          empty >< shape
        }
      }

      override def apply[T <: Tensor](input: T): ApplyT[T#_Shape] =
        ApplyT(input._shape)
    }

    {
      val data = EagerTensor(Shape(28, 28, 1))
      data.shape.reason

      val conv1 = Conv(Shape(6))
      val maxPool1 = Pooling()

      val conv2 = Conv(Shape(16))
      val maxPool2 = Pooling()

      val flatten = Flatten()

      val fc1 = FullyConnected(Shape(784), Shape(16))

      val ou0 = conv1.apply(data)
      ou0._shape.peek

      val ou1 = data into conv1

      ou1._shape.peek

      val output = data into_!
        conv1 into_!
        maxPool1 into_!
        conv2 into_!
        maxPool2 into_!
        flatten
//      into_!
//        fc1

    }
  }
}
