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

package org.springframework.batch.core.partition.gridgain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.SpringVersion;

import java.io.Serializable;

/** 
 * @author Dave Syer
 */
public class RemoteStepExecutor implements Serializable {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private final StepExecution stepExecution;

	private final String stepName;

	private final String configLocation;

	public RemoteStepExecutor(String configLocation, String stepName, StepExecution stepExecution) {
		this.configLocation = configLocation;
		this.stepName = stepName;
		this.stepExecution = stepExecution;
	}

	public StepExecution execute() {

		Step step = (Step) new ClassPathXmlApplicationContext(configLocation).getBean(stepName, Step.class);;

		logger.info("Spring Version: " + SpringVersion.getVersion());
		
		try {
			step.execute( stepExecution );
		}
		catch ( JobInterruptedException e ) {
			stepExecution.getJobExecution().setStatus(BatchStatus.STOPPING);
			throw new UnexpectedJobExecutionException("TODO: this should result in a stop", e);
		}
        
		return stepExecution;

	}

	public String getStepName() {
		return stepName;
	}

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": " +
                "step name [" + this.getStepName() + "], " +
                "step execution [ " + this.stepExecution + " ], " +
                "configuration [ " + this.configLocation + " ]";
    }
}
