package org.opensourcebank.batch.partition;

import com.hazelcast.core.Hazelcast;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.InitializingBean;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p> Depending on a grid size and a number of items to process,creates a map of
 *     {@link org.springframework.batch.item.ExecutionContext}s so they will be evenly distributed over nodes in a grid.
 * </p>
 * <p> Since each individual partition will get two numbers to represent a [from, to] range, this partitioner assumes
 *     that a map has {@link Long} keys ( IDs ) that are sorted sequentially form min to max ( e.g. 0,1,2,3,4,5, etc.. )
 * </p>
 *
 * @author anatoly.polinsky
 */
public class HazelcastMapPartitioner implements Partitioner, InitializingBean {

    private String mapName;

    public Map<String, ExecutionContext> partition(int gridSize) {

        Map<Long, Object> itemsMap = Hazelcast.getMap( mapName );
        Set<Long> itemsIds= itemsMap.keySet();

        long min = Collections.min( itemsIds );
        long max = Collections.max( itemsIds );

		long targetSize = (max - min) / gridSize + 1;

		Map<String, ExecutionContext> result = new HashMap<String, ExecutionContext>();
		int number = 0;
		long start = min;
		long end = start + targetSize - 1;

		while (start <= max) {

			ExecutionContext value = new ExecutionContext();
			result.put("partition" + number, value);

			if (end >= max) {
				end = max;
			}
			value.putLong( "fromId", start );
			value.putLong( "toId", end );
            value.putString( "mapName", mapName );
			start += targetSize;
			end += targetSize;
			number++;
		}

		return result;
    }

    public void afterPropertiesSet() throws Exception {
        if ( ( "".equals( mapName) ) || ( mapName == null ) ) {
            throw new IllegalArgumentException(
                    this.getClass().getSimpleName() + ": 'mapName' must be set" );
        }
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }
}
