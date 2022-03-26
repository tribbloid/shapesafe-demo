package shapesafe.demo.core

object TutorialPart4 {

  // If for some reason (e.g. there is no way to introduce implicit type class) there is no way to eagerly compute shapes
  // Simply replacing all `>>!` with `>>`
  // As described before, all outputs will be typed by computation graphs
  // until `eval` or `reason` is called

  object LazyTensorTyping {

    import TutorialPart3.TensorTyping._

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
