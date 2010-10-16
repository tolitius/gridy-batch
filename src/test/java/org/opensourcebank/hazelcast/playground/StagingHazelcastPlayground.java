/*
 * Copyright 2006-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opensourcebank.hazelcast.playground;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.opensourcebank.transaction.iso8583.Iso8583Transaction;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

/**
 * <p> Strictly for playing / experimenting purposes.. </p>
 *
 * @author anatoly.polinsky
 */

public class StagingHazelcastPlayground {

	static {
    	System.setProperty( "GRIDGAIN_HOME", "/opt/gridgain" );
	}

    public static void main( String arg[] ) throws Exception {

        FlatFileItemReader<Iso8583Transaction> stagingItemReader;

        ApplicationContext ac = new ClassPathXmlApplicationContext( "classpath:META-INF/conf/stage-test-data-context.xml" );

        stagingItemReader = ( FlatFileItemReader<Iso8583Transaction> ) ac.getBean( "stagingItemReader" );

        HazelcastInstance hz = Hazelcast.init( null );

        Map<Long, Iso8583Transaction> txMap =  hz.getMap( "offline-transactions" );

        //IdGenerator idGenerator = Hazelcast.getIdGenerator("customer-ids");
        //long id = idGenerator.newId();

        Iso8583Transaction tx;
        Long txId = 0L;

        stagingItemReader.open( new ExecutionContext() );

        while ( ( tx = stagingItemReader.read() ) != null) {
            txMap.put( txId++, tx );
        }

        while ( ( tx = stagingItemReader.read() ) != null) {
            System.out.println( "\t>>>> " + txMap.get( txId++ ) );
        }

        hz.shutdown();
    }
}

