package org.opensourcebank.batch.partition.splitter

import org.springframework.batch.core.partition.gridgain.RemoteStepExecutor
import org.springframework.batch.core.partition.StepExecutionSplitter
import org.springframework.batch.core.StepExecution

/**
 * <p> Given a grid size that is available at runtime from Gridgain and a custom partitioner injected to the step,
 *     call a step execution splitter, and using the result Set of multiple step executions creates and returns
 *     a collection of step executors (to be passed on to multiple computing nodes) </p>
 *
 * @author anatoly.polinsky
 */

object StepExecutorSplitter {

  def createRemoteStepExecutors( stepExecution: StepExecution,
                                 stepSplitter: StepExecutionSplitter,
                                 jobSpringConfigLocation: String,
                                 gridSize: Int ) = {

       var stepExecutors: List[RemoteStepExecutor] = List()

       // splitting step executions and converting a result java.util.Set to a Scala immutable Set:
       val stepExecutionsJavaSet: java.util.Set[StepExecution] = stepSplitter.split( stepExecution, gridSize )
       val stepExecutionsScalaSet: Set[StepExecution] = Set( stepExecutionsJavaSet.toArray(new Array[StepExecution](0)) : _* )

       for ( stepExecution <- stepExecutionsScalaSet ) {

         val rex: RemoteStepExecutor = new RemoteStepExecutor( jobSpringConfigLocation,
                                                               stepSplitter.getStepName,
                                                               stepExecution )
         stepExecutors ::= rex

       }

       stepExecutors
  }

}