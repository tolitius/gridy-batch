package org.opensourcebank.batch.partition.handler.scalar

import org.springframework.batch.core.partition.gridgain.{RemoteStepExecutor}

import org.springframework.batch.core.StepExecution

import org.springframework.batch.core.partition.PartitionHandler
import org.springframework.batch.core.partition.StepExecutionSplitter
import org.opensourcebank.batch.partition.splitter.StepExecutorSplitter;

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
class ScalarPartitionHandler( jobSpringConfigLocation: String ) extends PartitionHandler {

  /**
   * <p>Iterates over step executors and submits them as grid workers to available nodes in round robin fashion</p>
   * <p>Once all the workers are completed, it converts a resulting Seq of step executions into a java collection.</p>
   */
  @throws(classOf[Exception])
  override def handle( stepSplitter: StepExecutionSplitter, stepExecution: StepExecution ) = {

    scalar {

      grid !*~ (

              // map
              for ( stepExecutor <- StepExecutorSplitter.createRemoteStepExecutors ( stepExecution,
                                                                                     stepSplitter,
                                                                                     jobSpringConfigLocation,
                                                                                     grid.size( null ) ) )

              yield () => { stepExecutor.execute() },

              // reduce              
              ( se: Seq[ StepExecution ] ) => { unconvertList( new ArrayList ++ se ) }              
      )
    }
  }
}