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
import static org.junit.Assert.assertFalse;
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
 * {@linkplain DefaultImplementeeMethodInvocationInfoEvaluator}单元测试类。
 * 
 * @author earthangry@gmail.com
 * @date 2016年8月25日
 *
 */
public class DefaultImplementeeMethodInvocationInfoEvaluatorTest
		extends AbstractTestSupport
{
	private DefaultImplementeeMethodInvocationInfoEvaluator defaultImplementeeMethodInvocationInfoEvaluator;
	private ImplementationResolver implementationResolver;

	@Before
	public void setUp() throws Exception
	{
		this.defaultImplementeeMethodInvocationInfoEvaluator = new DefaultImplementeeMethodInvocationInfoEvaluator();
		this.implementationResolver = new ImplementationResolver();
	}

	@After
	public void tearDown() throws Exception
	{
		this.defaultImplementeeMethodInvocationInfoEvaluator = null;
		this.implementationResolver = null;
	}

	@Test
	public void evaluateTest() throws Throwable
	{
		// isImplementMethodParamValid(...) = false
		{
			Class<?> implementee = EvaluateTest.Implementee.class;
			Method implementeeMethod = getMethodByName(implementee, "plus");

			Map<Class<?>, Collection<?>> implementorBeansMap = new HashMap<Class<?>, Collection<?>>();
			implementorBeansMap.put(EvaluateTest.Implementor1.class,
					Arrays.asList(new EvaluateTest.Implementor1()));
			implementorBeansMap.put(EvaluateTest.Implementor2.class,
					Arrays.asList(new EvaluateTest.Implementor2()));

			assertNull(this.defaultImplementeeMethodInvocationInfoEvaluator
					.evaluate(
							this.implementationResolver.resolve(implementee,
									EvaluateTest.Implementor1.class,
									EvaluateTest.Implementor2.class),
							implementeeMethod, new Object[] { 1D, 2D },
							new SimpleImplementorBeanFactory(
									implementorBeansMap)));
		}

		// implementorBeans == null
		{
			Class<?> implementee = EvaluateTest.Implementee.class;
			Method implementeeMethod = getMethodByName(implementee, "plus");

			assertNull(this.defaultImplementeeMethodInvocationInfoEvaluator
					.evaluate(
							this.implementationResolver.resolve(implementee,
									EvaluateTest.Implementor0.class,
									EvaluateTest.Implementor1.class),
							implementeeMethod, new Object[] { 1D, 2D },
							new SimpleImplementorBeanFactory()));
		}

		// implementorBeans.isEmpty()
		{
			Class<?> implementee = EvaluateTest.Implementee.class;
			Method implementeeMethod = getMethodByName(implementee, "plus");

			Map<Class<?>, Collection<?>> implementorBeansMap = new HashMap<Class<?>, Collection<?>>();
			implementorBeansMap.put(EvaluateTest.Implementor1.class,
					Arrays.asList());
			implementorBeansMap.put(EvaluateTest.Implementor2.class,
					Arrays.asList());

			assertNull(this.defaultImplementeeMethodInvocationInfoEvaluator
					.evaluate(
							this.implementationResolver.resolve(implementee,
									EvaluateTest.Implementor0.class,
									EvaluateTest.Implementor1.class),
							implementeeMethod, new Object[] { 1D, 2D },
							new SimpleImplementorBeanFactory()));
		}

		// myImplementMethodInfo.getValidityMethod().invoke(...) = false
		{
			Class<?> implementee = EvaluateTest.Implementee.class;
			Method implementeeMethod = getMethodByName(implementee, "plus");

			Map<Class<?>, Collection<?>> implementorBeansMap = new HashMap<Class<?>, Collection<?>>();
			implementorBeansMap.put(EvaluateTest.Implementor2.class,
					Arrays.asList(new EvaluateTest.Implementor2()));
			implementorBeansMap.put(EvaluateTest.Implementor3.class,
					Arrays.asList(new EvaluateTest.Implementor3()));

			assertNull(this.defaultImplementeeMethodInvocationInfoEvaluator
					.evaluate(
							this.implementationResolver.resolve(implementee,
									EvaluateTest.Implementor2.class,
									EvaluateTest.Implementor3.class),
							implementeeMethod, new Object[] { -1, 2 },
							new SimpleImplementorBeanFactory(
									implementorBeansMap)));
		}

		// getPriorityValue() = Integer.MAX_VALUE
		{
			Class<?> implementee = EvaluateTest.Implementee.class;
			Method implementeeMethod = getMethodByName(implementee, "plus");

			Map<Class<?>, Collection<?>> implementorBeansMap = new HashMap<Class<?>, Collection<?>>();
			implementorBeansMap.put(EvaluateTest.Implementor0.class,
					Arrays.asList(new EvaluateTest.Implementor0()));
			implementorBeansMap.put(EvaluateTest.Implementor1.class,
					Arrays.asList(new EvaluateTest.Implementor1()));
			implementorBeansMap.put(EvaluateTest.Implementor2.class,
					Arrays.asList(new EvaluateTest.Implementor2()));
			implementorBeansMap.put(EvaluateTest.Implementor3.class,
					Arrays.asList(new EvaluateTest.Implementor3()));
			implementorBeansMap.put(EvaluateTest.Implementor4.class,
					Arrays.asList(new EvaluateTest.Implementor4()));

			ImplementeeMethodInvocationInfo implementeeMethodInvocationInfo = this.defaultImplementeeMethodInvocationInfoEvaluator
					.evaluate(
							this.implementationResolver.resolve(implementee,
									EvaluateTest.Implementor0.class,
									EvaluateTest.Implementor1.class,
									EvaluateTest.Implementor2.class,
									EvaluateTest.Implementor3.class,
									EvaluateTest.Implementor4.class),
							implementeeMethod, new Object[] { 1, 2 },
							new SimpleImplementorBeanFactory(
									implementorBeansMap));

			assertEquals(EvaluateTest.Implementor4.class,
					implementeeMethodInvocationInfo.getImplementMethodInfo()
							.getImplementor());
			assertTrue(implementeeMethodInvocationInfo
					.getImplementorBean() instanceof EvaluateTest.Implementor4);
		}

		// getPriorityMethod().invoke(...) = Integer.MAX_VALUE
		{
			Class<?> implementee = EvaluateTest.Implementee.class;
			Method implementeeMethod = getMethodByName(implementee, "plus");

			Map<Class<?>, Collection<?>> implementorBeansMap = new HashMap<Class<?>, Collection<?>>();
			implementorBeansMap.put(EvaluateTest.Implementor0.class,
					Arrays.asList(new EvaluateTest.Implementor0()));
			implementorBeansMap.put(EvaluateTest.Implementor1.class,
					Arrays.asList(new EvaluateTest.Implementor1()));
			implementorBeansMap.put(EvaluateTest.Implementor2.class,
					Arrays.asList(new EvaluateTest.Implementor2()));
			implementorBeansMap.put(EvaluateTest.Implementor3.class,
					Arrays.asList(new EvaluateTest.Implementor3()));
			implementorBeansMap.put(EvaluateTest.Implementor5.class,
					Arrays.asList(new EvaluateTest.Implementor5()));

			ImplementeeMethodInvocationInfo implementeeMethodInvocationInfo = this.defaultImplementeeMethodInvocationInfoEvaluator
					.evaluate(
							this.implementationResolver.resolve(implementee,
									EvaluateTest.Implementor0.class,
									EvaluateTest.Implementor1.class,
									EvaluateTest.Implementor2.class,
									EvaluateTest.Implementor3.class,
									EvaluateTest.Implementor5.class),
							implementeeMethod, new Object[] { 1, 2 },
							new SimpleImplementorBeanFactory(
									implementorBeansMap));

			assertEquals(EvaluateTest.Implementor5.class,
					implementeeMethodInvocationInfo.getImplementMethodInfo()
							.getImplementor());
			assertTrue(implementeeMethodInvocationInfo
					.getImplementorBean() instanceof EvaluateTest.Implementor5);
		}

		// compareImplementMethodInfoPriority(...) < 0
		{
			Class<?> implementee = EvaluateTest.Implementee.class;
			Method implementeeMethod = getMethodByName(implementee, "plus");

			Map<Class<?>, Collection<?>> implementorBeansMap = new HashMap<Class<?>, Collection<?>>();
			implementorBeansMap.put(EvaluateTest.Implementor0.class,
					Arrays.asList(new EvaluateTest.Implementor0()));
			implementorBeansMap.put(EvaluateTest.Implementor1.class,
					Arrays.asList(new EvaluateTest.Implementor1()));

			ImplementeeMethodInvocationInfo implementeeMethodInvocationInfo = this.defaultImplementeeMethodInvocationInfoEvaluator
					.evaluate(
							this.implementationResolver.resolve(implementee,
									EvaluateTest.Implementor0.class,
									EvaluateTest.Implementor1.class),
							implementeeMethod, new Object[] { 1, 2 },
							new SimpleImplementorBeanFactory(
									implementorBeansMap));

			assertEquals(EvaluateTest.Implementor1.class,
					implementeeMethodInvocationInfo.getImplementMethodInfo()
							.getImplementor());
			assertTrue(implementeeMethodInvocationInfo
					.getImplementorBean() instanceof EvaluateTest.Implementor1);
		}
	}

	public static class EvaluateTest
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
			@Priority(Integer.MAX_VALUE)
			public Number plus(Number a, Number b)
			{
				return 0;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor5
		{
			@Implement
			@Priority(method = "getPriority")
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

	@Test
	public void isImplementMethodParamValidTest()
	{
		Class<?> implementee = IsImplementMethodParamValidTest.Implementee.class;
		Method implementeeMethod = getMethodByName(implementee, "plus");

		Implementation implementation = this.implementationResolver.resolve(
				implementee,
				IsImplementMethodParamValidTest.Implementor0.class,
				IsImplementMethodParamValidTest.Implementor1.class);

		// implementMethodInfo.getParamTypes() = null
		{
			ImplementMethodInfo implementMethodInfo = implementation.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(getMethodByName(
							IsImplementMethodParamValidTest.Implementor0.class,
							"plus"));

			implementMethodInfo.setParamTypes(null);

			assertTrue(this.defaultImplementeeMethodInvocationInfoEvaluator
					.isImplementMethodParamValid(
							implementMethodInfo,
							new Object[] { 1, 2 }));
		}

		// implementMethodInfo.getParamTypes().length = 0
		{
			ImplementMethodInfo implementMethodInfo = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(getMethodByName(
							IsImplementMethodParamValidTest.Implementor0.class,
							"plus"));

			assertTrue(this.defaultImplementeeMethodInvocationInfoEvaluator
					.isImplementMethodParamValid(implementMethodInfo,
							new Object[] { 1, 2 }));
		}

		// implementMethodInfo.getParamTypes().length >
		// implementeeMethodParams.length
		{
			ImplementMethodInfo implementMethodInfo = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(getMethodByName(
							IsImplementMethodParamValidTest.Implementor1.class,
							"plus"));

			assertFalse(this.defaultImplementeeMethodInvocationInfoEvaluator
					.isImplementMethodParamValid(implementMethodInfo,
							new Object[] { 1 }));
		}

		// !myParamTypes[i].isInstance(myParams[i])
		{
			ImplementMethodInfo implementMethodInfo = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(getMethodByName(
							IsImplementMethodParamValidTest.Implementor1.class,
							"plus"));

			assertFalse(this.defaultImplementeeMethodInvocationInfoEvaluator
					.isImplementMethodParamValid(implementMethodInfo,
							new Object[] { 1, 2.0D }));
		}
	}

	public static class IsImplementMethodParamValidTest
	{
		public static interface Implementee
		{
			Number plus(Number a, Number b);
		}

		@Implementor(Implementee.class)
		public static class Implementor0
		{
			@Implement
			public Number plus()
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
	}
}
