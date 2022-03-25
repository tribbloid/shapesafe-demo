package shapesafe.demo.core

import shapesafe.core.Ops
import shapesafe.core.shape.Index.LtoR
import shapesafe.core.shape.{Indices, LeafShape, Shape}

object TutorialPart3 {

  /*
  In this slightly more complex tutorial, we will write a static verifier for the tensor typing example in:

  https://ai.facebook.com/blog/paving-the-way-for-software-20-with-kotlin/

  A 9-layer feedforward neural network
   */
  object TensorTyping {

    import shapesafe.core.shape.ProveShape.AsLeafShape._

    // Assuming that we already have an API for untyped tensors
    trait Tensor {
      // (runtime API omitted here)
    }
    // ... and untyped, functional modules, each defining a computation unit, like convolution or pooling
    trait Module[ACCEPTING <: Tensor] {

      def applyUntyped(input: ACCEPTING): Tensor = {
        // (runtime operation omitted here)
        ???
      }
    }

    // This is where the action happens:
    // Adding type and static verification is as simple as adding a new path-dependent type, `_Shape`
    trait TypedTensor extends Tensor {

      // Scala has no support for fully dependent type.
      // To make use of it, we'll need to write implementations with more specific `_Shape` types
      val shape: Shape
      type _Shape >: shape.type <: Shape

      def _shape: _Shape = shape
    }

    // The API of `Module` therefore adapts accordingly:
    trait TypedModule extends Module[TypedTensor] {

      // `apply` function now becomes parametric, accompanied by a type constructor
      def apply(input: TypedTensor): ApplyT[input._Shape]
      // Alas, the following type constructor definition is completely redundant, and much longer than I prefer.
      // But scala 2.13 can't perform eta-expansion on polymorphic functions & case classes
      type ApplyT[I <: Shape] <: TypedTensor // TODO: this can be superseded by polymorphic eta-extension in Scala 3

      final type ApplyShape[I <: Shape] = ApplyT[I]#_Shape
    }

    // Almost there! As a syntactic backup for old school ML engineers, let's add 2 shortcuts for sequential NN definition:
    trait SequentialTensor extends TypedTensor {

      /*
      The "lazy into" is the simpler API yielding a tensor typed by a computation-graph.
      It requires no implicit argument and very little type-checking, thus can be easily defined anywhere.
      It is also unique to shapesafe and has no counterpart in other typed tensor libraries.

      If you want to evaluate and verify this computation immediately, keep reading.
       */
      def >>(layer: TypedModule): layer.ApplyT[_Shape] = {
        val result: layer.ApplyT[_Shape] = layer(this)

        result
      }

      /*
      The "eager into" is the more complex API yielding a tensor typed by a fully computed and verified shape

      You'll see the effect at the end of this tutorial
       */
      def >>![O <: LeafShape](layer: TypedModule)(
          implicit
          //          to: layer.ApplyShape[_Shape]#ReasonTo[O] // FIXME: a compiler error in Scala 2.13.8 caused this shortcut to break
          evalTo: layer.ApplyShape[_Shape]#_ShapeType |-@- O
      ): Input[Shape.^[O]] = {
        val unevaluated = >>(layer)
        val proven: O = evalTo(unevaluated._shape.shapeType)

        Input(proven.^)
      }
    }

    // Finally we are done with abstractions! Now we just need to finish implementing all types of `SequentialTensor`
    case class Input[S <: Shape.Leaf_](override val shape: S) extends SequentialTensor {

      // if the Tensor is a manually defined input, `_Shape` will be a type parameter
      override type _Shape = S
    }
    trait Output extends SequentialTensor {

      // ... otherwise it is inferred automatically from Scala expressions
      override type _Shape = shape.type
    }

    // ... as well as implementing all types of `TypedModule`
    case class Conv2D[OC <: Shape.Leaf.Vector_](
        nChannel: OC
    ) extends TypedModule {

      // Here, The case class automatically overrides the super type constructor
      // This applies to all implementations of TypedModule
      case class ApplyT[I <: Shape](input: I) extends Output {

        val shape = {

          val ij = input
            .rearrangeBy(Indices >< LtoR(0) >< LtoR(1))
          ij >< nChannel
        }
      }

      // Same here, also applies to all implementations of TypedModule
      override def apply(input: TypedTensor): ApplyT[input._Shape] =
        ApplyT(input._shape)
    }

    case class Pooling2D() extends TypedModule {

      case class ApplyT[I <: Shape](input: I) extends Output {

        val shape = {

          val right = Shape(2, 2, 1)
          Ops.:/.applyByDim(input, right)
        }
      }

      override def apply(input: TypedTensor): ApplyT[input._Shape] =
        ApplyT(input._shape)
    }

    case class Flatten() extends TypedModule {

      case class ApplyT[I <: Shape](input: I) extends Output {

        val shape = {

          Ops.:*.reduceByName(input.:<<=*("i", "i", "i"))
        }
      }

      override def apply(input: TypedTensor): ApplyT[input._Shape] =
        ApplyT(input._shape)
    }

    case class FullyConnected[IS <: Shape, OS <: Shape](
        in: IS,
        out: OS
    ) extends TypedModule {

      case class ApplyT[I <: Shape](input: I) extends Output {

        val shape = {

          val empty = Ops.==!.applyByDim(input, in).select0

          val result = empty >< out
          result
        }
      }

      override def apply(input: TypedTensor): ApplyT[input._Shape] =
        ApplyT(input._shape)
    }

    // now all the ingredients are made type-safe, let's take it for a spin
    {
      val data = Input(Shape(28, 28, 1))
      // input looks reasonable
      data.shape.reason

      val conv1 = Conv2D(Shape(6))
      val maxPool1 = Pooling2D()

      val conv2 = Conv2D(Shape(16))
      val maxPool2 = Pooling2D()

      val flatten = Flatten()

      val fc1 = FullyConnected(Shape(784), Shape(120))
      val fc2 = FullyConnected(Shape(120), Shape(84))
      val fc3 = FullyConnected(Shape(84), Shape(10))
      // that's 9 layers

      object LazyTyping {

        val output = data >>
          conv1 >>
          maxPool1 >>
          conv2 >>
          maxPool2 >>
          flatten >>
          fc1 >>
          fc2 >>
          fc3

        output.shape.reason

        // what happens if you forgot maxPool2?
        val output2 = data >>
          conv1 >>
          maxPool1 >>
          conv2 >>
          flatten >>
          fc1 >>
          fc2 >>
          fc3

        output2.shape.reason

      }

      object EagerTyping {

        val output = data >>!
          conv1 >>!
          maxPool1 >>!
          conv2 >>!
          maxPool2 >>!
          flatten >>!
          fc1 >>!
          fc2 >>!
          fc3

        output.shape.reason

        // what happens if you forgot maxPool2?
        val output2 = data >>!
          conv1 >>!
          maxPool1 >>!
          conv2 >>!
          flatten >>!
          fc1 >>!
          fc2 >>!
          fc3

        // with eager verification this time, you don't need to call `eval` or `reason` anymore to find the problem
      }

    }
  }
}
