package shapesafe.demo.core

import shapesafe.demo.core.batch.ComplexBatch

class ShowCase_ComplexSpec extends ComplexBatch.SourceSpec {

  override def groundTruth: String =
    """
      |shapesafe/demo/core/ShowCase_Complex:42: info:       507 >< 507
      |  :=  Op2ByDim_Strict[ / ]#On[Op2ByDim_Strict[ / ]#On[Op2ByDim_Strict[ + ]#On[Op2ByDim_Strict[ - ]#On[Op2ByDim_Strict[ / ]#On[Op2ByDim_Strict[ + ]#On[Op2ByDim_Strict[ - ]#On[Op2ByDim_Strict[ / ]#On[Op2ByDim_Strict[ + ]#On[Op2ByDim_Strict[ - ]#On[1024 >< 1024,3 >< 3],0 >< 0],1 >< 1],3 >< 3],0 >< 0],1 >< 1],3 >< 3],0 >< 0],1 >< 1] :<<= (i >< j),2 >< 2]
      |    val s2 = c2.reason
      |                ^
      |shapesafe/demo/core/ShowCase_Complex:44: info:       507 :<<- i
      |  :=  Rearrange[ReduceByName[ == ]#On[507 >< 507 :<<= (i >< i)],i]
      |    s2.named("i", "i").einSum.-->*("i").reason
      |                                        ^
      |""".stripMargin

  override lazy val durationMSCap: Long = 17 * 1000
}
