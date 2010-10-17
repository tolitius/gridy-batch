package org.opensourcebank.batch.listener;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.Transaction;
import org.apache.log4j.Logger;
import org.opensourcebank.transaction.iso8583.AbstractIso8583Transaction;
import org.opensourcebank.transaction.iso8583.Iso8583Transaction;
import org.opensourcebank.transaction.iso8583.TransactionStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>TODO: Add Description</p>
 *
 * @author anatoly.polinsky
 */
public class TransactionProcessingReportStepListener {

    private static Logger logger = Logger.getLogger( TransactionProcessingReportStepListener.class );

    private String mapName;

    @BeforeStep
    public void showProcessingReportHeader( StepExecution stepExecution ){
        System.out.println( "\nProcessing Transactions:" );
        System.out.println( "----------------------------------------------------" +
                "----------------------------------------------" );
    }

    @AfterStep
    public void showTransactionProcessingReport( StepExecution stepExecution ) {

        System.out.println( "----------------------------------------------------" +
                "----------------------------------------------\n\n" );        

        Map<Long, Iso8583Transaction> txMap =  Hazelcast.getMap( mapName );

        System.out.println( "\nTransaction Processing Status for '" +
                Hazelcast.getCluster().getLocalMember() +"' hazelcast member:" );
        System.out.println( "----------------------------------------------------" +
                "----------------------------------------------\n" );

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
