 package org.shapesafe.demo.core.c1

 import org.shapesafe.core.arity.ops.ArityOps
 import org.shapesafe.core.shape.{Names, Shape, ShapeAPI}

 // TODO: compilation for such complex proof is seriously slow
 //  can scala compiler uses GPU?
 object S5_Composition extends App {

   import S4_ShapeArithmetics.conv2D

   def c3p1(
       in: ShapeAPI,
       kernel: ShapeAPI,
       padding: ShapeAPI,
       stride: ShapeAPI,
       pooling: ShapeAPI
   ) = {

     val ij = Names("i", "j")

     def conv(in: ShapeAPI) = {
       conv2D(in, kernel, padding, stride)
     }

     val c1 = conv(in)
     val c2 = conv(c1)
     val c3 = conv(c2)

     val _pooling = (pooling >< pooling).namedWith(ij)
     val p1 = c3.namedWith(ij).flattenWith(ArityOps.:/, _pooling)

     p1
   }

   val s1 = Shape(1024, 1024)
   val kernel = Shape(3, 3)

   val c2 = c3p1(s1, kernel, 0, 1, 2)
   val s2 = c2.reason

   s2.named("i", "i").einSum.-->*("i").reason
 }
