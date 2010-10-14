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
package org.opensourcebank.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertEquals;

//@ContextConfiguration(locations = {"classpath:/META-INF/conf/test-context.xml"})
//@RunWith(SpringJUnit4ClassRunner.class)
public class HazelcastPlaygroundIntegrationTest {

	@Test
	public void shouldShareMapBetweenTwoClusterMembers() throws Exception {
        // start the first member
        //HazelcastInstance h1 = Hazelcast.newHazelcastInstance(null);

        // get the map and put 1000 entries
        Map map1 = Hazelcast.getMap("testmap");
        for (int i = 0; i < 1000; i++) {
            map1.put(i, "value" + i);
        }
        // check the map size
        assertEquals(1000, map1.size());
        // start the second member
        HazelcastInstance h2 = Hazelcast.newHazelcastInstance(null);
        // get the same map from the second member
        Map map2 = h2.getMap("testmap");
        // check the size of map2
        assertEquals(1000, map2.size());
        // check the size of map1 again
        assertEquals(1000, map1.size());
	}
}
