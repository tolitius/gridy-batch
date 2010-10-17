package org.opensourcebank.transaction.repository;

import com.hazelcast.core.Hazelcast;
import org.opensourcebank.transaction.iso8583.AbstractIso8583Transaction;
import org.opensourcebank.transaction.iso8583.Iso8583Transaction;
import org.opensourcebank.transaction.iso8583.TransactionStatus;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;

/**
 * <p>TODO: Add Description</p>
 *
 * @author anatoly.polinsky
 */
public class HazelcastIso8583TransactionRepository implements Iso8583TransactionRepository{

    private static final String DEFAULT_MAP_NAME = "default";
    private String mapName;

    private HazelcastIso8583TransactionRepository( String mapName ) {
        if ( "".equals( mapName ) || mapName == null ) {
            mapName = DEFAULT_MAP_NAME;
        }
        else {
            this.mapName = mapName;
        }
    }

    public void create( Iso8583Transaction tx ) {
        Map<Long, Iso8583Transaction> txMap = Hazelcast.getMap( mapName );

        // obtaining a cluster lock,so the next ID is unique
        Lock lock = Hazelcast.getLock( txMap );
        lock.lock();
        try {

            long txNextId = 0;
            Set<Long> txIds = txMap.keySet();

            if ( txIds.size() > 0 ) {
                 txNextId = Collections.max( txIds ) + 1;
            }

            ( ( AbstractIso8583Transaction ) tx ).setId( txNextId );
            txMap.put( txNextId, tx );
            
        } finally {
            lock.unlock();
        }
    }

    public Iso8583Transaction findById( Long id ) {

        Map<Long, Iso8583Transaction> txMap = Hazelcast.getMap( mapName );

        return txMap.get( id );
    }

    public void update( Iso8583Transaction tx ) {

        ConcurrentMap<Long, Iso8583Transaction> txMap = Hazelcast.getMap( mapName );
        txMap.replace( ( ( AbstractIso8583Transaction ) tx ).getId(), tx );
    }

    public void delete( Iso8583Transaction tx ) {

        Map<Long, Iso8583Transaction> txMap = Hazelcast.getMap( mapName );
        txMap.remove( ( ( AbstractIso8583Transaction ) tx ).getId() );
    }

    public Long size() {
        return Long.valueOf( Hazelcast.getMap( mapName ).keySet().size() );
    }
}
