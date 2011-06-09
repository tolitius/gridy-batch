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

package org.opensourcebank.transaction.repository;

import com.hazelcast.core.Hazelcast;
import org.opensourcebank.transaction.iso8583.AbstractIso8583Transaction;
import org.opensourcebank.transaction.iso8583.Iso8583Transaction;
import org.opensourcebank.transaction.iso8583.TransactionStatus;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;

/**
 * <p>ISO 8583 transaction Hazelcast CRUD repository</p>
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
