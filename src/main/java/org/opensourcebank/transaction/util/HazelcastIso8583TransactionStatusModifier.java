package org.opensourcebank.transaction.util;

import com.hazelcast.core.Hazelcast;
import org.opensourcebank.transaction.iso8583.AbstractIso8583Transaction;
import org.opensourcebank.transaction.iso8583.Iso8583Transaction;
import org.opensourcebank.transaction.iso8583.TransactionStatus;

import java.util.concurrent.ConcurrentMap;

/**
 * <p>TODO: Add Description</p>
 *
 * @author anatoly.polinsky
 */
public final class HazelcastIso8583TransactionStatusModifier {

    private HazelcastIso8583TransactionStatusModifier(){}

    public static void changeStatus( Iso8583Transaction tx, TransactionStatus status, String mapName ) {

        ConcurrentMap<Long, Iso8583Transaction> txMap = Hazelcast.getMap( mapName );
        ( ( AbstractIso8583Transaction ) tx ).setStatus( status );
        txMap.replace( ( ( AbstractIso8583Transaction ) tx ).getId(), tx );
    }
}
