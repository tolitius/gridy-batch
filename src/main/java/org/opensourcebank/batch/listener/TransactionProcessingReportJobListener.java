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
 * <p>Reports all the transaction statuses after the job was completed</p>
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
