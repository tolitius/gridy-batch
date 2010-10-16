package org.opensourcebank.batch.listener;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.Transaction;
import org.apache.log4j.Logger;
import org.opensourcebank.transaction.iso8583.AbstractIso8583Transaction;
import org.opensourcebank.transaction.iso8583.Iso8583Transaction;
import org.opensourcebank.transaction.iso8583.TransactionStatus;
import org.springframework.batch.core.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>TODO: Add Description</p>
 *
 * @author anatoly.polinsky
 */
public class HazelcastTransactionItemWriteListener {

    private static Logger logger = Logger.getLogger( HazelcastTransactionItemWriteListener.class );
    
    private final ThreadLocal<Transaction> hzTransaction = new ThreadLocal<Transaction>();

    @BeforeWrite
    public void beginTransaction( List<? extends Iso8583Transaction> transactions ) {

        hzTransaction.set(Hazelcast.getTransaction());
        hzTransaction.get().begin();

        //logger.debug("\t\n starting hz tx \n");

        for ( Iso8583Transaction tx: transactions ) {
            ( ( AbstractIso8583Transaction ) tx ).setStatus( TransactionStatus.STARTING );
        }

    }

    @OnWriteError
    public void rollBackTransaction( Exception exception, List<? extends Iso8583Transaction> items ) {
        hzTransaction.get().rollback();
    }

    @AfterWrite
    public void updateStatusAndCommitTransaction( List<? extends Iso8583Transaction> transactions ) {

        //logger.debug("\t\n committing hz tx \n");
        hzTransaction.get().commit();

//        Map<Long, Iso8583Transaction> txMap =  Hazelcast.getMap( "offline-transactions" );
//
//        for ( Long id: txMap.keySet() ) {
//            System.out.println( ">>>>>>>>> " + txMap.get( id ) );
//        }
        
    }
}
