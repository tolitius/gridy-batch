package org.springframework.batch.core.partition.gridgain.scalar

import org.springframework.batch.core.partition.gridgain.{RemoteStepExecutor, PartitionProvider}

import org.springframework.batch.core.StepExecution
import java.util.{ArrayList, Collection}

import org.springframework.batch.core.partition.PartitionHandler
import collection.mutable.HashSet;
import org.springframework.batch.core.partition.StepExecutionSplitter;

import org.gridgain.scalar.scalar
import scalar._
import org.gridgain.grid.lang.{GridFunc => F}
import org.gridgain.grid.GridRichNode

/**
 * <p>Implements a "
 *
 *     public interface PartitionHandler  {
 *         Collection<StepExecution> handle( StepExecutionSplitter stepSplitter,
 *                                   StepExecution stepExecution) throws Exception; }
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
class GridGainPartitionHandler extends PartitionHandler {

  @throws(classOf[Exception])
  override def handle( stepSplitter: StepExecutionSplitter, stepExecution: StepExecution ) = {

    // GG crew, please don't judge to harsh, this is my first Scala adventure :)

    scalar {

      grid !*~ (

              // map
              for ( stepExecution <- Seq( (
                      new PartitionProvider( stepSplitter, stepExecution )
                      ).getStepExecutions( grid.size( null ) ).toArray( new Array[StepExecution]( 0 ) ) : _* ) )

              yield () => {

                new RemoteStepExecutor( "META-INF/conf/launch-context.xml",
                                        stepSplitter.getStepName,
                                        stepExecution ).execute()
              },

              // reduce
              ( se: Seq[ StepExecution ] ) => se.asInstanceOf[ Collection[ StepExecution ] ]
      )
    }
  }
}