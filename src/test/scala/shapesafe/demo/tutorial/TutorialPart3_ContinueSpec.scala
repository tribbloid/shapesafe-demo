package shapesafe.demo.tutorial

import shapesafe.demo.core.batch.ShouldFailBatch

class TutorialPart3_ContinueSpec extends ShouldFailBatch.SourceSpec {

  override lazy val durationMSCap: Long = 220000 * 2

  override def groundTruth: String =
    """
      |shapesafe/demo/core/TutorialPart3_Continue:169: info:       28 >< 28 >< 1
      |    data.shape.reason
      |               ^
      |shapesafe/demo/core/TutorialPart3_Continue:206: info:       10
      |  :=  Rearrange[Op2ByDim_Strict[ == ]#On[Rearrange[Op2ByDim_Strict[ == ]#On[Rearrange[Op2ByDim_Strict[ == ]#On[ReduceByName[ * ]#On[Op2ByDim_Strict[ / ]#On[Rearrange[Op2ByDim_Strict[ / ]#On[Rearrange[28 >< 28 >< 1,0 >< 1] >< 6,2 >< 2 >< 1],0 >< 1] >< 16,2 >< 2 >< 1] :<<= (i >< i >< i)],784],∅] >< 120,120],∅] >< 84,84],∅] >< 10
      |    output.shape.reason
      |                 ^
      |shapesafe/demo/core/TutorialPart3_Continue:218: info:     ▓▒░ cannot evaluate ░▒▓
      |Rearrange[Op2ByDim_Strict[ == ]#On[Rearrange[Op2ByDim_Strict[ == ]#On[Rearrange[Op2ByDim_Strict[ == ]#On[ReduceByName[ * ]#On[Rearrange[Op2ByDim_Strict[ / ]#On[Rearrange[28 >< 28 >< 1,0 >< 1] >< 6,2 >< 2 >< 1],0 >< 1] >< 16 :<<= (i >< i >< i)],784],∅] >< 120,120],∅] >< 84,84],∅] >< 10
      |    output2.shape.reason
      |                  ^
      |shapesafe/demo/core/TutorialPart3_Continue:218: error: ¯\_(ツ)_/¯ 3136 != 784
      |    ▓▒░ ... when proving arity ░▒▓
      |3136 == 784
      |    output2.shape.reason
      |                  ^
      |""".stripMargin
}
