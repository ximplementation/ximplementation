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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ximplementation.Implement;
import org.ximplementation.Implementor;
import org.ximplementation.Priority;
import org.ximplementation.Validity;

/**
 * {@linkplain CachedImplementeeMethodInvocationFactory} unit tests.
 * 
 * @author earthangry@gmail.com
 * @date 2016-12-6
 *
 */
public class CachedImplementeeMethodInvocationFactoryTest
		extends AbstractTestSupport
{
	private CachedImplementeeMethodInvocationFactory cachedImplementeeMethodInvocationFactory;
	private ImplementationResolver implementationResolver;

	@Before
	public void setUp() throws Exception
	{
		this.cachedImplementeeMethodInvocationFactory = new CachedImplementeeMethodInvocationFactory();
		this.implementationResolver = new ImplementationResolver();
	}

	@After
	public void tearDown() throws Exception
	{
		this.cachedImplementeeMethodInvocationFactory = null;
		this.implementationResolver = null;
	}

	@Test
	public void getTest() throws Throwable
	{
		// TODO
	}

	public static class GetTest
	{
		public static interface Implementee
		{
			Number plus(Number a, Number b);

			Number minus(Number a, Number b);
		}

		@Implementor(Implementee.class)
		public static class Implementor0
		{
			@Implement
			public Number plus(Number a, Number b)
			{
				return null;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor1
		{
			@Implement
			public int plus(Integer a, Integer b)
			{
				return 0;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor2
		{
			@Implement
			public float plus(Float a, Float b)
			{
				return 0;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor3
		{
			@Implement
			@Validity("isValid")
			public float plus(Integer a, Integer b)
			{
				return 0;
			}

			public boolean isValid(Integer a)
			{
				return a > 0;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor4
		{
			@Implement
			@Priority(priority = Integer.MAX_VALUE)
			public Number plus(Number a, Number b)
			{
				return 0;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor5
		{
			@Implement
			@Priority("getPriority")
			public Number plus(Number a, Number b)
			{
				return 0;
			}

			public int getPriority()
			{
				return Integer.MAX_VALUE;
			}
		}
	}
}
