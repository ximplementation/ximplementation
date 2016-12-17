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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ximplementation.Implement;
import org.ximplementation.Implementor;
import org.ximplementation.Index;
import org.ximplementation.Priority;
import org.ximplementation.Validity;

/**
 * {@linkplain AbstractImplementeeMethodInvocationFactory} unit tests.
 * 
 * @author earthangry@gmail.com
 * @date 2016-12-6
 *
 */
public class AbstractImplementeeMethodInvocationFactoryTest
		extends AbstractTestSupport
{
	private MockAbstractImplementeeMethodInvocationFactory mockAbstractImplementeeMethodInvocationFactory;
	private ImplementationResolver implementationResolver;

	@Before
	public void setUp() throws Exception
	{
		this.mockAbstractImplementeeMethodInvocationFactory = new MockAbstractImplementeeMethodInvocationFactory();
		this.implementationResolver = new ImplementationResolver();
	}

	@After
	public void tearDown() throws Exception
	{
		this.mockAbstractImplementeeMethodInvocationFactory = null;
		this.implementationResolver = null;
	}

	@SuppressWarnings(
	{ "rawtypes", "unchecked" })
	@Test
	public void getImplementorBeansWithCacheTest()
	{
		//cachedImplementorBeans.get(implementor) != null
		{
			List<Integer> cached = new ArrayList<Integer>();
			cached.add(1);
			
			Map<Class<?>, Collection<?>> cachedImplementorBeans = new HashMap<Class<?>, Collection<?>>();
			cachedImplementorBeans.put(Integer.class, cached);

			ImplementorBeanFactory implementorBeanFactory = SimpleImplementorBeanFactory
					.valueOf(Arrays.asList(new Integer(2)));

			Collection<?> implementorBeans = this.mockAbstractImplementeeMethodInvocationFactory
					.getImplementorBeansWithCache(cachedImplementorBeans, implementorBeanFactory, Integer.class);

			assertTrue(implementorBeans == cached);
		}

		// implementorBeans =
		// implementorBeanFactory.getImplementorBeans(implementor);
		{
			Map<Class<?>, Collection<?>> cachedImplementorBeans = new HashMap<Class<?>, Collection<?>>();

			ImplementorBeanFactory implementorBeanFactory = SimpleImplementorBeanFactory.valueOf(new Integer(1));

			assertNull(cachedImplementorBeans.get(Integer.class));
			assertThat(
					this.mockAbstractImplementeeMethodInvocationFactory.getImplementorBeansWithCache(
							cachedImplementorBeans, implementorBeanFactory, Integer.class),
					(Matcher) Matchers.contains(new Integer(1)));
			assertThat(cachedImplementorBeans.get(Integer.class), (Matcher) Matchers.contains(new Integer(1)));
		}
	}

	@Test
	public void isStaticImplementMethodTest()
	{
		// static method
		{
			Implementation<IsStaticImplementMethodTest.Implementee> implementation = this.implementationResolver
					.resolve(IsStaticImplementMethodTest.Implementee.class,
							IsStaticImplementMethodTest.Implementor0.class);

			Method implementeeMethod = getMethodByName(IsStaticImplementMethodTest.Implementee.class, "handle");

			ImplementInfo implementInfo = implementation.getImplementInfo(implementeeMethod);
			ImplementMethodInfo implementMethodInfo = implementInfo.getImplementMethodInfos()[0];

			assertTrue(
					this.mockAbstractImplementeeMethodInvocationFactory.isStaticImplementMethod(implementMethodInfo));
		}

		// not static method
		{
			Implementation<IsStaticImplementMethodTest.Implementee> implementation = this.implementationResolver
					.resolve(IsStaticImplementMethodTest.Implementee.class,
							IsStaticImplementMethodTest.Implementor1.class);

			Method implementeeMethod = getMethodByName(IsStaticImplementMethodTest.Implementee.class, "handle");

			ImplementInfo implementInfo = implementation.getImplementInfo(implementeeMethod);
			ImplementMethodInfo implementMethodInfo = implementInfo.getImplementMethodInfos()[0];

			assertFalse(
					this.mockAbstractImplementeeMethodInvocationFactory.isStaticImplementMethod(implementMethodInfo));
		}
	}

	public static class IsStaticImplementMethodTest
	{
		public static interface Implementee
		{
			void handle();
		}

		@org.ximplementation.Implementor(Implementee.class)
		public static class Implementor0
		{
			@Implement
			public static void handle()
			{
			}
		}

		public static class Implementor1 implements Implementee
		{
			@Override
			public void handle()
			{
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

			assertFalse(this.mockAbstractImplementeeMethodInvocationFactory
					.invokeValidityMethod(implementation, implementInfo,
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

			assertFalse(this.mockAbstractImplementeeMethodInvocationFactory
					.invokeValidityMethod(implementation, implementInfo,
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

			assertFalse(this.mockAbstractImplementeeMethodInvocationFactory
					.invokeValidityMethod(implementation, implementInfo,
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

			assertFalse(this.mockAbstractImplementeeMethodInvocationFactory
					.invokeValidityMethod(implementation, implementInfo,
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

			assertEquals(3, this.mockAbstractImplementeeMethodInvocationFactory
					.invokePriorityMethod(implementation, implementInfo,
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

			assertEquals(3, this.mockAbstractImplementeeMethodInvocationFactory
					.invokePriorityMethod(implementation, implementInfo,
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

			assertEquals(3, this.mockAbstractImplementeeMethodInvocationFactory
					.invokePriorityMethod(implementation, implementInfo,
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

			assertEquals(3, this.mockAbstractImplementeeMethodInvocationFactory
					.invokePriorityMethod(implementation, implementInfo,
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
	public void isImplementMethodParamTypeValidTest()
	{
		Class<?> implementee = IsImplementMethodParamValidTest.Implementee.class;
		Method implementeeMethod = getMethodByName(implementee, "plus");

		Implementation<?> implementation = this.implementationResolver.resolve(
				implementee,
				IsImplementMethodParamValidTest.Implementor0.class,
				IsImplementMethodParamValidTest.Implementor1.class,
				IsImplementMethodParamValidTest.Implementor2.class);

		// Class<?>[] myParamTypes = getActualImplementMethodParamTypes
		{
			Implementation<?> myImplementation = this.implementationResolver
					.resolve(IsImplementMethodParamValidTest.GImplementee.class,
							IsImplementMethodParamValidTest.GImplementor.class);

			Method myImplementeeMethod = getMethodByName(
					IsImplementMethodParamValidTest.GImplementee.class,
					"handle");

			ImplementInfo myImplementInfo = myImplementation
					.getImplementInfo(myImplementeeMethod);

			ImplementMethodInfo implementMethodInfo = myImplementInfo
					.getImplementMethodInfo(
							IsImplementMethodParamValidTest.GImplementor.class,
							getMethodByName(
									IsImplementMethodParamValidTest.GImplementor.class,
									"handle"));

			assertTrue(this.mockAbstractImplementeeMethodInvocationFactory
					.isImplementMethodParamTypeValid(myImplementation,
							myImplementInfo, implementMethodInfo,
							new Class<?>[] { Integer.class }));

			assertFalse(this.mockAbstractImplementeeMethodInvocationFactory
					.isImplementMethodParamTypeValid(myImplementation,
							myImplementInfo, implementMethodInfo,
							new Class<?>[] { Double.class }));
		}

		// myParamTypes == null
		{
			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			ImplementMethodInfo implementMethodInfo = implementInfo
					.getImplementMethodInfo(
							IsImplementMethodParamValidTest.Implementor0.class,
							getMethodByName(
									IsImplementMethodParamValidTest.Implementor0.class,
									"plus"));

			implementMethodInfo.setParamTypes(null);

			assertTrue(this.mockAbstractImplementeeMethodInvocationFactory
					.isImplementMethodParamTypeValid(implementation,
							implementInfo,
							implementMethodInfo,
							new Class<?>[] { Integer.class, Integer.class }));
		}

		// myParamTypes.length == 0
		{
			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			ImplementMethodInfo implementMethodInfo = implementInfo
					.getImplementMethodInfo(
							IsImplementMethodParamValidTest.Implementor0.class,
							getMethodByName(
							IsImplementMethodParamValidTest.Implementor0.class,
									"plus"));

			assertTrue(this.mockAbstractImplementeeMethodInvocationFactory
					.isImplementMethodParamTypeValid(implementation,
							implementInfo, implementMethodInfo,
							new Class<?>[] { Integer.class, Integer.class }));
		}

		// myParamTypes.length > invocationParamTypes.length
		{
			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			ImplementMethodInfo implementMethodInfo = implementInfo
					.getImplementMethodInfo(
							IsImplementMethodParamValidTest.Implementor1.class,
							getMethodByName(
							IsImplementMethodParamValidTest.Implementor1.class,
									"plus"));

			assertFalse(this.mockAbstractImplementeeMethodInvocationFactory
					.isImplementMethodParamTypeValid(implementation,
							implementInfo, implementMethodInfo,
							new Class<?>[] { Integer.class }));
		}

		// myInvocationParamType == null
		// myParamType.isPrimitive()
		{
			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			ImplementMethodInfo implementMethodInfo = implementInfo
					.getImplementMethodInfo(
							IsImplementMethodParamValidTest.Implementor2.class,
							getMethodByName(
							IsImplementMethodParamValidTest.Implementor2.class,
							"plus"));

			assertFalse(this.mockAbstractImplementeeMethodInvocationFactory
					.isImplementMethodParamTypeValid(implementation,
							implementInfo, implementMethodInfo,
							new Class<?>[] { Integer.class, null }));
		}

		// !toWrapperType(myParamType).isAssignableFrom(toWrapperType(myInvocationParamType))
		{
			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			ImplementMethodInfo implementMethodInfo = implementInfo
					.getImplementMethodInfo(
							IsImplementMethodParamValidTest.Implementor1.class,
							getMethodByName(
							IsImplementMethodParamValidTest.Implementor1.class,
									"plus"));

			assertFalse(this.mockAbstractImplementeeMethodInvocationFactory
					.isImplementMethodParamTypeValid(implementation,
							implementInfo, implementMethodInfo,
							new Class<?>[] { Integer.class, Double.class }));
		}

		// ok
		{
			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			ImplementMethodInfo implementMethodInfo = implementInfo
					.getImplementMethodInfo(
							IsImplementMethodParamValidTest.Implementor1.class,
							getMethodByName(
							IsImplementMethodParamValidTest.Implementor1.class,
									"plus"));

			assertTrue(this.mockAbstractImplementeeMethodInvocationFactory
					.isImplementMethodParamTypeValid(implementation,
							implementInfo, implementMethodInfo,
							new Class<?>[] { Integer.class, Integer.class }));
		}

		// ok, primitive parameter types
		{
			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			ImplementMethodInfo implementMethodInfo = implementInfo
					.getImplementMethodInfo(
							IsImplementMethodParamValidTest.Implementor2.class,
							getMethodByName(
									IsImplementMethodParamValidTest.Implementor2.class,
									"plus"));

			assertTrue(this.mockAbstractImplementeeMethodInvocationFactory
					.isImplementMethodParamTypeValid(implementation,
							implementInfo, implementMethodInfo,
							new Class<?>[] { Integer.class, Integer.class }));
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

		public static class GImplementee<T extends Number>
		{
			public void handle(T t)
			{
			}
		}

		public static class GImplementor extends GImplementee<Integer>
		{
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

		ImplementInfo implementInfo = implementation
				.getImplementInfo(implementeeMethod);

		// firstHasValidity || secondHasValidity
		{
			ImplementMethodInfo implementMethodInfo0 = implementInfo
					.getImplementMethodInfo(
							CompareImplementMethodInfoPriorityTest.Implementor0.class,
							getMethodByName(
							CompareImplementMethodInfoPriorityTest.Implementor0.class,
							"plus"));

			ImplementMethodInfo implementMethodInfo2 = implementInfo
					.getImplementMethodInfo(
							CompareImplementMethodInfoPriorityTest.Implementor2.class,
							getMethodByName(
							CompareImplementMethodInfoPriorityTest.Implementor2.class,
							"plus"));

			assertEquals(-1,
					this.mockAbstractImplementeeMethodInvocationFactory
							.compareImplementMethodInfoPriority(implementation,
									implementInfo,
									new Class<?>[] { Integer.class,
											Integer.class },
									implementMethodInfo0,
									implementMethodInfo2));

			assertEquals(1, this.mockAbstractImplementeeMethodInvocationFactory
					.compareImplementMethodInfoPriority(implementation,
							implementInfo,
							new Class<?>[] { Integer.class, Integer.class },
							implementMethodInfo2, implementMethodInfo0));
		}

		// !firstHasValidity && !secondHasValidity
		{
			ImplementMethodInfo implementMethodInfo0 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(
							CompareImplementMethodInfoPriorityTest.Implementor0.class,
							getMethodByName(
							CompareImplementMethodInfoPriorityTest.Implementor0.class,
							"plus"));

			ImplementMethodInfo implementMethodInfo1 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(
							CompareImplementMethodInfoPriorityTest.Implementor1.class,
							getMethodByName(
							CompareImplementMethodInfoPriorityTest.Implementor1.class,
							"plus"));

			assertEquals(0, this.mockAbstractImplementeeMethodInvocationFactory
					.compareImplementMethodInfoPriority(implementation,
							implementInfo,
							new Class<?>[] { Integer.class, Integer.class },
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

		ImplementInfo implementInfo = implementation
				.getImplementInfo(implementeeMethod);

		// firstCloserCount = secondCloserCount
		{
			ImplementMethodInfo implementMethodInfo0 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(
							CompareImplementMethodParamTypePriorityTest.Implementor0.class,
							getMethodByName(
							CompareImplementMethodParamTypePriorityTest.Implementor0.class,
							"plus"));

			ImplementMethodInfo implementMethodInfo1 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(
							CompareImplementMethodParamTypePriorityTest.Implementor1.class,
							getMethodByName(
							CompareImplementMethodParamTypePriorityTest.Implementor1.class,
							"plus"));

			assertEquals(0, this.mockAbstractImplementeeMethodInvocationFactory
					.compareImplementMethodParamTypePriority(implementation,
							implementInfo,
							new Class<?>[] { Integer.class, Integer.class,
									Integer.class },
							implementMethodInfo0, implementMethodInfo1));
		}

		// firstCloserCount < secondCloserCount
		{
			ImplementMethodInfo implementMethodInfo0 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(
							CompareImplementMethodParamTypePriorityTest.Implementor0.class,
							getMethodByName(
							CompareImplementMethodParamTypePriorityTest.Implementor0.class,
							"plus"));

			ImplementMethodInfo implementMethodInfo1 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(
							CompareImplementMethodParamTypePriorityTest.Implementor2.class,
							getMethodByName(
							CompareImplementMethodParamTypePriorityTest.Implementor2.class,
							"plus"));

			assertEquals(-1,
					this.mockAbstractImplementeeMethodInvocationFactory
							.compareImplementMethodParamTypePriority(
									implementation, implementInfo,
									new Class<?>[] { Integer.class,
											Integer.class, Integer.class },
									implementMethodInfo0,
									implementMethodInfo1));
		}

		// firstCloserCount - secondCloserCount = 2;
		{
			ImplementMethodInfo implementMethodInfo0 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(
							CompareImplementMethodParamTypePriorityTest.Implementor3.class,
							getMethodByName(
							CompareImplementMethodParamTypePriorityTest.Implementor3.class,
							"plus"));

			ImplementMethodInfo implementMethodInfo1 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(
							CompareImplementMethodParamTypePriorityTest.Implementor0.class,
							getMethodByName(
							CompareImplementMethodParamTypePriorityTest.Implementor0.class,
							"plus"));

			assertEquals(2,
					this.mockAbstractImplementeeMethodInvocationFactory
							.compareImplementMethodParamTypePriority(
									implementation, implementInfo,
									new Class<?>[] { Integer.class,
											Integer.class, Integer.class },
									implementMethodInfo0,
									implementMethodInfo1));
		}

		// firstCloserCount = secondCloserCount
		{
			ImplementMethodInfo implementMethodInfo0 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(
							CompareImplementMethodParamTypePriorityTest.Implementor3.class,
							getMethodByName(
							CompareImplementMethodParamTypePriorityTest.Implementor3.class,
							"plus"));

			ImplementMethodInfo implementMethodInfo1 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(
							CompareImplementMethodParamTypePriorityTest.Implementor4.class,
							getMethodByName(
							CompareImplementMethodParamTypePriorityTest.Implementor4.class,
							"plus"));

			assertEquals(0, this.mockAbstractImplementeeMethodInvocationFactory
					.compareImplementMethodParamTypePriority(implementation,
							implementInfo,
							new Class<?>[] { Integer.class, Integer.class,
									Integer.class },
							implementMethodInfo0, implementMethodInfo1));
		}

		// firstType.isPrimitive()
		{
			ImplementMethodInfo implementMethodInfo0 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(
							CompareImplementMethodParamTypePriorityTest.Implementor4.class,
							getMethodByName(
							CompareImplementMethodParamTypePriorityTest.Implementor4.class,
							"plus"));

			ImplementMethodInfo implementMethodInfo1 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(
							CompareImplementMethodParamTypePriorityTest.Implementor5.class,
							getMethodByName(
							CompareImplementMethodParamTypePriorityTest.Implementor5.class,
							"plus"));

			assertEquals(1, this.mockAbstractImplementeeMethodInvocationFactory
					.compareImplementMethodParamTypePriority(implementation,
							implementInfo,
							new Class<?>[] { Integer.class, Integer.class,
									Integer.class },
							implementMethodInfo1, implementMethodInfo0));
		}

		// secondType.isPrimitive()
		{
			ImplementMethodInfo implementMethodInfo0 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(
							CompareImplementMethodParamTypePriorityTest.Implementor4.class,
							getMethodByName(
							CompareImplementMethodParamTypePriorityTest.Implementor4.class,
							"plus"));

			ImplementMethodInfo implementMethodInfo1 = implementation
					.getImplementInfo(implementeeMethod)
					.getImplementMethodInfo(
							CompareImplementMethodParamTypePriorityTest.Implementor5.class,
							getMethodByName(
							CompareImplementMethodParamTypePriorityTest.Implementor5.class,
							"plus"));

			assertEquals(-1, this.mockAbstractImplementeeMethodInvocationFactory
					.compareImplementMethodParamTypePriority(implementation,
							implementInfo,
							new Class<?>[] { Integer.class, Integer.class,
									Integer.class },
							implementMethodInfo0, implementMethodInfo1));
		}

		// generic method in super class
		{
			Implementation<?> myImplementation = this.implementationResolver
					.resolve(
							CompareImplementMethodParamTypePriorityTest.GImplementee.class,
							CompareImplementMethodParamTypePriorityTest.GImplementee.class,
							CompareImplementMethodParamTypePriorityTest.GImplementor.class);

			Method myImplementeeMethod = getMethodByName(
					CompareImplementMethodParamTypePriorityTest.GImplementee.class,
					"handle");

			ImplementMethodInfo implementMethodInfo0 = myImplementation
					.getImplementInfo(myImplementeeMethod)
					.getImplementMethodInfo(
							CompareImplementMethodParamTypePriorityTest.GImplementee.class,
							getMethodByName(
							CompareImplementMethodParamTypePriorityTest.GImplementee.class,
							"handle"));

			ImplementMethodInfo implementMethodInfo1 = myImplementation
					.getImplementInfo(myImplementeeMethod)
					.getImplementMethodInfo(
							CompareImplementMethodParamTypePriorityTest.GImplementor.class,
							getMethodByName(
							CompareImplementMethodParamTypePriorityTest.GImplementor.class,
							"handle"));

			assertEquals(-1, this.mockAbstractImplementeeMethodInvocationFactory
					.compareImplementMethodParamTypePriority(implementation,
							implementInfo, new Class<?>[] { Integer.class },
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

		public static class GImplementee<T extends Number>
		{
			public void handle(T t)
			{
			}
		}

		public static class GImplementor extends GImplementee<Integer>
		{
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
		assertEquals(0, this.mockAbstractImplementeeMethodInvocationFactory
				.compareImplementorPriority(implementation,
						org.ximplementation.support.testpkg.ipkg.TImplementorSamePkg0.class,
						org.ximplementation.support.testpkg.ipkg.TImplementorSamePkg1.class));

		// firstSamePkg && !secondSamePkg
		assertEquals(-1, this.mockAbstractImplementeeMethodInvocationFactory
				.compareImplementorPriority(implementation,
						org.ximplementation.support.testpkg.ipkg.TImplementorSamePkg0.class,
						org.ximplementation.support.testpkg.TImplementorDiffPkg0.class));

		// !firstSamePkg && secondSamePkg
		assertEquals(1, this.mockAbstractImplementeeMethodInvocationFactory
				.compareImplementorPriority(implementation,
						org.ximplementation.support.testpkg.TImplementorDiffPkg0.class,
						org.ximplementation.support.testpkg.ipkg.TImplementorSamePkg0.class));

		// !firstSamePkg && !secondSamePkg
		assertEquals(0, this.mockAbstractImplementeeMethodInvocationFactory
				.compareImplementorPriority(implementation,
						org.ximplementation.support.testpkg.TImplementorDiffPkg0.class,
						org.ximplementation.support.testpkg.TImplementorDiffPkg0.class));
	}

	@Test
	public void getActualImplementMethodParamTypesTest()
	{
		@SuppressWarnings("rawtypes")
		Implementation<GetActualImplementMethodParamTypesTest.Implementee> implementation = this.implementationResolver
				.resolve(
						GetActualImplementMethodParamTypesTest.Implementee.class,
						GetActualImplementMethodParamTypesTest.Implementee.class,
						GetActualImplementMethodParamTypesTest.Implementor0.class,
						GetActualImplementMethodParamTypesTest.Implementor1.class);

		Method implementeeMethod0 = getMethodByName(
				GetActualImplementMethodParamTypesTest.Implementee.class,
				"handle0");
		ImplementInfo implementInfo0 = implementation
				.getImplementInfo(implementeeMethod0);

		Method implementeeMethod1 = getMethodByName(
				GetActualImplementMethodParamTypesTest.Implementee.class,
				"handle1");
		ImplementInfo implementInfo1 = implementation
				.getImplementInfo(implementeeMethod1);

		// implementMethod.getDeclaringClass().equals(implementor)
		{
			ImplementMethodInfo implementMethodInfo = implementInfo0.getImplementMethodInfo(GetActualImplementMethodParamTypesTest.Implementee.class, getMethodByName(
					GetActualImplementMethodParamTypesTest.Implementee.class,
					"handle0"));

			Class<?>[] actualParamTypes = this.mockAbstractImplementeeMethodInvocationFactory
					.getActualImplementMethodParamTypes(implementation,
							implementInfo0, implementMethodInfo);

			assertArrayEquals(implementMethodInfo.getParamTypes(),
					actualParamTypes);
			assertEquals(GetActualImplementMethodParamTypesTest.Para.class,
					actualParamTypes[0]);
			assertEquals(String.class, actualParamTypes[1]);

			assertNull(this.mockAbstractImplementeeMethodInvocationFactory
					.getCachedActualImplementMethodParamTypes(
							implementMethodInfo));
		}

		// Arrays.equals(paramTypes, gparamTypes)
		{
			ImplementMethodInfo implementMethodInfo = implementInfo1
					.getImplementMethodInfo(
							GetActualImplementMethodParamTypesTest.Implementor0.class,
							getMethodByName(
									GetActualImplementMethodParamTypesTest.Implementor0.class,
									"handle1"));

			Class<?>[] actualParamTypes = this.mockAbstractImplementeeMethodInvocationFactory
					.getActualImplementMethodParamTypes(implementation,
							implementInfo0, implementMethodInfo);

			assertArrayEquals(implementMethodInfo.getParamTypes(),
					actualParamTypes);
			assertEquals(String.class, actualParamTypes[0]);

			assertNull(this.mockAbstractImplementeeMethodInvocationFactory
					.getCachedActualImplementMethodParamTypes(
							implementMethodInfo));
		}

		// generic method not overridden in sub class
		{
			ImplementMethodInfo implementMethodInfo = implementInfo0
					.getImplementMethodInfo(
							GetActualImplementMethodParamTypesTest.Implementor0.class,
							getMethodByName(
									GetActualImplementMethodParamTypesTest.Implementor0.class,
									"handle0"));

			Class<?>[] actualParamTypes = this.mockAbstractImplementeeMethodInvocationFactory
					.getActualImplementMethodParamTypes(implementation,
							implementInfo0, implementMethodInfo);

			assertEquals(GetActualImplementMethodParamTypesTest.Para0.class,
					actualParamTypes[0]);
			assertEquals(String.class, actualParamTypes[1]);

			assertArrayEquals(actualParamTypes,
					this.mockAbstractImplementeeMethodInvocationFactory
							.getCachedActualImplementMethodParamTypes(
									implementMethodInfo));
		}

		// generic method overridden in sub class
		{
			ImplementMethodInfo implementMethodInfo = implementInfo0
					.getImplementMethodInfo(
							GetActualImplementMethodParamTypesTest.Implementor1.class,
							getMethodByName(
									GetActualImplementMethodParamTypesTest.Implementor1.class,
									"handle0"));

			Class<?>[] actualParamTypes = this.mockAbstractImplementeeMethodInvocationFactory
					.getActualImplementMethodParamTypes(implementation,
							implementInfo0, implementMethodInfo);

			assertEquals(GetActualImplementMethodParamTypesTest.Para1.class,
					actualParamTypes[0]);
			assertEquals(String.class, actualParamTypes[1]);

			assertNull(this.mockAbstractImplementeeMethodInvocationFactory
							.getCachedActualImplementMethodParamTypes(
									implementMethodInfo));
		}
	}

	protected static class GetActualImplementMethodParamTypesTest
	{
		public static class Implementee<T extends Para>
		{
			public void handle0(T t, String s)
			{
			}

			public void handle1(String str)
			{
			}
		}

		public static class Implementor0 extends Implementee<Para0>
		{

		}

		public static class Implementor1<T extends Para1>
				extends Implementee<T>
		{
			@Override
			public void handle0(T t, String s)
			{
			}
		}

		public static class Para
		{
		}

		public static class Para0 extends Para
		{
		}

		public static class Para1 extends Para
		{
		}
	}

	@Test
	public void extractTypesTest()
	{
		// objs == null
		{
			assertEquals(0, this.mockAbstractImplementeeMethodInvocationFactory
					.extractTypes(null).length);
		}

		// null element
		{
			Class<?>[] classes = this.mockAbstractImplementeeMethodInvocationFactory
					.extractTypes(new Object[] { null, 3, "s" });

			assertEquals(3, classes.length);
			assertNull(classes[0]);
			assertEquals(Integer.class, classes[1]);
			assertEquals(String.class, classes[2]);
		}
	}

	@Test
	public void copyArrayByIndexTest()
	{
		// indexes == null
		{
			Class<?>[] classes = this.mockAbstractImplementeeMethodInvocationFactory
					.copyArrayByIndex(new Class<?>[] { int.class }, null);

			assertEquals(0, classes.length);
		}

		{
			Class<?>[] classes = this.mockAbstractImplementeeMethodInvocationFactory
					.copyArrayByIndex(new Class<?>[] { int.class, String.class,
							Double.class }, new int[] { 2, 1 });

			assertEquals(Double.class, classes[0]);
			assertEquals(String.class, classes[1]);
		}
	}

	protected static class MockAbstractImplementeeMethodInvocationFactory
			extends AbstractImplementeeMethodInvocationFactory
	{
		@Override
		public ImplementeeMethodInvocation get(Implementation<?> implementation,
				Method implementeeMethod, Object[] invocationParams,
				ImplementorBeanFactory implementorBeanFactory) throws Throwable
		{
			throw new UnsupportedOperationException();
		}
	}
}
