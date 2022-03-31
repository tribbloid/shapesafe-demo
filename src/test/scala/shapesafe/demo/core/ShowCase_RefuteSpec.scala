package shapesafe.demo.core

import shapesafe.demo.core.batch.ShouldFailBatch

class ShowCase_RefuteSpec extends ShouldFailBatch.SourceSpec {

  override def groundTruth: String =
    """
      |
      |shapesafe/demo/core/ShowCase_Refute:12: info:     ▓▒░ cannot evaluate ░▒▓
      |Op2ByDim_DropLeft[ == ]#On[6561 :<<- i,256]
      |    flatten.requireEqual(Shape(256)).reason
      |                                     ^
      |shapesafe/demo/core/ShowCase_Refute:12: error: ¯\_(ツ)_/¯ 6561 != 256
      |    ▓▒░ ... when proving arity ░▒▓
      |6561 == 256
      |    flatten.requireEqual(Shape(256)).reason
      |                                     ^
      |shapesafe/demo/core/ShowCase_Refute:19: info:     ▓▒░ cannot evaluate ░▒▓
      |Op2ByDim_DropLeft[ == ]#On[Select1[100 >< 200 >< 3 :<<= (i >< j >< k),j],100]
      |    (x1 requireEqual Shape(100)).reason
      |                                 ^
      |shapesafe/demo/core/ShowCase_Refute:19: error: ¯\_(ツ)_/¯ 200 != 100
      |    ▓▒░ ... when proving arity ░▒▓
      |200 == 100
      |    (x1 requireEqual Shape(100)).reason
      |                                 ^
      |shapesafe/demo/core/ShowCase_Refute:20: info:     ▓▒░ cannot evaluate ░▒▓
      |Op2ByDim_DropLeft[ == ]#On[Select1[100 >< 200 >< 3 :<<= (i >< j >< k),1],100]
      |    (x2 requireEqual Shape(100)).reason
      |                                 ^
      |shapesafe/demo/core/ShowCase_Refute:20: error: ¯\_(ツ)_/¯ 200 != 100
      |    ▓▒░ ... when proving arity ░▒▓
      |200 == 100
      |    (x2 requireEqual Shape(100)).reason
      |                                 ^
      |shapesafe/demo/core/ShowCase_Refute:22: info:     ▓▒░ cannot evaluate ░▒▓
      |Op2ByDim_DropLeft[ == ]#On[Rearrange[100 >< 200 >< 3 :<<= (i >< j >< k),i >< j],100 >< 3]
      |    (m1 requireEqual Shape(100, 3)).reason
      |                                    ^
      |shapesafe/demo/core/ShowCase_Refute:22: error: ¯\_(ツ)_/¯ 200 != 3
      |    ▓▒░ ... when proving arity ░▒▓
      |200 == 3
      |    (m1 requireEqual Shape(100, 3)).reason
      |                                    ^
      |shapesafe/demo/core/ShowCase_Refute:23: info:     ▓▒░ cannot evaluate ░▒▓
      |Rearrange[ReduceByName[ == ]#On[Rearrange[100 >< 200 >< 3 :<<= (i >< j >< k),j >< k] >< Rearrange[100 >< 200 >< 3 :<<= (i >< j >< k),i >< j] :<<= (i >< j >< j >< k)],i >< k]
      |    m2.matMul(m1).reason
      |                  ^
      |shapesafe/demo/core/ShowCase_Refute:23: error: ¯\_(ツ)_/¯ 3 != 100
      |    ▓▒░ ... when proving arity ░▒▓
      |3 == 100
      |    m2.matMul(m1).reason
      |                  ^
      |shapesafe/demo/core/ShowCase_Refute:37: info:     ▓▒░ cannot evaluate ░▒▓
      |Rearrange[ReduceByName[ == ]#On[999999 >< 8 >< 3 :<<= (i >< j >< channel) >< (8 >< 361 >< 3 :<<= (j >< k >< channel))],i >< l >< channel]
      |      m.reason
      |        ^
      |shapesafe/demo/core/ShowCase_Refute:37: error: Indices not found
      |    ▓▒░ ... when proving shape ░▒▓
      |Rearrange[999999 :<<- i >< (8 :<<- j) >< (3 :<<- channel) >< (361 :<<- k),i >< l >< channel]
      |      m.reason
      |        ^
      |shapesafe/demo/core/ShowCase_Refute:47: info:     ▓▒░ cannot evaluate ░▒▓
      |Rearrange[ReduceByName[ == ]#On[999999 >< 8 >< 3 :<<= (i >< j >< k) >< (8 >< 361 >< 3 :<<= (j >< k >< channel))],i >< k >< channel]
      |      m.reason
      |        ^
      |shapesafe/demo/core/ShowCase_Refute:47: error: [CANNOT PROVE]:  |- O
      |      m.reason
      |        ^
      |shapesafe/demo/core/ShowCase_Refute:62: info:     ▓▒░ cannot evaluate ░▒▓
      |Rearrange[ReduceByName[ == ]#On[Op2ByDim_Strict[ * ]#On[1024 >< 8,2 >< 27] :<<= (t >< x) >< (8 >< 216 :<<= (x >< y))],t >< y]
      |    einsum.reason
      |           ^
      |shapesafe/demo/core/ShowCase_Refute:62: error: ¯\_(ツ)_/¯ 216 != 8
      |    ▓▒░ ... when proving arity ░▒▓
      |216 == 8
      |    einsum.reason
      |           ^
      |shapesafe/demo/core/ShowCase_Refute:70: info:     ▓▒░ cannot evaluate ░▒▓
      |Op2ByDim_Strict[ / ]#On[Op2ByDim_Strict[ + ]#On[Op2ByDim_Strict[ - ]#On[1024 >< 768,3 >< 3],1 >< 1],0 >< 0]
      |    val e2E = e2.reason
      |                 ^
      |shapesafe/demo/core/ShowCase_Refute:70: error: ¯\_(ツ)_/¯ (Illegal Operation) 1022 / 0
      |    ▓▒░ ... when proving arity ░▒▓
      |1022 / 0
      |    val e2E = e2.reason
      |                 ^
      |shapesafe/demo/core/ShowCase_Refute:72: info:     ▓▒░ cannot evaluate ░▒▓
      |Op2ByDim_DropLeft[ == ]#On[Op2ByDim_Strict[ / ]#On[Op2ByDim_Strict[ + ]#On[Op2ByDim_Strict[ - ]#On[1024 >< 768,3 >< 3],1 >< 1],2 >< 2],511 >< 511]
      |    s2.requireEqual(Shape(511, 511)).reason
      |                                     ^
      |shapesafe/demo/core/ShowCase_Refute:72: error: ¯\_(ツ)_/¯ 383 != 511
      |    ▓▒░ ... when proving arity ░▒▓
      |383 == 511
      |    s2.requireEqual(Shape(511, 511)).reason
      |                                     ^
      |shapesafe/demo/core/ShowCase_Refute:81: info:       507 >< 379
      |  :=  Op2ByDim_Strict[ / ]#On[Op2ByDim_Strict[ / ]#On[Op2ByDim_Strict[ + ]#On[Op2ByDim_Strict[ - ]#On[Op2ByDim_Strict[ / ]#On[Op2ByDim_Strict[ + ]#On[Op2ByDim_Strict[ - ]#On[Op2ByDim_Strict[ / ]#On[Op2ByDim_Strict[ + ]#On[Op2ByDim_Strict[ - ]#On[1024 >< 768,3 >< 3],0 >< 0],1 >< 1],3 >< 3],0 >< 0],1 >< 1],3 >< 3],0 >< 0],1 >< 1] :<<= (i >< j),2 >< 2]
      |    val s2 = c3p1(s1, kernel, 0, 1, 2).reason
      |                                       ^
      |shapesafe/demo/core/ShowCase_Refute:83: info:     ▓▒░ cannot evaluate ░▒▓
      |Rearrange[ReduceByName[ == ]#On[507 >< 379 :<<= (i >< i)],i]
      |    s2.named("i", "i").einSum.-->*("i").reason
      |                                        ^
      |shapesafe/demo/core/ShowCase_Refute:83: error: ¯\_(ツ)_/¯ 507 != 379
      |    ▓▒░ ... when proving arity ░▒▓
      |507 == 379
      |    s2.named("i", "i").einSum.-->*("i").reason
      |                                        ^
      |""".stripMargin

  override lazy val durationMSCap: Long = 30 * 1000
}
