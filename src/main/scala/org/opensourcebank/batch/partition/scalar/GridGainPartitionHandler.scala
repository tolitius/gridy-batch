package org.opensourcebank.batch.partition.scalar

import org.springframework.batch.core.partition.gridgain.{RemoteStepExecutor}

import org.springframework.batch.core.StepExecution

import org.springframework.batch.core.partition.PartitionHandler
import org.springframework.batch.core.partition.StepExecutionSplitter;

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
              for ( stepExecutor <- createRemoteStepExecutors ( stepExecution, stepSplitter, grid.size( null ) ) )
              yield () => { stepExecutor.execute() },

              // reduce              
              ( se: Seq[ StepExecution ] ) => { unconvertList( new ArrayList ++ se ) }              
      )
    }
  }

  /**
   * <p> Given a grid size that is available at runtime from Gridgain and a custom partitioner injected to the step,
   *     call a step execution splitter, and using the result Set of multiple step executions creates and returns
   *     a collection of step executors (to be passed on to multiple computing nodes) </p>
   */
  def createRemoteStepExecutors( stepExecution: StepExecution, stepSplitter: StepExecutionSplitter, gridSize: Int ) = {

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