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
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ximplementation.Implement;
import org.ximplementation.Implementor;
import org.ximplementation.Priority;
import org.ximplementation.Validity;

/**
 * {@linkplain SimpleImplementeeMethodInvocationFactory} unit tests.
 * 
 * @author earthangry@gmail.com
 * @date 2016-8-25
 *
 */
public class SimpleImplementeeMethodInvocationFactoryTest
		extends AbstractTestSupport
{
	private SimpleImplementeeMethodInvocationFactory simpleImplementeeMethodInvocationFactory;
	private ImplementationResolver implementationResolver;

	@Before
	public void setUp() throws Exception
	{
		this.simpleImplementeeMethodInvocationFactory = new SimpleImplementeeMethodInvocationFactory();
		this.implementationResolver = new ImplementationResolver();
	}

	@After
	public void tearDown() throws Exception
	{
		this.simpleImplementeeMethodInvocationFactory = null;
		this.implementationResolver = null;
	}

	@Test
	public void getTest() throws Throwable
	{
		// !isImplementMethodParamTypeValid(...)
		{
			Class<?> implementee = GetTest.Implementee.class;
			Method implementeeMethod = getMethodByName(implementee, "plus");

			Map<Class<?>, Collection<?>> implementorBeansMap = new HashMap<Class<?>, Collection<?>>();
			implementorBeansMap.put(GetTest.Implementor1.class,
					Arrays.asList(new GetTest.Implementor1()));
			implementorBeansMap.put(GetTest.Implementor2.class,
					Arrays.asList(new GetTest.Implementor2()));

			assertNull(this.simpleImplementeeMethodInvocationFactory
					.get(
							this.implementationResolver.resolve(implementee,
									GetTest.Implementor1.class,
									GetTest.Implementor2.class),
							implementeeMethod, new Object[] { 1D, 2D },
							new SimpleImplementorBeanFactory(
									implementorBeansMap)));
		}

		// implementorBeans == null
		{
			Class<?> implementee = GetTest.Implementee.class;
			Method implementeeMethod = getMethodByName(implementee, "plus");

			assertNull(this.simpleImplementeeMethodInvocationFactory
					.get(
							this.implementationResolver.resolve(implementee,
									GetTest.Implementor0.class,
									GetTest.Implementor1.class),
							implementeeMethod, new Object[] { 1D, 2D },
							new SimpleImplementorBeanFactory()));
		}

		// implementorBeans.isEmpty()
		{
			Class<?> implementee = GetTest.Implementee.class;
			Method implementeeMethod = getMethodByName(implementee, "plus");

			Map<Class<?>, Collection<?>> implementorBeansMap = new HashMap<Class<?>, Collection<?>>();
			implementorBeansMap.put(GetTest.Implementor1.class,
					Arrays.asList());
			implementorBeansMap.put(GetTest.Implementor2.class,
					Arrays.asList());

			assertNull(this.simpleImplementeeMethodInvocationFactory
					.get(
							this.implementationResolver.resolve(implementee,
									GetTest.Implementor0.class,
									GetTest.Implementor1.class),
							implementeeMethod, new Object[] { 1D, 2D },
							new SimpleImplementorBeanFactory()));
		}

		// invokeValidityMethod(...) is false
		{
			Class<?> implementee = GetTest.Implementee.class;
			Method implementeeMethod = getMethodByName(implementee, "plus");

			Map<Class<?>, Collection<?>> implementorBeansMap = new HashMap<Class<?>, Collection<?>>();
			implementorBeansMap.put(GetTest.Implementor2.class,
					Arrays.asList(new GetTest.Implementor2()));
			implementorBeansMap.put(GetTest.Implementor3.class,
					Arrays.asList(new GetTest.Implementor3()));

			assertNull(this.simpleImplementeeMethodInvocationFactory
					.get(
							this.implementationResolver.resolve(implementee,
									GetTest.Implementor2.class,
									GetTest.Implementor3.class),
							implementeeMethod, new Object[] { -1, 2 },
							new SimpleImplementorBeanFactory(
									implementorBeansMap)));
		}

		// getPriorityValue() = Integer.MAX_VALUE
		{
			Class<?> implementee = GetTest.Implementee.class;
			Method implementeeMethod = getMethodByName(implementee, "plus");

			Map<Class<?>, Collection<?>> implementorBeansMap = new HashMap<Class<?>, Collection<?>>();
			implementorBeansMap.put(GetTest.Implementor0.class,
					Arrays.asList(new GetTest.Implementor0()));
			implementorBeansMap.put(GetTest.Implementor1.class,
					Arrays.asList(new GetTest.Implementor1()));
			implementorBeansMap.put(GetTest.Implementor2.class,
					Arrays.asList(new GetTest.Implementor2()));
			implementorBeansMap.put(GetTest.Implementor3.class,
					Arrays.asList(new GetTest.Implementor3()));
			implementorBeansMap.put(GetTest.Implementor4.class,
					Arrays.asList(new GetTest.Implementor4()));

			DefaultImplementeeMethodInvocation implementeeMethodInvocationInfo = (DefaultImplementeeMethodInvocation) this.simpleImplementeeMethodInvocationFactory
					.get(
							this.implementationResolver.resolve(implementee,
									GetTest.Implementor0.class,
									GetTest.Implementor1.class,
									GetTest.Implementor2.class,
									GetTest.Implementor3.class,
									GetTest.Implementor4.class),
							implementeeMethod, new Object[] { 1, 2 },
							new SimpleImplementorBeanFactory(
									implementorBeansMap));

			assertEquals(GetTest.Implementor4.class,
					implementeeMethodInvocationInfo.getImplementMethodInfo()
							.getImplementor());
			assertTrue(implementeeMethodInvocationInfo
					.getImplementorBean() instanceof GetTest.Implementor4);
		}

		// invokePriorityMethod(...) = Integer.MAX_VALUE
		{
			Class<?> implementee = GetTest.Implementee.class;
			Method implementeeMethod = getMethodByName(implementee, "plus");

			Map<Class<?>, Collection<?>> implementorBeansMap = new HashMap<Class<?>, Collection<?>>();
			implementorBeansMap.put(GetTest.Implementor0.class,
					Arrays.asList(new GetTest.Implementor0()));
			implementorBeansMap.put(GetTest.Implementor1.class,
					Arrays.asList(new GetTest.Implementor1()));
			implementorBeansMap.put(GetTest.Implementor2.class,
					Arrays.asList(new GetTest.Implementor2()));
			implementorBeansMap.put(GetTest.Implementor3.class,
					Arrays.asList(new GetTest.Implementor3()));
			implementorBeansMap.put(GetTest.Implementor5.class,
					Arrays.asList(new GetTest.Implementor5()));

			DefaultImplementeeMethodInvocation implementeeMethodInvocationInfo = (DefaultImplementeeMethodInvocation) this.simpleImplementeeMethodInvocationFactory
					.get(
							this.implementationResolver.resolve(implementee,
									GetTest.Implementor0.class,
									GetTest.Implementor1.class,
									GetTest.Implementor2.class,
									GetTest.Implementor3.class,
									GetTest.Implementor5.class),
							implementeeMethod, new Object[] { 1, 2 },
							new SimpleImplementorBeanFactory(
									implementorBeansMap));

			assertEquals(GetTest.Implementor5.class,
					implementeeMethodInvocationInfo.getImplementMethodInfo()
							.getImplementor());
			assertTrue(implementeeMethodInvocationInfo
					.getImplementorBean() instanceof GetTest.Implementor5);
		}

		// compareImplementMethodInfoPriority(...) < 0
		{
			Class<?> implementee = GetTest.Implementee.class;
			Method implementeeMethod = getMethodByName(implementee, "plus");

			Map<Class<?>, Collection<?>> implementorBeansMap = new HashMap<Class<?>, Collection<?>>();
			implementorBeansMap.put(GetTest.Implementor0.class,
					Arrays.asList(new GetTest.Implementor0()));
			implementorBeansMap.put(GetTest.Implementor1.class,
					Arrays.asList(new GetTest.Implementor1()));

			DefaultImplementeeMethodInvocation implementeeMethodInvocationInfo = (DefaultImplementeeMethodInvocation) this.simpleImplementeeMethodInvocationFactory
					.get(
							this.implementationResolver.resolve(implementee,
									GetTest.Implementor0.class,
									GetTest.Implementor1.class),
							implementeeMethod, new Object[] { 1, 2 },
							new SimpleImplementorBeanFactory(
									implementorBeansMap));

			assertEquals(GetTest.Implementor1.class,
					implementeeMethodInvocationInfo.getImplementMethodInfo()
							.getImplementor());
			assertTrue(implementeeMethodInvocationInfo
					.getImplementorBean() instanceof GetTest.Implementor1);
		}
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
