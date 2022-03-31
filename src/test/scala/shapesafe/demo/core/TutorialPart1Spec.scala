package shapesafe.demo.core

import shapesafe.demo.core.batch.ShouldFailBatch

class TutorialPart1Spec extends ShouldFailBatch.SourceSpec {

  override def groundTruth: String =
    """
      |shapesafe/demo/core/TutorialPart1:41: info:       9
      |  :=  1 + 2 * 3
      |    expr.peek
      |         ^
      |shapesafe/demo/core/TutorialPart1:48: info:     ▓▒░ cannot evaluate ░▒▓
      |1 / 0
      |    illegal.peek
      |            ^
      |shapesafe/demo/core/TutorialPart1:49: info:     ▓▒░ cannot evaluate ░▒▓
      |1 == 0
      |    illegal2.peek
      |             ^
      |shapesafe/demo/core/TutorialPart1:53: info:       9
      |    result.peek
      |           ^
      |shapesafe/demo/core/TutorialPart1:55: error: ¯\_(ツ)_/¯ (Illegal Operation) 1 / 0
      |    ▓▒░ ... when proving arity ░▒▓
      |1 / 0
      |    illegal.eval
      |            ^
      |shapesafe/demo/core/TutorialPart1:63: error: [CANNOT PROVE]:  |- O
      |      result.eval
      |             ^
      |shapesafe/demo/core/TutorialPart1:68: error: ¯\_(ツ)_/¯ 2 != 3
      |    ▓▒░ ... when proving arity ░▒▓
      |2 == 3
      |    isEven(Arity(3)).eval
      |                     ^
      |shapesafe/demo/core/TutorialPart1:71: info:       9
      |  :=  1 + 2 * 3
      |    val result2 = expr.reason
      |                       ^
      |shapesafe/demo/core/TutorialPart1:72: info:       9
      |    result2.peek
      |            ^
      |shapesafe/demo/core/TutorialPart1:81: info:       3
      |  :=  1 + 2
      |      (v1 :+ v2).reason
      |                 ^
      |shapesafe/demo/core/TutorialPart1:84: info:       0
      |  :=  1 + 2 - 3
      |      vx.reason
      |         ^
      |shapesafe/demo/core/TutorialPart1:87: info:     ▓▒░ cannot evaluate ░▒▓
      |1 / 0
      |      (v1 :/ v0).reason
      |                 ^
      |shapesafe/demo/core/TutorialPart1:87: error: ¯\_(ツ)_/¯ (Illegal Operation) 1 / 0
      |    ▓▒░ ... when proving arity ░▒▓
      |1 / 0
      |      (v1 :/ v0).reason
      |                 ^
      |shapesafe/demo/core/TutorialPart1:88: info:     ▓▒░ cannot evaluate ░▒▓
      |1 / (1 + 2 - 3)
      |      (v1 :/ vx).reason
      |                 ^
      |shapesafe/demo/core/TutorialPart1:88: error: ¯\_(ツ)_/¯ (Illegal Operation) 1 / 0
      |    ▓▒░ ... when proving arity ░▒▓
      |1 / 0
      |      (v1 :/ vx).reason
      |                 ^
      |shapesafe/demo/core/TutorialPart1:91: info:       _UNCHECKED_
      |  :=  1 + _UNCHECKED_
      |      (v1 :+ vU).reason
      |                 ^
      |shapesafe/demo/core/TutorialPart1:98: info:     ▓▒░ cannot evaluate ░▒▓
      |1 == 2
      |      (v1 ==! v2).reason
      |                  ^
      |shapesafe/demo/core/TutorialPart1:98: error: ¯\_(ツ)_/¯ 1 != 2
      |    ▓▒░ ... when proving arity ░▒▓
      |1 == 2
      |      (v1 ==! v2).reason
      |                  ^
      |shapesafe/demo/core/TutorialPart1:99: info:       3
      |  :=  1 + 2 == 3
      |      (v1 :+ v2 ==! v3).reason
      |                        ^
      |shapesafe/demo/core/TutorialPart1:100: info:     ▓▒░ cannot evaluate ░▒▓
      |1 == (2 + 3)
      |      (v1 ==! v2 :+ v3).reason
      |                        ^
      |shapesafe/demo/core/TutorialPart1:100: error: ¯\_(ツ)_/¯ 1 != 5
      |    ▓▒░ ... when proving arity ░▒▓
      |1 == 5
      |      (v1 ==! v2 :+ v3).reason
      |                        ^
      |shapesafe/demo/core/TutorialPart1:104: info:       1
      |  :=  1 == _UNCHECKED_
      |      (v1 ==! vU).reason
      |                  ^
      |shapesafe/demo/core/TutorialPart1:105: info:       1
      |  :=  _UNCHECKED_ == 1
      |      (vU ==! v1).reason
      |                  ^
      |shapesafe/demo/core/TutorialPart1:108: info:       1
      |  :=  1 =/= (2 + 3)
      |      (v1 =/=! v2 :+ v3).reason
      |                         ^
      |shapesafe/demo/core/TutorialPart1:110: info:       1
      |  :=  1 <= (2 + 3)
      |      (v1 <=! v2 :+ v3).reason
      |                        ^
      |shapesafe/demo/core/TutorialPart1:111: info:     ▓▒░ cannot evaluate ░▒▓
      |1 >= (2 + 3)
      |      (v1 >=! v2 :+ v3).reason
      |                        ^
      |shapesafe/demo/core/TutorialPart1:111: error: ¯\_(ツ)_/¯ 1 < 5
      |    ▓▒░ ... when proving arity ░▒▓
      |1 >= 5
      |      (v1 >=! v2 :+ v3).reason
      |                        ^
      |""".stripMargin

}
