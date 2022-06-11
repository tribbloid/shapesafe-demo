package shapesafe.demo.tutorial

import shapesafe.core.Ops
import shapesafe.core.shape.Index.LtoR
import shapesafe.core.shape.{Indices, LeafShape, Shape, ShapeType}

object TutorialPart3_Continue {

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
    trait Module {
      // (runtime operation omitted here)
    }

    // This is where the action happens:
    // Adding type and static verification is as simple as adding a new path-dependent type, `_Shape`
    trait TypedTensor extends Tensor {

      // Scala has no support for fully dependent type.
      // To make use of it, we'll need to write implementations with more specific `_Shape` types
      val shape: Shape
      final type _Shape = shape._ShapeType

      def _shape: Shape.^[_Shape] = shape.shapeType.^
    }

    // The API of `Module` therefore adapts accordingly:
    trait TypedModule extends Module {

      // `apply` function now becomes parametric, accompanied by a type constructor
      def apply(input: TypedTensor): ApplyT[input._Shape]
      // Alas, the following type constructor definition is completely redundant, and much longer than I prefer.
      // But scala 2.13 can't perform eta-expansion on polymorphic functions & case classes
      type ApplyT[
        I <: ShapeType
      ] <: TypedTensor // TODO: this can be superseded by polymorphic eta-extension in Scala 3

      final type ApplyShape[I <: ShapeType] = ApplyT[I]#_Shape
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
      def >>![O <: LeafShape](layer: TypedModule)(implicit
                                                  //          to: layer.ApplyShape[_Shape]#ReasonTo[O] // FIXME: a compiler error in Scala 2.13.8 caused this shortcut to break
                                                  evalTo: layer.ApplyShape[_Shape] |-@- O
      ): Input[O] = {
        val unevaluated = >>(layer)
        val proven: O = evalTo(unevaluated._shape.shapeType)

        Input(proven.^)
      }
    }

    // Finally we are done with abstractions! Now we just need to finish implementing all types of `TypedModule`
    case class Conv2D[OC <: Shape.Leaf.Vector_](
                                                 nChannel: OC
                                               ) extends TypedModule {

      // Here, The case class automatically overrides the super type constructor
      // This applies to all implementations of TypedModule
      case class ApplyT[I <: ShapeType](input: Shape.^[I])
        extends SequentialTensor {

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

      case class ApplyT[I <: ShapeType](input: Shape.^[I])
        extends SequentialTensor {

        val shape = {

          val right = Shape(2, 2, 1)
          Ops.:/.applyByDim(input, right)
        }
      }

      override def apply(input: TypedTensor): ApplyT[input._Shape] =
        ApplyT(input._shape)
    }

    case class Flatten() extends TypedModule {

      case class ApplyT[I <: ShapeType](input: Shape.^[I])
        extends SequentialTensor {

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

      case class ApplyT[I <: ShapeType](input: Shape.^[I])
        extends SequentialTensor {

        val shape = {

          val empty = Ops.==!.applyByDim(input, in).dropAll

          val result = empty >< out
          result
        }
      }

      override def apply(input: TypedTensor): ApplyT[input._Shape] =
        ApplyT(input._shape)
    }

    // A manually-typed `Input` tensor also need a minimalistic type constructor`
    case class Input[S <: LeafShape](override val shape: Shape.^[S])
      extends SequentialTensor {}

    // now all the ingredients are made type-safe, let's take it for a spin
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
  }
  // TODO: until this point all above are plain copy of code in TutorialPart3

  // If for some reason (e.g. there is no way to introduce implicit type class) there is no way to eagerly compute shapes
  // Simply replacing all `>>!` with `>>`
  // As described before, all outputs will be typed by computation graphs
  // until `eval` or `reason` is called
  object LazyTensorTyping {

    import TensorTyping._

    val output = data >>
      conv1 >>
      maxPool1 >>
      conv2 >>
      maxPool2 >>
      flatten >>
      fc1 >>
      fc2 >>
      fc3

    // You may realised that type-checking with such paradigm is significantly slower than eager typing
    // Hopefully this problem will be addressed by compiler improvements or aggressive caching
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
}
