package org.springframework.batch.core.partition.gridgain;

import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.partition.StepExecutionSplitter;

import java.util.Set;

/**
 * @author Dave Syer
 */
public class PartitionProvider {

	private final StepExecutionSplitter stepSplitter;
	private final StepExecution stepExecution;

	public PartitionProvider(StepExecutionSplitter stepSplitter, StepExecution stepExecution) {
		this.stepSplitter = stepSplitter;
		this.stepExecution = stepExecution;
	}

	public String getStepName() {
		return stepSplitter.getStepName();
	}

	public Set<StepExecution> getStepExecutions(int gridSize) throws JobExecutionException {
		return stepSplitter.split(stepExecution, gridSize);
	}

}
