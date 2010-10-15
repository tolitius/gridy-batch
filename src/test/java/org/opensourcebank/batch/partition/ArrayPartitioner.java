package org.opensourcebank.batch.partition;

import org.opensourcebank.batch.reader.ExampleItemReader;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

public class ArrayPartitioner implements Partitioner {
    public Map<String, ExecutionContext> partition(int gridSize) {


        int max = ExampleItemReader.input.length - 1;
        int min = 0;

		int targetSize = (max - min) / gridSize + 1;

		Map<String, ExecutionContext> result = new HashMap<String, ExecutionContext>();
		int number = 0;
		int start = min;
		int end = start + targetSize - 1;

		while (start <= max) {

			ExecutionContext value = new ExecutionContext();
			result.put("partition" + number, value);

			if (end >= max) {
				end = max;
			}
			value.putInt("fromIndex", start);
			value.putInt("toIndex", end);
			start += targetSize;
			end += targetSize;
			number++;
		}

		return result;
    }
}
