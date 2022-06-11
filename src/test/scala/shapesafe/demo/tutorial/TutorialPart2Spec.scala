package shapesafe.demo.tutorial

import shapesafe.demo.core.batch.ShouldFailBatch

class TutorialPart2Spec extends ShouldFailBatch.SourceSpec {

  override def groundTruth: String =
    """
      |shapesafe/demo/core/TutorialPart2:21: info:       4
      |      v3.reason
      |         ^
      |shapesafe/demo/core/TutorialPart2:22: info:       3 >< 4
      |      v34.reason
      |          ^
      |shapesafe/demo/core/TutorialPart2:23: info:       3 >< 4 >< 5
      |      v345.reason
      |           ^
      |shapesafe/demo/core/TutorialPart2:26: info:       3 >< 4
      |      (Shape & Arity(3) & Arity(4)).reason
      |                                    ^
      |shapesafe/demo/core/TutorialPart2:27: info:       3 >< 4
      |      (Shape & 3 & 4).reason
      |                      ^
      |shapesafe/demo/core/TutorialPart2:37: info:       3 :<<- latitude >< (4 :<<- longitude)
      |        (Arity(4) :<<- "longitude")).reason
      |                                     ^
      |shapesafe/demo/core/TutorialPart2:41: info:       3 :<<- latitude >< (4 :<<- longitude)
      |  :=  3 >< 4 :<<= (latitude >< longitude)
      |      (v34 :<<= names).reason
      |                       ^
      |shapesafe/demo/core/TutorialPart2:44: info:       3 :<<- lat >< (4 :<<- lng)
      |  :=  3 >< 4 :<<= (lat >< lng)
      |      val namedV34 = (v34 :<<= Names("lat", "lng")).reason
      |                                                    ^
      |shapesafe/demo/core/TutorialPart2:47: info:       3 :<<- lat >< (4 :<<- lng)
      |  :=  3 >< 4 :<<= (lat >< lng)
      |      (v34 :<<=* ("lat", "lng")).reason
      |                                 ^
      |shapesafe/demo/core/TutorialPart2:50: info:     ▓▒░ cannot evaluate ░▒▓
      |3 >< 4 >< 5 :<<= (lat >< lng)
      |      (v345 :<<=* ("lat", "lng")).reason
      |                                  ^
      |shapesafe/demo/core/TutorialPart2:50: error: Dimension mismatch
      |    ▓▒░ ... when proving shape ░▒▓
      |3 >< 4 >< 5 :<<= (lat >< lng)
      |      (v345 :<<=* ("lat", "lng")).reason
      |                                  ^
      |shapesafe/demo/core/TutorialPart2:53: info:       3 >< 4
      |  :=  3 :<<- lat >< (4 :<<- lng) :<<= ( >< )
      |      namedV34.:<<=*("", "").reason
      |                             ^
      |shapesafe/demo/core/TutorialPart2:54: info:       3 :<<- lat >< 4
      |  :=  3 :<<- lat >< (4 :<<- lng) :<<= (lat >< )
      |      namedV34.:<<=*("lat", "").reason
      |                                ^
      |shapesafe/demo/core/TutorialPart2:64: info:       4 >< 3
      |  :=  Rearrange[3 >< 4,1 >< 0]
      |        .reason
      |         ^
      |shapesafe/demo/core/TutorialPart2:71: info:     ▓▒░ cannot evaluate ░▒▓
      |Rearrange[3 >< 4,2 >< 1]
      |        .reason
      |         ^
      |shapesafe/demo/core/TutorialPart2:71: error: Indices not found
      |    ▓▒░ ... when proving shape ░▒▓
      |Rearrange[3 >< 4,2 >< 1]
      |        .reason
      |         ^
      |shapesafe/demo/core/TutorialPart2:76: info:       4 :<<- y >< (3 :<<- x)
      |  :=  Rearrange[3 >< 4 :<<= (x >< y),y >< x]
      |        .reason
      |         ^
      |shapesafe/demo/core/TutorialPart2:80: info:       4 :<<- y >< (3 :<<- x)
      |  :=  Rearrange[3 >< 4 :<<= (x >< y),y >< x]
      |        .reason
      |         ^
      |shapesafe/demo/core/TutorialPart2:85: info:     ▓▒░ cannot evaluate ░▒▓
      |Rearrange[3 >< 4 :<<= (x >< y),y >< z]
      |        .reason
      |         ^
      |shapesafe/demo/core/TutorialPart2:85: error: Indices not found
      |    ▓▒░ ... when proving shape ░▒▓
      |Rearrange[3 :<<- x >< (4 :<<- y),y >< z]
      |        .reason
      |         ^
      |shapesafe/demo/core/TutorialPart2:88: info:       4 >< 3
      |  :=  Rearrange[RequireNumOfDimensions[3 >< 4,Succ[Succ[_0]]],1 >< 0]
      |      v34.transpose.reason
      |                    ^
      |shapesafe/demo/core/TutorialPart2:91: info:     ▓▒░ cannot evaluate ░▒▓
      |Rearrange[RequireNumOfDimensions[3 >< 4 >< 5,Succ[Succ[_0]]],1 >< 0]
      |      v345.transpose.reason
      |                     ^
      |shapesafe/demo/core/TutorialPart2:91: error: Accepting only 2 dimension(s)
      |    ▓▒░ ... when proving shape ░▒▓
      |RequireNumOfDimensions[3 >< 4 >< 5,Succ[Succ[_0]]]
      |      v345.transpose.reason
      |                     ^
      |shapesafe/demo/core/TutorialPart2:97: info:       4 >< 3
      |  :=  4 >< 3
      |      (v3 >< v4).reason
      |                 ^
      |shapesafe/demo/core/TutorialPart2:101: info:       3 :<<- x >< (4 :<<- y) >< (3 :<<- i) >< (4 :<<- j) >< (5 :<<- k)
      |  :=  3 >< 4 :<<= (x >< y) >< (3 >< 4 >< 5 :<<= (i >< j >< k))
      |      x1.reason
      |         ^
      |shapesafe/demo/core/TutorialPart2:104: info:       3 :<<- x >< (4 :<<- y) >< (3 :<<- i) >< (4 :<<- j) >< (5 :<<- k) >< (3 :<<- x) >< (4 :<<- y) >< (3 :<<- i) >< (4 :<<- j) >< (5 :<<- k)
      |  :=  3 >< 4 :<<= (x >< y) >< (3 >< 4 >< 5 :<<= (i >< j >< k)) >< (3 >< 4 :<<= (x >< y) >< (3 >< 4 >< 5 :<<= (i >< j >< k)))
      |      (x1 >< x1).reason
      |                 ^
      |shapesafe/demo/core/TutorialPart2:111: info:       12 >< 12
      |  :=  Op2ByDim_Strict[ * ]#On[3 >< 4,Rearrange[RequireNumOfDimensions[3 >< 4,Succ[Succ[_0]]],1 >< 0]]
      |      Ops.:*.applyByDim(v34, v34.transpose).reason
      |                                            ^
      |shapesafe/demo/core/TutorialPart2:114: info:       7 >< 7
      |  :=  Op2ByDim_Strict[ + ]#On[Rearrange[RequireNumOfDimensions[3 >< 4,Succ[Succ[_0]]],1 >< 0],3 >< 4]
      |      Ops.:+.applyByDim(v34.transpose, v34).reason
      |                                            ^
      |shapesafe/demo/core/TutorialPart2:117: info:     ▓▒░ cannot evaluate ░▒▓
      |Op2ByDim_Strict[ + ]#On[Rearrange[RequireNumOfDimensions[3 >< 4,Succ[Succ[_0]]],1 >< 0],3 >< 4 >< 5]
      |      Ops.:+.applyByDim(v34.transpose, v345).reason
      |                                             ^
      |shapesafe/demo/core/TutorialPart2:117: error: Dimension mismatch
      |    ▓▒░ ... when proving shape ░▒▓
      |Op2ByDim_Strict[ + ]#On[4 >< 3,3 >< 4 >< 5]
      |      Ops.:+.applyByDim(v34.transpose, v345).reason
      |                                             ^
      |shapesafe/demo/core/TutorialPart2:120: info:       8 >< 8
      |  :=  Op2ByDim_DropLeft[ + ]#On[Rearrange[RequireNumOfDimensions[3 >< 4,Succ[Succ[_0]]],1 >< 0],3 >< 4 >< 5]
      |      Ops.:+.applyByDimDropLeft(v34.transpose, v345).reason
      |                                                     ^
      |shapesafe/demo/core/TutorialPart2:126: info:       12 :<<- x >< (30 :<<- y)
      |  :=  ReduceByName[ * ]#On[3 >< 4 >< 5 >< 6 :<<= (x >< x >< y >< y)]
      |      Ops.:*.reduceByName(Shape(3, 4, 5, 6) :<<=* ("x", "x", "y", "y")).reason
      |                                                                        ^
      |shapesafe/demo/core/TutorialPart2:132: info:       15 :<<- x >< (24 :<<- y)
      |  :=  ReduceByName[ * ]#On[3 >< 4 :<<= (x >< y) >< (5 >< 6 :<<= (x >< y))]
      |      ).reason
      |        ^
      |shapesafe/demo/core/TutorialPart2:135: info:       15 :<<- x >< (24 :<<- y)
      |  :=  ReduceByName[ * ]#On[3 >< 4 :<<= (x >< y) >< (5 >< 6 :<<= (x >< y))]
      |      ).reason
      |        ^
      |shapesafe/demo/core/TutorialPart2:144: info:       3 :<<- i >< (5 :<<- k)
      |  :=  Rearrange[ReduceByName[ == ]#On[3 >< 4 :<<= (i >< j) >< (4 >< 5 :<<= (j >< k))],i >< k]
      |        .reason
      |         ^
      |shapesafe/demo/core/TutorialPart2:147: info:       1
      |  :=  Rearrange[ReduceByName[ == ]#On[3 >< 3 :<<= (i >< i)],∅] >< 1
      |      Shape(3).dot(Shape(3)).reason
      |                             ^
      |shapesafe/demo/core/TutorialPart2:148: info:     ▓▒░ cannot evaluate ░▒▓
      |Rearrange[ReduceByName[ == ]#On[3 >< 4 :<<= (i >< i)],∅] >< 1
      |      Shape(3).dot(Shape(4)).reason
      |                             ^
      |shapesafe/demo/core/TutorialPart2:148: error: ¯\_(ツ)_/¯ 3 != 4
      |    ▓▒░ ... when proving arity ░▒▓
      |3 == 4
      |      Shape(3).dot(Shape(4)).reason
      |                             ^
      |shapesafe/demo/core/TutorialPart2:149: info:       3 :<<- i >< (5 :<<- k)
      |  :=  Rearrange[ReduceByName[ == ]#On[3 >< 4 >< (4 >< 5) :<<= (i >< j >< j >< k)],i >< k]
      |      Shape(3, 4).matMul(Shape(4, 5)).reason
      |                                      ^
      |shapesafe/demo/core/TutorialPart2:150: info:     ▓▒░ cannot evaluate ░▒▓
      |Rearrange[ReduceByName[ == ]#On[3 >< 4 >< (5 >< 4) :<<= (i >< j >< j >< k)],i >< k]
      |      Shape(3, 4).matMul(Shape(5, 4)).reason
      |                                      ^
      |shapesafe/demo/core/TutorialPart2:150: error: ¯\_(ツ)_/¯ 4 != 5
      |    ▓▒░ ... when proving arity ░▒▓
      |4 == 5
      |      Shape(3, 4).matMul(Shape(5, 4)).reason
      |                                      ^
      |""".stripMargin
}
