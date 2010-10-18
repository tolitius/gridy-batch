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
package org.opensourcebank.transaction.processor;

import com.hazelcast.core.Hazelcast;
import org.gridgain.grid.GridFactory;
import org.junit.*;
import org.junit.runner.RunWith;
import org.opensourcebank.transaction.iso8583.AbstractIso8583Transaction;
import org.opensourcebank.transaction.iso8583.Iso8583Transaction;
import org.opensourcebank.transaction.iso8583.TransactionStatus;
import org.opensourcebank.transaction.repository.Iso8583TransactionRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * <p>Using {@link org.springframework.batch.item.file.FlatFileItemReader} stages transactions from a file to Hazelcast
 *    and runs the job over multiple GridGain nodes</p>
 *
 * @author anatoly.polinsky
 */
@ContextConfiguration(locations = {"classpath:/META-INF/conf/offline-tx-processing-test-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class OfflineTransactionProcessingIntegrationTest {

	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private Job job;

    @Autowired
    private FlatFileItemReader<Iso8583Transaction> stagingItemReader;
    @Autowired
    private Iso8583TransactionRepository iso8583TransactionRepository;

    @Before
    public void stageTransactionsFromFileToCache() throws Exception {

        //IdGenerator idGenerator = Hazelcast.getIdGenerator("txIds");
        //long id = idGenerator.newId();

        Iso8583Transaction tx;

        stagingItemReader.open( new ExecutionContext() );
        while ( ( tx = stagingItemReader.read() ) != null) {
            iso8583TransactionRepository.create( tx );
        }
    }

	@Test
	public void shouldProcessAllIso8583Transactions() throws Exception {

  		assertNotNull( jobLauncher.run( job, new JobParametersBuilder().addString(
                  "run.id",
                  "offline-transaction-processing-integration.test" ).toJobParameters() ) );

        long repoSize = iso8583TransactionRepository.size();

        // this test will always start with an empty map, hence the first ID is 0
        for ( long id = 0; id < repoSize; id++ ) {
            Iso8583Transaction tx = iso8583TransactionRepository.findById( id );

            assertEquals( "transaction was not completed: [" + tx + "]",
                    TransactionStatus.COMPLETED, ( ( AbstractIso8583Transaction ) tx ).getStatus() );
        }

	}

    @BeforeClass
    public static void startGridFactory() throws Exception {
        System.setProperty( "GRIDGAIN_HOME", "/opt/gridgain" );        
        GridFactory.start();
    }

    @AfterClass
    public static void stopGridFactory() throws Exception {
        GridFactory.stop( true );
    }

    @After
    public void emptyTransactionRepository() throws Exception {

        long repoSize = iso8583TransactionRepository.size();
        
        // this test will always start with an empty map, hence the first ID is 0
        for ( long id = 0; id < repoSize; id++ ) {
            Iso8583Transaction tx = iso8583TransactionRepository.findById( id );
            iso8583TransactionRepository.delete( tx );
        }
    }
}
