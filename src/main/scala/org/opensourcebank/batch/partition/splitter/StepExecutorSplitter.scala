/*
 * Copyright 2011 Anatoly Polinsky
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
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