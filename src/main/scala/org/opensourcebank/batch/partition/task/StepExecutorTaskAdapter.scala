package org.opensourcebank.batch.partition.task

import org.springframework.batch.core.partition.gridgain.RemoteStepExecutor
import java.util.concurrent.Callable
import org.springframework.batch.core.StepExecution
import java.io.Serializable

/**
 * <p>Adapter that takes in a {@link org.springframework.batch.core.partition.gridgain.RemoteStepExecutor} and makes
 *    it {@link java.util.concurrent.Callable}</p> 
 *
 * @author anatoly.polinsky
 */

class StepExecutorTaskAdapter ( stepExecutor: RemoteStepExecutor ) extends Callable[ StepExecution ] with Serializable {

  def call = {
     stepExecutor.execute   
  }
}