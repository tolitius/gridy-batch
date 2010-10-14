package org.opensourcebank.batch.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.InitializingBean;

/**
 * {@link org.springframework.batch.item.ItemReader} with hard-coded input data.
 */
public class ExampleItemReader implements ItemReader<String>, InitializingBean {

	public final static String[] input =
            { "Hi",
                    "Welcome to Spring Batch!",
                    "I am going to be your guide for today.",
                    "I have Nicolas here with me",
                    "he is going to run on one of the nodes",
                    "he dose not know it yet",
                    "but he will once it happens.",
                    "one..",
                    "two..",
                    "three..",
                    "We will learn many interesting things,",
                    "and experience many 'WOW' moments",
                    "Get ready for a crazy journey.",
                    "You are going to be blown away,",
                    "( but caution, you may never like the world with no batch ever again ).",
                    "So now as you are prepared..",
                    "Let's begin...",
                    "You are entering the world of SB",
                    "Good Luck!"};

    private int fromIndex = -1;
    private int toIndex = -1;

    private int index;

	/**
	 * Reads next record from input
	 */
	public String read() throws Exception {

        if ( index <= toIndex ) {
            return input[index++];
        }
        else return null;
	}

    public void setFromIndex(int fromIndex) {
        this.fromIndex = fromIndex;
    }

    public void setToIndex(int toIndex) {
        this.toIndex = toIndex;
    }

    public void afterPropertiesSet() throws Exception {
        if ( fromIndex < 0 || toIndex < 0  ) {
            throw new IllegalArgumentException( "both: fromIndex and toIndex should be set and be non negative" );
        }
        index = fromIndex;
    }
}
