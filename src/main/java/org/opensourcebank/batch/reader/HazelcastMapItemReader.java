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

package org.opensourcebank.batch.reader;

import com.hazelcast.core.Hazelcast;
import org.apache.log4j.Logger;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Map;

/**
 * <p>An {@link ItemReader} that given a map name, pulls data item by item from a Hazelcast (distributed) Map.</p>
 *
 * <p>This reader makes two assumptions:
 *
 *                1. Keys are {@link Long},
 *                2. Keys are sequential ( 4,5,6,7,8,9,10,11, etc.. )</p>
 *
 * @author anatoly.polinsky
 */
public class HazelcastMapItemReader<T> implements ItemReader<T>, ItemStream, InitializingBean {

    private static final String CURRENT_ITEM_ID = "current.item.id";
    private static final Logger logger = Logger.getLogger( HazelcastMapItemReader.class );

    Map<Long, T> itemMap;
    String mapName;

    long fromId;
    long toId;

    long currentItemId;    

    public T read() throws Exception {

        if ( currentItemId <= toId ) {
            return itemMap.get( currentItemId++ );
        }

        return null;
    }

    public void open( ExecutionContext executionContext ) throws ItemStreamException {

        // staring Hazelcast to have this node join the cluster
        // and getting a reference to an items map
        itemMap = Hazelcast.getMap( mapName );

        // possibly restarted, but all items were completed / evicted by another node
        // or just a wrong map name
        if ( itemMap.size() == 0 ) {
            logger.warn( "Map [ " + mapName + " ] is empty, no items to read" );
        }

        // is this step restarted?
        if( executionContext.containsKey( CURRENT_ITEM_ID ) ) {
            currentItemId = new Long( executionContext.getLong( CURRENT_ITEM_ID ) ).intValue();
        }
        else {
            currentItemId = fromId;
        }
    }

    public void update( ExecutionContext executionContext ) throws ItemStreamException {

        // save the current item ID to make this step restartable
        executionContext.putLong( CURRENT_ITEM_ID, new Long( currentItemId ).longValue() );
    }

    public void close() throws ItemStreamException {}

    public void afterPropertiesSet() throws Exception {
        if ( ( "".equals( mapName) ) || ( mapName == null ) ) {
            throw new IllegalArgumentException(
                    this.getClass().getSimpleName() + ": 'mapName' must be set" );
        }
        if ( ( fromId >= 0 ) && ( toId > 0 ) ) {
            if ( fromId >= toId ) {
                throw new IllegalArgumentException(
                        this.getClass().getSimpleName() + ": 'fromId' must be less than 'toId'" );
            }
        }
        else {
            throw new IllegalArgumentException(
                    this.getClass().getSimpleName() +
                            ": 'fromId' and 'toId' must be set, and 'fromId' should be less than 'toId'" );                            
        }
    }


    // public accessors

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }
}

