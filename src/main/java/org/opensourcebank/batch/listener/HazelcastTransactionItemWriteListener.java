package org.opensourcebank.batch.listener;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.Transaction;
import org.apache.log4j.Logger;
import org.opensourcebank.transaction.iso8583.AbstractIso8583Transaction;
import org.opensourcebank.transaction.iso8583.Iso8583Transaction;
import org.opensourcebank.transaction.iso8583.TransactionStatus;
import org.opensourcebank.transaction.repository.HazelcastIso8583TransactionRepository;
import org.opensourcebank.transaction.repository.Iso8583TransactionRepository;
import org.springframework.batch.core.annotation.*;

import java.util.List;

/**
 * <p>Since {@link org.springframework.batch.item.ItemWriter} encapsulates a collection of items that represent a commit
 *    interval chunk,it is an ideal place to manage Hazelcast transactions, and set an initial / pre-process transaction
 *    statuses</p>
 *
 * @author anatoly.polinsky
 */
public class HazelcastTransactionItemWriteListener {

    private static Logger logger = Logger.getLogger( HazelcastTransactionItemWriteListener.class );
    
    private final ThreadLocal<Transaction> hzTransaction = new ThreadLocal<Transaction>();

    private Iso8583TransactionRepository transactionRepository;


    @BeforeWrite
    public void beginTransaction( List<? extends Iso8583Transaction> transactions ) {

        hzTransaction.set(Hazelcast.getTransaction());
        hzTransaction.get().begin();

        for ( Iso8583Transaction tx: transactions ) {
            
            AbstractIso8583Transaction txImpl = ( AbstractIso8583Transaction ) tx;

            // if an ISO 8583 transaction was not previously completed, then it is just STARTING...
            if ( ! TransactionStatus.COMPLETED.equals( txImpl.getStatus() ) ) {
                txImpl.setStatus( TransactionStatus.STARTING );
                transactionRepository.update( txImpl );
            }
        }
    }

    @OnWriteError
    public void rollBackTransaction( Exception exception, List<? extends Iso8583Transaction> items ) {
        hzTransaction.get().rollback();
    }

    @AfterWrite
    public void updateStatusAndCommitTransaction( List<? extends Iso8583Transaction> transactions ) {
        hzTransaction.get().commit();
        System.out.println( "\n===============================  * committed *  =================================\n" );
    }

    
    public void setTransactionRepository(Iso8583TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
}
