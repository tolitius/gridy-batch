package org.opensourcebank.batch.writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opensourcebank.transaction.iso8583.ISO8583Transaction;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * Dummy {@link org.springframework.batch.item.ItemWriter} which only logs data it receives.
 */
public class OfflineTransactionItemWriter implements ItemWriter<ISO8583Transaction> {

	private static final Log log = LogFactory.getLog( OfflineTransactionItemWriter.class );

	public void write( List<? extends ISO8583Transaction> transactions ) throws Exception {

        for( ISO8583Transaction tx: transactions ) {
            log.info("\t\n" + tx + "\n");   
        }
	}

}
