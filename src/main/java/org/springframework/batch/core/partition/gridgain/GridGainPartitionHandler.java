package org.springframework.batch.core.partition.gridgain;

import org.gridgain.grid.Grid;
import org.gridgain.grid.GridFactory;
import org.gridgain.grid.GridTaskFuture;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.StepExecutionSplitter;

import java.util.Collection;

public class GridGainPartitionHandler implements PartitionHandler {

	public Collection<StepExecution> handle(StepExecutionSplitter stepSplitter, StepExecution stepExecution) throws Exception {
		Grid grid = GridFactory.grid();
		PartitionProvider partitionProvider = new PartitionProvider(stepSplitter, stepExecution);
		GridTaskFuture<Collection<StepExecution>> future = grid.execute(GridGainPartitionTask.class, partitionProvider );
		return future.get();
	}

}
