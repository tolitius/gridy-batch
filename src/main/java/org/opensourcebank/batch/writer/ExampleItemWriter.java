package org.opensourcebank.batch.writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * Dummy {@link org.springframework.batch.item.ItemWriter} which only logs data it receives.
 */
public class ExampleItemWriter implements ItemWriter<Object> {

	private static final Log log = LogFactory.getLog(ExampleItemWriter.class);

	/**
	 * @see org.springframework.batch.item.ItemWriter#write(Object)
	 */
	public void write(List<? extends Object> data) throws Exception {
		//log.info(data);

        System.err.println( "\t\n\n" + data + "\n\n" );
	}

}
