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