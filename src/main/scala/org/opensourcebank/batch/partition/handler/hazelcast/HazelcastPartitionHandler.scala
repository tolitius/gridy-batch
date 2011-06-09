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
package org.opensourcebank.batch.partition.handler.hazelcast

import org.springframework.batch.core.partition.gridgain.RemoteStepExecutor

import org.springframework.batch.core.StepExecution

import org.springframework.batch.core.partition.PartitionHandler
import org.springframework.batch.core.partition.StepExecutionSplitter
import org.opensourcebank.batch.partition.splitter.StepExecutorSplitter
import org.opensourcebank.batch.partition.task.StepExecutorTaskAdapter
import com.hazelcast.examples.TestApp.Echo
import com.hazelcast.core.{DistributedTask, Hazelcast}
import java.util.concurrent.{Future, FutureTask, ExecutorService}


import scala.collection.jcl.Conversions.unconvertList
import scala.collection.jcl.ArrayList

import org.gridgain.scalar.scalar
import scalar._

/**
 * <p>Implements a "
 *
 *     public interface PartitionHandler  {
 *         Collection<StepExecution> handle( StepExecutionSplitter stepSplitter,
 *                                           StepExecution stepExecution ) throws Exception; }
 *    " interface.
 *
 * The splitter creates all the executions that need to be farmed out, along with their input parameters
 * (in the form of their  {@link ExecutionContext } ). The master step execution is used to identify the partition
 * and group together the results logically.
 *
 * @param stepSplitter a strategy for generating a collection of {@link StepExecution } instances
 * @param stepExecution the master step execution for the whole partition
 * @return a collection of completed  { @link StepExecution } instances
 * @throws Exception if anything goes wrong. This allows implementations to be liberal and rely on the caller
 * to translate an exception into a step failure as necessary.
 *
 * @author anatoly.polinsky
 *
 **/
class HazelcastPartitionHandler( jobSpringConfigLocation: String ) extends PartitionHandler {

  /**
   * <p>Iterates over step executors and submits them as xxxx to available nodes in round robin fashion</p>
   * <p>Once all the xxxx are completed, it converts a resulting Seq of step executions into a java collection.</p>
   */
  @throws(classOf[Exception])
  override def handle( stepSplitter: StepExecutionSplitter, stepExecution: StepExecution ) = {

    var tasks = List[ Future[ StepExecution ] ]()
    var stepExecutions = List[ StepExecution ]()

    // map
    for ( stepExecutor <- StepExecutorSplitter.createRemoteStepExecutors ( stepExecution,
                                                                           stepSplitter,
                                                                           jobSpringConfigLocation,
                                                                           Hazelcast.getCluster.getMembers.size ) ) {

      val executorService = Hazelcast.getExecutorService();
      val task = executorService.submit( new StepExecutorTaskAdapter( stepExecutor ) );
      tasks ::= task
    }

    // reduce
    for ( task <- tasks ) {
      stepExecutions ::= task.get
    }

    unconvertList( new ArrayList ++ stepExecutions )
  }
}