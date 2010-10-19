package org.opensourcebank.scalar.playground

import org.gridgain.scalar.scalar
import scalar._
import org.gridgain.grid._

/**
 * <p>TODO: Add Description</p> 
 *
 * @author anatoly.polinsky
 */

object ClassicMapReduce extends Application {

  System.setProperty( "GRIDGAIN_HOME", "/opt/gridgain" )

  scalar.start()
  grid !!~ ( for ( w <- " Say Hello Functional Scala".split( " " ) ) yield () => println( w ) )
}