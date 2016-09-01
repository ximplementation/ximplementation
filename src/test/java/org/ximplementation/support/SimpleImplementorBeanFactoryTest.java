/**
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *  
  * 	http://www.apache.org/licenses/LICENSE-2.0
  *  
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License. 
  */

package org.ximplementation.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@linkplain SimpleImplementorBeanFactory} unit tests.
 * 
 * @author earthangry@gmail.com
 * @date 2016-9-1
 *
 */
public class SimpleImplementorBeanFactoryTest extends AbstractTestSupport
{
	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void getImplementorBeansTest()
	{
		// this.implementorBeansMap == null
		{
			SimpleImplementorBeanFactory factory = new SimpleImplementorBeanFactory();
			assertNull(factory.getImplementorBeans(Number.class));
		}

		// this.implementorBeansMap != null
		{
			List<Number> numbers = new ArrayList<Number>();
			numbers.add(1);
			numbers.add(2.0F);

			List<Integer> integers = new ArrayList<Integer>();
			integers.add(1);
			integers.add(2);

			Map<Class<?>, Collection<?>> map = new HashMap<Class<?>, Collection<?>>();
			map.put(Number.class, numbers);
			map.put(Integer.class, integers);

			SimpleImplementorBeanFactory factory = new SimpleImplementorBeanFactory(
					map);

			assertEquals(numbers, factory.getImplementorBeans(Number.class));
			assertEquals(integers, factory.getImplementorBeans(Integer.class));
		}
	}
}
