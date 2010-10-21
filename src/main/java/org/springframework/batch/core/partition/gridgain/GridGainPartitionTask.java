package org.springframework.batch.core.partition.gridgain;

import org.gridgain.grid.*;
import org.gridgain.grid.logger.GridLogger;
import org.gridgain.grid.resources.GridLoggerResource;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.StepExecution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>10/21/2010 Anatoly: upgraded to GridGain 3.0.0 APIs, and added a fail over capability for user exceptions.</p>
 *
 * @author Dave Syer
 * @author anatoly.polinsky
 */
public class GridGainPartitionTask extends GridTaskSplitAdapter<PartitionProvider, Collection<StepExecution>> {

	@GridLoggerResource
	private GridLogger log = null;

	@Override
	protected Collection<? extends GridJob> split(int gridSize, PartitionProvider stepSplit) throws GridException {

		log.info("Executing steps for grid size=" + gridSize);

		//List<GridJob> jobs = new ArrayList<GridJob>(gridSize);

        List<GridJobAdapterEx> jobs = new ArrayList<GridJobAdapterEx>(gridSize);

		final String stepName = stepSplit.getStepName();

		try {
			for (final StepExecution stepExecution : stepSplit.getStepExecutions(gridSize)) {

				RemoteStepExecutor stepExecutor = new RemoteStepExecutor("META-INF/conf/gridgain/with-gridgain-launch-context.xml", stepName, stepExecution);
				jobs.add(new GridJobAdapterEx(stepExecutor) {
					public Serializable execute() {
						RemoteStepExecutor stepExecutor = argument();
						return stepExecutor.execute();
					}
				});

			}
		}
		catch (JobExecutionException e) {
			throw new GridException("Could not execute split step", e);
		}

		return jobs;
	}

	public Collection<StepExecution> reduce(List<GridJobResult> results) throws GridException {
		Collection<StepExecution> total = new ArrayList<StepExecution>();
		for (GridJobResult res : results) {
			StepExecution status = res.getData();
			total.add(status);
		}
		return total;
	}

    @Override
    public GridJobResultPolicy result(GridJobResult result, List receivedResults) throws GridException {

        if (result.getException() != null) {
            
            // Delegate to failover SPI to pick another node.
            log.info( "\n Trying to failover... \n" );
            return GridJobResultPolicy.FAILOVER;
        }

        // Wait for other results to come in, or reduce if all results have arrived.
        return GridJobResultPolicy.WAIT;
    }
}
