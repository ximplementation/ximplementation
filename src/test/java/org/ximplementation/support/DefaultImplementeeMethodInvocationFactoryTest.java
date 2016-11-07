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
import org.ximplementation.Index;
import org.ximplementation.Priority;
import org.ximplementation.Validity;
import org.ximplementation.support.DefaultImplementeeMethodInvocationFactory.SimpleImplementeeMethodInvocation;

/**
 * {@linkplain DefaultImplementeeMethodInvocationFactory} unit tests.
 * 
 * @author earthangry@gmail.com
 * @date 2016-8-25
 *
 */
public class DefaultImplementeeMethodInvocationFactoryTest
		extends AbstractTestSupport
{
	private DefaultImplementeeMethodInvocationFactory defaultImplementeeMethodInvocationFactory;
	private ImplementationResolver implementationResolver;

	@Before
	public void setUp() throws Exception
	{
		this.defaultImplementeeMethodInvocationFactory = new DefaultImplementeeMethodInvocationFactory();
		this.implementationResolver = new ImplementationResolver();
	}

	@After
	public void tearDown() throws Exception
	{
		this.defaultImplementeeMethodInvocationFactory = null;
		this.implementationResolver = null;
	}

	@Test
	public void getTest() throws Throwable
	{
		// isImplementMethodParamValid(...) = false
		{
			Class<?> implementee = GetTest.Implementee.class;
			Method implementeeMethod = getMethodByName(implementee, "plus");

			Map<Class<?>, Collection<?>> implementorBeansMap = new HashMap<Class<?>, Collection<?>>();
			implementorBeansMap.put(GetTest.Implementor1.class,
					Arrays.asList(new GetTest.Implementor1()));
			implementorBeansMap.put(GetTest.Implementor2.class,
					Arrays.asList(new GetTest.Implementor2()));

			assertNull(this.defaultImplementeeMethodInvocationFactory
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

			assertNull(this.defaultImplementeeMethodInvocationFactory
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

			assertNull(this.defaultImplementeeMethodInvocationFactory
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

			assertNull(this.defaultImplementeeMethodInvocationFactory
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

			SimpleImplementeeMethodInvocation implementeeMethodInvocationInfo = (SimpleImplementeeMethodInvocation) this.defaultImplementeeMethodInvocationFactory
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

			SimpleImplementeeMethodInvocation implementeeMethodInvocationInfo = (SimpleImplementeeMethodInvocation) this.defaultImplementeeMethodInvocationFactory
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

			SimpleImplementeeMethodInvocation implementeeMethodInvocationInfo = (SimpleImplementeeMethodInvocation) this.defaultImplementeeMethodInvocationFactory
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

	@Test
	public void invokeValidityMethodTest() throws Throwable
	{
		// public validity method
		{
			Implementation<InvokeValidityMethodTest.Implementee> implementation = this.implementationResolver
					.resolve(InvokeValidityMethodTest.Implementee.class,
							InvokeValidityMethodTest.Implementor0.class);

			Method implementeeMethod = getMethodByName(
					InvokeValidityMethodTest.Implementee.class, "handle");

			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);
			ImplementMethodInfo implementMethodInfo = implementInfo
					.getImplementMethodInfos()[0];
			Object[] params = {};

			assertFalse(this.defaultImplementeeMethodInvocationFactory
					.invokeValidityMethod(implementation, implementeeMethod,
							implementMethodInfo,
							implementMethodInfo.getValidityMethod(), params,
							new InvokeValidityMethodTest.Implementor0()));
		}

		// default validity method
		{
			Implementation<InvokeValidityMethodTest.Implementee> implementation = this.implementationResolver
					.resolve(InvokeValidityMethodTest.Implementee.class,
							InvokeValidityMethodTest.Implementor1.class);

			Method implementeeMethod = getMethodByName(
					InvokeValidityMethodTest.Implementee.class, "handle");

			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);
			ImplementMethodInfo implementMethodInfo = implementInfo
					.getImplementMethodInfos()[0];
			Object[] params = {};

			assertFalse(this.defaultImplementeeMethodInvocationFactory
					.invokeValidityMethod(implementation, implementeeMethod,
							implementMethodInfo,
							implementMethodInfo.getValidityMethod(), params,
							new InvokeValidityMethodTest.Implementor1()));
		}

		// protected validity method
		{
			Implementation<InvokeValidityMethodTest.Implementee> implementation = this.implementationResolver
					.resolve(InvokeValidityMethodTest.Implementee.class,
							InvokeValidityMethodTest.Implementor2.class);

			Method implementeeMethod = getMethodByName(
					InvokeValidityMethodTest.Implementee.class, "handle");

			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);
			ImplementMethodInfo implementMethodInfo = implementInfo
					.getImplementMethodInfos()[0];
			Object[] params = {};

			assertFalse(this.defaultImplementeeMethodInvocationFactory
					.invokeValidityMethod(implementation, implementeeMethod,
							implementMethodInfo,
							implementMethodInfo.getValidityMethod(), params,
							new InvokeValidityMethodTest.Implementor2()));
		}

		// private validity method
		{
			Implementation<InvokeValidityMethodTest.Implementee> implementation = this.implementationResolver
					.resolve(InvokeValidityMethodTest.Implementee.class,
							InvokeValidityMethodTest.Implementor3.class);

			Method implementeeMethod = getMethodByName(
					InvokeValidityMethodTest.Implementee.class, "handle");

			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);
			ImplementMethodInfo implementMethodInfo = implementInfo
					.getImplementMethodInfos()[0];
			Object[] params = {};

			assertFalse(this.defaultImplementeeMethodInvocationFactory
					.invokeValidityMethod(implementation, implementeeMethod,
							implementMethodInfo,
							implementMethodInfo.getValidityMethod(), params,
							new InvokeValidityMethodTest.Implementor3()));
		}
	}

	protected static class InvokeValidityMethodTest
	{
		public static class Implementee
		{
			public void handle()
			{
			}
		}

		public static class Implementor0 extends Implementee
		{
			@Validity("isValid")
			@Override
			public void handle()
			{
				super.handle();
			}

			public boolean isValid()
			{
				return false;
			}
		}

		public static class Implementor1 extends Implementee
		{
			@Validity("isValid")
			@Override
			public void handle()
			{
				super.handle();
			}

			boolean isValid()
			{
				return false;
			}
		}

		public static class Implementor2 extends Implementee
		{
			@Validity("isValid")
			@Override
			public void handle()
			{
				super.handle();
			}

			protected boolean isValid()
			{
				return false;
			}
		}

		public static class Implementor3 extends Implementee
		{
			@Validity("isValid")
			@Override
			public void handle()
			{
				super.handle();
			}

			@SuppressWarnings("unused")
			private boolean isValid()
			{
				return false;
			}
		}
	}

	@Test
	public void invokePriorityMethodTest() throws Throwable
	{
		// public
		{
			Implementation<InvokePriorityMethodTest.Implementee> implementation = this.implementationResolver
					.resolve(InvokePriorityMethodTest.Implementee.class,
							InvokePriorityMethodTest.Implementor0.class);

			Method implementeeMethod = getMethodByName(
					InvokePriorityMethodTest.Implementee.class, "handle");

			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);
			ImplementMethodInfo implementMethodInfo = implementInfo
					.getImplementMethodInfos()[0];
			Object[] params = {};

			assertEquals(3, this.defaultImplementeeMethodInvocationFactory
					.invokePriorityMethod(implementation, implementeeMethod,
							implementMethodInfo,
							implementMethodInfo.getPriorityMethod(), params,
							new InvokePriorityMethodTest.Implementor0()));
		}

		// default
		{
			Implementation<InvokePriorityMethodTest.Implementee> implementation = this.implementationResolver
					.resolve(InvokePriorityMethodTest.Implementee.class,
							InvokePriorityMethodTest.Implementor1.class);

			Method implementeeMethod = getMethodByName(
					InvokePriorityMethodTest.Implementee.class, "handle");

			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);
			ImplementMethodInfo implementMethodInfo = implementInfo
					.getImplementMethodInfos()[0];
			Object[] params = {};

			assertEquals(3, this.defaultImplementeeMethodInvocationFactory
					.invokePriorityMethod(implementation, implementeeMethod,
							implementMethodInfo,
							implementMethodInfo.getPriorityMethod(), params,
							new InvokePriorityMethodTest.Implementor1()));
		}

		// protected
		{
			Implementation<InvokePriorityMethodTest.Implementee> implementation = this.implementationResolver
					.resolve(InvokePriorityMethodTest.Implementee.class,
							InvokePriorityMethodTest.Implementor2.class);

			Method implementeeMethod = getMethodByName(
					InvokePriorityMethodTest.Implementee.class, "handle");

			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);
			ImplementMethodInfo implementMethodInfo = implementInfo
					.getImplementMethodInfos()[0];
			Object[] params = {};

			assertEquals(3, this.defaultImplementeeMethodInvocationFactory
					.invokePriorityMethod(implementation, implementeeMethod,
							implementMethodInfo,
							implementMethodInfo.getPriorityMethod(), params,
							new InvokePriorityMethodTest.Implementor2()));
		}

		// private
		{
			Implementation<InvokePriorityMethodTest.Implementee> implementation = this.implementationResolver
					.resolve(InvokePriorityMethodTest.Implementee.class,
							InvokePriorityMethodTest.Implementor3.class);

			Method implementeeMethod = getMethodByName(
					InvokePriorityMethodTest.Implementee.class, "handle");

			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);
			ImplementMethodInfo implementMethodInfo = implementInfo
					.getImplementMethodInfos()[0];
			Object[] params = {};

			assertEquals(3, this.defaultImplementeeMethodInvocationFactory
					.invokePriorityMethod(implementation, implementeeMethod,
							implementMethodInfo,
							implementMethodInfo.getPriorityMethod(), params,
							new InvokePriorityMethodTest.Implementor3()));
		}
	}

	protected static class InvokePriorityMethodTest
	{
		public static class Implementee
		{
			public void handle()
			{
			}
		}

		public static class Implementor0 extends Implementee
		{
			@Priority("getPriority")
			@Override
			public void handle()
			{
				super.handle();
			}

			public int getPriority()
			{
				return 3;
			}
		}

		public static class Implementor1 extends Implementee
		{
			@Priority("getPriority")
			@Override
			public void handle()
			{
				super.handle();
			}

			int getPriority()
			{
				return 3;
			}
		}

		public static class Implementor2 extends Implementee
		{
			@Priority("getPriority")
			@Override
			public void handle()
			{
				super.handle();
			}

			protected int getPriority()
			{
				return 3;
			}
		}

		public static class Implementor3 extends Implementee
		{
			@Priority("getPriority")
			@Override
			public void handle()
			{
				super.handle();
			}

			@SuppressWarnings("unused")
			private int getPriority()
			{
				return 3;
			}
		}
	}

	@Test
	public void isImplementMethodParamValidTest()
	{
		Class<?> implementee = IsImplementMethodParamValidTest.Implementee.class;
		Method implementeeMethod = getMethodByName(implementee, "plus");

		Implementation<?> implementation = this.implementationResolver.resolve(
				implementee,
				IsImplementMethodParamValidTest.Implementor0.class,
				IsImplementMethodParamValidTest.Implementor1.class,
				IsImplementMethodParamValidTest.Implementor2.class);

		// implementMethodInfo.getParamTypes() = null
		{
			ImplementMethodInfo implementMethodInfo = implementation.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(getMethodByName(
							IsImplementMethodParamValidTest.Implementor0.class,
							"plus"));

			implementMethodInfo.setParamTypes(null);

			assertTrue(this.defaultImplementeeMethodInvocationFactory
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

			assertTrue(this.defaultImplementeeMethodInvocationFactory
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

			assertFalse(this.defaultImplementeeMethodInvocationFactory
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

			assertFalse(this.defaultImplementeeMethodInvocationFactory
					.isImplementMethodParamValid(implementMethodInfo,
							new Object[] { 1, 2.0D }));
		}

		{
			ImplementMethodInfo implementMethodInfo = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(getMethodByName(
							IsImplementMethodParamValidTest.Implementor1.class,
							"plus"));

			assertTrue(this.defaultImplementeeMethodInvocationFactory
					.isImplementMethodParamValid(implementMethodInfo,
							new Object[] { 1, 2 }));
		}

		// primitive parameter types
		{
			ImplementMethodInfo implementMethodInfo = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(getMethodByName(
							IsImplementMethodParamValidTest.Implementor2.class,
							"plus"));

			assertTrue(this.defaultImplementeeMethodInvocationFactory
					.isImplementMethodParamValid(implementMethodInfo,
							new Object[] { 1, 2 }));
		}

		// primitive parameter types and null params
		{
			ImplementMethodInfo implementMethodInfo = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(getMethodByName(
							IsImplementMethodParamValidTest.Implementor2.class,
							"plus"));

			assertFalse(this.defaultImplementeeMethodInvocationFactory
					.isImplementMethodParamValid(implementMethodInfo,
							new Object[] { null, 2 }));
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

		@Implementor(Implementee.class)
		public static class Implementor2
		{
			@Implement
			public int plus(int a, int b)
			{
				return 0;
			}
		}
	}

	@Test
	public void compareImplementMethodInfoPriorityTest()
	{
		Class<?> implementee = CompareImplementMethodInfoPriorityTest.Implementee.class;
		Method implementeeMethod = getMethodByName(implementee, "plus");

		Implementation<?> implementation = this.implementationResolver.resolve(
				implementee,
				CompareImplementMethodInfoPriorityTest.Implementor0.class,
				CompareImplementMethodInfoPriorityTest.Implementor1.class,
				CompareImplementMethodInfoPriorityTest.Implementor2.class,
				CompareImplementMethodInfoPriorityTest.Implementor3.class);

		// firstHasValidity || secondHasValidity
		{
			ImplementMethodInfo implementMethodInfo0 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(getMethodByName(
							CompareImplementMethodInfoPriorityTest.Implementor0.class,
							"plus"));

			ImplementMethodInfo implementMethodInfo2 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(getMethodByName(
							CompareImplementMethodInfoPriorityTest.Implementor2.class,
							"plus"));

			assertEquals(-1,
					this.defaultImplementeeMethodInvocationFactory
							.compareImplementMethodInfoPriority(implementation,
									implementeeMethod, new Object[] { 1, 2 },
									implementMethodInfo0,
									implementMethodInfo2));

			assertEquals(1, this.defaultImplementeeMethodInvocationFactory
					.compareImplementMethodInfoPriority(implementation,
							implementeeMethod, new Object[] { 1, 2 },
							implementMethodInfo2, implementMethodInfo0));
		}

		// !firstHasValidity && !secondHasValidity
		{
			ImplementMethodInfo implementMethodInfo0 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(getMethodByName(
							CompareImplementMethodInfoPriorityTest.Implementor0.class,
							"plus"));

			ImplementMethodInfo implementMethodInfo1 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(getMethodByName(
							CompareImplementMethodInfoPriorityTest.Implementor1.class,
							"plus"));

			assertEquals(0, this.defaultImplementeeMethodInvocationFactory
					.compareImplementMethodInfoPriority(implementation,
							implementeeMethod, new Object[] { 1, 2 },
							implementMethodInfo0, implementMethodInfo1));

		}
	}

	public static class CompareImplementMethodInfoPriorityTest
	{
		public static interface Implementee
		{
			Number plus(Number a, Number b);
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
			public Number plus(Number a, Number b)
			{
				return null;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor2
		{
			@Implement
			@Validity("isValid")
			public Number plus(Number a, Number b)
			{
				return null;
			}

			public boolean isValid()
			{
				return true;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor3
		{
			@Implement
			@Validity("isValid")
			public Number plus(Number a, Number b)
			{
				return null;
			}

			public boolean isValid()
			{
				return true;
			}
		}
	}

	@Test
	public void compareImplementMethodParamTypePriorityTest()
	{
		Class<?> implementee = CompareImplementMethodParamTypePriorityTest.Implementee.class;
		Method implementeeMethod = getMethodByName(implementee, "plus");

		Implementation<?> implementation = this.implementationResolver.resolve(
				implementee,
				CompareImplementMethodParamTypePriorityTest.Implementor0.class,
				CompareImplementMethodParamTypePriorityTest.Implementor1.class,
				CompareImplementMethodParamTypePriorityTest.Implementor2.class,
				CompareImplementMethodParamTypePriorityTest.Implementor3.class,
				CompareImplementMethodParamTypePriorityTest.Implementor4.class,
				CompareImplementMethodParamTypePriorityTest.Implementor5.class);

		// firstCloserCount = secondCloserCount
		{
			ImplementMethodInfo implementMethodInfo0 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(getMethodByName(
							CompareImplementMethodParamTypePriorityTest.Implementor0.class,
							"plus"));

			ImplementMethodInfo implementMethodInfo1 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(getMethodByName(
							CompareImplementMethodParamTypePriorityTest.Implementor1.class,
							"plus"));

			assertEquals(0, this.defaultImplementeeMethodInvocationFactory
					.compareImplementMethodParamTypePriority(implementation,
							implementeeMethod, new Object[] { 0, 1, 2 },
							implementMethodInfo0, implementMethodInfo1));
		}

		// firstCloserCount < secondCloserCount
		{
			ImplementMethodInfo implementMethodInfo0 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(getMethodByName(
							CompareImplementMethodParamTypePriorityTest.Implementor0.class,
							"plus"));

			ImplementMethodInfo implementMethodInfo1 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(getMethodByName(
							CompareImplementMethodParamTypePriorityTest.Implementor2.class,
							"plus"));

			assertEquals(-1,
					this.defaultImplementeeMethodInvocationFactory
							.compareImplementMethodParamTypePriority(
									implementation, implementeeMethod,
									new Object[] { 0, 1, 2 },
									implementMethodInfo0,
									implementMethodInfo1));
		}

		// firstCloserCount - secondCloserCount = 2;
		{
			ImplementMethodInfo implementMethodInfo0 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(getMethodByName(
							CompareImplementMethodParamTypePriorityTest.Implementor3.class,
							"plus"));

			ImplementMethodInfo implementMethodInfo1 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(getMethodByName(
							CompareImplementMethodParamTypePriorityTest.Implementor0.class,
							"plus"));

			assertEquals(2,
					this.defaultImplementeeMethodInvocationFactory
							.compareImplementMethodParamTypePriority(
									implementation, implementeeMethod,
									new Object[] { 0, 1, 2 },
									implementMethodInfo0,
									implementMethodInfo1));
		}

		// firstCloserCount = secondCloserCount
		{
			ImplementMethodInfo implementMethodInfo0 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(getMethodByName(
							CompareImplementMethodParamTypePriorityTest.Implementor3.class,
							"plus"));

			ImplementMethodInfo implementMethodInfo1 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(getMethodByName(
							CompareImplementMethodParamTypePriorityTest.Implementor4.class,
							"plus"));

			assertEquals(0, this.defaultImplementeeMethodInvocationFactory
					.compareImplementMethodParamTypePriority(implementation,
							implementeeMethod, new Object[] { 0, 1, 2 },
							implementMethodInfo0, implementMethodInfo1));
		}

		// firstType.isPrimitive()
		{
			ImplementMethodInfo implementMethodInfo0 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(getMethodByName(
							CompareImplementMethodParamTypePriorityTest.Implementor4.class,
							"plus"));

			ImplementMethodInfo implementMethodInfo1 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(getMethodByName(
							CompareImplementMethodParamTypePriorityTest.Implementor5.class,
							"plus"));

			assertEquals(1, this.defaultImplementeeMethodInvocationFactory
					.compareImplementMethodParamTypePriority(implementation,
							implementeeMethod, new Object[] { 0, 1, 2 },
							implementMethodInfo1, implementMethodInfo0));
		}

		// secondType.isPrimitive()
		{
			ImplementMethodInfo implementMethodInfo0 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(getMethodByName(
							CompareImplementMethodParamTypePriorityTest.Implementor4.class,
							"plus"));

			ImplementMethodInfo implementMethodInfo1 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(getMethodByName(
							CompareImplementMethodParamTypePriorityTest.Implementor5.class,
							"plus"));

			assertEquals(-1, this.defaultImplementeeMethodInvocationFactory
					.compareImplementMethodParamTypePriority(implementation,
							implementeeMethod, new Object[] { 0, 1, 2 },
							implementMethodInfo0, implementMethodInfo1));
		}

	}

	public static class CompareImplementMethodParamTypePriorityTest
	{
		public static interface Implementee
		{
			Number plus(Number a, Number b, Number c);
		}

		@Implementor(Implementee.class)
		public static class Implementor0
		{
			@Implement
			public Number plus(Number a, Number b, Number c)
			{
				return null;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor1
		{
			@Implement
			public Number plus(Number a, Number b, Number c)
			{
				return null;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor2
		{
			@Implement
			public Number plus(Integer a, Number b, Number c)
			{
				return null;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor3
		{
			@Implement
			public Number plus(Integer a, Integer b, Number c)
			{
				return null;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor4
		{
			@Implement
			public Number plus(@Index(1) Integer b)
			{
				return null;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor5
		{
			@Implement
			public Number plus(@Index(1) int b)
			{
				return null;
			}
		}
	}

	@Test
	public void compareImplementorPriorityTest()
	{
		Class<?> implementee = org.ximplementation.support.testpkg.ipkg.TImplementee.class;

		Implementation<?> implementation = this.implementationResolver.resolve(
				implementee,
				org.ximplementation.support.testpkg.ipkg.TImplementorSamePkg0.class,
				org.ximplementation.support.testpkg.ipkg.TImplementorSamePkg1.class,
				org.ximplementation.support.testpkg.TImplementorDiffPkg0.class,
				org.ximplementation.support.testpkg.TImplementorDiffPkg1.class);

		// firstSamePkg && secondSamePkg
		assertEquals(0, this.defaultImplementeeMethodInvocationFactory
				.compareImplementorPriority(implementation,
						org.ximplementation.support.testpkg.ipkg.TImplementorSamePkg0.class,
						org.ximplementation.support.testpkg.ipkg.TImplementorSamePkg1.class));

		// firstSamePkg && !secondSamePkg
		assertEquals(-1, this.defaultImplementeeMethodInvocationFactory
				.compareImplementorPriority(implementation,
						org.ximplementation.support.testpkg.ipkg.TImplementorSamePkg0.class,
						org.ximplementation.support.testpkg.TImplementorDiffPkg0.class));

		// !firstSamePkg && secondSamePkg
		assertEquals(1, this.defaultImplementeeMethodInvocationFactory
				.compareImplementorPriority(implementation,
						org.ximplementation.support.testpkg.TImplementorDiffPkg0.class,
						org.ximplementation.support.testpkg.ipkg.TImplementorSamePkg0.class));

		// !firstSamePkg && !secondSamePkg
		assertEquals(0, this.defaultImplementeeMethodInvocationFactory
				.compareImplementorPriority(implementation,
						org.ximplementation.support.testpkg.TImplementorDiffPkg0.class,
						org.ximplementation.support.testpkg.TImplementorDiffPkg0.class));
	}
}
