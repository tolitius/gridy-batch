package org.opensourcebank.batch.listener;

import com.hazelcast.core.Hazelcast;
import org.apache.log4j.Logger;
import org.opensourcebank.transaction.iso8583.Iso8583Transaction;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>TODO: Add Description</p>
 *
 * @author anatoly.polinsky
 */
public class TransactionProcessingReportJobListener {

    private static Logger logger = Logger.getLogger( TransactionProcessingReportJobListener.class );

    private String mapName;

    @AfterJob
    public void showTransactionProcessingReport( JobExecution jobExecution ) {

        Map<Long, Iso8583Transaction> txMap = Hazelcast.getMap( mapName );

        System.out.println( "----------------------------------------------------" +
                "----------------------------------------------\n\n" );

        System.out.println( "\n'Offline Transaction Processing' job status:" );
        System.out.println( "----------------------------------------------------" +
                "----------------------------------------------" );

        // sorting keys strictly for demo / reporting purposes
        List<Long> txIds = new ArrayList<Long>( txMap.keySet() );
        Collections.sort( txIds );

        for ( Long id: txIds) {
            System.out.println( "\t" + txMap.get( id ) );
        }
        System.out.println( "----------------------------------------------------" +
                "----------------------------------------------\n" );
    }

    
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

}
