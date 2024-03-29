package shapesafe.demo.tutorial

import shapesafe.demo.core.batch.ShouldFailBatch

class TutorialPart3Spec extends ShouldFailBatch.SourceSpec {

  override def groundTruth: String =
    """
      |shapesafe/demo/tutorial/TutorialPart3:165: info:       28 >< 28 >< 1
      |    data.shape.reason
      |               ^
      |shapesafe/demo/tutorial/TutorialPart3:186: info:       28 >< 28 >< 6
      |  :=  Rearrange[28 >< 28 >< 1,0 >< 1] >< 6
      |    val output = data >>!
      |                      ^
      |shapesafe/demo/tutorial/TutorialPart3:187: info:       14 >< 14 >< 6
      |  :=  Op2ByDim_Strict[ / ]#On[28 >< 28 >< 6,2 >< 2 >< 1]
      |      conv1 >>!
      |            ^
      |shapesafe/demo/tutorial/TutorialPart3:188: info:       14 >< 14 >< 16
      |  :=  Rearrange[14 >< 14 >< 6,0 >< 1] >< 16
      |      maxPool1 >>!
      |               ^
      |shapesafe/demo/tutorial/TutorialPart3:189: info:       7 >< 7 >< 16
      |  :=  Op2ByDim_Strict[ / ]#On[14 >< 14 >< 16,2 >< 2 >< 1]
      |      conv2 >>!
      |            ^
      |shapesafe/demo/tutorial/TutorialPart3:190: info:       784 :<<- i
      |  :=  ReduceByName[ * ]#On[7 >< 7 >< 16 :<<= (i >< i >< i)]
      |      maxPool2 >>!
      |               ^
      |shapesafe/demo/tutorial/TutorialPart3:191: info:       120
      |  :=  Rearrange[Op2ByDim_Strict[ == ]#On[784 :<<- i,784],∅] >< 120
      |      flatten >>!
      |              ^
      |shapesafe/demo/tutorial/TutorialPart3:192: info:       84
      |  :=  Rearrange[Op2ByDim_Strict[ == ]#On[120,120],∅] >< 84
      |      fc1 >>!
      |          ^
      |shapesafe/demo/tutorial/TutorialPart3:193: info:       10
      |  :=  Rearrange[Op2ByDim_Strict[ == ]#On[84,84],∅] >< 10
      |      fc2 >>!
      |          ^
      |shapesafe/demo/tutorial/TutorialPart3:196: info:       10
      |    output.shape.reason
      |                 ^
      |shapesafe/demo/tutorial/TutorialPart3:199: info:       28 >< 28 >< 6
      |  :=  Rearrange[28 >< 28 >< 1,0 >< 1] >< 6
      |    val output2 = data >>!
      |                       ^
      |shapesafe/demo/tutorial/TutorialPart3:200: info:       14 >< 14 >< 6
      |  :=  Op2ByDim_Strict[ / ]#On[28 >< 28 >< 6,2 >< 2 >< 1]
      |      conv1 >>!
      |            ^
      |shapesafe/demo/tutorial/TutorialPart3:201: info:       14 >< 14 >< 16
      |  :=  Rearrange[14 >< 14 >< 6,0 >< 1] >< 16
      |      maxPool1 >>!
      |               ^
      |shapesafe/demo/tutorial/TutorialPart3:202: info:       3136 :<<- i
      |  :=  ReduceByName[ * ]#On[14 >< 14 >< 16 :<<= (i >< i >< i)]
      |      conv2 >>!
      |            ^
      |shapesafe/demo/tutorial/TutorialPart3:203: info:     ▓▒░ cannot evaluate ░▒▓
      |Rearrange[Op2ByDim_Strict[ == ]#On[3136 :<<- i,784],∅] >< 120
      |      flatten >>!
      |              ^
      |shapesafe/demo/tutorial/TutorialPart3:203: error: ¯\_(ツ)_/¯ 3136 != 784
      |    ▓▒░ ... when proving arity ░▒▓
      |3136 == 784
      |      flatten >>!
      |              ^
      |""".stripMargin

  override lazy val durationMSCap: Long = 10 * 1000
}
